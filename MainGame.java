import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

// Megaman X4
// Edward Tsai
// 2012, 06, 15

// MainGame Class
// This is the main class, which puts everything together
// The user will be able to select four bosses with its own unique level
// the user will control a character named Megaman X, who will need to navigate through the levels and defeat the bosses




/* Legend:
 *Black/Blue - wall
 *Red - instant death
 *Green - ladder
 *cyan - protal
 */
 


/* 
 * Features missing:
 * Special weapons
 * Map scrolling
 * Dash
 */

public class MainGame extends JFrame implements MouseListener,MouseMotionListener,KeyListener{
	public final int RIGHT=39, LEFT=37, F=70, Q=81, W=87, D=68, UP=38,DOWN=40, ENTER=10, SPACE=32; // ASCII value for keys
	private Color[] col = {Color.BLACK,Color.WHITE,Color.GREEN,Color.RED,Color.BLUE,Color.CYAN,new Color(255, 127, 0)}; // colours on the map
	private boolean[] keys,mouse; // boolean for keyboard and mouse buttons
	private boolean keyReleased; // check if a key is released
	private int mx,my; // mouse position
	private int stage,cur; // stage - menu page, cur - boss selection
	private int counter; // counter for sprintes
	private Scanner inFile; // file scanner
	private Image dbImage; // doublebuffered image
	private Graphics dbg; // graphics
	private X Ex; // X, main character
	private Platform[][] maps;	// 2-D array for paltforms that represents the map
	private SpiderBoss spider; // spider boss
	private MushroomBoss mush; // mushroom boss
	private PeacockBoss peak; // peacock boss
	private OwlBoss owl; // owl boss
	private Enemy enemy; // class for all the enemy
	private LinkedList<Bullet> bullets; // friendly bullets
	private LinkedList<EnemyBullet> eBullets; // enemy bullets
	private LinkedList<Item> items; // items
	private Boss boss; // class for all the bosses
	private HashMap<String,Image[]>UI; // sprites for user interface
	
