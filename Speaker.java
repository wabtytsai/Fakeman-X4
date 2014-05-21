import java.io.*;
import javax.sound.sampled.*;

/* NOT CREATED BY ME
 * Source: David Valleau
 * 
 * Creates an object that allows the user to play sound clips
 */

public class Speaker{
	File soundFile;
	Clip clip;
	public Speaker(String file){
		try{
			soundFile = new File(file);
			AudioInputStream audio=AudioSystem.getAudioInputStream(soundFile);
			clip = AudioSystem.getClip();
			clip.open(audio);
		}
		catch (UnsupportedAudioFileException ex){
			System.out.println(ex);
		}
		catch (IOException ex){
			System.out.println(ex);
		}
		catch (LineUnavailableException ex){
			System.out.println(ex);
		}

	}
	public void play(){
		if(clip.isRunning()){
		}
		else{
			clip.setFramePosition(0);
			clip.start();
		}
	}
//	public Speaker getSound(String name){
//		//returns a sound clip 
//		
//		for (int i=0;i<soundName.length;i++){
//			if(soundName[i].equals(name)){
//				return sounds[i];
//			}
//		}
//		System.out.println("Cannot find "+name+"!");
//		return sounds[0];
//	}
}