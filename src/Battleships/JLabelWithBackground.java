package zolotykh_CSCI201_Assignment4;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;

public class JLabelWithBackground extends JLabel {
	//custom JLabel class used to paint background and foreground
	//for a regular JLabel
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BufferedImage background = null; //image references
	private BufferedImage foreground = null;
	
	JLabelWithBackground (BufferedImage background, BufferedImage foreground) {
		this.background = background; //takes in a reference to change the background/foreground
		this.foreground = foreground;
	}
	
	protected void paintComponent (Graphics g) {
		//function responsible for actually painting the images on the label
		super.paintComponent(g);
		g.drawImage(background, 0, 0, null);
		g.drawImage(foreground, this.getWidth()/2-8, this.getHeight()/2-8, null);
	}
	
	public void changeBackground (BufferedImage newBackground) {
		//changes the reference for the background and repaints
		background = newBackground;
		repaint();
	}
	
	public void changeForeground (BufferedImage newForeground) {
		//changes the reference for the foreground and repaints
		foreground = newForeground;
		repaint();
	}
}