	public MainGame(){
		super("Megaman X4");
		
		addMouseListener(this);
		addKeyListener(this);
		addMouseMotionListener(this);
		
		keys=new boolean[2000];
		mouse=new boolean[3];
		inFile=null;
		keyReleased=true;
		mx=0;
		my=0;
		stage=0;
		counter=0;
		cur=0;
		
		bullets = new LinkedList<Bullet>();
		eBullets = new LinkedList<EnemyBullet>();
		items = new LinkedList<Item>();
		enemy = new Enemy();
		loadPics();
			
		Ex = new X(0,0);
		spider = new SpiderBoss(300,200);
		mush = new MushroomBoss(410,300);
		peak = new PeacockBoss(400,400);
		owl = new OwlBoss(470,540);
		boss = new Boss(spider,mush,peak,owl);
		
		setSize(1000,830);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public void move(){
		// Move main character
		if(stage==4){
			if(keys[RIGHT]){
				cur=(cur+1+4)%4;
			}
			if(keys[LEFT]){
				cur=(cur-1+4)%4;
			}
		}
		if(stage==5){
			Ex.move(maps,bullets);
			if(Ex.getBoss()){
				maps=getMap("boss");
				boss.activate();
				enemy.clear();
				items.clear();
				bullets.clear();
				eBullets.clear();
			}
			Ex.setUp(keys[UP]);
			Ex.setDown(keys[DOWN]);
			if(keys[W]||keys[D]){
				Ex.charge();
			}
			if(keys[RIGHT]){
				Ex.setHor(10);
			}
			if(keys[LEFT]){
				Ex.setHor(-10);
			}
			if(keys[F]){
				// jump
				keys[F]=false;
				if(Ex.getOnWall()){
					Ex.setJumped(-30);
					Ex.setHor(Ex.getHor()*-2);
				}
				if(Ex.getJumped()==false){
					Ex.setJumped(-30);
				}
			}
// 			Main Character ends
			
			
			//Boss
			if(boss.activated()){
				boss.move(maps,bullets,Ex,eBullets);
			}
			if(!boss.alive()){
				boss.deActivate();
			}
			
			
			enemy.move(maps,bullets,Ex,eBullets,items);
			
			LinkedList removes = new LinkedList();
			//Bullets
			for(Bullet i:bullets){
				i.move(maps);
				if(!i.alive() || Math.abs(Ex.getX()-i.getX())>550 || Math.abs(Ex.getY()-i.getY())>550){
					removes.add(i);
				}
			}
			for(int i=0;i<removes.size();i++){
				bullets.remove(removes.get(i));
			}
			removes.clear();
			//EnemyBullets
			for(EnemyBullet i:eBullets){
				i.move(maps,Ex);
				if(!i.alive() || Math.abs(Ex.getX()-i.getX())>800 || Math.abs(Ex.getY()-i.getY())>800){
					removes.add(i);
				}
			}
			for(int i=0;i<removes.size();i++){
				eBullets.remove(removes.get(i));
			}
			removes.clear();
			// items
			for(Item i:items){
				i.move(maps,Ex);
				if(!i.alive() || Math.abs(Ex.getX()-i.getX())>500 || Math.abs(Ex.getY()-i.getY())>500){
					removes.add(i);
				}
			}
			for(int i=0;i<removes.size();i++){
				items.remove(removes.get(i));
			}
		}
		else{
			keys[RIGHT]=false;
			keys[LEFT]=false;
		}
	}
	
	public void paint (Graphics g){
		if(stage==5){
			if(dbg!=null){
				// health bars
				dbg.setColor(Color.BLACK);
				dbg.drawRect(Ex.getX()+50+29,Ex.getY()+200+29,21,101);
				dbg.setColor(Color.WHITE);
				dbg.fillRect(Ex.getX()+50+30,Ex.getY()+200+30,20,100);
				dbg.setColor(Color.RED);
				dbg.fillRect(Ex.getX()+50+30,Ex.getY()+200+30+100-Ex.getHealth(),20,Ex.getHealth());
				if(boss.activated()){
					dbg.setColor(Color.BLACK);
					dbg.drawRect(Ex.getX()+900+29,Ex.getY()+200+29,21,101);
					dbg.setColor(Color.WHITE);
					dbg.fillRect(Ex.getX()+900+30,Ex.getY()+200+30,20,100);
					dbg.setColor(Color.RED);
					dbg.fillRect(Ex.getX()+900+30,Ex.getY()+200+30+100-(int)(boss.getHealth()/10.0),20,(int)(boss.getHealth()/10.0));
				}
			}
			g.drawImage(dbImage,-(Ex.getX()-500)-500,-(Ex.getY()-400)+30-500,this);
			if(Ex.getHealth()<=0){
				// character died
				g.setColor(Color.RED);
				g.drawString("You Died",500,400);
				counter++;
				if(counter==20){
					stage=4;
					boss.deActivate();
				}
			}
			if(!boss.alive()){
				// boss died
				g.setColor(Color.RED);
				g.drawString("You Won",500,400);
				counter++;
				if(counter==20){
					stage=4;
					boss.deActivate();
				}
			}
		}
		else{
			g.drawImage(dbImage,0,30,this);
		}
	}
	
	public void display(){
		if(dbImage==null){
			dbImage = createImage(3000,2600);
			dbg=dbImage.getGraphics();
		}
		if(stage==0){
			// intro page
			dbg.drawImage(UI.get("menu")[counter%59],0,0,this);
			if(counter==60 || keys[ENTER] || keys[SPACE]){
				stage=1;
				counter=-1;
				keys[ENTER]=false;
				keys[SPACE]=false;
			}
		}
		else if(stage==1){
			// press enter to continue
			dbg.drawImage(UI.get("start")[counter/4%2],0,0,this);
			if(keys[ENTER]||keys[SPACE]){
				stage=2;
				counter=-1;
				keys[ENTER]=false;
				keys[SPACE]=false;
			}
		}
		else if(stage==2){
			// character select
			counter=-1;
			dbg.drawImage(UI.get("char")[0],0,0,this);
			if(keys[ENTER]){
				stage=4;
				counter=-1;
				keys[ENTER]=false;
				keys[SPACE]=false;
			}
			else if(keys[SPACE]){
				stage=3;
				keys[ENTER]=false;
				keys[SPACE]=false;
			}
		}
		else if(stage==3){
			// controls
			counter=-1;
			dbg.drawImage(UI.get("con")[0],0,0,this);
			if(keys[ENTER]){
				stage=2;
				keys[ENTER]=false;
				keys[SPACE]=false;
			}
		}
		else if(stage==4){
			// boss select
			int[] pos={167,346,519,672};
			dbg.drawImage(UI.get("boss")[0],0,0,this);
			dbg.drawImage(UI.get("pick")[counter%3],pos[cur],178,this);
			dbg.drawImage(UI.get("map")[cur],405,435,this);
			if(keys[ENTER]){
				stage=5;
				keys[ENTER]=false;
				keys[SPACE]=false;
				boss.setType(cur);
				counter=-1;
				enemy.clear();
				items.clear();
				bullets.clear();
				eBullets.clear();
				maps=getMap("level "+cur);
			}
		}
		else if(stage==5){
			// main game
			dbg.setColor(new Color(0,0,0));
			dbg.fillRect(0,0,3000,2600);
			drawMap(dbImage,maps);
			dbg.setColor(Color.BLACK);
//			dbg.drawRect(Ex.getX()+200,Ex.getY()+300,600,400);
			Ex.drawImage(dbg,this);
			if(boss.activated()){
				boss.drawImage(dbg,this);
			}
			
			enemy.display(dbg,this);
			for(Bullet i:bullets){
				i.drawImage(dbg,this);
			}
			for(EnemyBullet i:eBullets){
				i.drawImage(dbg,this);
			}
			for(Item i:items){
				i.drawImage(dbg,this);
			}
		}
		if(stage!=5){
			counter++;
		}
	}
    public Platform[][] getMap(String name){
    	// loads the map from a file
    	try{
    		inFile=new Scanner(new BufferedReader(new FileReader(name+".txt")));
    	}
    	catch(IOException ex){
    		System.out.println("No such file: "+name);
    	}
    	Platform[][] maps = new Platform[80][100];
    	for(int y=0;y<80;y++){
    		for (int x=0;x<100;x++){
    			int num=Integer.parseInt(inFile.next());
    			if(num==6){
    				//met spawn point
    				enemy.addMet(x*20,y*20);
    				num=1;
    			}
    			if(num==7){
    				//bb spawn point
    				enemy.addBB(x*20,y*20);
    				num=1;
    			}
    			if(num==8){
    				//x spawn point
    				Ex = new X(x*20,y*20);
    				num=1;
    			}
    			if(num==9){
    				//grenade spawn point
    				enemy.addNade(x*20,y*20);
    				num=1;
    			}
    			if(num==10){
    				//armor spawn point
    				enemy.addMor(x*20,y*20);
    				num=1;
    			}
    			maps[y][x]=new Platform(x*20,y*20,20,20,col[num]);
    		}
    	}
    	return maps;
    }
    public void drawMap(Image mapImage,Platform[][] maps){
    	//displays the map
    	Graphics mapG = mapImage.getGraphics();
    	for(int y=0;y<80;y++){
    		for (int x=0;x<100;x++){
    			mapG.setColor(maps[y][x].getColor());
    			mapG.fillRect(x*20+500,y*20+500,20,20);
    		}
    	}
    }
    public void loadPics(){
    	
    	UI= new HashMap<String,Image[]>();
    	String tmp;
    	int num;
    	
    	tmp="boss";
    	num=1;
    	UI.put(tmp,new Image[num]);
    	for(int i=0;i<num;i++){
    		UI.get(tmp)[i]=new ImageIcon("UI\\"+tmp+" "+i+".png").getImage();
    	}
    	tmp="char";
    	num=1;
    	UI.put(tmp,new Image[num]);
    	for(int i=0;i<num;i++){
    		UI.get(tmp)[i]=new ImageIcon("UI\\"+tmp+" "+i+".png").getImage();
    	}
    	tmp="con";
    	num=1;
    	UI.put(tmp,new Image[num]);
    	for(int i=0;i<num;i++){
    		UI.get(tmp)[i]=new ImageIcon("UI\\"+tmp+" "+i+".png").getImage();
    	}
    	tmp="map";
    	num=4;
    	UI.put(tmp,new Image[num]);
    	for(int i=0;i<num;i++){
    		UI.get(tmp)[i]=new ImageIcon("UI\\"+tmp+" "+i+".png").getImage();
    	}
    	tmp="menu";
    	num=61;
    	UI.put(tmp,new Image[num]);
    	for(int i=0;i<num;i++){
    		UI.get(tmp)[i]=new ImageIcon("UI\\"+tmp+" "+i+".png").getImage();
    	}
    	tmp="pick";
    	num=3;
    	UI.put(tmp,new Image[num]);
    	for(int i=0;i<num;i++){
    		UI.get(tmp)[i]=new ImageIcon("UI\\"+tmp+" "+i+".png").getImage();
    	}
    	tmp="start";
    	num=2;
    	UI.put(tmp,new Image[num]);
    	for(int i=0;i<num;i++){
    		UI.get(tmp)[i]=new ImageIcon("UI\\"+tmp+" "+i+".png").getImage();
    	}
    }
    
    public static void main(String[]args) throws IOException{
    	MainGame frame = new MainGame();
    	System.out.println();
    	while(true){
    		frame.display();
    		frame.move();
    		frame.repaint();
    		delay(20);
    	}
    }
    
    //Keyboard
	public void keyPressed(KeyEvent e){
		if(keyReleased){
			keyReleased=false;
			keys[e.getKeyCode()]=true;
		}
		if(stage==5){
			keys[e.getKeyCode()]=true;
		}
		if(e.getKeyCode()==27){
			System.exit(0);
		}

	}
	public void keyReleased(KeyEvent e){
		keyReleased=true;
		keys[e.getKeyCode()]=false;
		if(stage==5){
			if(e.getKeyCode() == e.VK_RIGHT){
				Ex.setHor(0);
			}
			if(e.getKeyCode() == e.VK_LEFT){
				Ex.setHor(0);
			}
			if(e.getKeyCode() == e.VK_UP){
				Ex.setUp(false);
			}
			if(e.getKeyCode() == e.VK_DOWN){
				Ex.setDown(false);
			}
			if(e.getKeyCode() == e.VK_F){
				Ex.setReleased();
			}
			if(e.getKeyCode() == e.VK_W || e.getKeyCode() == e.VK_D){
				Ex.setShoot();
			}
		}
	}
	public void keyTyped(KeyEvent e){
	}
	//Mouse
	public void mousePressed(MouseEvent e){
		if(e.getButton()==e.BUTTON1){
			mouse[0]=true;
		}
		else if(e.getButton()==e.BUTTON2){
			mouse[1]=true;
		}
		else if(e.getButton()==e.BUTTON3){
			mouse[2]=true;
		}
	}
	public void mouseEntered(MouseEvent e){
	}
	public void mouseReleased(MouseEvent e){
		if(e.getButton()==e.BUTTON1){
			mouse[0]=false;
		}
		else if(e.getButton()==e.BUTTON2){
			mouse[1]=false;
		}
		else if(e.getButton()==e.BUTTON3){
			mouse[2]=false;
		}
	}
	public void mouseExited(MouseEvent e){
	}
	public void mouseClicked(MouseEvent e){
	}
	//MouseMotion
	public void mouseMoved(MouseEvent e){
		mx=e.getX();
		my=e.getY();
	}
	public void mouseDragged(MouseEvent e){
	}
	public static void delay(long len){
		//delaying
		try{
			Thread.sleep(len);
		}
		catch(InterruptedException ex){
			System.out.println(ex);
		}
	}
}

/*Log
 *5.21:
 *Bullet
 *Character - shooting, health and damage
 *5.27:
 *Met - AI, shooting and block
 *Sprites
 *Character changed to X
 *X - charged shooting
 *Offscreen activation/reset
 *BattleBone - AI, dropping bomb
 *5.28
 *Item
 *5.30
 *SpiderBoss - AI ( first half), shooting web
 *6.09
 *SpiderBoss complete
 *levels
 *Colours added
 *6.10
 *MushroomBoss - complete
 *6.11
 *PeacockBoss - nothing really
 *6.12
 *PeacockBoss - complete
 *EnemyBullet - peacock related bullets
 *6.13
 *OwlBoss - added
 *6.14
 *OwlBoss - complete
 *Armor, Grenade added
 */