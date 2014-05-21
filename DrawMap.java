import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;


public class DrawMap {
	Scanner inFile;
	Color[] col = {Color.BLACK,Color.WHITE,Color.GREEN,Color.RED,Color.BLUE};

    public DrawMap() {
    	inFile = null;
    }
    public Platform[][] getMap(String name,Image dbImage){
    	try{
    		inFile=new Scanner(new BufferedReader(new FileReader(name+".txt")));
    	}
    	catch(IOException ex){
    		System.out.println("No such file");
    	}
    	Graphics dbg = dbImage.getGraphics();
    	Platform[][] maps = new Platform[40][50];
    	for(int y=0;y<40;y++){
    		for (int x=0;x<50;x++){
    			maps[y][x]=new Platform(x*20,y*20,20,20,col[Integer.parseInt(inFile.next())]);
    		}
    	}
    	return maps;
    }
    
    
}