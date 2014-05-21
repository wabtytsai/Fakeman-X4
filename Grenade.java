import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

// Grenade Class
// grenade throws a grenade towards the player when player is in sight
// it then runs away
// if in contact with a wall, it instantly dies

public class Grenade {

	private boolean inAir,stop; // flag for inair and stop moving
	private boolean active,reset; // activated and able to be reset
	private int hp,dmg; // hit point and damage
	private int vx,vy; // velocity
	private int counter; // counter for movement
	private Rectangle rect; // rectangle
	private String spriteName; // sprite name
	private double spriteNum; // counter for the sprite
	private HashMap<String,Image[]>sprites; // sprites 
	private int ox,oy; // original position

    public Grenade(int x,int y) {
    	ox=x;
    	oy=y;
    	hp=40;
    	dmg=10;
    	vx=0;
    	vy=0;
    	counter=0;
    	inAir=false;
    	stop=true;
    	reset=true;
    	active=false;
    	rect = new Rectangle(ox,oy,40,60);
    	spriteName = "rest";
    	spriteNum = 0;
    	loadSprites();
    }
    
    public Rectangle getRect(){return rect;}
    public int ox(){return ox;}
    public int oy(){return oy;}
    public int getX(){return (int)(rect.getX());}
    public int getY(){return (int)(rect.getY());}
    public int getWidth(){return (int)(rect.getWidth());}
    public int getHeight(){return (int)(rect.getHeight());}
    public int getHealth(){return hp;}
    public int getDmg(){return dmg;}
    public void receiveDmg(int n){
    	hp-=n;
    	if(hp<=0){
    		spriteName="die";
    		stop=true;
    	}
    }
    public boolean alive(){return hp>0;}
    
    public boolean activated(){return active;}
    public void setReset(boolean n){reset=n;}
    public void activate(){
    	if(!active && reset){
	    	active=true;
	    	hp=40;
	    	dmg=10;
	    	vx=0;
	    	vy=0;
	    	counter=0;
	    	stop=true;
	    	inAir=false;
	    	rect = new Rectangle(ox,oy,40,60);
	    	spriteName = "rest";
	    	spriteNum = 0;
	    	loadSprites();
    	}
    }
    public void deActivate(){
    	active=false;
    }
    
    public void loadSprites(){
    	sprites= new HashMap<String,Image[]>();
    	String tmp;
    	int num;
    	
    	tmp="rest";
    	num=7*2;
    	sprites.put(tmp,new Image[num]);
    	for(int i=0;i<num;i++){
    		sprites.get(tmp)[i]=new ImageIcon("Sprites\\grenade_"+tmp+"\\grenade_"+tmp+" "+i/2+".png").getImage();
    	}
    	tmp="run";
    	num=7*2;
    	sprites.put(tmp,new Image[num]);
    	for(int i=0;i<num;i++){
    		sprites.get(tmp)[i]=new ImageIcon("Sprites\\grenade_"+tmp+"\\grenade_"+tmp+" "+i/2+".png").getImage();
    	}
    	tmp="throw";
    	num=7*2;
    	sprites.put(tmp,new Image[num]);
    	for(int i=0;i<num;i++){
    		sprites.get(tmp)[i]=new ImageIcon("Sprites\\grenade_"+tmp+"\\grenade_"+tmp+" "+i/2+".png").getImage();
    	}
		tmp="die";
		sprites.put(tmp,new Image[22]);
		for(int i=0;i<22;i++){
			sprites.get(tmp)[i]=new ImageIcon("Sprites\\explosion_gray\\explosion_gray "+i/2+".png").getImage();
		}
		
    }
    public void drawImage(Graphics g, MainGame b){
    	Image tmp = sprites.get(spriteName)[(int)spriteNum%sprites.get(spriteName).length];
    	int x=getX()+500;
    	int y=getY()+500;
    	int w=tmp.getWidth(b);
    	int h=tmp.getHeight(b);
    	if(spriteName.equals("run")){
    		x+=10;
    	}
    	else if(spriteName.equals("throw")){
    		x-=20;
    	}
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
    
    public void move(Platform[][] maps, LinkedList<Bullet> stuff,X Ex,LinkedList<EnemyBullet> eBullets){
    	// movement
    	// maps is the all the platforms in the level
    	// stuff is friendly bullets
    	// Ex is the main character
    	// eBullets is enemy bullets
    	
		String oriSprite=spriteName;
		if(vx==0){
			// first decides the direction its facing
			vx=-1;
			if(Ex.getX()>getX()){
				vx=1;
			}
		}
		if(Math.abs(Ex.getX()-getX())<=200){
			// when player is in sight
			if(counter==0){
				counter=1;
				spriteName="throw";
			}
		}
		if(spriteName.equals("throw")){
			// throws the grenade
			counter++;
			if(counter==12){
				eBullets.add(new EnemyBullet("grenade",getX(),getY()+50,20,vx*20,0));
			}
			if(counter==16){
				stop=false;
				
				spriteName="run";
				vx*=-10;
				counter=20;
			}
		}
    	if(inAir==false && colFromMap((getY()+65)/20,(getX()+1)/20,maps)==Color.WHITE &&
    		colFromMap((getY()+65)/20,(getX()+39)/20,maps)==Color.WHITE){
    		// falling
    		inAir=true;
    		vy=0;
    	}
    	if( typeFromMap((getY()+15)/20,(getX()+10+vx*1.5)/20,maps).equals("wall") ||
    		typeFromMap((getY()+35)/20,(getX()+10+vx*1.5)/20,maps).equals("wall") ||
    		typeFromMap((getY()+55)/20,(getX()+10+vx*1.5)/20,maps).equals("wall")){
    		// touching the wall
	    	receiveDmg(100);
    	}

    	if(!stop){
	    	moveRect((getX()+vx)%2000,(getY()+vy+1600)%1600);
    	}
    	
		for(Bullet i: stuff){
			if(i.getRect().intersects(rect)){
				i.receiveDmg(100);
				receiveDmg(i.getDmg());
			}
		}
		if(inAir){
			vy+=5;
		}
		spriteNum+=1;
		if(!spriteName.equals(oriSprite)){
			spriteNum=0;
		}
    }
}