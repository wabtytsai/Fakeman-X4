import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

// Item Class
// there are 4 types of items which all have cooldowns, which disaapears when its up
// 2 for hp healing (big amount and small amount)
// 2 for weapon charging healing (big amount and small amount)
// although there is weapon charging, the game does not have the feature

public class Item {
	
	private boolean inAir; // in air
	private int vy; // verticle velocity 
	private int hp,wp,health; // amount healed for hp, weapon, and its own health
	private int coolDown; // cool down 
	private Rectangle rect; // rectangle
	private double spriteNum; // counter for sprite
	private Image[] sprites; //sprites

    public Item(int x,int y, int h,int e) {
    	health=100;
    	hp=h*20;
    	wp=e*20;
    	vy=0;
    	coolDown=200;
    	inAir=false;
    	rect = new Rectangle(x,y,10,10);
    	spriteNum = 0;
    	loadSprites();
    }
    public Rectangle getRect(){return rect;}
    public int getX(){return (int)(rect.getX());}
    public int getY(){return (int)(rect.getY());}
    public int getWidth(){return (int)(rect.getWidth());}
    public int getHeight(){return (int)(rect.getHeight());}
    public int getHeal(){return hp;}
    public int getWeapon(){return wp;}
    public void receiveDmg(int n){health-=n;}
    public boolean alive(){return health>0;}
    public void loadSprites(){
		if(hp==20){
			sprites=new Image[5];
			for(int i=0;i<5;i++){
				sprites[i]=new ImageIcon("Sprites\\items\\small_health "+i+".png").getImage();	
			}
		}
		else if(hp==40){
			sprites=new Image[7];
			for(int i=0;i<7;i++){
				sprites[i]=new ImageIcon("Sprites\\items\\big_health "+i+".png").getImage();	
			}
		}
		else if(wp==20){
			sprites=new Image[8];
			for(int i=0;i<8;i++){
				sprites[i]=new ImageIcon("Sprites\\items\\small_weapon "+i+".png").getImage();	
			}
		}
		else{
			sprites=new Image[8];
			for(int i=0;i<8;i++){
				sprites[i]=new ImageIcon("Sprites\\items\\small_weapon "+i+".png").getImage();	
			}
		}
		
		
    }
    public void drawImage(Graphics g, MainGame b){
    	Image tmp = sprites[(int)spriteNum%sprites.length];
    	int x=getX()+495;
    	int y=getY()+480;
    	int w=tmp.getWidth(b);
    	int h=tmp.getHeight(b);
    	if(hp==20){
    		y+=22;
    	}
    	if(hp==40){
    		y+=17;
    	}
    	if(wp==20){
    		y+=18;
    	}
    	if(wp==40){
    		y+=18;
    	}
    	
    	g.drawImage(tmp,x,y,w,h,b);
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
    public void move(Platform[][] maps,X Ex){
    	// movement
    	// maps is the all the platforms in the level
    	// Ex is the main character
		
		
		
		coolDown--;
		if(coolDown==0){
			// when times up, item disappears
			receiveDmg(100);
		}
		
		
    	
    	if(inAir==false && colFromMap((getY()+10)/20,(getX()+1)/20,maps)==Color.WHITE &&
    		colFromMap((getY()+10)/20,(getX()+1)/20,maps)==Color.WHITE){
    		// falling in air
    		inAir=true;
    		vy=0;
    	}
    	if(vy>0){
    		// falling
    		for(int i=0;i<vy+5;i+=5){
    			if( colFromMap((getY()+i+10)/20,(getX()+9)/20,maps)!=Color.WHITE ||
    				colFromMap((getY()+i+10)/20,(getX()+1)/20,maps)!=Color.WHITE){
    				vy=i;
    				inAir=false;
    				break;
    			}
    		}
    	}
    	else if(vy<0){
    		// raising
    		for(int i=0;i>vy-5;i-=5){
    			if( colFromMap((getY()+i-5)/20,(getX()+9)/20,maps)!=Color.WHITE ||
    				colFromMap((getY()+i-5)/20,(getX()+1)/20,maps)!=Color.WHITE){
    				vy=i;
    				inAir=false;
    				break;
    			}
    		}
    	}
    	
    	moveRect((getX())%2000,(getY()+vy+1600)%1600);
    	
    	if(Ex.getRect().intersects(rect)){
    		Ex.receiveDmg(-hp);
    		receiveDmg(100);
    	}
    	
		spriteNum+=.5;
		if(spriteNum==sprites.length){
			spriteNum-=3;
		}
		if(inAir){
			// gravity
			spriteNum=0;
			coolDown++;
			vy=Math.min(vy+10,20);
		}
    }
    
    
}