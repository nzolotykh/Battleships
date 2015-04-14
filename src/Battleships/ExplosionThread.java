package zolotykh_CSCI201_Assignment4;

import java.awt.event.ActionEvent;

public class ExplosionThread extends Thread {
	//used for when an explosion happens ... aka when a ship is hit
	private GUI gui;
	private int index = 0;
	private JLabelWithBackground label;
	private Ship ship;
	boolean computerSide;
	
	ExplosionThread (GUI gui, JLabelWithBackground label, Ship ship, boolean computerSide) {
		this.gui = gui;
		this.label = label;
		this.ship = ship;
		this.computerSide = computerSide;
	}
	
	public void run() {
		//play the cannon sound first
		if (!gui.cannonSound[0].isPlaying()) {
			//if the first sound is already playing, play the second one!
			gui.cannonSound[0].play();
		} else {
			gui.cannonSound[1].play();
		}
		try {
			//cannon sound
			sleep(1593);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		if (!gui.explodeSound[0].isPlaying()) {
			//if the first sound is already playing, play the second one!
			gui.explodeSound[0].play();
		} else {
			gui.explodeSound[1].play();
		}
		for (int i = 0; i < 5; i++) {
			label.changeForeground(gui.explosion[index]);
			index++;
			if (index == 5) {
				index = 0;
				if (!ship.sunken) {
					//change the label only if the ship has NOT been sunken
					//if it has, the splash sinking animation will change it to what's needed
					if (computerSide) {
						label.changeForeground(gui.images[5-ship.health]);
					} else {
						label.changeForeground(gui.images[6]);
					}
				}
			}
			try {
				//explosion sound length divided by 5
				sleep(463);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}
