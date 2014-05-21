import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

//Armor Class
//Armor is a type of enemy with high health and strong attack
//however it cannot move and can only attack the player when its right in front

public class Armor {

	private boolean active,reset; // if armor is currently active and whether it can be reset or not
	private boolean stop; // a flag to control the movement
	private int hp,dmg; // hit point and damage
	private int vx,vy; // velocity
	private int counter; // counter for the moves
	private Rectangle rect; // rectangle representing the position
	private String spriteName; // sprite name
	private double spriteNum; // sprite counter
	private HashMap<String,Image[]>sprites; // sprites
	private int ox,oy; // original position
	
    public Armor(int x,int y) {
    	ox=x;
    	oy=y;
    	hp=100;
    	dmg=15;
    	vx=0;
    	vy=0;
    	counter=0;
    	stop=true;
    	reset=true;
    	active=false;
    	rect = new Rectangle(ox,oy,60,60);
    	spriteName = "start";
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
    public void receiveDmg(int n){
    	hp-=n;
    	if(hp<=0){
    		stop=true;
    	}
    }
    public boolean alive(){return hp>0;}
    
    public boolean activated(){return active;}
    public void setReset(boolean n){reset=n;}
    public void activate(){
    	if(!active && reset){
    		// activates
    		// resets all the stats
	    	active=true;
	    	hp=100;
	    	dmg=15;
	    	vx=0;
	    	vy=0;
	    	counter=0;
	    	stop=true;
	    	rect = new Rectangle(ox,oy,60,60);
	    	spriteName = "start";
	    	spriteNum = 0;
	    	loadSprites();
    	}
    }
    public void deActivate(){
    	active=false;
    }
    public void loadSprites(){
    	// load the sprites
    	// maps a String to an Image array
    	
    	sprites= new HashMap<String,Image[]>();
    	String tmp;
    	int num;
    	
    	tmp="start";
    	num=8*2;
    	sprites.put(tmp,new Image[num]);
    	for(int i=0;i<num;i++){
    		sprites.get(tmp)[i]=new ImageIcon("Sprites\\armor_"+tmp+"\\armor_"+tmp+" "+i/2+".png").getImage();
    	}
		tmp="die";
		sprites.put(tmp,new Image[22]);
		for(int i=0;i<22;i++){
			sprites.get(tmp)[i]=new ImageIcon("Sprites\\explosion_gray\\explosion_gray "+i/2+".png").getImage();
		}
		
    }
    public void drawImage(Graphics g, MainGame b){
    	// draw the image onto Graphics g, which is a doublebuffered image
    	Image tmp = sprites.get(spriteName)[(int)spriteNum%sprites.get(spriteName).length];
    	int x=getX()+490;
    	int y=getY()+450;
    	int w=tmp.getWidth(b);
    	int h=tmp.getHeight(b);
//    	g.setColor(Color.BLACK);
//    	g.fillRect(getX()+500,getY()+500,getWidth(),getHeight());
    	if(vx<=0){
	    	g.drawImage(tmp,x,y,w,h,b);
    	}
    	else{
    		// horizontal flip
    		g.drawImage(tmp,x+30,y,-w,h,b);
    	}
    }
    public Color colFromMap(double ay,double ax,Platform[][] maps){
    	// get the color from the map
    	int x=(int)ax;
    	int y=(int)ay;
    	return maps[(80+y)%80][(x+100)%100].getColor();
    }
    public String typeFromMap(double ay,double ax,Platform[][] maps){
    	// get the type from the map
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
			// first decides which way to face
			vx=-1;
			if(Ex.getX()>getX()){
				vx=1;
			}
		}
		if(Math.abs(Ex.getX()-getX())>100){
			//when X is not up close, do not change spriteNum
			spriteNum=-1;
		}
		else{
			//in range to attack
			if(spriteNum==8){
				eBullets.add(new EnemyBullet("armor",getX()-37,getY()-37,30,0,0));
				spriteNum=-1;
			}
		}

    	if(!stop){
	    	moveRect((getX())%2000,(getY()+vy+1600)%1600);
    	}
    	
		for(Bullet i: stuff){
			//check collision with bullets
			if(i.getRect().intersects(rect)){
				i.receiveDmg(100);
				receiveDmg(i.getDmg());
			}
		}
		
		spriteNum+=1;
		if(!spriteName.equals(oriSprite)){
			spriteNum=0;
		}
    }
    
    
}