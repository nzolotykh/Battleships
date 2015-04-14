package zolotykh_CSCI201_Assignment4;

import java.util.Random;

import javax.swing.JLabel;

public class TimerThread extends Thread {
	//used for the main timer inside the game
	//responsible for the count down, as well as computer moves
	
	JLabel timerLabel;
	GUI gui;
	Random rand;
	Random rand2;
	int randomTime;
	boolean needToRestartRandom = true;
	
	public TimerThread (GUI gui) {
		this.timerLabel = gui.timerLabel;
		this.gui = gui;
	}
	
	public void run() {
		if (gui.gameOver) {
			//game is over, stop the timer
			return;
		}
		
		if (needToRestartRandom) {
			//restarts the random seeds when needed
			rand = new Random();
			rand2 = new Random();
			randomTime = rand2.nextInt((10-1) + 1) + 1;
			needToRestartRandom = false;
		}
		
		if (gui.seconds >= 10)
			timerLabel.setText("Time - " + "0:" + (gui.seconds));
		if (gui.seconds < 10)
			timerLabel.setText("Time - " + "0:0" + (gui.seconds));
		if (gui.seconds == 3) {
			//3 seconds left warning
			gui.textArea.setText(gui.textArea.getText() + "\nWarning - 3 seconds left in the round!");
		}
		if (gui.seconds == 0) {
			//time runs out
			timerLabel.setText("Time - 0:00");
			gui.seconds = 15;
			gui.player1Moved = false;
			gui.player2Moved = false;
			gui.textArea.setText(gui.textArea.getText() + "\nRound " + gui.roundNumber);
			gui.roundNumber++;
			needToRestartRandom = true;
		}
		gui.seconds--;
		
		if (gui.seconds == (15-randomTime)) {
			//computer shoots
			int randomCoordinate = rand.nextInt((120 - 12) + 1) + 12;
			while (true) {
				if (randomCoordinate%11 == 0) {
					randomCoordinate = rand.nextInt((120 - 12) + 1) + 12;
				} else {
					break;
				}
			}
			String result = Battleships.checkEnemyCoordinates(randomCoordinate/11-1, randomCoordinate%11-1, gui);
			while (result.equals("GUESSED")) {
				randomCoordinate = rand.nextInt((120 - 12) + 1) + 12;
				while (true) {
					if (randomCoordinate%11 == 0) {
						randomCoordinate = rand.nextInt((120 - 12) + 1) + 12;
					} else {
						break;
					}
				}
				result = Battleships.checkEnemyCoordinates(randomCoordinate/11-1, randomCoordinate%11-1, gui);
			}
			String cellClicked = Character.toString(gui.yaxis.charAt(randomCoordinate/11-1)) + (randomCoordinate%11);
			gui.player2Moved = true;
			if (result.equals("HIT")) {
				//computer hit a player ship
				if (!gui.cannonSound[0].isPlaying()) {
					//if the first sound is already playing, play the second one!
					gui.cannonSound[0].play();
				} else {
					gui.cannonSound[1].play();
				}
				gui.textArea.setText(gui.textArea.getText() + "\nComputer hit " + cellClicked + 
						" and hit " + (String) Battleships.lastHitShip.type + "! (" + gui.getTime() + ")");
				if (Battleships.lastHitShip.sunken) {
					//just sank a ship!!
					gui.textArea.setText(gui.textArea.getText() + "\nComputer sunk Player's " + 
							(String) Battleships.lastHitShip.type + "!");
				}
			} else if (result.equals("MISS")) {
				if (!gui.cannonSound[0].isPlaying()) {
					//if the first sound is already playing, play the second one!
					gui.cannonSound[0].play();
				} else {
					gui.cannonSound[1].play();
				}
				gui.textArea.setText(gui.textArea.getText() + "\nComputer hit " + cellClicked + 
						" and missed! (" + gui.getTime() + ")");
			}
			
			if (Battleships.checkEnemyVictory()) {
				//ENEMY WON???
				gui.gameOver = true;
				gui.textArea.setText(gui.textArea.getText() + "\nComputer has won!");
				gui.executor.submit(new GameOverThread(gui));
				return;
			}
			if (gui.player1Moved && gui.player2Moved) {
				//computer just shot, see if the player has shot as well
				//if so, start the next round
				gui.player1Moved = false;
				gui.player2Moved = false;
				gui.seconds = 15;
				timerLabel.setText("Time - 0:15");
				gui.textArea.setText(gui.textArea.getText() + "\nRound " + gui.roundNumber);
				gui.roundNumber++;
				needToRestartRandom = true;
			}
		}
		
		try {
			//one second
			sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		run();
	}
}
