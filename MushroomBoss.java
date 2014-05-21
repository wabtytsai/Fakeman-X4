import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

// MushroomBoss Class
// One of the four bosses in the game
// It will jump across the map and produce type-1 clones to help attacking the player
// When in rage mode, it will produce type-2 clone and starts to walk in opposite directions
 

public class MushroomBoss {
	
	private Rectangle rect; // a Rectangle object representing the character
	private int vx,vy; // character's movement
	private int hp,dmg; // health point and damage
	private int invinced; // cool down for invincibility
	private int shroomCount,bounceCounter,counter; // counter for producing clones, number of bounces, and movement counter
	private int rageCounter; // counter 
	private int stage; // stages of movement
	private String spriteName; // sprite name
	private double spriteNum; // sprite num
	private HashMap<String,Image[]>sprites; // sprites
	private boolean active,reset; // activated and able to be reset
	private boolean stop; // flag to control velocity
	private boolean rage,inAir,bounce; // flag for raged, in air, and bouncing
	private int ox,oy; // original position 
	private LinkedList<Mushroom> shrooms; // list of clones

    public MushroomBoss(int x,int y) {
    	ox=x;
    	oy=y;
    	vx=0;
    	vy=0;
    	stage=0;
    	invinced=0;
    	rageCounter=0;
    	counter=0;
    	shroomCount=0;
    	bounceCounter=0;
    	hp=1000;
    	dmg=20;
    	bounce=false;
    	spriteName="spin";
    	spriteNum=0;
    	
    	shrooms = new LinkedList<Mushroom>();
    	inAir=true;
    	stop=false;
    	rage=false;
    	active=false;
    	reset = true;
	    rect = new Rectangle(ox,oy,45,45);
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
    	if(invinced==0){
    		hp-=n;
    		invinced=30;
    	}
    	if(hp<=0){
    		// explodes
    		stop=true;
    		spriteName="die";
    	}
    	
    }
    public boolean alive(){return hp>0;}
    
    public boolean activated(){return active;}
    public void setReset(boolean n){reset=n;}
    public void activate(){
    	// activate and reset all stats
    	if(!active && reset){
    		reset = false;
    		vx=0;
	    	vy=0;
    		shroomCount=0;
	    	stage=0;
	    	invinced=0;
	    	rageCounter=0;
	    	counter=0;
	    	bounceCounter=0;
	    	hp=1000;
	    	dmg=20;
	    	bounce=false;
	    	spriteName="spin";
	    	spriteNum=0;
	    	
	    	shrooms.clear();
    		inAir=true;
	    	rage=false;
	    	active=true;
	    	stop=false;
		    rect = new Rectangle(ox,oy,45,45);
    	}
    }
    public void deActivate(){
    	active=false;
    }
    
