import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

// SpiderBoss Class
// One of the four bosses in the game
// it will climb up and down its web, and trys to catch the player by shoot webs
// the web will follow the player for a while, if caught, the player will be stuck onto the web for a little moment
// during rage mode, the boss will spawn small spiders


public class SpiderBoss {
	
	private Rectangle rect; // a Rectangle object representing the character
	private int vx,vy; // character's movemnt
	private int hp,dmg; // health point and damage
	private int invinced; //  cool down for invincibility
	private int throwCD,counter; // counter for shooting webs, and counter for moving
	private int vertMove, horzMove; // verticle movement and horizontal movement
	private int rageCounter; // rage counter
	private String spriteName; // sprite name
	private double spriteNum; // counter for sprites
	private HashMap<String,Image[]>sprites; // sprites
	private boolean active,reset; // active and able to be reset
	private boolean up,down,rage,stop; // flag for moving up and down, rage and stop movement
	private int ox,oy; // original position
	private int[] rageX={-5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, -5, -5, -5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, 5, 5, 5, 5, 5, 5, 5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, 5, 5, 5, 5, 5, -5, -5, -5, -5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, 5, 5, 5, 5, 5, 5, 5, -5, -5, -5, -5, -5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, -5, -5, -5};
	private int[] rageY={-5, -5, -5, -5, -5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, -5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5};
	// rageX and rageY is the path that the boss will take during rage mode
	private LinkedList<SmallSpider> sspiders; // list of small spiders

    public SpiderBoss(int x,int y) { 
    	ox=x;
    	oy=y;
    	vx=0;
    	vy=-20;
    	invinced=0;
    	throwCD=0;
    	vertMove=25;
    	horzMove=(int)(Math.random()*100%20+30);
    	rageCounter=0;
    	counter=0;
    	stop=false;
    	up=false;
    	rage=false;
    	down=true;
    	hp=1000;
    	dmg=20;
    	spriteName="walk";
    	spriteNum=0;
    	active=false;
    	reset = true;
	    rect = new Rectangle(ox,oy,60,60);
    	loadSprites();
		sspiders = new LinkedList<SmallSpider>();
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
    	else if(rage && down){
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
    		reset = false;
    		vx=0;
	    	vy=-20;
	    	invinced=0;
    		throwCD=0;
    		vertMove=20;
    		rageCounter=0;
	    	up=false;
    		horzMove=(int)(Math.random()*100%20+30);
	    	down=true;
    		counter=0;
    		rage=false;
    		stop=false;
	    	hp=1000;
	    	dmg=20;
	    	spriteName="walk";
	    	spriteNum=0;
	    	active=true;
	    	rect = new Rectangle(ox,oy,60,60);
	    	sspiders.clear();
    	}
    }
    public void deActivate(){
    	active=false;
    }
    
