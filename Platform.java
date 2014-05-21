import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

// Platform Class
// the entire map is consist of 50x40 '20 pixel by 20 pixel' rectangles
// the platform class is used to store the type of each rectangle

public class Platform extends Rectangle{
	
	private Color col; // colour of the block
	private String type; // type of block
    public Platform(int a, int b, int w, int h, Color c) {
    	
		x=a;
		y=b;
		width=w;
		height=h;
		col=c;
		if(col==Color.BLACK || col==Color.BLUE){
			type="wall";
		}
		else if(col==Color.WHITE){
			type="empty";
		}
		else if(col==Color.RED){
			type="death";
		}
		else if(col==Color.GREEN){
			type="ladder";
		}
		else if(col==Color.CYAN){
			type="portal";
		}
    }
    public Color getColor(){
    	return col;
    }
    public String getType(){
    	return type;
    }
}