    public void loadSprites(){
    	sprites= new HashMap<String,Image[]>();
    	String tmp;
    	int num;
    	
    	tmp="spin";
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
    	tmp="walk";
    	num=8;
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
		tmp="die";
		sprites.put(tmp,new Image[22]);
		for(int i=0;i<22;i++){
			sprites.get(tmp)[i]=new ImageIcon("Sprites\\explosion_gray\\explosion_gray "+i/2+".png").getImage();
		}
    	
    }
    public void drawImage(Graphics g, MainGame b){
    	// draw images onto Graphics g which is a doublebuffered image
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
		for(Mushroom i: shrooms){
//			g.fillRect(i.getX()+500,i.getY()+500,i.getWidth(),i.getHeight());
			i.drawImage(g,b);
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
    	rect.move(a,b);
//    	rect.move((a+1000)%1000,(b+800)%800);
    }
    public void move(Platform[][] maps, LinkedList<Bullet> stuff,X Ex,LinkedList<EnemyBullet> eb){
    	// movement
    	// maps is the all the platforms in the level
    	// stuff is friendly bullets
    	// Ex is the main character
    	// eBullets is enemy bullets
    	
    	String oriSprite=spriteName;
    	
		LinkedList<Mushroom> removes = new LinkedList<Mushroom>();
		
		for(Mushroom i:shrooms){
			// move all the clones
			i.move(maps,Ex,eb);
			if(!i.alive() || Math.abs(Ex.getX()-i.getX())>550 || Math.abs(Ex.getY()-i.getY())>550){
				removes.add(i);
			}
		}
		for(Mushroom i:removes){
			shrooms.remove(i);
		}
		
		
		if(invinced==30){
			// create poisonous spores when attacked
			eb.add(new EnemyBullet("mushroom",getX()-5,getY()-5,10,0,0));
			eb.add(new EnemyBullet("mushroom",getX()+25,getY()-5,10,0,0));
		}
		invinced=Math.max(0,invinced-1);
		if(!rage &&(spriteName.equals("rest") || spriteName.equals("wall"))){
			counter++;
		}
		
		if(!rage){
			if(stage==1){
				//sticking to the wall
				shroomCount++;
			}
			if(stage==2){
				//finished cloning, ready to jump onto the opposite side
				shroomCount=0;
			}
			if(shroomCount==28 || shroomCount==48||shroomCount==68){
				if(counter>100){
					shrooms.addFirst(new Mushroom(getX()-45,getY(),1));
				}
				else{
					shrooms.addFirst(new Mushroom(getX()+45,getY(),1));
				}
			}
			if (counter==20){
				// sticking to the wall to produce minions
				stage=1;
				counter++;
				spriteName="jump";
				inAir=true;
				vx=-30;
				vy=-40;
			}
			else if(counter==80){
				// finished producing minions
				stage=2;
				counter++;
				spriteName="jump";
				inAir=true;
				vy=0;
				vx=10;
			}
			else if(counter==100){
				// jump to the opposite wall
				stage=1;
				counter++;
				spriteName="jump";
				inAir=true;
				vx=30;
				vy=-40;
			}
			else if(counter==160){
				// finished producing minions 
				stage=2;
				counter++;
				spriteName="jump";
				inAir=true;
				vy=0;
				vx=-10;
			}
			else if(counter==190){
				// jump to air and spin
				stage=3;
				counter++;
				spriteName="jump";
				inAir=true;
				vx=-20;
				vy=-50;
			}
			if (bounce){
				//bounces off the ground
				vy=(int)(vy*-1)/5*5;
				bounceCounter++;
				bounce=false;
			}
			if(bounceCounter==3){
				// can only bounce three times
				bounceCounter=0;
				spriteName="jump";
			}
			if(counter==240){
				// finish spinning
				stage=5;
				counter++;
				vy=0;
				vx=10;
				inAir=true;
				if(hp<400){
					shrooms.add(new Mushroom(getX(),getY(),2));
				}
			}
			if(counter>=191 && stage!=5){
				// move to the middle of the screen
				stage=4;
				if(getX()<=260){
					counter++;
					spriteName="spin";
					vy=0;
					vx=0;
					inAir=false;
				}
			}
    	}
    	else{
    		// rage mode
    		rageCounter++;
    		if(rageCounter==10){
    			// walking accross the map
    			bounceCounter=1;
    			vx=-10;
    			spriteName="walk";
    		}
    		if(bounceCounter==12){
    			// 12 bounces
    			rage=false;
    			counter=0;
    			rageCounter=0;
    			bounceCounter=0;
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
		    		if(!stop){
		    			moveRect((getX()+i),getY());
		    		}
			    	vx=0;
			    	if(rage){
			    		// if in raged mode, keep on walking
			    		bounceCounter++;
				    	spriteName="walk";
			    	}
		    		if(counter==101){
		    			// else sticking onto the wall and produce clones
			    		spriteName="wall";
			    		inAir=false;
			    		vy=0;
			    		vx=1;
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
		    		if(!stop){
		    			moveRect(getX()+i+5,getY());
		    		}
			    	vx=0;
			    	if(rage){
			    		// if in raged mode, jump to opposite side
			    		vx=40;
			    		vy=-30;
			    		inAir=true;
			    		spriteName="jump";
			    	}
		    		if(counter==21){
		    			// else stick onto wall and produce clones
			    		spriteName="wall";
			    		inAir=false;
			    		vy=-1;
		    		}
		    	}
			}
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
					if(rage){
						// raged mode, landed, ready to walk to the other side
						inAir=false;
						vx=-(bounceCounter/4)*5-10;
						spriteName="walk";
					}
					else{
						//else
						if(counter==241){
							// finished one round of pattern
							// resets counter 
							stage=0;
							vx=0;
							if(!stop){
								moveRect((getX()+5)%2000,(getY())%1600);
							}
							inAir=false;
							spriteName="rest";
							if(hp>400){
								counter=0;
							}
							else{
								rage=true;
								counter++;
								rageCounter=0;
							}
						}
						else if(stage==2){
							//finished producing clones
							vx=0;
							inAir=false;
	    					spriteName="rest";
						}
	    				else if(spriteName.equals("spin")){
	    					// spining means able to bounce
	    					bounce=true;
	    				}
	    				else{
	    					// just fall normally
	    					spriteName="rest";
	    					inAir=false;
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
    				break;
    			}
    		}
    	}
    	
		if(!spriteName.equals("wall")){
	    	if(!stop){
	    		moveRect((getX()+vx),(getY()+vy));
	    	}
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

		spriteNum+=1;
		if(oriSprite.equals("die")){
			spriteName="die";
		}
		if(!spriteName.equals(oriSprite)){
			spriteNum=0;
		}
		if(inAir ){
			// gravity
			vy=Math.min(vy+5,20);
		}
//		System.out.println(getX());
    }
}