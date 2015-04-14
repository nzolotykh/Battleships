package zolotykh_CSCI201_Assignment4;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Battleships {
	//the main class of the program
	//includes things like Input/Output, gameplay, score tracking
	private static boolean [][] guessed = new boolean [10][10]; //keeps track of which game-field square has been guessed
	private static boolean [][] computerGuessed = new boolean [10][10]; //keeps track of which game-field square has been guessed
	public static ArrayList<String> players = new ArrayList<String>(); //list of player names
	public static String [] field = new String [10]; //stores field lines in plain text

	//declaration of the in-game ships
	public static Ship [] ships = new Ship[5];
	public static Ship [] playerShips = new Ship[5];
	//--------------------------------------------
	public static Ship lastHitShip;
	

	public static void Input (String fileName) {
		//function for reading in the input from the file
		//assigns values to different variables

		try {
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			//format check starts here
			//first check high scores
			String line = br.readLine();
			//check the field + add the ships
			int fieldCount = 0; //counts number of field lines
			while (line != null) {
				//runs through the field
				if (line.matches("[ABCDX]{10}") && fieldCount < 10) {
					//line is 10 chars long, consists of A, B, C, D, or X
					field[fieldCount] = line;
					line = br.readLine();
				} else {
					//bad file format, the field is over 10 lines, or has inappropriate characters
					System.out.println("Format mismatch. Select a file with proper format and try again.");
					br.close();
					System.exit(0);
				}
				fieldCount++;
			}
			//these count the number of each type of a ship
			int numA = 0;
			int numB = 0;
			int numC = 0;
			int numD = 0;
			
			boolean [][] visited = new boolean[10][10]; //keeps track of each tile that has been visited
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 10; j++) {
					//starting all to false
					visited[i][j] = false;
				}
			}
			
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 10; j++) {
					//these loops run through the whole field and accomplish two tasks:
					//1. count the number of ships of each type
					//2. make sure ships are of correct length
					if (!visited[i][j]) {
						visited[i][j] = true;
						if (field[i].charAt(j) == 'A') {
							numA++; //number of aircraft carriers on the battlefield increases by 1
							if (numA > 1) {
								//more than 1 aircraft carrier, must be an error!
								//bad format
								System.out.println("Format mismatch. There are more than 1 Aircraft Carriers on the field.");
								br.close();
								System.exit(0);
							}
							ships[0].coordinates[i][j] = true;
							//A.coordinates[i][j] = true;
							//given one coordinate, we know that the rest should either be completely to the right
							//or completely to the downward direction (b/c we're reading left to right, top to bottom)
							//the following lines make sure that this is followed
							//similar check is performed on the different types of ships
							for (int a = 1; a <= 4; a++) {
								//this loops runs from 1 to 4 because aircraft carrier is 5-tiles long
								//the first one was already accounted for above
								if (j+a < 10 && field[i].charAt(j+a) == 'A' && !visited[i][j+a]) {
									//check to the right of the first 'A'
									ships[0].coordinates[i][j+a] = true;
									visited[i][j+a] = true;
								} else if (i+a < 10 && field[i+a].charAt(j) == 'A' && !visited[i+a][j]) {
									//check below the first 'A'
									ships[0].coordinates[i+a][j] = true;
									visited[i+a][j] = true;
								} else {
									//bad format
									System.out.println("Format mismatch. Double-check the placement of Aircraft Carriers.");
									br.close();
									System.exit(0);
								}
							}
						}
						if (field[i].charAt(j) == 'B') {
							//same ideas as the Aircraft check
							numB++; //number of battleships on the battlefield increases by 1
							if (numB > 1) {
								//more than 1 battleships, must be an error!
								//bad format
								System.out.println("Format mismatch. There are more than 1 Battleships on the field.");
								br.close();
								System.exit(0);
							}
							ships[1].coordinates[i][j] = true;
							for (int a = 1; a <= 3; a++) {
								if (j+a < 10 && field[i].charAt(j+a) == 'B' && !visited[i][j+a]) {
									ships[1].coordinates[i][j+a] = true;
									visited[i][j+a] = true;
								} else if (i+a < 10 && field[i+a].charAt(j) == 'B' && !visited[i+a][j]) {
									ships[1].coordinates[i+a][j] = true;
									visited[i+a][j] = true;
								} else {
									//bad format
									System.out.println("Format mismatch. Double-check the placement of Battleships.");
									br.close();
									System.exit(0);
								}
							}
						}
						if (field[i].charAt(j) == 'C') {
							//same ideas as the Aircraft check
							numC++; //number of cruisers on the battlefield increases by 1
							if (numC > 1) {
								//more than 1 cruiser, must be an error!
								//bad format
								System.out.println("Format mismatch. There are more than 1 Cruisers on the field.");
								br.close();
								System.exit(0);
							}
							ships[2].coordinates[i][j] = true;
							for (int a = 1; a <= 2; a++) {
								if (j+a < 10 && field[i].charAt(j+a) == 'C' && !visited[i][j+a]) {
									ships[2].coordinates[i][j+a] = true;
									visited[i][j+a] = true;
								} else if (i+a < 10 && field[i+a].charAt(j) == 'C' && !visited[i+a][j]) {
									ships[2].coordinates[i+a][j] = true;
									visited[i+a][j] = true;
								} else {
									//bad format
									System.out.println("Format mismatch. Double-check the placement of Cruisers.");
									br.close();
									System.exit(0);
								}
							}
						}
						if (field[i].charAt(j) == 'D') {
							//same ideas as the Aircraft check, except the total number of Destroyers is 2
							numD++; //number of destroyers on the battlefield increases by 1
							if (numD > 2) {
								//more than 2 destroyers, must be an error!
								//bad format
								System.out.println("Format mismatch. There are more than 2 Destroyers on the field.");
								br.close();
								System.exit(0);
							}
							if (numD == 1) {
								//accounting for the first destroyer
								ships[3].coordinates[i][j] = true;
								for (int a = 1; a <= 1; a++) {
									if (j+a < 10 && field[i].charAt(j+a) == 'D' && !visited[i][j+a]) {
										ships[3].coordinates[i][j+a] = true;
										visited[i][j+a] = true;
									} else if (i+a < 10 && field[i+a].charAt(j) == 'D' && !visited[i+a][j]) {
										ships[3].coordinates[i+a][j] = true;
										visited[i+a][j] = true;
									} else {
										//bad format
										System.out.println("Format mismatch. Double-check the placement of Destroyers.");
										br.close();
										System.exit(0);
									}
								}
							}
							if (numD == 2) {
								//accounting for the second destroyer
								ships[4].coordinates[i][j] = true;
								for (int a = 1; a <= 1; a++) {
									if (j+a < 10 && field[i].charAt(j+a) == 'D' && !visited[i][j+a]) {
										ships[4].coordinates[i][j+a] = true;
										visited[i][j+a] = true;
									} else if (i+a < 10 && field[i+a].charAt(j) == 'D' && !visited[i+a][j]) {
										ships[4].coordinates[i+a][j] = true;
										visited[i+a][j] = true;
									} else {
										//bad format
										System.out.println("Format mismatch. Double-check the placement of Destroyers.");
										br.close();
										System.exit(0);
									}
								}
							}
						}
					}
				}
			}
			br.close();
			fr.close();
		} catch (FileNotFoundException fnfe) {
			System.out.println("FileNotFoundException: " + fnfe.getMessage());
			System.out.println("The file was not found. Try again.");
			System.exit(0);
		} catch (IOException ioe) {
			System.out.println("IOException: " + ioe.getMessage());
			System.out.println("The file was not found. Try again.");
			System.exit(0);
		}
	} //INPUT END
	
	public static void cleanup(GUI gui) {
		//responsible for all the cleanup operations after the game is over
		//resets everything back to initial state
		gui.gameOver = false;
		gui.timerLabel.setText("Time - 0:15");
		
		gui.statusLabel.setText("Log: You are in edit mode, click to place your ships.");
		gui.fileLabel.setText("File: ");
		gui.fileButton.setVisible(true);
		gui.fileLabel.setVisible(true);
		gui.startButton.setVisible(true);
		gui.startButton.setEnabled(false);
		gui.shipsPlaced = false;
		gui.fileSelected = false;
		gui.started = false;
		gui.selectedFile = null;
		
		gui.player1Moved = false;
		gui.player2Moved = false;
		gui.timerThread.needToRestartRandom = true;
		
		gui.roundNumber = 2; //restart round #
		gui.seconds = 15; //restart timer seconds
		gui.gbPanel.remove(gui.gbPanel.getComponent(2)); //GUI reset happens here
		gui.gbPanel.add(gui.getBottomInfoPanel(), BorderLayout.SOUTH);
		gui.setSize(875, 535);
		
		gui.textArea.setText(gui.textArea.getText() + "\nNew game!\nRound 1");
		for (int i = 11; i < 121; i++) {
			if (i%11 == 0) {

			} else {
				//everything else filled with ?
				JLabelWithBackground label = (JLabelWithBackground) gui.playerLabels[i];
				label.changeForeground(gui.images[5]);
				label = (JLabelWithBackground) gui.computerLabels[i];
				label.changeForeground(gui.images[5]);
			}
		}
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				guessed[x][y] = false;
				computerGuessed[x][y] = false;
			}
		}
		for (int i = 0; i < ships.length; i++) {
			ships[i].placed = false;
			playerShips[i].placed = false;
			ships[i].numberOfHits = 0;
			playerShips[i].numberOfHits = 0;
			ships[i].sunken = false;
			playerShips[i].sunken = false;
			
			for (int x = 0; x < 10; x++) {
				for (int y = 0; y < 10; y++) {
					ships[i].coordinates[x][y] = false;
					playerShips[i].coordinates[x][y] = false;
				}
			}
		}
	}
	
	public static String checkCoordinates(int x, int y, GUI gui) {
		//used for gameplay
		//function runs through coordinates of all ships, checking if there is a match
		//returns true if we need to increase turn count, returns false if we don't need to
		if (guessed[x][y] == true) {
			//this has been guessed already!
			return "GUESSED";
		}
		guessed[x][y] = true;
		for (int i = 0; i < ships.length; i++) {
			if (ships[i].coordinates[x][y] == true) {
				//ship i has been hit
				//count i's health + change the GUI
				ships[i].numberOfHits++;
				JLabelWithBackground label = (JLabelWithBackground) gui.computerLabels[11*x + y + 12];
				gui.executor.submit(new ExplosionThread(gui, label, ships[i], true));
				if(ships[i].numberOfHits == ships[i].health) {
					//sinking condition
					ships[i].sunken = true;
					gui.executor.submit(new SplashThread(gui, ships[i], true));
				}
				lastHitShip = ships[i];
				return "HIT";
			}
		}
		//at this point none of the ships' coordinates match the entered coordinates
		//therefore, it is a miss!
		JLabelWithBackground label = (JLabelWithBackground) gui.computerLabels[11*x+y+12];
		gui.executor.submit(new SplashThread(gui, label));
		//label.changeForeground(gui.images[4]);
		//gui.computerLabels[11*x + y + 12].setHorizontalAlignment(JLabel.CENTER);
		
		return "MISS";
	}
	
	public static String checkEnemyCoordinates(int x, int y, GUI gui) {
		//used for gameplay
		//function runs through coordinates of all ships, checking if there is a match
		//returns true if we need to increase turn count, returns false if we don't need to
		if (computerGuessed[x][y] == true) {
			//this has been guessed already!
			return "GUESSED";
		}
		computerGuessed[x][y] = true;
		for (int i = 0; i < playerShips.length; i++) {
			if (playerShips[i].coordinates[x][y] == true) {
				//ship i has been hit
				//count i's health + change the GUI
				playerShips[i].numberOfHits++;
				JLabelWithBackground label = (JLabelWithBackground) gui.playerLabels[11*x+y+12];
				gui.executor.submit(new ExplosionThread(gui, label, playerShips[i], false));
				if(playerShips[i].numberOfHits == playerShips[i].health) {
					//sinking condition
					playerShips[i].sunken = true;
					gui.executor.submit(new SplashThread(gui, playerShips[i], false));
				}
				lastHitShip = playerShips[i];
				return "HIT";
			}
		}
		//at this point none of the ships' coordinates match the entered coordinates
		//therefore, it is a miss!
		JLabelWithBackground label = (JLabelWithBackground) gui.playerLabels[11*x+y+12];
		gui.executor.submit(new SplashThread(gui, label));
		//label.changeForeground(gui.images[4]);
		//gui.playerLabels[11*x + y + 12].setHorizontalAlignment(JLabel.CENTER);
		return "MISS";
	}
	
	public static boolean checkVictory() {
		//returns true if all computer ships have been sunk
		int count = 0;
		for (int i = 0; i < ships.length; i++) {
			if (ships[i].sunken) {
				count++;
			}
		}
		
		if (count == ships.length) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean checkEnemyVictory() {
		//returns true if all player ships have been sunk
		int count = 0;
		for (int i = 0; i < playerShips.length; i++) {
			if (playerShips[i].sunken) {
				count++;
			}
		}
		
		if (count == playerShips.length) {
			return true;
		} else {
			return false;
		}
	}

	public static void main (String [] args) {
		//---initialize ships-----
		ships[0] = new AircraftCarrier();
		ships[1] = new Battleship();
		ships[2] = new Cruiser();
		ships[3] = new Destroyer();
		ships[4] = new Destroyer();
		//------------------------
		playerShips[0] = new AircraftCarrier();
		playerShips[1] = new Battleship();
		playerShips[2] = new Cruiser();
		playerShips[3] = new Destroyer();
		playerShips[4] = new Destroyer();
		//-------------------------
		
		GUI gui = new GUI(players, ships, playerShips);
	}
}