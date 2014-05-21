import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

// Bullet class
// This class stores all the friendly bullets

public class Bullet {
	
	private int vx,vy; // velocity
	private int dmg,hp; // damage, hit point
	private Rectangle rect; // rectangle 
	private HashMap<Integer,Image[]>sprites; // sprites
	private double spriteNum; // counter for sprites
	
    public Bullet(int x, int y, int admg, int avx, int avy) {
    	vx=avx;
    	vy=avy;
    	hp=1;
    	dmg=admg;
    	int s=0;
    	if(dmg==5){
    		// small bullet
    		s=10;
    		x-=10;
    	}
    	if(dmg==15){
    		// medium bullet
    		s=20;
    		y-=5;
    		
    	}
    	if(dmg==30){
    		// large bullet
    		s=30;
    		y-=10;
    	}
    	rect = new Rectangle(x,y,s,s);
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
    public boolean alive(){
    	return hp>0;
    }
    public void receiveDmg(int n){
    	hp-=n;
    }
    
    public void loadSprites(){
		sprites = new HashMap<Integer,Image[]>();
		sprites.put(30,new Image[6]);
		for(int i=0;i<6;i++){
			sprites.get(30)[i]=new ImageIcon("Sprites\\buster_large\\buster_large "+i+".png").getImage();
		}
		sprites.put(15,new Image[7]);
		for(int i=1;i<8;i++){
			sprites.get(15)[i-1]=new ImageIcon("Sprites\\buster_medium\\buster_medium "+i+".png").getImage();
		}
		sprites.put(5,new Image[4]);
		for(int i=0;i<4;i++){
			sprites.get(5)[i]=new ImageIcon("Sprites\\buster_small\\buster_small "+i+".png").getImage();
		}
    }
    public void drawImage(Graphics g, MainGame b){
    	// draws sprites onto doublebuffered image
    	Image tmp = sprites.get(dmg)[(int)spriteNum];
    	int x=getX()+495;
    	int y=getY()+480;
    	int w=tmp.getWidth(b);
    	int h=tmp.getHeight(b);
    	if(dmg==5){
    		y+=10;
    		if(vx<0){
    			x-=20;
    		}
    	}
    	if(dmg==15){
    		x-=5;
    		y+=5;
    	}
    	if(dmg==30){
    		x-=20;
    		y+=10;
    		if(vx<0){
    			x+=40;
    		}
    	}
    	if(vx>0){
		    g.drawImage(tmp,x,y+10,w,h,b);
    	}
    	else{
    		g.drawImage(tmp,x+40,y+10,-w,h,b);
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
    
    
    public void move(Platform[][] maps){
    	// movement
    	// maps is the all the platforms in the level
    	spriteNum+=.75;
    	if(spriteNum>=sprites.get(dmg).length){
    		spriteNum-=2;
    	}
    	moveRect(getX()+vx,getY()+vy);
    	if(	typeFromMap((getY()+getWidth()/2)/20,(getX()+getWidth()/2)/20,maps).equals("wall")){
    		receiveDmg(100);
    	}
    }
    
    
}