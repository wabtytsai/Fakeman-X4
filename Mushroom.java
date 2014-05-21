import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

// Mushroom Class
// A mushroom is just a clone of the MushroomBoss
// It cannot be hit, yet can attack the player
// It has two types of movements:
// 1. Jumps three times then fly to the player's location
// 2. When MushroomBoss rages, it will walk around the room and ignore the player

public class Mushroom {

	private Rectangle rect; // a Rectangle object representing the character
	private int vx,vy; // character's movement
	private int hp,dmg; // health point and damage
	private int type; // type of movement 
	private int bounceCounter,counter; // number of bounces of the ground, and counter for movements
	private String spriteName; // sprite name
	private double spriteNum; // counter for sprite 
	private HashMap<String,Image[]>sprites; // sprites
	private boolean inAir,bounce,rage; // if in air, is bouncing, or raged

    public Mushroom(int x,int y,int type) {
    	vx=0;
    	vy=0;
    	this.type = type;
    	if(type==2){
    		vx=-10;
    	}
    	counter=0;
    	bounceCounter=0;
    	hp=100;
    	dmg=10;
    	bounce=false;
    	spriteName="grow";
    	spriteNum=0;
    	
    	rage=false;
    	inAir=true;
	    rect = new Rectangle(x,y,45,45);
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
    	String tmp;
    	int num;
    	
    	tmp="spin";
    	num=8;
    	sprites.put(tmp,new Image[num]);
    	for(int i=0;i<num;i++){
    		sprites.get(tmp)[i]=new ImageIcon("Sprites\\mushroom_"+tmp+"\\mushroom_"+tmp+" "+i+".png").getImage();
    	}
    	tmp="grow";
    	num=8*4;
    	sprites.put(tmp,new Image[num]);
    	for(int i=0;i<num;i++){
    		sprites.get(tmp)[i]=new ImageIcon("Sprites\\mushroom_"+tmp+"\\mushroom_"+tmp+" "+i/4+".png").getImage();
    	}
    	tmp="walk";
    	num=8;
    	sprites.put(tmp,new Image[num]);
    	for(int i=0;i<num;i++){
    		sprites.get(tmp)[i]=new ImageIcon("Sprites\\mushroom_"+tmp+"\\mushroom_"+tmp+" "+i+".png").getImage();
    	}
    	tmp="jump";
    	num=4;
    	sprites.put(tmp,new Image[num]);
    	for(int i=0;i<num;i++){
    		sprites.get(tmp)[i]=new ImageIcon("Sprites\\mushroom_"+tmp+"\\mushroom_"+tmp+" "+i+".png").getImage();
    	}
    	tmp="rest";
    	num=3*8;
    	sprites.put(tmp,new Image[num]);
    	for(int i=0;i<num;i++){
    		sprites.get(tmp)[i]=new ImageIcon("Sprites\\mushroom_"+tmp+"\\mushroom_"+tmp+" "+i/8+".png").getImage();
    	}
    	tmp="wall";
    	num=2*10;
    	sprites.put(tmp,new Image[num]);
    	for(int i=0;i<num;i++){
    		sprites.get(tmp)[i]=new ImageIcon("Sprites\\mushroom_"+tmp+"\\mushroom_"+tmp+" "+i/10+".png").getImage();
    	}
		
    }
    public void drawImage(Graphics g, MainGame b){
    	Image tmp = sprites.get(spriteName)[(int)spriteNum%sprites.get(spriteName).length];
    	int x=getX()+500;
    	int y=getY()+500;
    	int w=tmp.getWidth(b);
    	int h=tmp.getHeight(b);
    	
    	
    	if(vx<=0){
	    	g.drawImage(tmp,x,y,w,h,b);
    	}
    	else{
    		g.drawImage(tmp,x+w,y,-w,h,b);
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
    public void move(Platform[][] maps,X Ex,LinkedList<EnemyBullet> eBullets){
    	// movement
    	// maps is the all the platforms in the level
    	// stuff is friendly bullets
    	// Ex is the main character
    	// eBullets is enemy bullets
    	
		String oriSprite=spriteName;
		if(type==1){
			// type 1: bounces three times then fly to player's location
			
	    	if(spriteName.equals("grow")){
	    		// spawning
	    		inAir=false;
	    		if(spriteNum==32){
	    			inAir=true;
	    			vy=0;
	    			spriteName="jump";
	    		}
	    	}
	    	else{
	    		// bouncing
		    	if(inAir==false && colFromMap((getY()+25)/20,(getX()+1)/20,maps)==Color.WHITE &&
		    		colFromMap((getY()+25)/20,(getX()+19)/20,maps)==Color.WHITE){
		    		// falling to ground
		    		inAir=true;
		    		vy=0;
		    	}
		    	if(bounceCounter==3){
		    		// flying towards the player using similar triangles
		    		bounceCounter++;
		    		spriteName="spin";
		    		rage=true;
		    		inAir=true;
		    		int dx=-getX()+Ex.getX();
		    		int dy=-getY()+Ex.getY();
		    		double dist = Math.sqrt(dx*dx+dy*dy);
		    		vy=(int)(30*dy/dist);
		    		vx=(int)(30*dx/dist);
		    	}
		    	if(!rage){
		    		// not rage = not three bounced yet
			    	if(bounce){
			    		bounceCounter++;
			    		spriteName="jump";
			    		bounce=false;
			    		inAir=true;
			    		vy=-30;
			    	}
			    	if(vy>0){
			    		// falling
						if(spriteName.equals("jump")){
							spriteNum=1;
						}
			    		for(int i=0;i<vy+5;i+=5){
			    			if( typeFromMap((getY()+i+45)/20,(getX()+44)/20,maps).equals("wall") ||
			    				typeFromMap((getY()+i+45)/20,(getX()+1)/20,maps).equals("wall")){
		    					vy=i;
		    					if(bounceCounter<3){
		    						bounce=true;
		    					}
		    					spriteName="rest";
		    					inAir=false;
			    				
			    			}
			    		}
			    	}
			    	else if(vy<0){
			    		// raising
			    		if(spriteName.equals("jump")){
			    			spriteNum=0;
			    		}
			    		for(int i=0;i>vy-5;i-=5){
			    			if( typeFromMap((getY()+i-5)/20,(getX()+19)/20,maps).equals("wall") ||
			    				typeFromMap((getY()+i-5)/20,(getX()+1)/20,maps).equals("wall")){
			    				vy=i;
			    				break;
			    			}
			    		}
			    	}
		    	}
	    	}
		}
		else{
			if(rage){
				// type 2: walks across the map, then jumps back - repeats
				counter++;
				if(bounceCounter==11){
					// return to type 1 - fly towards the player
					if(counter>20){
						counter=0;
					}
					spriteName="spin";
					vx=0;
					vy=0;
				}
				if(bounceCounter==11 && counter>=10){
					// using similar triangles
					bounceCounter=13;
		    		inAir=true;
		    		int dx=-getX()+Ex.getX();
		    		int dy=-getY()+Ex.getY();
		    		double dist = Math.sqrt(dx*dx+dy*dy);
		    		vy=(int)(30*dy/dist);
		    		vx=(int)(30*dx/dist);
				}
				if(counter==10 && bounceCounter!=13){
					// starting to walk
					bounceCounter=1;
	    			vx=10;
	    			spriteName="walk";
				}
			}
			if(vx>0 && vx!=1){
				for(int i=0;i<vx+5;i+=5){
					if((typeFromMap((getY())/20,(getX()+45+i)/20,maps).equals("wall") ||
			    		typeFromMap((getY()+10)/20,(getX()+45+i)/20,maps).equals("wall") ||
			    		typeFromMap((getY()+20)/20,(getX()+45+i)/20,maps).equals("wall") ||
			    		typeFromMap((getY()+30)/20,(getX()+45+i)/20,maps).equals("wall") ||
			    		typeFromMap((getY()+35)/20,(getX()+45+i)/20,maps).equals("wall"))){
			    		// touching the wall
			    		if(bounceCounter==13){
			    			// 13 bounces - end of the clone
			    			receiveDmg(100);
			    		}
			    		moveRect((getX()+i),getY());
				    	vx=0;
				    	if(rage){
				    		// jumps back to the opposite end
				    		vx=-40;
				    		vy=-30;
				    		inAir=true;
				    		spriteName="jump";
				    	}
			    	}
				}
			}
			else if(vx<0){
				for(int i=0;i>vx-5;i-=5){
					if((typeFromMap((getY())/20,(getX()+i)/20,maps).equals("wall") ||
			    		typeFromMap((getY()+10)/20,(getX()+i)/20,maps).equals("wall") ||
			    		typeFromMap((getY()+20)/20,(getX()+i)/20,maps).equals("wall") ||
			    		typeFromMap((getY()+30)/20,(getX()+i)/20,maps).equals("wall") ||
			    		typeFromMap((getY()+35)/20,(getX()+i)/20,maps).equals("wall"))){
			    		// touching the wall
			    		if(bounceCounter==13){
			    			receiveDmg(100);
			    		}
			    		moveRect(getX()+i+5,getY());
				    	vx=0;
				    	if(rage){
				    		// walk towards the other end
				    		bounceCounter++;
				    		spriteName="walk";
				    	}
			    	}
				}
			}
			if(vy>0){
				if(spriteName.equals("jump")){
					spriteNum=1;
				}
	    		for(int i=0;i<vy+5;i+=5){
	    			if( typeFromMap((getY()+i+45)/20,(getX()+44)/20,maps).equals("wall") ||
	    				typeFromMap((getY()+i+45)/20,(getX()+1)/20,maps).equals("wall")){
						vy=i;
						// touching the ground
			    		if(bounceCounter==13){
			    			receiveDmg(100);
			    		}
						if(type==1){
							if(!rage){
								// still bouncing
								rage=true;
								counter=0;
								vx=0;
								moveRect((getX()-5)%2000,(getY())%1600);
								inAir=false;
							}
		    				else{
		    					spriteName="rest";
		    					inAir=false;
		    				}
						}
						else{
							// type 2
							if(rage){
								// still walking
								inAir=false;
								vx=10;
								spriteName="walk";
							}
							else{
								spriteName="rest";
								inAir=false;
								rage=true;
								counter=0;
							}
						}
	    				break;
	    			}
	    		}
	    	}
	    	else if(vy<0){
	    		// raising
	    		if(spriteName.equals("jump")){
	    			spriteNum=0;
	    		}
	    		for(int i=0;i>vy-5;i-=5){
	    			if( typeFromMap((getY()+i-5)/20,(getX()+19)/20,maps).equals("wall") ||
	    				typeFromMap((getY()+i-5)/20,(getX()+1)/20,maps).equals("wall")){
	    				vy=i;
	    				// can only touch the wall if its trying to attack the player, which means time to die
			    		if(bounceCounter==13){
			    			receiveDmg(100);
			    		}
	    				break;
	    			}
	    		}
	    	}
		}
    	
    	moveRect((getX()+vx)%2000,(getY()+vy+1600)%1600);
    	
    	
		if(rect.intersects(Ex.getRect())){
			Ex.receiveDmg(dmg);
		}
		
    	
		if(inAir){
			if((type==2&& bounceCounter!=13) || (type==1 && !rage)){
				vy=Math.min(vy+5,20);
			}
		}
		spriteNum+=1;
		if(!spriteName.equals(oriSprite)){
			spriteNum=0;
		}
    }
    
}