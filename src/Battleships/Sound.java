package zolotykh_CSCI201_Assignment4;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {
	//general class responsible for all the in-game sounds
	//constructor takes in the name of the sound file and
	//makes a stream for it
	
	private Clip sound;
	Sound(String fileName) {
		try {
			AudioInputStream stream = AudioSystem.getAudioInputStream(new File(fileName));
			sound = AudioSystem.getClip();
			sound.open(stream);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	public void play() {
		//restarts the clip and plays it
		sound.setFramePosition(0);
		sound.start();
	}
	
	public boolean isPlaying() {
		//checks if the clip is currently playing
		if (sound.isRunning()) {
			return true;
		} else {
			return false;
		}
	}
}
