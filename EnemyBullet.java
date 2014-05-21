import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

//EnemyBullet Class
// this class stores all the enemy bullet take can harm the player
// most have their own path to follow, and have varing size and sprites

public class EnemyBullet {
	
	private String type; // type of bullets
	private int vx,vy,speed; // velocity and speed
	private int dmg,hp; // damage and hit point
	private int counter; // counter for how long it has existed
	private Rectangle rect; // rectangle
	private Image[] sprites; // sprites
	private double spriteNum; // counter for sprites
	private boolean hitWall; // if hitting the wall will make the bullet disappear
	private boolean isPeacock; // if the bullets are spawned by peacock boss
	private int featherType; // the feather type
	private int owlType; // the type of owl bullet
	
    public EnemyBullet(String atype,int x, int y, int admg, int avx, int avy) {
    	type=atype;
    	vx=avx;
    	vy=avy;
    	hp=1;
    	counter=0;
    	dmg=admg;
    	int w=5;
    	int h=5;
    	isPeacock=false;
    	hitWall=true;
    	speed=18;
    	featherType=0;
    	owlType=0;
    	if(type.equals("BB")){
    		// battle bone
    		w=20;
    		h=30;
    		hitWall=false;
    	}
    	else if(type.equals("web")){
    		// spider web
    		hp=100;
    		w=50;
    		h=50;
    		hitWall=false;
    	}
    	else if(type.equals("mushroom")){
    		// mushroom spore
    		hp=10000;
    		w=50;
    		h=50;
    	}
    	else if(type.equals("peak")){
    		// peacock plumage
    		isPeacock=true;
    		hp=10000;
    		w=200;
    		h=120;
    	}
    	else if(type.equals("peakfly")){
    		// peacock flying
    		isPeacock=true;
    		hp=10000;
    		w=120;
    		h=100;
    	}
    	else if(type.equals("aim")){
    		// peacock's target
    		isPeacock=true;
    		hp=10;
    		w=30;
    		h=30;
    		speed=40;
    	}
    	else if(type.equals("feather")){
    		// peacock's feather
    		isPeacock=true;
    		w=30;
    		h=30;
    		speed=15;
    	}
    	else if(type.equals("armor")){
    		// armor
    		hp=100000;
    		w=100;
    		h=100;
    	}
    	else if(type.equals("owl")){
    		// owl's bullets
    		if (Math.abs(vx)==11){
    			owlType=1;
    			if(vy>0){
    				owlType=3;
    			}
    		}
    		if (Math.abs(vx)==14){
    			owlType=2;
    			if(vy>0){
    				owlType=4;
    			}
    		}
    	}
    	else if(type.equals("cyclone")){
    		// owl's cyclone
    		w=20;
    		h=20;
    		if(vx!=0){
    			counter=-10;
    		}
    	}
    	rect = new Rectangle(x,y,w,h);
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
    public boolean alive(){return hp>0;}
    public void receiveDmg(int n){hp-=n;}
    
    public void loadSprites(){
		if(type.equals("met")){
			sprites=new Image[2];
			for(int i=0;i<2;i++){
				sprites[i]=new ImageIcon("Sprites\\enemy_bullet\\"+type+"_bullet "+i+".png").getImage();
			}
		}
		else if(type.equals("BB")){
			sprites=new Image[3];
			for(int i=0;i<3;i++){
				sprites[i]=new ImageIcon("Sprites\\enemy_bullet\\"+type+"_bullet "+i+".png").getImage();
			}
		}
		else if(type.equals("mushroom")){
			sprites=new Image[13];
			for(int i=0;i<13;i++){
				sprites[i]=new ImageIcon("Sprites\\enemy_bullet\\"+type+"_bullet "+i+".png").getImage();
			}
			
		}
		else if(type.equals("peak")){
			sprites=new Image[9];
			for(int i=0;i<9;i++){
				sprites[i]=new ImageIcon("Sprites\\enemy_bullet\\"+type+"_bullet "+i+".png").getImage();
			}
			
		}
		else if(type.equals("peakfly")){
			sprites=new Image[9];
			for(int i=0;i<9;i++){
				sprites[i]=new ImageIcon("Sprites\\enemy_bullet\\"+type+"_bullet "+i+".png").getImage();
			}
			
		}
		else if(type.equals("aim")){
			sprites=new Image[4];
			for(int i=0;i<4;i++){
				sprites[i]=new ImageIcon("Sprites\\enemy_bullet\\"+type+"_bullet "+i+".png").getImage();
			}
			
		}
		else if(type.equals("feather")){
			sprites=new Image[6];
			for(int i=0;i<6;i++){
				sprites[i]=new ImageIcon("Sprites\\enemy_bullet\\"+type+"_bullet "+i+".png").getImage();
			}
			
		}
		else if(type.equals("owl")){
			sprites=new Image[1];
			for(int i=0;i<1;i++){
				sprites[i]=new ImageIcon("Sprites\\enemy_bullet\\"+type+owlType+"_bullet "+i+".png").getImage();
			}
			
		}
		else if(type.equals("cyclon")){
			sprites=new Image[9];
			for(int i=0;i<9;i++){
				sprites[i]=new ImageIcon("Sprites\\enemy_bullet\\"+type+owlType+"_bullet "+i+".png").getImage();
			}
		}
		else if(type.equals("grenade")){
			sprites=new Image[1];
			for(int i=0;i<1;i++){
				sprites[i]=new ImageIcon("Sprites\\enemy_bullet\\"+type+"_bullet "+i+".png").getImage();
			}
		}
		else if(type.equals("armor")){
			sprites=new Image[2];
			for(int i=0;i<2;i++){
				sprites[i]=new ImageIcon("Sprites\\enemy_bullet\\"+type+"_bullet "+i+".png").getImage();
			}
		}
		else{
			sprites=new Image[3];
			for(int i=0;i<3;i++){
				sprites[i]=new ImageIcon("Sprites\\enemy_bullet\\"+type+"_bullet "+i+".png").getImage();
			}
		}
    }
    public void drawImage(Graphics g, MainGame b){
    	// draws the sprite onto a doublebuffered image
    	Image tmp;
    	if(type.equals("feather")){
    		tmp = sprites[(int)(spriteNum)%2+featherType];
    	}
    	else{
    		tmp = sprites[(int)(spriteNum)%sprites.length];
    	}
    	int x=getX()+495;
    	int y=getY()+480;
    	int w=tmp.getWidth(b);
    	int h=tmp.getHeight(b);
    	if(type.equals("met")){
    		y-=10;
    	}
    	else if(type.equals("grenade")){
    		x+=5;
    		y-=10;
    	}
    	else if(type.equals("BB")){
    		x-=15;
    	}
    	else if(type.equals("feather")){
    		y=y;
    	}
    	else if(isPeacock){
    		x+=5;
    		y+=20;
    	}
    	else if(type.equals("owl")){
    		y-=10;
    	}
    	else{
    		y+=10;
    		x+=5;
    	}
    	if(vx>=0){
		    g.drawImage(tmp,x,y+10,w,h,b);
    	}
    	else{
    		g.drawImage(tmp,x+w,y+10,-w,h,b);
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
    
    
    public void move(Platform[][] maps,X Ex){
    	// movement
    	// maps is the all the platforms in the level
    	// Ex is the main character
    	
	    spriteNum=(spriteNum+0.5);
		if(spriteNum==12.5 && type.equals("mushroom")){
			// spores last 25 frames
			receiveDmg(1000000);
		}
		if(spriteNum==9.5 && (type.equals("peak") || type.equals("peakfly"))){
			// peacock attack lasts 19 frames 
			receiveDmg(100000);
		}
		if(spriteNum==4 && type.equals("armor")){
			// armor attack lasts 8 frames
			receiveDmg(100000);
		}
		if(spriteNum>2 && type.equals("peakfly")){
			// peacock flying up
			vy-=2;
		}
    	counter++;
    	if(counter==370 && type.equals("aim")){
    		// aim lasts 370 frames
    		receiveDmg(100000);
    	}
    	
    	
    	if(type.equals("cyclone") && counter>-1 && counter<15){
    		// cyclone follows the player
    		int dx=-getX()+Ex.getX()+vx*6; // vx*6 is to follow the previous position
    		int dy=-getY()+Ex.getY()+vy*6;
    		double dist = Math.sqrt(dx*dx+dy*dy);
    		vy=(int)(25*dy/dist);
    		vx=(int)(25*dx/dist);
    		
    	}
    	if(type.equals("feather")){
    		// feather follows the player
    		int dx=-getX()+Ex.getX()+vx*6;
    		int dy=-getY()+Ex.getY()+vy*6;
    		double dist = Math.sqrt(dx*dx+dy*dy);
    		vy=(int)(speed*dy/dist);
    		vx=(int)(speed*dx/dist);
    		if(Math.abs(vx)<5){
    			if(vy>0){
    				// changes the direction
    				featherType=4;
    			}
    			else{
    				// changes the direction
    				featherType=2;
    			}
    		}
    		else{
    			featherType=0;
    		}
    		if(counter==40){
    			receiveDmg(100);
    		}
    	}
    	else if(type.equals("aim")||(counter<=15 && type.equals("web"))){
    		// follows the player
    		int dx=-getX()+Ex.getX();
    		int dy=-getY()+Ex.getY();
    		double dist = Math.sqrt(dx*dx+dy*dy);
    		vy=(int)(speed*dy/dist);
    		vx=(int)(speed*dx/dist);
    	}

		if(Ex.getRect().intersects(rect)){
			if(type.equals("aim")){
				moveRect(Ex.getX()-10,Ex.getY()-10);
				vx=0;
				vy=0;
			}
			else{
				Ex.receiveDmg(dmg);
				if(type.equals("web")){
					// stuns the player
					vx=0;
					vy=0;
					moveRect(Ex.getX(),Ex.getY()+vy);
					Ex.stun(true);
				}
				receiveDmg(3);
				if(!alive()){
					Ex.stun(false);
				}
			}
			
		}
		else{
			Ex.stun(false);
		}
    	if(typeFromMap((getY())/20,(getX())/20,maps).equals("wall") ||
    	   typeFromMap((getY()+getWidth()-10)/20,(getX())/20,maps).equals("wall") ||
    	   typeFromMap((getY())/20,(getX()+getWidth()-5)/20,maps).equals("wall") ||
    	   typeFromMap((getY()+getWidth()/2)/20,(getX()+getWidth()/2)/20,maps).equals("wall") ||
    	   typeFromMap((getY()+getWidth()-10)/20,(getX()+getWidth()-5)/20,maps).equals("wall")){
    	   	if(!hitWall){
	    		receiveDmg(10000);
    	   	}
    	}
    	moveRect(getX()+vx,getY()+vy);
    }
    
    
}