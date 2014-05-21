import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

// X Class
// This is the main character
// The player will control X and perform multiple tasks such as navigate through series of platforms
// and defeat creeps and the final bosses
// X is able to use his X-Buster and shoot out plasma cannons to attack enemy

public class X {
	private boolean up,down,left,right; // flag for directions
	private boolean inAir,shoot,stunned,boss; // check if its in air, shooting, stunned, or reached the boss
	private boolean jumpReleased; // see if the character is able to jump again (only if the user lets go of the key)
	private Rectangle rect; // a Rectangle object representing the character
	private int vx,vy; // character's velocity
	private int ox,oy; // original position
	private int hp; // health point
	private int maxVy,shootCD;  // maximum verticle velocity, and shooting cool down
	private int invinced,landingCD; // counter for invincibility and landing cool down
	private int bulletCharge; // counter for the amount of charge on X-Buster
	private int facing; // direction which the character is facing
	private String spriteName; // sprite name
	private double spriteNum; // counter for sprite
	private HashMap<String,Image[]>sprites; // sprites
	
    public X(int a,int b) {
    	ox=a;
    	oy=b;
		hp=100;
		boss=false;
		up=false;
		down=false;
		left=false;
		right=false;
		inAir=false;
		stunned=false;
		jumpReleased=true;
		shoot=false;
		facing = 1;
		invinced=0;
		landingCD=0;
		shootCD=0;
		maxVy=30;
    	spriteName = "rest";
    	spriteNum = 0;
    	bulletCharge=0;
    	rect = new Rectangle(a,b,20,40);
    	loadSprites();
    }
    
    public void loadSprites(){
    	String tmp="walk";
    	int num;
		sprites = new HashMap<String,Image[]>();
		num=14;
		sprites.put(tmp,new Image[num]);
		for(int i=0;i<num;i++){
			sprites.get(tmp)[i]=new ImageIcon("Sprites\\x_"+tmp+"\\x_"+tmp+" "+i+".png").getImage();
		}
		tmp="rest";
		sprites.put(tmp,new Image[8*4]);
		for(int i=0;i<8*4;i++){
			sprites.get(tmp)[i]=new ImageIcon("Sprites\\x_"+tmp+"\\x_"+tmp+" "+i/4+".png").getImage();
		}tmp="rest_hurt";
		sprites.put(tmp,new Image[6*4]);
		for(int i=0;i<6*4;i++){
			sprites.get(tmp)[i]=new ImageIcon("Sprites\\x_"+tmp+"\\x_"+tmp+" "+i/4+".png").getImage();
		}
		tmp="shoot";
		sprites.put(tmp,new Image[12]);
		for(int i=0;i<8;i++){
			sprites.get(tmp)[i]=new ImageIcon("Sprites\\x_"+tmp+"\\x_"+tmp+" "+i+".png").getImage();
		}
		for(int i=8;i<12;i++){
			sprites.get(tmp)[i]=new ImageIcon("Sprites\\x_"+tmp+"\\x_"+tmp+" "+7+".png").getImage();	
		}
		tmp="jump";
		sprites.put(tmp,new Image[11]);
		for(int i=0;i<11;i++){
			sprites.get(tmp)[i]=new ImageIcon("Sprites\\x_"+tmp+"\\x_"+tmp+" "+i+".png").getImage();
		}
		tmp="hurt";
		sprites.put(tmp,new Image[4]);
		for(int i=0;i<4;i++){
			sprites.get(tmp)[i]=new ImageIcon("Sprites\\x_"+tmp+"\\x_"+tmp+" "+i+".png").getImage();
		}
		tmp="die";
		sprites.put(tmp,new Image[22]);
		for(int i=0;i<22;i++){
			sprites.get(tmp)[i]=new ImageIcon("Sprites\\explosion_yellow\\explosion_yellow "+i/2+".png").getImage();
		}
		
		tmp="jump_wall";
		sprites.put(tmp,new Image[1]);
		sprites.get(tmp)[0]=new ImageIcon("Sprites\\x_"+tmp+"\\x_"+tmp+" "+0+".png").getImage();

		tmp="charging";
		sprites.put(tmp,new Image[9]);
		for(int i=0;i<9;i++){
			sprites.get(tmp)[i]=new ImageIcon("Sprites\\"+tmp+"\\"+tmp+" "+i+".png").getImage();
		}
		tmp="full_charge";
		sprites.put(tmp,new Image[4]);
		for(int i=0;i<4;i++){
			sprites.get(tmp)[i]=new ImageIcon("Sprites\\"+tmp+"\\"+tmp+" "+i+".png").getImage();
		}
		
    }
 
