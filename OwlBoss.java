import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

// OwlBoss Class
// One of the four bosses in the game
// it spends most of its time in air, flying accross the map
// 3 types of attacking mode:
// 1. fly towards player, once close enough, dive down and try to catches the player and drag the player into the air
// 2. fly low on the ground, shoots feathers at the player
// 3. fly high in the air, shoots feathers at the player
// in raged mode, every time being attacked will produce a cyclone, which will chase the player
// in addition to the 3 types of normal attacking, it has a change of producing four cyclones at the same time

public class OwlBoss {
	
	private Rectangle rect; // a Rectangle object representing the character
	private int vx,vy; // character's movement velocity
	private int hp,dmg; // health point and damage
	private int invinced,counter; // cool down for invinciblilty and counter for movement
	private int rageCounter; // rage counter
	private int stage; // which stage is the boss in
	private String spriteName; // sprite name
	private double spriteNum; // counter for sprites
	private HashMap<String,Image[]>sprites; // sprites 
	private boolean active,reset; // activated and able to be reset
	private boolean rage,inAir,stop; // flag for raged mode, in air, and stopping the movement
	private int ox,oy; // original position
	
    public OwlBoss(int x,int y) {
    	ox=x;
    	oy=y;
    	vx=0;
    	vy=0;
    	stage=0;
    	invinced=0;
    	rageCounter=0;
    	counter=0;
    	hp=399;
    	dmg=20;
    	spriteName="spawn";
    	spriteNum=0;
    	
    	inAir=true;
    	stop=false;
    	rage=false;
    	active=false;
    	reset = true;
	    rect = new Rectangle(ox,oy,40,40);
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
	    	hp=1000;
	    	dmg=20;
	    	spriteName="spawn";
	    	spriteNum=0;
	    	
	    	inAir=true;
	    	rage=false;
	    	stop=false;
	    	active=true;
	    	reset = false;
		    rect = new Rectangle(ox,oy,40,40);
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
    	num=12*3;
    	sprites.put(tmp,new Image[num]);
    	for(int i=0;i<num;i++){
    		sprites.get(tmp)[i]=new ImageIcon("Sprites\\owl_"+tmp+"\\owl_"+tmp+" "+i/3+".png").getImage();
    	}
    	tmp="fly";
    	num=4*3;
    	sprites.put(tmp,new Image[num]);
    	for(int i=0;i<num;i++){
    		sprites.get(tmp)[i]=new ImageIcon("Sprites\\owl_"+tmp+"\\owl_"+tmp+" "+i/3+".png").getImage();
    	}
    	tmp="catch";
    	num=1;
    	sprites.put(tmp,new Image[num]);
    	for(int i=0;i<num;i++){
    		sprites.get(tmp)[i]=new ImageIcon("Sprites\\owl_"+tmp+"\\owl_"+tmp+" "+i+".png").getImage();
    	}
    	tmp="drop";
    	num=1;
    	sprites.put(tmp,new Image[num]);
    	for(int i=0;i<num;i++){
    		sprites.get(tmp)[i]=new ImageIcon("Sprites\\owl_"+tmp+"\\owl_"+tmp+" "+i+".png").getImage();
    	}
    	tmp="shootUp";
    	num=8*3;
    	sprites.put(tmp,new Image[num]);
    	for(int i=0;i<num;i++){
    		sprites.get(tmp)[i]=new ImageIcon("Sprites\\owl_"+tmp+"\\owl_"+tmp+" "+i/3+".png").getImage();
    	}
    	tmp="shootDown";
    	num=6*3;
    	sprites.put(tmp,new Image[num]);
    	for(int i=0;i<num;i++){
    		sprites.get(tmp)[i]=new ImageIcon("Sprites\\owl_"+tmp+"\\owl_"+tmp+" "+i/3+".png").getImage();
    	}
    	tmp="hurt";
    	num=5*2;
    	sprites.put(tmp,new Image[num]);
    	for(int i=0;i<num;i++){
    		sprites.get(tmp)[i]=new ImageIcon("Sprites\\owl_"+tmp+"\\owl_"+tmp+" "+i/2+".png").getImage();
    	}
    	tmp="ult";
    	num=5*2;
    	sprites.put(tmp,new Image[num]);
    	for(int i=0;i<num;i++){
    		sprites.get(tmp)[i]=new ImageIcon("Sprites\\owl_"+tmp+"\\owl_"+tmp+" "+i/2+".png").getImage();
    	}
		tmp="die";
		sprites.put(tmp,new Image[22]);
		for(int i=0;i<22;i++){
			sprites.get(tmp)[i]=new ImageIcon("Sprites\\explosion_gray\\explosion_gray "+i/2+".png").getImage();
		}
    	
    }
    public void drawImage(Graphics g, MainGame b){
    	Image tmp = sprites.get(spriteName)[(int)spriteNum];
    	int x=getX()+490;
    	int y=getY()+500;
    	int w=tmp.getWidth(b);
    	int h=tmp.getHeight(b);
    	
//    	g.fillRect(getX()+500,getY()+500,getWidth(),getHeight());
    	if(spriteName.equals("spawn")){
    		y-=30;
    	}
    	if(spriteName.equals("shootUp")){
    		y-=15;
    	}
    	if(vx<=0){
	    	g.drawImage(tmp,x,y,w,h,b);
    	}
    	else{
    		g.drawImage(tmp,x+w,y,-w,h,b);
    	}
    	if(invinced!=0){
    		g.drawImage(sprites.get("hurt")[(int)spriteNum%5],x,y,b);
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
    	
		counter++;
		if(invinced==30 && hp<400){
			// getting attacked, produce a cyclone if under 400 health
			eb.add(new EnemyBullet("cyclone",getX()+20,getY()+20,15,0,0));
		}
		invinced=Math.max(0,invinced-1);
		if (counter==36){
			// just finished spawning, starts to fly
			spriteName="fly";
			vy=-5;
		}
		if (counter==70){
			// attack-1: fly towards the player and tries to dive
			vy=0;
			vx=-10;
		}
		if(counter>70 && counter<110){
			if(Math.abs(getX()+vx-Ex.getX())<=100){
				// if its close enough with the player, dive
				spriteName="catch";
				counter=200;
				if(hp<400){
					int num = (int)(Math.random()*100)%2;
					if(num==0){
						counter=720;
					}
				}
			}
		}
		if (counter==110){
			// did not get close enough with the player, stops in air
			stop=true;
			vx=1;
		}
		if(counter==150){
			// flys to the opposite direction
			stop=false;
			counter=270;
		}
		if(counter==200){
			// spotted the player, dives down using similar triangles
    		int dx=-getX()+Ex.getX()+vx*6;
    		int dy=-getY()+Ex.getY()+vy*6;
    		double dist = Math.sqrt(dx*dx+dy*dy);
    		vy=(int)(15*dy/dist);
    		vx=(int)(15*dx/dist);
    		if(Math.abs(getX()+vx-Ex.getX())<=20 && Math.abs(getY()+vy-Ex.getY())<=20){
    			// caught the player, drags the player along with it
    			Ex.stun(true);
    			Ex.moveRect(getX()+25,getY()+45);
    			counter=210;
    		}
		}
		if(counter==201){
			counter--;
    		int dx=-getX()+Ex.getX()+vx*6;
    		int dy=-getY()+Ex.getY()+vy*6;
    		double dist = Math.sqrt(dx*dx+dy*dy);
    		vy=(int)(15*dy/dist);
    		if(Math.abs(getX()+vx-Ex.getX())<=40 && Math.abs(getY()+vy-Ex.getY())<=40){
    			// caught the player, drags the player along with it
    			Ex.stun(true);
    			Ex.moveRect(getX()+25,getY()+45);
    			counter=210;
    		}
    		if(getY()>Ex.getY()){
    			counter=250;
    		}
		}
		if(counter>210 && counter<240){
			// moves up
			Ex.stun(true);
			Ex.moveRect(getX(),getY()+45);
			vx=0;
			vy=-5;
		}
		if(counter==240){
			// slams the player to the ground
			spriteName="drop";
			counter--;
			Ex.stun(true);
			Ex.moveRect(getX(),getY()+45);
			vy=40;
			if(Ex.getY()>540){
				// hitting the ground
				Ex.moveRect(Ex.getX()/10*10,540);
				counter=250;
				Ex.stun(false);
			}
		}
		if(counter==250){
			// flys away
			spriteName="fly";
			vy=-20;
			vx=-20;
		}
		
		if(counter==270){
			// flying to the opposite side
			moveRect(-100,370);
			int num = (int)(Math.random()*102)%3;
			// 1/3 chance for each type of attack taking place every time
			if(num==0){
				counter=500;
				moveRect(-100,oy);
			}
			else if(num==1){
				counter=600;
				moveRect(-100,300);
			}
			vx=10;
			vy=0;
		}
		if(counter>270 && counter<320){
			// attack-1 fly towards the player and try to dive
			if(Math.abs(getX()+vx-Ex.getX())<=100){
				// spotted the player
				spriteName="catch";
				counter=350;
				if(hp<400){
					int num = (int)(Math.random()*100)%2;
					// a chance of using ultimate when raged
					if(num==0){
						counter=720;
					}
				}
			}
		}
		if(counter==320){
			// did not spot the player
			stop=true;
			vx=-1;
		}
		if(counter==340){
			// stops in air
			stop=false;
			counter=69;
		}
		if(counter==350){
			// spotted the player, diving using similar triangles
    		int dx=-getX()+Ex.getX()+vx*6;
    		int dy=-getY()+Ex.getY()+vy*6;
    		double dist = Math.sqrt(dx*dx+dy*dy);
    		vy=(int)(15*dy/dist);
    		vx=(int)(15*dx/dist);
    		if(Math.abs(getX()+vx-Ex.getX())<=20 && Math.abs(getY()+vy-Ex.getY())<=20){
    			// caught the player
    			Ex.stun(true);
    			Ex.moveRect(getX()+25,getY()+45);
    			counter=400;
    		}
		}
		if(counter==351){
			counter--;
    		int dx=-getX()+Ex.getX()+vx*6;
    		int dy=-getY()+Ex.getY()+vy*6;
    		double dist = Math.sqrt(dx*dx+dy*dy);
    		vy=(int)(15*dy/dist);
    		if(Math.abs(getX()+vx-Ex.getX())<=40 && Math.abs(getY()+vy-Ex.getY())<=40){
    			// caught the player
    			Ex.stun(true);
    			Ex.moveRect(getX()+25,getY()+45);
    			counter=400;
    		}
    		if(getY()>Ex.getY()){
    			counter=450;
    		}
		}
		if(counter>400 && counter<430){
			// drags the player along
			Ex.stun(true);
			Ex.moveRect(getX(),getY()+45);
			vx=0;
			vy=-5;
			
		}
		if(counter==430){
			// slams the player to the ground
			spriteName="drop";
			counter--;
			Ex.stun(true);
			Ex.moveRect(getX(),getY()+45);
			vy=40;
			if(Ex.getY()>540){
				Ex.moveRect(Ex.getX()/10*10,540);
				counter=450;
				Ex.stun(false);
			}
		}
		if(counter==450){
			// flys away
			spriteName="fly";
			vy=-20;
			vx=20;
		}
		if(counter==470){
			vy=0;
			vx=-10;
			moveRect(650,370);
			counter=40;
			int num = (int)(Math.random()*102)%3;
			// 1/3 chance for each attack
			if(num==0){
				counter=550;
				moveRect(650,oy);
			}
			if(num==1){
				counter=650;
				moveRect(650,300);
			}
		}
		
		
		// attack-2/3 Shooting 
		if(counter==520){
			vx=1;
			stop=true;
			spriteName="shootUp";
		}
		if(counter==530 || counter==533 || counter==536 || counter == 539){
			// adds bullets
			eb.add(new EnemyBullet("owl",getX()+20,getY()+20,10,11,-10));
			eb.add(new EnemyBullet("owl",getX()+20,getY()+20,10,14,-5));
			eb.add(new EnemyBullet("owl",getX()+20,getY()+20,10,15,0));
			if(hp<400 && (counter==530)){
				eb.add(new EnemyBullet("cyclone",getX()+20,getY()+20,15,0,0));
			}
		}
		if(counter==539){
			// flys away
			stop=false;
			counter=249;
		}
		if(counter==570){
			vx=-1;
			stop=true;
			spriteName="shootUp";
		}
		if(counter==580 || counter==583 || counter==586 || counter == 589){
			// adds bullets
			eb.add(new EnemyBullet("owl",getX()+20,getY()+20,10,-11,-10));
			eb.add(new EnemyBullet("owl",getX()+20,getY()+20,10,-14,-5));
			eb.add(new EnemyBullet("owl",getX()+20,getY()+20,10,-15,0));
			if(hp<400 && (counter==580)){
				eb.add(new EnemyBullet("cyclone",getX()+20,getY()+20,15,0,0));
			}
		}
		if(counter==589){
			//flys away
			stop=false;
			counter=449;
		}
		
		if(counter==620){
			vx=1;
			stop=true;
			spriteName="shootDown";
		}
		if(counter==630 || counter==633 || counter==636 || counter == 639){
			eb.add(new EnemyBullet("owl",getX()+20,getY()+20,10,11,10));
			eb.add(new EnemyBullet("owl",getX()+20,getY()+20,10,14,5));
			eb.add(new EnemyBullet("owl",getX()+20,getY()+20,10,15,0));
			if(hp<400 && (counter==630)){
				eb.add(new EnemyBullet("cyclone",getX()+20,getY()+20,15,0,0));
			}
		}
		if(counter==639){
			stop=false;
			counter=249;
		}
		if(counter==670){
			vx=-1;
			stop=true;
			spriteName="shootDown";
		}
		if(counter==680 || counter==683 || counter==686 || counter == 689){
			eb.add(new EnemyBullet("owl",getX()+20,getY()+20,10,-11,10));
			eb.add(new EnemyBullet("owl",getX()+20,getY()+20,10,-14,5));
			eb.add(new EnemyBullet("owl",getX()+20,getY()+20,10,-15,0));
			if(hp<400 && (counter==680)){
				eb.add(new EnemyBullet("cyclone",getX()+20,getY()+20,15,0,0));
			}
		}
		if(counter==689){
			stop=false;
			counter=449;
		}
		
		//Ultimate
		if(counter==720){
			spriteName="ult";
			stop=true;
		}
		if(counter==730){
			eb.add(new EnemyBullet("cyclone",getX()+20,getY()+20,15,15,15));
			eb.add(new EnemyBullet("cyclone",getX()+20,getY()+20,15,-15,15));
			eb.add(new EnemyBullet("cyclone",getX()+20,getY()+20,15,15,-15));
			eb.add(new EnemyBullet("cyclone",getX()+20,getY()+20,15,-15,-15));
			spriteName="fly";
			stop=false;
			counter=449;
		}
		
		
		if(!stop){
			moveRect(getX()+vx,getY()+vy);
		}
		
		if(rect.intersects(Ex.getRect())){
    		Ex.receiveDmg(dmg);
    	}
		for(Bullet i: stuff){
			if(i.getRect().intersects(rect)){
				i.receiveDmg(100);
				receiveDmg(i.getDmg());
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