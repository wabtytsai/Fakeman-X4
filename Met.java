import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

// Met Class
// met is an enemy who will walk straight until an obstacle is hit
// it will occasionally hide under the helmet which, during the time, it takes no damage
// when player is seen at level with met, it will attack the player by shooting a bullet towards the player

public class Met {
	
	private boolean inAir; // check if it is in air
	private boolean active,reset; // activated or able to be reset
	private int hp,dmg; // hit point and damage
	private int vx,vy; // velocity 
	private int hideCD,shootCD; // cooldown for hiding and shooting 
	private Rectangle rect; // rectangle 
	private String spriteName; // sprite name 
	private double spriteNum; // counter for sprite 
	private HashMap<String,Image[]>sprites; // sprites
	private int ox,oy; // original position

    public Met(int x,int y) {
    	ox=x;
    	oy=y;
    	hp=30;
    	dmg=10;
    	vx=5;
    	vy=0;
    	hideCD=0;
    	shootCD=0;
    	inAir=false;
    	reset=true;
    	active=false;
    	rect = new Rectangle(ox,oy,20,20);
    	spriteName = "walk";
    	spriteNum = 0;
    	loadSprites();
    }
    public Rectangle getRect(){return rect;}
    public int getX(){return (int)(rect.getX());}
    public int getY(){return (int)(rect.getY());}
    public int ox(){return ox;}
    public int oy(){return oy;}
    public int getWidth(){return (int)(rect.getWidth());}
    public int getHeight(){return (int)(rect.getHeight());}
    public int getHealth(){return hp;}
    public int getDmg(){return dmg;}
    public void receiveDmg(int n){hp-=n;}
    public boolean alive(){return hp>0;}
    
    public boolean activated(){return active;}
    public void setReset(boolean n){reset=n;}
    public void activate(){
    	// activates and reset all stats
    	if(!active && reset){
	    	active=true;
	    	hp=30;
	    	dmg=10;
	    	vx=5;
	    	vy=0;
	    	hideCD=0;
	    	shootCD=0;
	    	inAir=false;
	    	rect = new Rectangle(ox,oy,20,20);
	    	spriteName = "walk";
	    	spriteNum = 0;
	    	loadSprites();
    	}
    }
    public void deActivate(){
    	active=false;
    }
    
    public void loadSprites(){
		sprites = new HashMap<String,Image[]>();
    	String tmp="walk";
		sprites.put(tmp,new Image[8]);
		for(int i=0;i<8;i++){
			sprites.get(tmp)[i]=new ImageIcon("Sprites\\met_"+tmp+"\\met_"+tmp+" "+i+".png").getImage();
		}
		tmp="hide";
		sprites.put(tmp,new Image[4]);
		for(int i=0;i<4;i++){
			sprites.get(tmp)[i]=new ImageIcon("Sprites\\met_"+tmp+"\\met_"+tmp+" "+i+".png").getImage();
		}
		tmp="shoot";
		sprites.put(tmp,new Image[5]);
		for(int i=0;i<5;i++){
			sprites.get(tmp)[i]=new ImageIcon("Sprites\\met_"+tmp+"\\met_"+tmp+" "+i+".png").getImage();
		}
		
    }
    public void drawImage(Graphics g, MainGame b){
    	// draws the image to Graphics g, which is a doublebuffered image
    	Image tmp = sprites.get(spriteName)[(int)spriteNum%sprites.get(spriteName).length];
    	int x=getX()+495;
    	int y=getY()+480;
    	int w=tmp.getWidth(b);
    	int h=tmp.getHeight(b);
    	y+=5;
    	if(spriteName.equals("hide")){
    		y+=5;
    	}
    	if(vx<0){
	    	g.drawImage(tmp,x,y,w,h,b);
    	}
    	else{
    		g.drawImage(tmp,x+30,y,-w,h,b);
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
    public void moveRect(int a,int b){
    	rect.move((a+2000)%2000,(b+1600)%1600);
    }
    public void move(Platform[][] maps, LinkedList<Bullet> stuff,X Ex,LinkedList<EnemyBullet> eBullets){
    	// movement
    	// maps is the all the platforms in the level
    	// stuff is friendly bullets
    	// Ex is the main character
    	// eBullets is enemy bullets
    	
		String oriSprite=spriteName;
		
		shootCD=Math.max(shootCD-1,0); // shooting cooldown
		if(!inAir && shootCD<40){
			hideCD--;
		}
		
		if(shootCD>40){
			spriteName="shoot";
		}
		else if(hideCD<=0){
			spriteName="hide";
			spriteNum=Math.min(spriteNum,3);
			if(hideCD==-20){
				hideCD=50;
			}
		}
		else{
			spriteName="walk";
		}
		
		if(vx>0 && shootCD==0 && Ex.getX()>getX() && Ex.getY()+20==getY()){
			// shoots if cooldown is off, and sees player in front
			shootCD=50;
			eBullets.add(new EnemyBullet("met",getX(),getY(),10,10,0));
		}
		else if(vx<0 && shootCD==0 && Ex.getX()<getX() && Ex.getY()+20==getY()){
			shootCD=50;
			eBullets.add(new EnemyBullet("met",getX(),getY(),10,-10,0));
		}
		
    	if( typeFromMap((getY()+5)/20,(getX()+10+vx*1.5)/20,maps).equals("wall") ||
    		typeFromMap((getY()+10)/20,(getX()+10+vx*1.5)/20,maps).equals("wall") ||
    		typeFromMap((getY()+15)/20,(getX()+10+vx*1.5)/20,maps).equals("wall") ||
    		colFromMap((getY()+25)/20,(getX()+10+vx*1.5)/20,maps)==Color.WHITE){
    		// touching the wall
    		if(!inAir && hideCD>0){
	    		vx*=-1;
    		}
    	}
    	else{
    		if(hideCD>0 && shootCD<40){
	    		moveRect((getX()+vx+2000)%2000,(getY())%1600);
    		}
    	}

    	if(inAir==false && colFromMap((getY()+25)/20,(getX()+1)/20,maps)==Color.WHITE &&
    		colFromMap((getY()+25)/20,(getX()+19)/20,maps)==Color.WHITE){
    		// in air, drops down
    		inAir=true;
    		vy=0;
    	}
    	if(vy>0){
    		// falling
    		for(int i=0;i<vy+5;i+=5){
    			if( typeFromMap((getY()+i+20)/20,(getX()+19)/20,maps).equals("wall") ||
    				typeFromMap((getY()+i+20)/20,(getX()+1)/20,maps).equals("wall")){
    				vy=i;
    				inAir=false;
    				break;
    			}
    		}
    	}
    	else if(vy<0){
    		// raising
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
    	
    	
		for(Bullet i: stuff){
			if(i.getRect().intersects(rect)){
				i.receiveDmg(100);
				if(hideCD>0){
					receiveDmg(i.getDmg());
				}
			}
		}
		
    	
		if(inAir){
			// gravity
			vy=Math.min(vy+5,20);
		}
		spriteNum+=0.5;
		if(!spriteName.equals(oriSprite)){
			spriteNum=0;
		}
//    	spriteNum=(spriteNum+1)%sprites.get(spriteName).length;
    }
    
    
}