 	public void reset(){
 		// eitheer player died or boss died
 		// resets all stat
		hp=100;
		up=false;
		down=false;
		left=false;
		right=false;
		inAir=false;
		stunned=false;
		jumpReleased=true;
		shoot=false;
		facing = 1;
		invinced=0;
		landingCD=0;
		shootCD=0;
		maxVy=30;
    	spriteName = "rest";
    	spriteNum = 0;
    	bulletCharge=0;
    	moveRect(ox,oy);
 	}
    public void moveRect(int a,int b){rect.move((a+2000)%2000,(b+1600)%1600); }
    public Rectangle getRect(){    	return rect;    }
    public int getX(){    	return (int)(rect.getX());    }
    public int getY(){    	return (int)(rect.getY());    }
    public int getWidth(){    	return (int)(rect.getWidth());    }
    public int getHeight(){    	return (int)(rect.getHeight());    }
    public boolean getBoss(){return boss;}
    public void setHor(int n){
    	// horizontal movement
    	vx=n;
    	if(n!=0){
    		facing = (n>0)?1:-1;
    	}
    }
    public int getHor(){    	return vx;    }
    public void setUp(boolean a){    	up=a;    }
    public void setDown(boolean a){    	down=a;    }
    public void setJumped(int val){
    	inAir=true;
    	jumpReleased=false;
    	vy=val;
    }
    public void setReleased(){    	jumpReleased=true;    }
    public boolean getJumped(){    	return inAir || !jumpReleased;    }
    public boolean getOnWall(){    	return maxVy==5 && jumpReleased && inAir;    }
    public void setShoot(){
    	// shooting
    	if(shootCD<7){
        	shoot=true;
    	}
    	else{
    		bulletCharge=0;
    	}
        
    }
  	public void charge(){
  		bulletCharge+=1;
  	}
  	public void stun(boolean n){
  		stunned=n;
  	}
    
    public int getHealth(){
    	return hp;
    }
    public void receiveDmg(int n){
    	if(n<=0){
    		hp=Math.min(100,hp-n);
    	}
    	else if(invinced==0){
	    	hp-=n;
	    	invinced=20;
	    	if(hp<=0){
	    		spriteName="die";
	    		stunned=true;
	    	}
    	}
    }
    public void drawImage(Graphics g, MainGame b){
    	// draws the sprite onto a doublebuffered image
    	Image tmp = sprites.get(spriteName)[(int)spriteNum%sprites.get(spriteName).length];
    	int x=getX()+490;
    	int y=getY()+495;
    	int w=tmp.getWidth(b);
    	int h=tmp.getHeight(b);
    	if(spriteName.equals("hurt")){
    		y-=10;
    		x+=5;
    	}
    	if(facing==1){
	    	g.drawImage(tmp,x,y,w,h,b);
    	}
    	else{
    		g.drawImage(tmp,x+40,y,-w,h,b);
    	}
    	if(hp>0){
    		// bullet charge sprite
	    	if(bulletCharge>10){
	    		g.drawImage(sprites.get("charging")[bulletCharge%9],x-25,y-25,b);
	    	}
	    	if(bulletCharge>20){
	    		g.drawImage(sprites.get("full_charge")[bulletCharge%4],x-15,y-5,b);
	    	}
    	}
//    	g.drawImage(tmp,x+w,y,x,y+h,x,y,x+w,y+h,b);
    }
    
    public Color colFromMap(double ay,double ax,Platform[][] maps){
    	int x=(int)ax;
    	int y=(int)ay;
    	return maps[(80+y)%80][(x+100)%100].getColor();
    }
    
