import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

// SmallSpider Class
// SmallSpider is minion spawned by the SpiderBoss during rage mode
// it crawls towards the player and climbs up the wall apon contact


public class SmallSpider {

	private boolean inAir;  // check if its in air
	private int hp,dmg; // hit point and damage
	private int vx,vy; // velocity 
	private Rectangle rect; // rectangle 
	private String spriteName; // sprite name
	private double spriteNum; // counter for the sprite
	private HashMap<String,Image[]>sprites; // sprites
	
    public SmallSpider(int x,int y, int avx, int avy) {
    	hp=30;
    	dmg=10;
    	vx=avx;
    	vy=avy;
    	inAir=true;
    	rect = new Rectangle(x,y,20,20);
    	spriteName = "fly";
    	spriteNum = 0;
    	loadSprites();
    }
    
    public Rectangle getRect(){return rect;}
    public int getX(){return (int)(rect.getX());}
    public int getY(){return (int)(rect.getY());}
    public int getWidth(){return (int)(rect.getWidth());}
    public int getHeight(){return (int)(rect.getHeight());}
    public int getHealth(){return hp;}
    public int getDmg(){return dmg;}
    public void receiveDmg(int n){hp-=n;}
    public boolean alive(){return hp>0;}
    
    public void loadSprites(){
		sprites = new HashMap<String,Image[]>();
    	String tmp="fly";
    	int num;
		sprites.put(tmp,new Image[1]);
		for(int i=0;i<1;i++){
			sprites.get(tmp)[i]=new ImageIcon("Sprites\\sspider_down\\sspider_down 0.png").getImage();
		}
    	tmp="down";
    	num=4;
    	sprites.put(tmp,new Image[num]);
    	for(int i=0;i<num;i++){
    		sprites.get(tmp)[i]=new ImageIcon("Sprites\\sspider_"+tmp+"\\sspider_"+tmp+" "+i+".png").getImage();
    	}
    	tmp="side";
    	num=4;
    	sprites.put(tmp,new Image[num]);
    	for(int i=0;i<num;i++){
    		sprites.get(tmp)[i]=new ImageIcon("Sprites\\sspider_"+tmp+"\\sspider_"+tmp+" "+i+".png").getImage();
    	}
		
    }
    
    public void drawImage(Graphics g, MainGame b){
    	// draw sprites onto a doublebuffered image
    	Image tmp = sprites.get(spriteName)[(int)spriteNum%sprites.get(spriteName).length];
    	int x=getX()+495;
    	int y=getY()+500;
    	int w=tmp.getWidth(b);
    	int h=tmp.getHeight(b);
    	if(vx<0){
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
    	rect.move((a+2000)%2000,(b+1600)%1600);
    }
    public void move(Platform[][] maps, LinkedList<Bullet> stuff,X Ex){
    	// movement
    	// maps is the all the platforms in the level
    	// stuff is friendly bullets
    	// Ex is the main character
    	// eBullets is enemy bullets
    	
		String oriSprite=spriteName;
		
		
		if( typeFromMap((getY()+5)/20,(getX()+10+vx*1.5)/20,maps).equals("wall") ||
    		typeFromMap((getY()+10)/20,(getX()+10+vx*1.5)/20,maps).equals("wall") ||
    		typeFromMap((getY()+15)/20,(getX()+10+vx*1.5)/20,maps).equals("wall")){
    		// touching the wall
    		if(!inAir){
    			// climbs
	    		vy=-5;
	    		spriteName="side";
    		}
    	}
    	else{
    		moveRect((getX()+vx)%2000,(getY())%1600);
    	}
    	
    	if(spriteName.equals("side")){
	    	if( typeFromMap((getY()+10+vy*1.5)/20,(getX()+10)/20,maps).equals("wall") ||
	    		typeFromMap((getY()+10+vy*1.5)/20,(getX()+10)/20,maps).equals("wall") ||
	    		typeFromMap((getY()+10+vy*1.5)/20,(getX()+10)/20,maps).equals("wall")){
	    		// touching the ceiling - instant death
	    		receiveDmg(1000);
	    	}
    	}
    	else{
    		// if climbing on the wall, gravity doesn't affect 

	    	if(inAir==false && colFromMap((getY()+25)/20,(getX()+1)/20,maps)==Color.WHITE &&
	    		colFromMap((getY()+55)/20,(getX()+19)/20,maps)==Color.WHITE){
	    		inAir=true;
	    		vy=0;
	    	}
	    	
	    	if(vy>0 ){
	    		// falling
	    		for(int i=0;i<vy+5;i+=5){
	    			if( typeFromMap((getY()+i+20)/20,(getX()+19)/20,maps).equals("wall") ||
	    				typeFromMap((getY()+i+20)/20,(getX()+1)/20,maps).equals("wall")){
	    				vy=i;
	    				if(Ex.getX()>getX()){
	    					vx=5;
	    				}
	    				else{
	    					vx=-5;
	    				}
	    				spriteName="down";
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
    	}
    	moveRect((getX())%2000,(getY()+vy+1600)%1600);
    	
    	
		for(Bullet i: stuff){
			if(i.getRect().intersects(rect)){
				i.receiveDmg(100);
				receiveDmg(i.getDmg());
				
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
    }
}