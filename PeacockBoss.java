import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

// PeacockBoss Class
// One of the four bosses in game
// It will disappear, and spawn near the player and trys to attack the player
// if the player is in range, it will expend its plumage and attack the player
// if the player is above the boss, it will fly up and attack the player
// if the player is out of range, it will simply disappear again
// during rage mode, it will lock on to the player, and shoots a feather that will chase the player

public class PeacockBoss {
	
	private Rectangle rect; // a Rectangle object representing the character
	private int vx,vy; // character's movement velocity 
	private int hp,dmg; // health point  and damage
	private int invinced,counter; // counter for invincibility and movement
	private int rageCounter; // rage counter
	private int stage; // which stage of attacking and moving
	private String spriteName; // sprite name
	private double spriteNum; // counter for sprites
	private HashMap<String,Image[]>sprites; // sprite
	private boolean active,reset; // flag for activated and able to be reset
	private boolean rage,inAir,stop; // flag for raged, in air, and stop moving
	private int ox,oy; // original position

    public PeacockBoss(int x,int y) {
    	ox=x;
    	oy=y;
    	vx=0;
    	vy=0;
    	stage=0;
    	invinced=0;
    	rageCounter=0;
    	counter=0;
    	hp=1000;
    	dmg=20;
    	spriteName="spawn";
    	spriteNum=0;
    	
    	inAir=true;
    	rage=false;
    	stop=false;
    	active=false;
    	reset = true;
	    rect = new Rectangle(ox,oy,50,50);
    	loadSprites();
    }
    public Rectangle getRect(){return rect;}
    public int getX(){return (int)(rect.getX());}
    public int getY(){return (int)(rect.getY());}
    public int getWidth(){return (int)(rect.getWidth());}
    public int getHeight(){return (int)(rect.getHeight());}
    public int getHealth(){return hp;}
    public int getDmg(){return dmg;}
    public void receiveDmg(int n){
    	if(invinced==0 && !spriteName.equals("hide")){
    		hp-=n;
    		invinced=30;
    	}
    	if(hp<=0){
    		stop=true;
    		spriteName="die";
    	}
    	
    }
    public boolean alive(){return hp>0;}
    
    public boolean activated(){return active;}
    public void setReset(boolean n){reset=n;}
    public void activate(){
    	if(!active && reset){
    		vx=0;
	    	vy=0;
	    	stage=0;
	    	invinced=0;
	    	rageCounter=0;
	    	counter=0;
	    	hp=380;
	    	dmg=20;
	    	spriteName="spawn";
	    	spriteNum=0;
	    	
	    	inAir=true;
	    	rage=false;
	    	stop=false;
	    	active=true;
	    	reset = false;
		    rect = new Rectangle(ox,oy,50,50);
    	}
    }
    public void deActivate(){
    	active=false;
    }
    