    public String typeFromMap(double ay,double ax,Platform[][] maps){
    	int x=(int)ax;
    	int y=(int)ay;
    	return maps[(80+y)%80][(x+100)%100].getType();
    }
    
    
    public void move(Platform[][] maps, LinkedList<Bullet> stuff){
    	// main movement
    	// maps is the all the platforms in the level
    	// stuff is friendly bullets
    	
    	int dash=0; // had a dash feature, but was removed
    	String oriSprite=spriteName;
		if(invinced>0){
			spriteName="hurt";
		}
		else if(getOnWall() && vy>0){
			spriteName="jump_wall";
		}
		else if(landingCD!=0 || inAir){
			spriteName="jump";
		}
		else if(shootCD!=0){
			spriteName="shoot";
		}
		else if(vx==0){
			spriteName="rest";
			if(hp<40){
				spriteName+="_hurt";
			}
		}
		else{
    		spriteName="walk";
		}
		invinced=Math.max(0,invinced-1);
		shootCD=Math.max(0,shootCD-1);
		landingCD=Math.max(0,landingCD-1);
		if(!stunned){
			
			if(shoot){
				// shooting
				spriteName="shoot";
				shootCD=10;
				int tmp=facing;
				if (getOnWall()){
					tmp*=-1;
				}
				if(bulletCharge<10){
					stuff.add(new Bullet(getX(),getY()+5,5,tmp*20,0));
				}
				else if(bulletCharge<20){
					stuff.add(new Bullet(getX(),getY()+5,15,tmp*20,0));
				}
				else{
					stuff.add(new Bullet(getX(),getY()+5,30,tmp*20,0));
				}
				shoot=false;
				bulletCharge=0;
			}
			if( typeFromMap((getY())/20,(getX()+10+vx*1.5+dash*(vx/10))/20,maps).equals("portal") ||
	    		typeFromMap((getY()+10)/20,(getX()+10+vx*1.5+dash*(vx/10))/20,maps).equals("portal") ||
	    		typeFromMap((getY()+20)/20,(getX()+10+vx*1.5+dash*(vx/10))/20,maps).equals("portal") ||
	    		typeFromMap((getY()+30)/20,(getX()+10+vx*1.5+dash*(vx/10))/20,maps).equals("portal") ||
	    		typeFromMap((getY()+35)/20,(getX()+10+vx*1.5+dash*(vx/10))/20,maps).equals("portal")){
	    		// touching the portal
	    		boss=true;
	    		moveRect(50,500);
	    	}
	    	if( typeFromMap((getY())/20,(getX()+10+vx*1.5+dash*(vx/10))/20,maps).equals("wall") ||
	    		typeFromMap((getY()+10)/20,(getX()+10+vx*1.5+dash*(vx/10))/20,maps).equals("wall") ||
	    		typeFromMap((getY()+20)/20,(getX()+10+vx*1.5+dash*(vx/10))/20,maps).equals("wall") ||
	    		typeFromMap((getY()+30)/20,(getX()+10+vx*1.5+dash*(vx/10))/20,maps).equals("wall") ||
	    		typeFromMap((getY()+35)/20,(getX()+10+vx*1.5+dash*(vx/10))/20,maps).equals("wall")){
	    		// touching the wall
	    		maxVy=5;
	    	}
	    	else{
	    		maxVy=30;
		    	moveRect((getX()+vx+2000+dash*(vx/10))%2000,(getY())%1600);
	    	}
	    	if(inAir==false && colFromMap((getY()+45)/20,(getX()+1)/20,maps)==Color.WHITE &&
	    		colFromMap((getY()+45)/20,(getX()+19)/20,maps)==Color.WHITE){
	    		// falling in air
	    		inAir=true;
	    		vy=0;
	    		spriteName="jump";
	    		spriteNum=6;
	    	}
	    	
	    	//Ladder
	    	if(down){
		    	if( colFromMap((getY()+45)/20,(getX()+1)/20,maps)==Color.GREEN &&
		    		colFromMap((getY()+45)/20,(getX()+19)/20,maps)==Color.GREEN){
	    			moveRect((getX())%2000,(getY()+10+1600)%1600);
	    			inAir=false;
	    			vy=0;
		    	}
	    	}
	    	if(up){
		    	if ( ( colFromMap((getY())/20,(getX()+1)/20,maps)==Color.GREEN &&
		    		colFromMap((getY())/20,(getX()+19)/20,maps)==Color.GREEN)||
		    		( colFromMap((getY()+20)/20,(getX()+1)/20,maps)==Color.GREEN &&
		    		colFromMap((getY()+20)/20,(getX()+19)/20,maps)==Color.GREEN)){
	    			moveRect((getX())%2000,(getY()-10+1600)%1600);
	    			vy=0;
	    			inAir=false;
		    	}
	    	}
	    	
	    	
	    	if(vy>0){
	    		// falling
				if(!getOnWall()){
					spriteName="jump";
					if(spriteNum>6){spriteNum=7;}
				}
	    		for(int i=0;i<vy+5;i+=5){
	    			if( typeFromMap((getY()+i+40)/20,(getX()+19)/20,maps).equals("wall") ||
	    				typeFromMap((getY()+i+40)/20,(getX()+1)/20,maps).equals("wall")){
	    				vy=i;
	    				inAir=false;
	    				landingCD=3;
	    				break;
	    			}
	    		}
	    	}
	    	else if(vy<0){
	    		// raising
	    		spriteName="jump";
	    		for(int i=0;i>vy-5;i-=5){
	    			if( typeFromMap((getY()+i-5)/20,(getX()+19)/20,maps).equals("wall") ||
	    				typeFromMap((getY()+i-5)/20,(getX()+1)/20,maps).equals("wall")){
	    				vy=i;
	    				inAir=false;
	    				break;
	    			}
	    		}
	    	}
    		moveRect((getX())%2000,(getY()+vy+1600)%1600);
			if(inAir){
				// gravity
				vy=Math.min(vy+5,maxVy);
			}
		}
		else{
			// stunned
			vx=0;
			vy=0;
		}
    	
		if( colFromMap((getY())/20,(getX()+10)/20,maps)==Color.RED ||
			colFromMap((getY()+10)/20,(getX()+10)/20,maps)==Color.RED ||
			colFromMap((getY()+20)/20,(getX()+10)/20,maps)==Color.RED ||
			colFromMap((getY()+30)/20,(getX()+10)/20,maps)==Color.RED ||
			colFromMap((getY()+40)/20,(getX()+10)/20,maps)==Color.RED){
			// touching red, instant death
			receiveDmg(100);
		}
		
		spriteNum+=1;
		if(oriSprite.equals("die")){
			spriteName="die";
		}
		if(!spriteName.equals(oriSprite)){
			spriteNum=0;
		}
    }
    
}