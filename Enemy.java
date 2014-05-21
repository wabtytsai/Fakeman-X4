import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

// Enemy Class
// Since all the enemy will go through the same commands (attack, move, drawimage), I put it all under the same class so it is more organized
// It consists of 4 different enemies: Met, BattleBone, Grenade, and Armor
// all enemy deactivates when leaving the player's 'sight', which is a 600 pixels by 400 pixels box center at the player
// it will reactivate when it re-enter back into the box

public class Enemy {
	
	
	private LinkedList<Met> mets; // list of mets
	private LinkedList<BattleBone> BBs; // list of battlebones
	private LinkedList<Grenade> nades; // list of grenades
	private LinkedList<Armor> mors; // list or armors

    public Enemy() {
		mets = new LinkedList<Met>();
		BBs = new LinkedList<BattleBone>();
		nades = new LinkedList<Grenade>();
		mors = new LinkedList<Armor>();
    }
    public void addMet(int x,int y){
    	mets.add(new Met(x,y));
    }
    public void addBB(int x,int y){
    	BBs.add(new BattleBone(x,y));
    }
    public void addNade(int x,int y){
    	nades.add(new Grenade(x,y));
    }
    public void addMor(int x, int y){
    	mors.add(new Armor(x,y));
    }
    
    public void display(Graphics g, MainGame b){
    	// draws all the sprites
		for(Met tmp:mets){
			if(tmp.activated()){
				tmp.drawImage(g,b);
//				g.fillRect(tmp.getX()+500,tmp.getY()+500,tmp.getWidth(),tmp.getHeight());
			}
		}
		for(BattleBone tmp: BBs){
			if(tmp.activated()){
				tmp.drawImage(g,b);
//				g.fillRect(tmp.getX()+500,tmp.getY()+500,tmp.getWidth(),tmp.getHeight());
			}
		}
		for(Grenade tmp: nades){
			if(tmp.activated()){
				tmp.drawImage(g,b);
//				g.fillRect(tmp.getX()+500,tmp.getY()+500,tmp.getWidth(),tmp.getHeight());
			}
		}
		for(Armor tmp: mors){
			if(tmp.activated()){
				tmp.drawImage(g,b);
//				g.fillRect(tmp.getX()+500,tmp.getY()+500,tmp.getWidth(),tmp.getHeight());
			}
		}
    }
    
    public void move(Platform[][] maps, LinkedList<Bullet> bullets,X Ex,LinkedList<EnemyBullet> eBullets,LinkedList<Item> items){
    	moveMets(maps,bullets,Ex,eBullets,items);
    	moveBBs(maps,bullets,Ex,eBullets,items);
    	moveNades(maps,bullets,Ex,eBullets,items);
    	moveMors(maps,bullets,Ex,eBullets,items);
    }
    public void moveMets(Platform[][] maps, LinkedList<Bullet> bullets,X Ex,LinkedList<EnemyBullet> eBullets,LinkedList<Item> items){
		
		for(Met tmp:mets){
			if(tmp.activated()){
				tmp.move(maps,bullets,Ex,eBullets);
				if(Ex.getRect().intersects(tmp.getRect())){
					//collide with bullets
					Ex.receiveDmg(tmp.getDmg());
				}
				if(!tmp.alive()){
					// if is destoryed, have a chance to drop items
					int a = (int)(Math.random()*100%3);
					int b = (int)(Math.random()*100%3);
					if((a!=0 && b==0)||(a==0 && b!=0)){
						items.add(new Item(tmp.getX()/20*20,tmp.getY()/20*20,a,b));
					}
					tmp.deActivate();
					tmp.setReset(false);
				}
			}
			if(tmp.getX()>Ex.getX()-300 && tmp.getX()<Ex.getX()+300 && tmp.getY()>Ex.getY()-200 && tmp.getY()<Ex.getY()+200){
				// if it moves back to the sight
				tmp.activate();
			}
			else{
				if(!(tmp.ox()>Ex.getX()-300 && tmp.ox()<Ex.getX()+300 && tmp.oy()>Ex.getY()-200 && tmp.oy()<Ex.getY()+200)){			
					// able to be reset when player moved pass original spawn point
					tmp.setReset(true);
				}
				tmp.deActivate();
			}
		}
    }
    public void moveBBs(Platform[][] maps, LinkedList<Bullet> bullets,X Ex,LinkedList<EnemyBullet> eBullets,LinkedList<Item> items){
    	// similar to moveMets
    	for(BattleBone tmp:BBs){
			if(tmp.activated()){
				tmp.move(maps,bullets,Ex,eBullets);
				if(Ex.getRect().intersects(tmp.getRect())){
					Ex.receiveDmg(tmp.getDmg());
				}
				if(!tmp.alive()){
					int a = (int)(Math.random()*100%3);
					int b = (int)(Math.random()*100%3);
					if((a!=0 && b==0)||(a==0 && b!=0)){
						items.add(new Item(tmp.getX()/20*20,tmp.getY()/20*20,a,b));
					}
					tmp.deActivate();
					tmp.setReset(false);
				}
			}
			if(tmp.getX()>Ex.getX()-300 && tmp.getX()<Ex.getX()+300 && tmp.getY()>Ex.getY()-200 && tmp.getY()<Ex.getY()+200){
				tmp.activate();
			}
			else{
				if(!(tmp.ox()>Ex.getX()-300 && tmp.ox()<Ex.getX()+300 && tmp.oy()>Ex.getY()-200 && tmp.oy()<Ex.getY()+200)){			
					tmp.setReset(true);
				}
				tmp.deActivate();
			}
		}
    }
    
