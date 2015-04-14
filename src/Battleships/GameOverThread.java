package zolotykh_CSCI201_Assignment4;

import javax.swing.JOptionPane;

public class GameOverThread extends Thread {
	//starts when the game is over
	//first waits for the animations to finish
	//then shows the pop-up window and cleans up the game
	GUI gui;
	
	GameOverThread(GUI gui) {
		this.gui = gui;
	}
	
	public void run() {
		try {
			//cannon sound + explosion sound + sinking sound
			//by the time this wait is over, the other sounds/animations
			//are guaranteed to have stopped, because this wait is so long
			sleep(5455);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JOptionPane.showMessageDialog(gui, 
				"Congratulations, admiral! You defeated the enemy fleet", 
				"Superior victory", 
				JOptionPane.PLAIN_MESSAGE, 
				null);
		System.out.println("about to call cleanup");
		Battleships.cleanup(gui);
	}
}
