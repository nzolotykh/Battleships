package zolotykh_CSCI201_Assignment4;

import javax.swing.JLabel;

public class SplashThread extends Thread {
	//happens every time a MISS happens, or a ship is sunk
	//two constructors are used to signal the thread when either of those has happened
	private GUI gui;
	private int index = 0;
	private JLabelWithBackground label = null;
	private Ship ship = null;
	private boolean needToSleep = false;
	private boolean computerSide;
	
	SplashThread (GUI gui, JLabelWithBackground label) {
		//regular miss constructor
		this.gui = gui;
		this.label = label;
	}
	
	SplashThread (GUI gui, Ship ship, boolean computerSide) {
		//ship is sunk constructor
		this.gui = gui;
		this.ship = ship;
		this.computerSide = computerSide;
		needToSleep = true;
	}
	
	public void run() {
		if (needToSleep) {
			//ship has been hit, need to wait for explosion to happen
			JLabelWithBackground [] sunkShipLabels = new JLabelWithBackground[ship.health];
			int labelIndex = 0;
			try {
				//cannon shot + explosion
				sleep(3908);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (!gui.sinkingSound[0].isPlaying()) {
				//if the first sound is already playing, play the second one!
				gui.sinkingSound[0].play();
			} else {
				gui.sinkingSound[1].play();
			}			//now we need to find out which labels to execute the animation on
			for (int x = 0; x < 10; x++) {
				for (int y = 0; y < 10; y++) {
					if (ship.coordinates[x][y]) {
						if (computerSide) {
							sunkShipLabels[labelIndex] = (JLabelWithBackground)gui.computerLabels[11*(x+1) + (y+1)];
						} else {
							sunkShipLabels[labelIndex] = (JLabelWithBackground)gui.playerLabels[11*(x+1) + (y+1)];
						}
						labelIndex++;
					}
				}
			}
			//here we will execute the animation on all the found labels
			//(i.e., the labels of the sunk ship)
			for (int i = 0; i < 7; i++) {
				for (int a = 0; a < sunkShipLabels.length; a++) {
					sunkShipLabels[a].changeForeground(gui.splash[index]);
				}
				index++;
				if (index == 7) {
					index = 0;
					for (int a = 0; a < sunkShipLabels.length; a++) {
						//replacing all the ship's labels with X, or the name of the ship
						if (computerSide) {
							sunkShipLabels[a].changeForeground(gui.images[5-ship.health]);
						} else {
							sunkShipLabels[a].changeForeground(gui.images[6]);
						}
					}
				}
				try {
					//sleeping between splash images
					//sinking sound length divided by 7
					sleep(221);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} else {
			//ship hasn't been hit
			//need to play cannon and then regular splash
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
			if (!gui.splashSound[0].isPlaying()) {
				//if the first sound is already playing, play the second one!
				gui.splashSound[0].play();
			} else {
				gui.splashSound[1].play();
			}
			for (int i = 0; i < 7; i++) {
				label.changeForeground(gui.splash[index]);
				index++;
				if (index == 7) {
					index = 0;
					label.changeForeground(gui.images[4]);
				}
				try {
					//splash sound length divided by 7
					sleep(162);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