    public void moveNades(Platform[][] maps, LinkedList<Bullet> bullets,X Ex,LinkedList<EnemyBullet> eBullets,LinkedList<Item> items){
		//similar to moveMets
		for(Grenade tmp:nades){
			if(tmp.activated()){
				tmp.move(maps,bullets,Ex,eBullets);
				if(Ex.getRect().intersects(tmp.getRect())){
					Ex.receiveDmg(tmp.getDmg());
				}
				if(!tmp.alive()){
					int a = (int)(Math.random()*100%3);
					int b = (int)(Math.random()*100%3);
					if((a!=0 && b==0)||(a==0 && b!=0)){
						items.add(new Item(tmp.getX()/20*20,tmp.getY()/20*20,a,b));
					}
					tmp.deActivate();
					tmp.setReset(false);
				}
			}
			if(tmp.getX()>Ex.getX()-300 && tmp.getX()<Ex.getX()+300 && tmp.getY()>Ex.getY()-200 && tmp.getY()<Ex.getY()+200){
				tmp.activate();
			}
			else{
				if(!(tmp.ox()>Ex.getX()-300 && tmp.ox()<Ex.getX()+300 && tmp.oy()>Ex.getY()-200 && tmp.oy()<Ex.getY()+200)){			
					tmp.setReset(true);
				}
				tmp.deActivate();
			}
		}
    }
    public void moveMors(Platform[][] maps, LinkedList<Bullet> bullets,X Ex,LinkedList<EnemyBullet> eBullets,LinkedList<Item> items){
		//similar to moveMets
		for(Armor tmp:mors){
			if(tmp.activated()){
				tmp.move(maps,bullets,Ex,eBullets);
				if(Ex.getRect().intersects(tmp.getRect())){
					Ex.receiveDmg(tmp.getDmg());
				}
				if(!tmp.alive()){
					int a = (int)(Math.random()*100%3);
					int b = (int)(Math.random()*100%3);
					if((a!=0 && b==0)||(a==0 && b!=0)){
						items.add(new Item(tmp.getX()/20*20,tmp.getY()/20*20,a,b));
					}
					tmp.deActivate();
					tmp.setReset(false);
				}
			}
			if(tmp.getX()>Ex.getX()-300 && tmp.getX()<Ex.getX()+300 && tmp.getY()>Ex.getY()-200 && tmp.getY()<Ex.getY()+200){
				tmp.activate();
			}
			else{
				tmp.deActivate();
				tmp.setReset(true);
			}
		}
    }
    public void clear(){
    	BBs.clear();
    	mets.clear();
    	nades.clear();
    	mors.clear();
    }
    
}