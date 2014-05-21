import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

// Boss Class
// This class stores all the information for the four bosses
// the main class will be able to access only the current selected boss

public class Boss {
	private int type; // current selected boss
	private SpiderBoss spider; // spider boss
	private MushroomBoss mush; // mushroom boss
	private PeacockBoss peak; // peacock boss
	private OwlBoss owl; // owl boss
	
    public Boss(SpiderBoss a, MushroomBoss b, PeacockBoss c,OwlBoss d) {
    	spider=a;
    	mush=b;
    	peak=c;
    	owl=d;
    }
    public void setType(int n){
    	type=n;
    }
    public void activate(){
    	// activate
    	if(type==0){
    		spider.activate();
    	}
    	else if(type==3){
    		mush.activate();
    	}
    	else if(type==1){
    		peak.activate();
    	}
    	else{
    		owl.activate();
    	}
    }
    public void move(Platform[][] maps, LinkedList<Bullet> stuff,X Ex,LinkedList<EnemyBullet> eb){
    	// movement
    	// maps is the all the platforms in the level
    	// stuff is friendly bullets
    	// Ex is the main character
    	// eBullets is enemy bullets
    	
    	if(type==0){
    		spider.move(maps,stuff,Ex,eb);
    	}
    	else if(type==3){
    		mush.move(maps,stuff,Ex,eb);
    	}
    	else if(type==1){
    		peak.move(maps,stuff,Ex,eb);
    	}
    	else{
    		owl.move(maps,stuff,Ex,eb);
    	}
    }
    public boolean alive(){
    	// check hp
    	if(type==0){
    		return spider.alive();
    	}
    	else if(type==3){
    		return mush.alive();
    	}
    	else if(type==1){
    		return peak.alive();
    	}
    	else{
    		return owl.alive();
    	}
    }
    public boolean activated(){
    	// check active
    	if(type==0){
    		return spider.activated();
    	}
    	else if(type==3){
    		return mush.activated();
    	}
    	else if(type==1){
    		return peak.activated();
    	}
    	else{
    		return owl.activated();
    	}
    }
    public void deActivate(){
    	// deactivate
    	if(type==0){
    		spider.deActivate();
    	}
    	else if(type==3){
    		mush.deActivate();
    	}
    	else if(type==1){
    		peak.deActivate();
    	}
    	else{
    		owl.deActivate();
    	}
    }
    public int getHealth(){
    	// get hp
    	if(type==0){
    		return spider.getHealth();
    	}
    	else if(type==3){
    		return mush.getHealth();
    	}
    	else if(type==1){
    		return peak.getHealth();
    	}
    	else{
    		return owl.getHealth();
    	}
    }
    public void drawImage(Graphics dbg, MainGame b){
    	// draw sprites onto doublebuffered image
    	if(type==0){
    		spider.drawImage(dbg,b);
    	}
    	else if(type==3){
    		mush.drawImage(dbg,b);
    	}
    	else if(type==1){
    		peak.drawImage(dbg,b);
    	}
    	else{
    		owl.drawImage(dbg,b);
    	}
    	
    }
    
    
}