    public void loadSprites(){
    	sprites= new HashMap<String,Image[]>();
    	String tmp;
    	int num;
    	
    	tmp="walk";
    	num=8;
    	sprites.put(tmp,new Image[num]);
    	for(int i=0;i<num;i++){
    		sprites.get(tmp)[i]=new ImageIcon("Sprites\\spider_"+tmp+"\\spider_"+tmp+" "+i+".png").getImage();
    	}
    	tmp="drop";
    	num=1;
    	sprites.put(tmp,new Image[num]);
    	for(int i=0;i<num;i++){
    		sprites.get(tmp)[i]=new ImageIcon("Sprites\\spider_"+tmp+"\\spider_"+tmp+" "+i+".png").getImage();
    	}
    	tmp="throw";
    	num=8;
    	sprites.put(tmp,new Image[num]);
    	for(int i=0;i<num;i++){
    		sprites.get(tmp)[i]=new ImageIcon("Sprites\\spider_"+tmp+"\\spider_"+tmp+" "+i+".png").getImage();
    	}
    	tmp="hurt";
    	num=4;
    	sprites.put(tmp,new Image[num]);
    	for(int i=0;i<num;i++){
    		sprites.get(tmp)[i]=new ImageIcon("Sprites\\spider_"+tmp+"\\spider_"+tmp+" "+i+".png").getImage();
    	}
    	tmp="web";
    	num=4;
    	sprites.put(tmp,new Image[num]);
    	for(int i=0;i<num;i++){
    		sprites.get(tmp)[i]=new ImageIcon("Sprites\\spider_"+tmp+"\\spider_"+tmp+" "+i+".png").getImage();
    	}
    	tmp="crawl";
    	num=6;
    	sprites.put(tmp,new Image[num]);
    	for(int i=0;i<num;i++){
    		sprites.get(tmp)[i]=new ImageIcon("Sprites\\spider_"+tmp+"\\spider_"+tmp+" "+i+".png").getImage();
    	}
    	tmp="release_web";
    	num=10;
    	sprites.put(tmp,new Image[num]);
    	for(int i=0;i<num;i++){
    		sprites.get(tmp)[i]=new ImageIcon("Sprites\\spider_"+tmp+"\\spider_"+tmp+" "+i+".png").getImage();
    	}
    	tmp="release_spider";
    	num=10;
    	sprites.put(tmp,new Image[num]);
    	for(int i=0;i<num;i++){
    		sprites.get(tmp)[i]=new ImageIcon("Sprites\\spider_"+tmp+"\\spider_"+tmp+" "+i+".png").getImage();
    	}
		tmp="die";
		sprites.put(tmp,new Image[22]);
		for(int i=0;i<22;i++){
			sprites.get(tmp)[i]=new ImageIcon("Sprites\\explosion_gray\\explosion_gray "+i/2+".png").getImage();
		}
    	
    }
    public void drawImage(Graphics g, MainGame b){
    	//draws sprite onto a doublebuffered image
    	Image tmp = sprites.get(spriteName)[(int)spriteNum%sprites.get(spriteName).length];
    	int x=getX()+495;
    	int y=getY()+500;
    	int w=tmp.getWidth(b);
    	int h=tmp.getHeight(b);
    	if(!rage || (rage && down)){
	    	for(int i=y;i>oy+500;i-=15){
	    		g.drawImage(sprites.get("web")[(int)(spriteNum)%2],x+30,i,b);
	    	}
    	}
    	else if(rage && (!down || counter>=vertMove+20)){
    		g.drawImage(sprites.get("web")[(int)(spriteNum)%2+2],385+350,350+500,b);
    		g.drawImage(sprites.get("web")[(int)(spriteNum)%2+2],385+350,450+500,b);
    		g.drawImage(sprites.get("web")[(int)(spriteNum)%2+2],335+350,400+500,b);
    		g.drawImage(sprites.get("web")[(int)(spriteNum)%2+2],435+350,400+500,b);
    	}
    	if(spriteName.equals("throw")){
    		x-=5;
    	}
    	else if(spriteName.equals("crawl")){
    		x-=12;
    	}
    	else if(spriteName.equals("release_spider")){
    		x-=5;
    		y-=5;
    	}
    	if(vx>=0){
	    	g.drawImage(tmp,x,y,w,h,b);
    	}
    	else{
    		g.drawImage(tmp,x+w,y,-w,h,b);
    	}
    	if(invinced>0){
    		g.drawImage(sprites.get("hurt")[invinced%4],x-8,y-20,b);
    	}
    	
		for(SmallSpider i: sspiders){
			i.drawImage(g,b);
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
    
    
    public void move(Platform[][] maps, LinkedList<Bullet> stuff,X Ex,LinkedList<EnemyBullet> eb){
    	// movement
    	// maps is the all the platforms in the level
    	// stuff is friendly bullets
    	// Ex is the main character
    	// eBullets is enemy bullets
    	
    	String oriSprite=spriteName;
		
		LinkedList<SmallSpider> removes = new LinkedList<SmallSpider>();
		for(SmallSpider i:sspiders){
			//move small spiders
			i.move(maps,stuff,Ex);
			if(Ex.getRect().intersects(i.getRect())){
					Ex.receiveDmg(i.getDmg());
			}
			if(!i.alive() || Math.abs(Ex.getX()-i.getX())>550 || Math.abs(Ex.getY()-i.getY())>550){
				removes.add(i);
			}
		}
		for(int i=0;i<removes.size();i++){
			sspiders.remove(removes.get(i));
		}
		
		invinced=Math.max(0,invinced-1);
		
		if(rage){
			// rage mode
			if(down){
				// drops down first
				spriteName="drop";
	    		if(counter==vertMove+40){
	    			counter=0;
	    			down=false;
	    		}
	    		else if(counter>=vertMove){
	    			spriteName="release_web";
	    		}
	    		else{
	    			if(!stop){
			    		moveRect((getX())%2000,(getY()+10+1600)%1600);
	    			}
	    		}
	    		counter++;
			}
			else{
				// once dropped, starts crawling around the web
				spriteName="crawl";
				
				if(throwCD!=0){
					if(rageCounter%3!=1){
						// shoots webs twice, then release spiders once
						throwCD--;
			    		spriteName="walk";
			    		if(throwCD<8*4){
			    			spriteName="throw";
			    			if(throwCD==10){
			    				eb.add(new EnemyBullet("web",getX()-20,getY()+40,10,0,0));
			    			}
			    			if(throwCD==0){
								horzMove+=(int)(Math.random()*100%20+30);
			    			}
			    		}
					}
					else{
						// release spiders
						throwCD--;
			    		spriteName="walk";
			    		if(throwCD<8*4){
			    			spriteName="release_spider";
			    			if(throwCD==10){
			    				sspiders.add(new SmallSpider(getX()+20,getY()+10,15,-10));
			    				sspiders.add(new SmallSpider(getX()+20,getY()+10,5,-10));
			    				sspiders.add(new SmallSpider(getX()+20,getY()+10,-5,-10));
			    				sspiders.add(new SmallSpider(getX()+20,getY()+10,-15,-10));
			    			}
			    			if(throwCD==0){
			    				horzMove+=(int)(Math.random()*100%20+30);
			    			}
			    		}
					}
				}
				if(counter==horzMove){
					// times up, stop crawling and starts attacking
					throwCD=8*4*2;
					rageCounter++;
				}
				if(throwCD==0 || counter==horzMove){
					// moving
					counter++;
					int rx=rageX[counter%150];
					int ry=rageY[counter%150];
					if(!stop){
						moveRect((getX()+rx)%2000,(getY()+ry)%1600);
					}
				}
			}
		}
		else{
			// normal mode, drops down, shoots, climbs back up
	    	if(down){
	    		spriteName="drop";
	    		if(!stop){
	    			moveRect((getX())%2000,(getY()+10+1600)%1600);
	    		}
	    		if(counter==vertMove){
	    			down=false;
	    			throwCD=8*4;
	    			counter=0;
	    		}
	    	}
	    	else if(up){
	    		spriteName="drop";
	    		if(!stop){
	    			moveRect((getX())%2000,(getY()-10+1600)%1600);
	    		}
	    		if(counter==vertMove){
	    			up=false;
	    			down=true;
	    			counter=0;
	    			if(hp>400){
	    				// setting random movement
	    				vertMove=(int)(Math.random()*100%4*5+10);
	    				if(!stop){
	    					moveRect((int)(Math.random()*1000%350+50),(getY()));
	    				}
	    			}
	    			else{
	    				rage=true;
	    				vertMove=20;
	    				if(!stop){
	    					moveRect(250,getY());
	    				}
	    			}
	    		}
	    	}
	    	else if(!down && !up){
	    		// when reaching the bottom, starts shooting
	    		throwCD--;
	    		spriteName="walk";
	    		if(throwCD<0){
	    			spriteName="throw";
	    			if(throwCD==-8*4+10){
	    				eb.add(new EnemyBullet("web",getX()-20,getY()+40,10,0,0));
	    			}
	    			if(throwCD==-8*4){
	    				throwCD=0;
	    				up=true;
	    				counter=0;
	    			}
	    		}
	    	}
    		counter++;
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

		spriteNum+=0.25;
		if(oriSprite.equals("die")){
			spriteName="die";
		}
		if(!spriteName.equals(oriSprite)){
			spriteNum=0;
		}
    }
    
    
}