    public void loadSprites(){
    	sprites= new HashMap<String,Image[]>();
    	String tmp;
    	int num;
    	
    	tmp="spawn";
    	num=18*2;
    	sprites.put(tmp,new Image[num]);
    	for(int i=0;i<num;i++){
    		sprites.get(tmp)[i]=new ImageIcon("Sprites\\peacock_"+tmp+"\\peacock_"+tmp+" "+i/2+".png").getImage();
    	}
    	tmp="fly";
    	num=9;
    	sprites.put(tmp,new Image[num]);
    	for(int i=0;i<num;i++){
    		sprites.get(tmp)[i]=new ImageIcon("Sprites\\peacock_"+tmp+"\\peacock_"+tmp+" "+i+".png").getImage();
    	}
    	tmp="appear";
    	num=3*2;
    	sprites.put(tmp,new Image[num]);
    	for(int i=0;i<num;i++){
    		sprites.get(tmp)[i]=new ImageIcon("Sprites\\peacock_"+tmp+"\\peacock_"+tmp+" "+i/2+".png").getImage();
    	}
    	tmp="fade";
    	num=3*2;
    	sprites.put(tmp,new Image[num]);
    	for(int i=0;i<num;i++){
    		sprites.get(tmp)[i]=new ImageIcon("Sprites\\peacock_"+tmp+"\\peacock_"+tmp+" "+i/2+".png").getImage();
    	}
    	tmp="expend";
    	num=17*2;
    	sprites.put(tmp,new Image[num]);
    	for(int i=0;i<num;i++){
    		sprites.get(tmp)[i]=new ImageIcon("Sprites\\peacock_"+tmp+"\\peacock_"+tmp+" "+i/2+".png").getImage();
    	}
    	tmp="hide";
    	num=1;
    	sprites.put(tmp,new Image[num]);
    	for(int i=0;i<num;i++){
    		sprites.get(tmp)[i]=new ImageIcon("Sprites\\peacock_"+tmp+"\\peacock_"+tmp+" "+i+".png").getImage();
    	}
    	tmp="rest";
    	num=1;
    	sprites.put(tmp,new Image[num]);
    	for(int i=0;i<num;i++){
    		sprites.get(tmp)[i]=new ImageIcon("Sprites\\peacock_"+tmp+"\\peacock_"+tmp+" "+i+".png").getImage();
    	}
    	tmp="aim";
    	num=15*2;
    	sprites.put(tmp,new Image[num]);
    	for(int i=0;i<num;i++){
    		sprites.get(tmp)[i]=new ImageIcon("Sprites\\peacock_"+tmp+"\\peacock_"+tmp+" "+i/2+".png").getImage();
    	}
    	tmp="fire";
    	num=1;
    	sprites.put(tmp,new Image[num]);
    	for(int i=0;i<num;i++){
    		sprites.get(tmp)[i]=new ImageIcon("Sprites\\peacock_"+tmp+"\\peacock_"+tmp+" "+i+".png").getImage();
    	}
		tmp="die";
		sprites.put(tmp,new Image[22]);
		for(int i=0;i<22;i++){
			sprites.get(tmp)[i]=new ImageIcon("Sprites\\explosion_gray\\explosion_gray "+i/2+".png").getImage();
		}
    	
    	
    }
    public void drawImage(Graphics g, MainGame b){
    	Image tmp = sprites.get(spriteName)[(int)spriteNum];
    	int x=getX()+460;
    	int y=getY()+450;
    	int w=tmp.getWidth(b);
    	int h=tmp.getHeight(b);
    	
    	if(spriteName.equals("fade") || spriteName.equals("appear")){
    		x+=40;
    		y+=20;
    	}
    	else if(spriteName.equals("expend")){
    		x-=30;
    		y-=20;
    	}
    	else if(spriteName.equals("aim")||spriteName.equals("fire")){
    		x+=20;
    		y+=10;
    	}
    	if(vx<=0){
	    	g.drawImage(tmp,x,y,w,h,b);
    	}
    	else{
    		g.drawImage(tmp,x+w,y,-w,h,b);
    	}
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
    public void moveRect(int a,int b){
    	rect.move(a,b);
    }
    public void move(Platform[][] maps, LinkedList<Bullet> stuff,X Ex,LinkedList<EnemyBullet> eb){
    	// movement
    	// maps is the all the platforms in the level
    	// stuff is friendly bullets
    	// Ex is the main character
    	// eBullets is enemy bullets
    	String oriSprite=spriteName;
    	
		invinced=Math.max(0,invinced-1);
	    counter++;
		if(!rage){
	    	// normal mode
	    	if(counter==36){
	    		// finished spawning
	    		spriteName="rest";
	    	}
	    	if(counter==50){
	    		// disappears
	    		spriteName="fade";
	    	}
	    	if(counter==56){
	    		// hides, takes no damage
	    		spriteName="hide";
	    		if(hp<400){
	    			// rage mode
	    			if(rageCounter==0){
		    			rage=true;
		    			if(!stop){
			    			moveRect(ox,oy);
		    			}
		    			counter=-10;
	    			}
	    			else{
	    				rageCounter--;
	    			}
	    		}
	    	}
	    	if(counter==75){
	    		// move nears the player
	    		if(!stop){
	    			moveRect(Math.min((Ex.getX()+50)/50*50,450),530);
	    		}
	    	}
	    	if(counter==80){
	    		// spawns
	    		spriteName="appear";
	    	}
	    	if(counter==86){
	    		spriteName="rest";
	    	}
	    	if(counter==100){
	    		// if player is near, attacks
	    		if(Math.abs(Ex.getX()-20-getX())<=100){
		    		if(Ex.getY()<getY()){
		    			counter=200;
		    			eb.add(new EnemyBullet("peakfly",getX()-20,getY()-40,20,0,0));
		    			spriteName="hide";
		    		}
		    		else{
			    		spriteName="expend";
		    		}
	    		}
	    		else{
	    			counter=49;
	    		}
	    	}
	    	if(counter==110){
	    		eb.add(new EnemyBullet("peak",getX()-70,getY()-80,20,0,0));
	    		spriteName="hide";
	    	}
	    	if(counter==133 || counter==215){
	    		// disappers
	    		counter=55;
	    	}
		}
		else{
			// raged
			if(counter==0){
				// apears
				rageCounter++;
				spriteName="appear";
			}
			if(counter==6){
				spriteName="rest";
			}
			if(counter==10){
				spriteName="aim";
			}
			if(counter==40){
				// shoots a aiming target first, then shoots 8 feathers
				spriteName="fire";
				eb.add(new EnemyBullet("aim",getX()+10,getY()+30,0,0,0));
			}
			if(counter%40==0 && counter!=40 && counter !=0){
				eb.add(new EnemyBullet("feather",getX()+10,getY()+10,15,0,0));
			}
			if(counter==360){
				spriteName="rest";
			}
			if(counter==365){
				spriteName="fade";
			}
			if(counter==370){
				spriteName="hide";
				if(!stop){
					moveRect((rageCounter%2==1)?100:400,oy);
				}
				vx=(rageCounter%2==1)?1:-1;
				counter=-10;
				if (rageCounter==4){
					rage=false;
					counter=55;
				}
			}
		}
    	
    	
    	
    	
    	if(!spriteName.equals("hide")){
	    	if(rect.intersects(Ex.getRect())){
	    		Ex.receiveDmg(dmg);
	    	}
			for(Bullet i: stuff){
				if(i.getRect().intersects(rect)){
					i.receiveDmg(100);
					receiveDmg(i.getDmg());
				}
			}
    		
    	}

		spriteNum=(spriteNum+1)%sprites.get(spriteName).length;
		if(oriSprite.equals("die")){
			spriteName="die";
		}
		if(!spriteName.equals(oriSprite)){
			spriteNum=0;
		}
    }
}