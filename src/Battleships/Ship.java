package zolotykh_CSCI201_Assignment4;

public abstract class Ship {
	//general ship class
	boolean [][] coordinates = new boolean [10][10]; //coordinates of where the ship is. True if the ship occupies the coordinate
	String type; //name of the ship type
	int numberOfHits = 0; //number of hits the ship has experienced. If equal the ship's length, should be sunken
	int health;
	boolean sunken = false; //alive/dead status
	boolean placed = false; //placed on the board?
}

class AircraftCarrier extends Ship {
	AircraftCarrier() {
		type = "Aircraft Carrier"; //ship name
		health = 5;
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				coordinates[i][j] = false; //initialize coordinates to false
			}
		}
	}
}

class Battleship extends Ship {
	Battleship() {
		type = "Battleship";
		health = 4;
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				coordinates[i][j] = false;
			}
		}
	}
}

class Cruiser extends Ship {
	Cruiser() {
		type = "Cruiser";
		health = 3;
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				coordinates[i][j] = false;
			}
		}
	}
}

class Destroyer extends Ship {
	Destroyer() {
		type = "Destroyer";
		health = 2;
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				coordinates[i][j] = false;
			}
		}
	}
}