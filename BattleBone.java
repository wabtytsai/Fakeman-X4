import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

// BattleBone Class
// BattleBone is a bat-like enemy who will carry a bomb and try to drop onto X's head

public class BattleBone {
	
	private boolean inAir; // check if its in air
	private boolean active,reset; // whether is activated and wheter it is able to be reset
	private int hp,dmg; // hit point and damage
	private int vx,vy; // velocity 
	private int bombCD; // cooldown for dropping bomb
	private boolean bombDropped; // flag for bomb dropped
	private boolean reached; // if it reached the desire height
	private Rectangle rect; // rectangle
	private String spriteName; // sprite name
	private double spriteNum; // counter for sprite
	private HashMap<String,Image[]>sprites; // sprites
	private int ox,oy; // original position

    public BattleBone(int x,int y) {
    	ox=x;
    	oy=y;
    	hp=30;
    	dmg=10;
    	vx=0;
    	vy=0;
    	bombCD=0;
    	bombDropped=false;
    	reset=true;
    	reached=false;
    	inAir=false;
    	active=false;
    	rect = new Rectangle(ox,oy,20,20);
    	spriteName = "with_bomb";
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
    public boolean activated(){return active;}
    public void setReset(boolean n){reset=n;}
    public void activate(){
    	// activate and reset stats
    	if(!active && reset){
	    	active=true;
	    	hp=30;
	    	dmg=10;
	    	vx=0;
	    	vy=0;
	    	bombCD=0;
	    	bombDropped=false;
	    	reached=false;
	    	inAir=false;
	    	rect = new Rectangle(ox,oy,20,20);
	    	spriteName = "with_bomb";
	    	spriteNum = 0;
	    	loadSprites();
    	}
    }
    public void deActivate(){
    	active=false;
    }
    public void receiveDmg(int n){hp-=n;}
    
    public void loadSprites(){
    	//load sprites
    	// maps string to image[]
		sprites = new HashMap<String,Image[]>();
    	String tmp="no_bomb";
		sprites.put(tmp,new Image[6]);
		for(int i=0;i<6;i++){
			sprites.get(tmp)[i]=new ImageIcon("Sprites\\BB_"+tmp+"\\BB_"+tmp+" "+i+".png").getImage();
		}
		tmp="drop_bomb";
		sprites.put(tmp,new Image[2]);
		for(int i=0;i<2;i++){
			sprites.get(tmp)[i]=new ImageIcon("Sprites\\BB_"+tmp+"\\BB_"+tmp+" "+i+".png").getImage();
		}
		tmp="with_bomb";
		sprites.put(tmp,new Image[4]);
		for(int i=0;i<4;i++){
			sprites.get(tmp)[i]=new ImageIcon("Sprites\\BB_"+tmp+"\\BB_"+tmp+" "+i+".png").getImage();
		}
		sprites.put("bomb",new Image[3]);
		for(int i=0;i<3;i++){
			sprites.get("bomb")[i]=new ImageIcon("Sprites\\enemy_bullet\\BB_bullet "+i+".png").getImage();
		}
		
    }
    public void drawImage(Graphics g, MainGame b){
    	//draws the sprite onto g, which is a doublebuffered image
    	Image tmp = sprites.get(spriteName)[(int)spriteNum%sprites.get(spriteName).length];
    	int x=getX()+495;
    	int y=getY()+480;
    	int w=tmp.getWidth(b);
    	int h=tmp.getHeight(b);
    	if(!bombDropped || bombCD>0){
    		y+=20;
    	}
    	if(vx<0){
	    	g.drawImage(tmp,x,y,w,h,b);
	    	if(!bombDropped){
	    		g.drawImage(sprites.get("bomb")[(int)spriteNum%3],x+5,y+20,b);
	    	}
    	}
    	else{
    		g.drawImage(tmp,x+30,y,-w,h,b);
	    	if(!bombDropped){
	    		g.drawImage(sprites.get("bomb")[(int)spriteNum%3],x,y+20,b);
	    	}
    	}
//    	g.drawImage(tmp,x+w,y,x,y+h,x,y,x+w,y+h,b);
    }
    
    
    public Color colFromMap(double ay,double ax,Platform[][] maps){
    	// get colour from map
    	int x=(int)ax;
    	int y=(int)ay;
    	return maps[(80+y)%80][(x+100)%100].getColor();
    }
    public String typeFromMap(double ay,double ax,Platform[][] maps){
    	// get type from map
    	int x=(int)ax;
    	int y=(int)ay;
    	return maps[(80+y)%80][(x+100)%100].getType();
    }
    public void moveRect(int a,int b){
    	if(a<0 || a>3000 || b<0 || b>2600){
    		deActivate();
    	}
    	else{
	    	rect.move(a,b);
    	}
    }
    public boolean alive(){
    	return hp>0;
    }
    public void move(Platform[][] maps, LinkedList<Bullet> stuff,X Ex,LinkedList<EnemyBullet> eBullets){
    	// movement
		String oriSprite=spriteName;
		
		
		// facing which direction
		int dir=1;
		if(Ex.getX()<getX()){
			dir=-1;
		}
		if(vx==0){
			// change velocity only once
			vx=dir*5;
			vy=(int)(Math.sqrt(100-vx*vx));
			if(Ex.getY()-80<getY()){
				vy*=-1;
			}
		}
    	int x = getX()-Ex.getX()-vx*2;
    	if(Math.abs(x)<10){
    		// if its close to X, drop the bomb
    		if(!bombDropped){
	    		bombCD=4;
				eBullets.add(new EnemyBullet("BB",getX(),getY(),10,0,10));
    			vy*=-1;
    		}
    		spriteName="drop_bomb";
    		bombDropped=true;
    	}
		int ax,ay;
		ax=vx;
		ay=vy;
		if(Math.abs(Ex.getY()-80-getY())<10){
			reached=true;
		}
		if(reached && !bombDropped){
			ay=0;
		}
		
    	if(bombDropped){
    		if(bombCD>0){
    			bombCD--;
    		}
    		else{
    			spriteName="no_bomb";
				moveRect((getX()+ax),(getY()+ay));
    		}
    	}
    	else{    		
	    	moveRect((getX()+ax),(getY()+ay));
    	}
    	
		for(Bullet i: stuff){
			if(i.getRect().intersects(rect)){
				i.receiveDmg(100);
				receiveDmg(i.getDmg());
			}
		}
		spriteNum+=0.5;
		if(!spriteName.equals(oriSprite)){
			spriteNum=0;
		}
    }
    
    
}