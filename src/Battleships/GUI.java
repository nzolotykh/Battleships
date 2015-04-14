package zolotykh_CSCI201_Assignment4;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Random;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;

public class GUI extends JFrame {
	//main GUI class
	private static final long serialVersionUID = 1;
	public JLabel [] playerLabels = new JLabel[121]; //field squares, including the coordinate axes A-J and 1-10
	public JLabel [] computerLabels = new JLabel[121]; //field squares, including the coordinate axes A-J and 1-10
	JPanel gbPanel; //main GUI panel
	File selectedFile; //which file was selected in the file chooser
	JLabel statusLabel; //self-explanatory
	JButton fileButton;
	JLabel fileLabel;
	JButton startButton;
	String yaxis = "ABCDEFGHIJ"; //used for reference to make cell names
	Ship [] ships; //computer ships
	Ship [] playerShips; //player ships
	boolean fileSelected = false; //various booleans to check different statuses inside the game
	boolean shipsPlaced = false;
	boolean started = false;
	boolean player1Moved = false;
	boolean player2Moved = false;
	boolean gameOver = false;
	boolean roundOver = false;
	static BufferedImage [] images; //array of loaded images used throughout the game
	JTextArea textArea; //log text area
	JLabel timerLabel; //timer count down label
	int roundNumber = 2; //self-explanatory
	BufferedImage background;	
	public static BufferedImage labelBackground1; //two background images, pre-loaded
	public static BufferedImage labelBackground2;
	BufferedImage [] explosion; //explosion animation
	BufferedImage [] splash; //splash animation
	
	TimerThread timerThread; //thread for the timer
	ExecutorService executor; //pool executor for all threads

	public int seconds = 15; //used for the count down timer
	
	Sound [] cannonSound = new Sound[2]; //sounds, pre-loaded
	Sound [] splashSound = new Sound[2];
	Sound [] explodeSound = new Sound[2];
	Sound [] sinkingSound = new Sound[2];
	


	public GUI(ArrayList<String> players, Ship [] ships, Ship [] playerShips) {
		//main window
		super("Battleships Game v. 1.2");
		setSize(875, 535);
		setLocation(100, 50);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getRootPane().setBorder(new EmptyBorder(0, 0, 5, 0)); //make it look pretty, yeah!
		
		images = new BufferedImage[7];
		explosion = new BufferedImage[5];
		splash = new BufferedImage[7];

		//-----------------LOADING IMAGES + SOUNDS-----------------------------
		try {
			labelBackground1 = ImageIO.read(new File("animatedWater/water1.png"));
			labelBackground2 = ImageIO.read(new File("animatedWater/water2.png"));
			
			images[0] = ImageIO.read(new File("Tiles/A.png"));
			images[1] = ImageIO.read(new File("Tiles/B.png"));
			images[2] = ImageIO.read(new File("Tiles/C.png"));
			images[3] = ImageIO.read(new File("Tiles/D.png"));
			images[4] = ImageIO.read(new File("Tiles/M.png"));
			images[5] = ImageIO.read(new File("Tiles/Q.png"));
			images[6] = ImageIO.read(new File("Tiles/X.png"));
			
			explosion[0] = ImageIO.read(new File("explosion/expl1.png"));
			explosion[1] = ImageIO.read(new File("explosion/expl2.png"));
			explosion[2] = ImageIO.read(new File("explosion/expl3.png"));
			explosion[3] = ImageIO.read(new File("explosion/expl4.png"));
			explosion[4] = ImageIO.read(new File("explosion/expl5.png"));
			
			splash[0] = ImageIO.read(new File("splash/splash1.png"));
			splash[1] = ImageIO.read(new File("splash/splash2.png"));
			splash[2] = ImageIO.read(new File("splash/splash3.png"));
			splash[3] = ImageIO.read(new File("splash/splash4.png"));
			splash[4] = ImageIO.read(new File("splash/splash5.png"));
			splash[5] = ImageIO.read(new File("splash/splash6.png"));
			splash[6] = ImageIO.read(new File("splash/splash7.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		background = labelBackground1;
		
		cannonSound[0] = new Sound("Sounds/cannon.wav");
		cannonSound[1] = new Sound("Sounds/cannon.wav");
		splashSound[0] = new Sound("Sounds/splash.wav");
		splashSound[1] = new Sound("Sounds/splash.wav");
		explodeSound[0] = new Sound("Sounds/explode.wav");
		explodeSound[1] = new Sound("Sounds/explode.wav");
		sinkingSound[0] = new Sound("Sounds/sinking.wav");
		sinkingSound[1] = new Sound("Sounds/sinking.wav");
		//---------------------LOAD COMPLETE-------------------------------------
		
		gbPanel = getGBPanel(players);
		add(gbPanel);
		getTheMenuBar();
		
		setResizable(false); //cannot resize the main window
		setVisible(true);
		WaterThread waterThread = new WaterThread(GUI.this);

		executor = Executors.newCachedThreadPool();
		executor.submit(waterThread);
		
		this.ships = ships;
		this.playerShips = playerShips;		
	}
		
	private JPanel getGBPanel(ArrayList<String> players) {
		//main window getter
		JPanel jp = new JPanel();
		jp.setLayout(new BorderLayout());
		
		JPanel titlesPanel = new JPanel();
		titlesPanel.setLayout(new FlowLayout());
		
		//----FIELD LABELS--------------
		JLabel playerLabel = new JLabel("PLAYER");
		JLabel computerLabel = new JLabel("COMPUTER");
		timerLabel = new JLabel("Time - 0:15");
		playerLabel.setBorder(new EmptyBorder(0, 20, 0, 150));
		computerLabel.setBorder(new EmptyBorder(0, 150, 0, 0));
		
		titlesPanel.add(playerLabel);
		titlesPanel.add(timerLabel);
		titlesPanel.add(computerLabel);
		//-------------------------------
		
		//-----FIELDS GENERATION----------
		//-----Player Side----------------
		JPanel fieldsPanel = new JPanel();
		fieldsPanel.setLayout(new FlowLayout());
		
		JPanel playerPanel = new JPanel();
		playerPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		playerPanel.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.black), new EmptyBorder(10, 10, 20, 20)));
		playerPanel.setBackground(new Color(15, 150, 225));
		
		playerLabels[0] = new JLabel(" "); //top left corner
		for (int i = 1; i < 11; i++) {
			//top axis with numbers
			playerLabels[i] = new JLabel("" + i);
			playerLabels[i].setBorder(new EmptyBorder(0, 10, 2, 0));
			playerLabels[i].setPreferredSize(new Dimension(35,35));
		}
		for (int i = 11; i < 121; i++) {
			if (i%11 == 0) {
				//vertical axis with letters
				playerLabels[i] = new JLabel("" + yaxis.charAt(i/11-1));
				playerLabels[i].setBorder(new EmptyBorder(0, 10, 0, 3));
			} else {
				//everything else filled with ?
				playerLabels[i] = new JLabelWithBackground(background, images[5]);
				//playerLabels[i].setIcon(icons[5]);
				playerLabels[i].setHorizontalAlignment(JLabel.CENTER);
				playerLabels[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
				playerLabels[i].addMouseListener(new clickListener(i, this));
			}
			playerLabels[i].setPreferredSize(new Dimension(35,35)); //make each tile square
		}
		//the following actually adds the labels to the panel
		for (int y = 0; y < 11; y++) {
			//running from top to bottom
			gbc.gridy = y;
			for (int x = 0; x < 11; x++) {
				//running from left to right
				gbc.gridx = x;
				playerPanel.add(playerLabels[x+11*y], gbc);
			}
		}
		//------Player Side done------------------
		
		//------Computer Side--------------------		
		JPanel computerPanel = new JPanel();
		computerPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc2 = new GridBagConstraints();
		computerPanel.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.black), new EmptyBorder(10, 10, 20, 20)));
		computerPanel.setBackground(new Color(15, 150, 225));
		
		computerLabels[0] = new JLabel(" "); //top left corner
		for (int i = 1; i < 11; i++) {
			//top axis with numbers
			computerLabels[i] = new JLabel("" + i);
			computerLabels[i].setBorder(new EmptyBorder(0, 10, 2, 0));
			computerLabels[i].setPreferredSize(new Dimension(35,35));
		}
		for (int i = 11; i < 121; i++) {
			if (i%11 == 0) {
				//vertical axis with letters
				computerLabels[i] = new JLabel("" + yaxis.charAt(i/11-1));
				computerLabels[i].setBorder(new EmptyBorder(0, 10, 0, 3));
			} else {
				//everything else filled with ?
				computerLabels[i] = new JLabelWithBackground(background, images[5]);
				//computerLabels[i].setIcon(icons[5]);
				computerLabels[i].setHorizontalAlignment(JLabel.CENTER);
				computerLabels[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
				computerLabels[i].addMouseListener(new enemyClickListener(i));
			}
			computerLabels[i].setPreferredSize(new Dimension(35,35)); //make each tile square
		}
		//the following actually adds the labels to the panel
		for (int y = 0; y < 11; y++) {
			//running from top to bottom
			gbc2.gridy = y;
			for (int x = 0; x < 11; x++) {
				//running from left to right
				gbc2.gridx = x;
				computerPanel.add(computerLabels[x+11*y], gbc2);
			}
		}
		//---Computer Side done---------------
		
		fieldsPanel.add(playerPanel);
		fieldsPanel.add(computerPanel);
		//-------------Fields complete----------
		
		jp.add(titlesPanel, BorderLayout.NORTH);
		jp.add(fieldsPanel, BorderLayout.CENTER);
		jp.add(getBottomInfoPanel(), BorderLayout.SOUTH);
		return jp;
	}
	
	protected JPanel getBottomInfoPanel() {
		//Bottom info panel, before the game starts
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
		
		statusLabel = new JLabel("Log: You are in edit mode, click to place your ships.");
		fileButton = new JButton("Select File...");
		fileLabel = new JLabel("File: ");
		startButton = new JButton("START");
		statusLabel.setBorder(new EmptyBorder(0, 15, 0, 100));
		fileButton.addActionListener(new openFileAction());
		fileLabel.setBorder(new EmptyBorder(0, 5, 0, 100));
		startButton.setEnabled(false);
		startButton.addActionListener(new startGameAction(this, bottomPanel));
		
		bottomPanel.add(statusLabel);
		bottomPanel.add(fileButton);
		bottomPanel.add(fileLabel);
		bottomPanel.add(startButton);
		bottomPanel.setPreferredSize(bottomPanel.getPreferredSize());
		return bottomPanel;
	}
	
	protected JPanel getLogPanel() {
		//Bottom log panel, after the game starts
		JPanel jp = new JPanel();
		jp.setLayout(new BorderLayout());
		jp.setBorder(BorderFactory.createTitledBorder("Game Log"));
		
		textArea = new JTextArea();
		DefaultCaret caret = (DefaultCaret) textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		textArea.setText("Round 1");
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setBorder(new EmptyBorder(5, 20, 5, 20));
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		textArea.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		scrollPane.setPreferredSize(new Dimension(jp.getWidth(), 80));

		jp.add(scrollPane, BorderLayout.CENTER);
		
		return jp;
	}
	
	protected void fileChooserCalled() {
		//file chooser that is called when the Select File button is pressed
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
		FileNameExtensionFilter ff = new FileNameExtensionFilter("Battle Files", "battle");
		//fileChooser.addChoosableFileFilter(ff);
		fileChooser.setFileFilter(ff);
		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			//get the name of the file, read the input
		    selectedFile = fileChooser.getSelectedFile();
			fileLabel.setText("File: " + selectedFile.getName().substring(0, selectedFile.getName().length()-7));
			fileSelected = true;
			if (shipsPlaced) {
				startButton.setEnabled(true);
			}
			Battleships.Input(selectedFile.getName());
		}
	}
	
	class openFileAction implements ActionListener {
		//calls the file chooser
		public openFileAction() {
		}
		public void actionPerformed(ActionEvent e) {
			fileChooserCalled();
		}
	}
	
	class clickListener implements MouseListener {
		//click listener for when the user clicks the enemy field
		int i; //index of the label
		JFrame frame; //rest is self-explanatory
		JComboBox shipCombo;
		ButtonGroup radioGroup;
		JButton placeButton;
		clickListener (int i, JFrame frame) {
			this.i = i;
			this.frame = frame;
		}
		public void mouseClicked(MouseEvent arg0) {
			if (!started) {
				//if game not started, we can place on our own field
				//--------REMOVAL OF A SHIP----------------------------------------------
				for (int a = 0; a < playerShips.length; a++) {
					//check if there's already a ship in the clicked coordinates
					if (playerShips[a].coordinates[i/11-1][i%11-1]) {
						//there's a ship!
						//clear that baby out
						for (int x = 0; x < 10; x++) {
							for (int y = 0; y < 10; y++) {
								if (playerShips[a].coordinates[x][y]) {
									//replacing all the coordinates and putting the ? back
									playerShips[a].coordinates[x][y] = false;
									playerShips[a].placed = false;
									JLabelWithBackground label = (JLabelWithBackground)playerLabels[11*(x+1) + (y+1)];
									label.changeForeground(images[5]);
									playerLabels[11*(x+1) + (y+1)].setHorizontalAlignment(JLabel.CENTER);
								}
							}
						}
						shipsPlaced = false; //if a ship was removed, there is no way the game is ready to be played
						startButton.setEnabled(false);
						return;
					}
				}
				//------------------------------------------------------------------------
				
				int placedShipsCount = 0;
				int destroyersPlaced = 0;
				for (int i = 0; i < playerShips.length; i++) {
					//count how many ships have been placed, so we could create the list of objects
					if (playerShips[i].placed) {
						placedShipsCount++;
					}
				}
				if(playerShips[3].placed) //special destroyer counter for "accounting" purposes
					destroyersPlaced++;
				if(playerShips[4].placed)
					destroyersPlaced++;
				

				if (placedShipsCount < 5) {
					if (destroyersPlaced == 0) {
						//"accounting"
						placedShipsCount++;
					}
					
					Object [] values = new Object[5-placedShipsCount]; //making objects for the combo box
					placedShipsCount = 0;
					for (int i = 0; i < playerShips.length; i++) {
						//prep for the combo box
						if (!playerShips[i].placed) {
							//ship is still not placed, so we can add it to the list
							if (!Arrays.asList(values).contains(playerShips[i].type)) {
								//reading in the values ... as long as they are not already there (e.g., destroyer!)
								values[placedShipsCount] = playerShips[i].type;
								placedShipsCount++;
							}
						}
					}
					
					JPanel jp = new JPanel();
					jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));
					
					//do some math to figure out the cell name based on the label index
					String cellName = Character.toString(yaxis.charAt(i/11-1)) + (i%11);

					final JDialog dialog = new JDialog(frame, "Select ship at " + cellName);
					
					//Place ship button
					JPanel placeButtonPanel = new JPanel();
					placeButton = new JButton("Place Ship");
					placeButton.setEnabled(false);
					placeButtonPanel.add(placeButton);
					//-----------------
					
					//------------POP-UP PLACE SHIP WINDOW--------------------------------
					JPanel shipPanel = new JPanel();
					JLabel shipLabel = new JLabel("Select Ship: ");
					shipCombo = new JComboBox(values);
					shipPanel.add(shipLabel);
					shipPanel.add(shipCombo);
					
					JPanel northsouthPanel = new JPanel();
					JRadioButton northRadio = new JRadioButton("North");
					JRadioButton southRadio = new JRadioButton("South");
					northRadio.addItemListener(new radioListener(i));
					southRadio.addItemListener(new radioListener(i));

					northsouthPanel.add(northRadio);
					northsouthPanel.add(southRadio);
					
					JPanel eastwestPanel = new JPanel();
					JRadioButton eastRadio = new JRadioButton("East");
					JRadioButton westRadio = new JRadioButton("West");
					eastRadio.addItemListener(new radioListener(i));
					westRadio.addItemListener(new radioListener(i));
					
					eastwestPanel.add(eastRadio);
					eastwestPanel.add(westRadio);
					
					radioGroup = new ButtonGroup();
					radioGroup.add(northRadio);
					radioGroup.add(southRadio);
					radioGroup.add(eastRadio);
					radioGroup.add(westRadio);
					
					shipCombo.addItemListener(new comboListener(i));
					placeButton.addActionListener(new placeShipAction(dialog, i));
					
					jp.add(shipPanel);
					jp.add(northsouthPanel);
					jp.add(eastwestPanel);
					jp.add(placeButtonPanel);
					
					dialog.add(jp);
					dialog.pack();
					dialog.setLocation(300, 300);
					dialog.setResizable(false);
					dialog.setModal(true);
					dialog.setVisible(true);
					//-----------------------------------------------------------------

				} else {
					
				}
			}
		}
		public void mouseEntered(MouseEvent arg0) {
		}
		public void mouseExited(MouseEvent arg0) {
		}
		public void mousePressed(MouseEvent e) {
		}
		public void mouseReleased(MouseEvent e) {			
		}
		class radioListener implements ItemListener {
			//used inside the pop-up Place Ship window
			//radio buttons listener ... checks placement of ships
			//and turns on/off the place ship button
			String shipType;
			int cell;
			radioListener(int cell) {
				this.shipType = shipCombo.getSelectedItem().toString();
				this.cell = cell;
			}
			public void itemStateChanged(ItemEvent e) {
				AbstractButton aButton = (AbstractButton)e.getSource();
				int state = e.getStateChange();
				if (state == ItemEvent.SELECTED) {
					//button is selected
					checkPlacement(aButton.getText(), cell);
				}
			}
		}
		class comboListener implements ItemListener {
			int cell;
			JButton placeButton;
			String direction;
			comboListener(int cell) {
				this.cell = cell;
				this.direction = "";
			}
			public void itemStateChanged(ItemEvent e) {
				for (Enumeration<AbstractButton> buttons = radioGroup.getElements(); buttons.hasMoreElements();) {
					AbstractButton button = buttons.nextElement();
					if (button.isSelected()) {
						direction = button.getText();
					}
				}
				int state = e.getStateChange();
				if (state == ItemEvent.SELECTED) {
					//an option is selected
					if (direction.equals("")) {
						//no direction given yet
						return;
					}
					String shipType = shipCombo.getSelectedItem().toString();
					checkPlacement(direction, cell);
				}
			}
		}
		protected void checkPlacement(String direction, int cell) {
			//checks if the ship can be placed on the board
			int shipLength = 0;
			for (int i = 0; i < playerShips.length; i++) {
				if (playerShips[i].type.equals(shipCombo.getSelectedItem().toString())) {
					//find which ship it is, assign length
					shipLength = playerShips[i].health;
				}
			}
			if (direction.equals("West")) {
				//pointing west
				if ((cell - shipLength) >= (cell/11 * 11)) {
					//fits on the left
					for (int i = 0; i < playerShips.length; i++) {
						//run through other ships
						for (int j = 0; j < shipLength; j++) {
							if ((cell%11-j-1) < 0 ) {
								continue;
							} else {
								if (playerShips[i].coordinates[cell/11-1][cell%11-j-1]) {
									placeButton.setEnabled(false);
									return;
								}
							}
						}
					}
					//at this point, we've done all the checks, the button can be enabled!
					placeButton.setEnabled(true);
				} else {
					//doesn't fit on the left
					placeButton.setEnabled(false);
					return;
				}
			}
			if (direction.equals("East")) {
				//pointing east
				if ((cell + shipLength) <= ((cell/11+1) * 11)) {
					//fits on the right
					for (int i = 0; i < playerShips.length; i++) {
						//run through other ships
						for (int j = 0; j < shipLength; j++) {
							if ((cell%11+j-1) > 9) {
								continue;
							} else {
								if (playerShips[i].coordinates[cell/11-1][cell%11+j-1]) {
									placeButton.setEnabled(false);
									return;
								}
							}
						}
					}
					//at this point, we've done all the checks, the button can be enabled!
					placeButton.setEnabled(true);
				} else {
					//doesn't fit on the right
					placeButton.setEnabled(false);
				}
			}
			if (direction.equals("North")) {
				//pointing west
				if ((cell - 11*(shipLength-1)) >= (cell%11+11)) {
					//fits on the top
					for (int i = 0; i < playerShips.length; i++) {
						//run through other ships
						for (int j = 0; j < shipLength; j++) {
							if ((cell/11-j-1) < 0) {
								continue;
							} else {
								if (playerShips[i].coordinates[cell/11-j-1][cell%11-1]) {
									placeButton.setEnabled(false);
									return;
								}
							}
						}
					}
					//at this point, we've done all the checks, the button can be enabled!
					placeButton.setEnabled(true);
				} else {
					//doesn't fit on the top
					placeButton.setEnabled(false);
				}
			}
			if (direction.equals("South")) {
				//pointing west
				if ((cell + 11*(shipLength-1)) <= (110+cell%11)) {
					//fits on the bottom
					for (int i = 0; i < playerShips.length; i++) {
						//run through other ships
						for (int j = 0; j < shipLength; j++) {
							if ((cell/11+j-1) > 9) {
								//the coordinates are out of bounds, so no ship can be there, we can skip
								continue;
							} else {
								if (playerShips[i].coordinates[cell/11+j-1][cell%11-1]) {
									placeButton.setEnabled(false);
									return;
								}
							}
						}
					}
					//at this point, we've done all the checks, the button can be enabled!
					placeButton.setEnabled(true);
				} else {
					//doesn't fit on the bottom
					placeButton.setEnabled(false);
				}
			}
		}

		class placeShipAction implements ActionListener {
			//reads in the input from the Place Ship pop-up window
			//and actually places the ship on the field
			JDialog dialog;
			int cell;
			String shipType;
			
			public placeShipAction(JDialog dialog, int cell) {
				this.dialog = dialog;
				this.cell = cell;
			}
			
			public void actionPerformed(ActionEvent e) {
				String direction = "";
				shipType = shipCombo.getSelectedItem().toString();
				for (Enumeration<AbstractButton> buttons = radioGroup.getElements(); buttons.hasMoreElements();) {
					AbstractButton button = buttons.nextElement();
					if (button.isSelected()) {
						direction = button.getText();
					}
				}
				
				for (int i = 0; i < playerShips.length; i++) {
					//running through the ships
					if (playerShips[i].type.equals(shipType) && !playerShips[i].placed) {
						//same type AND not placed yet
						if (direction.equals("West")) {
							for (int j = 0; j < playerShips[i].health; j++) {
								playerShips[i].coordinates[cell/11-1][cell%11-j-1] = true;
								JLabelWithBackground label = (JLabelWithBackground) playerLabels[cell-j];
								label.changeForeground(images[5-playerShips[i].health]);
								//playerLabels[cell-j].setIcon(icons[5-playerShips[i].health]);
								playerLabels[cell-j].setHorizontalAlignment(JLabel.CENTER);
							}
						}
						if (direction.equals("East")) {
							for (int j = 0; j < playerShips[i].health; j++) {
								playerShips[i].coordinates[cell/11-1][cell%11+j-1] = true;
								JLabelWithBackground label = (JLabelWithBackground) playerLabels[cell+j];
								label.changeForeground(images[5-playerShips[i].health]);
								//playerLabels[cell+j].setIcon(icons[5-playerShips[i].health]);
								playerLabels[cell+j].setHorizontalAlignment(JLabel.CENTER);
							}
						}
						if (direction.equals("North")) {
							for (int j = 0; j < playerShips[i].health; j++) {
								playerShips[i].coordinates[cell/11-j-1][cell%11-1] = true;
								JLabelWithBackground label = (JLabelWithBackground) playerLabels[cell-11*j];
								label.changeForeground(images[5-playerShips[i].health]);
								//playerLabels[cell - 11*j].setIcon(icons[5-playerShips[i].health]);
								playerLabels[cell - 11*j].setHorizontalAlignment(JLabel.CENTER);
							}
						}
						if (direction.equals("South")) {
							for (int j = 0; j < playerShips[i].health; j++) {
								playerShips[i].coordinates[cell/11+j-1][cell%11-1] = true;
								JLabelWithBackground label = (JLabelWithBackground) playerLabels[cell+11*j];
								label.changeForeground(images[5-playerShips[i].health]);
								//playerLabels[cell + 11*j].setIcon(icons[5-playerShips[i].health]);
								playerLabels[cell + 11*j].setHorizontalAlignment(JLabel.CENTER);
							}
						}
						playerShips[i].placed = true;
						dialog.setVisible(false);
						dialog.dispose();
						int placedShipsCount = 0;
						for (int c = 0; c < playerShips.length; c++) {
							if (playerShips[c].placed)
								placedShipsCount++;
						}
						if (placedShipsCount == playerShips.length) {
							shipsPlaced = true;
							if (fileSelected) {
								startButton.setEnabled(true);
							}
						} else {
							shipsPlaced = false;
							startButton.setEnabled(false);
						}
						return; //placed ship, finish function
					}
				}
			}
		}
	
	}
	
	class startGameAction implements ActionListener {
		//self-explanatory, happens when the Start button is clicked
		//changes the GUI and sets off the game timer
		GUI gui;
		JPanel bottomPanel;
		
		startGameAction(GUI gui, JPanel bottomPanel) {
			this.gui = gui;
			this.bottomPanel = bottomPanel;
		}

		public void actionPerformed(ActionEvent e) {
			//START OF THE GAME!
			started = true;
			gui.gbPanel.remove(gui.gbPanel.getComponent(2)); //changing the GUI
			gui.gbPanel.add(getLogPanel(), BorderLayout.SOUTH);
			gui.setSize(875, 615);
			timerThread = new TimerThread(GUI.this); //starting the timer
			executor.submit(timerThread);
		}
	}
	
	class enemyClickListener implements MouseListener {
		//used for clicking on the enemy field
		int i; //label number
		String cellClicked;
		enemyClickListener(int i) {
			this.i = i;
			cellClicked = Character.toString(yaxis.charAt(i/11-1)) + (i%11); //converting the label number to a proper cell name using a formula
		}
		
		public void mouseClicked(MouseEvent e) {
			if (started && !player1Moved) {
				//game is started and player hasn't moved
				//so have to check for if the ship was hit
				String result = Battleships.checkCoordinates(i/11-1, i%11-1, GUI.this);
				if (result.equals("HIT")) {
					//a ship has been hit, check for victory!
					player1Moved = true;
					textArea.setText(textArea.getText() + "\nPlayer hit " + cellClicked + 
							" and hit " + (String) Battleships.lastHitShip.type + "! (" + getTime() + ")");
					
					if (Battleships.lastHitShip.sunken) {
						//just sank a ship!!
						textArea.setText(textArea.getText() + "\nPlayer sunk Computer's " + (String) Battleships.lastHitShip.type + "!");
					}
					if (Battleships.checkVictory()) {
						//VICTORY!
						gameOver = true;
						textArea.setText(textArea.getText() + "\nPlayer has won!");
						executor.submit(new GameOverThread(GUI.this)); //starting the game over thread
						return;
					}
				} else if (result.equals("MISS")) {
					//player missed!
					player1Moved = true;
					textArea.setText(textArea.getText() + "\nPlayer hit " + cellClicked + 
							" and missed! (" + getTime() + ")");
				} else {
					return;
				}
				
				if (player1Moved && player2Moved) {
					//player just shot, check if computer has already shot
					//if so, start the next round
					player1Moved = false;
					player2Moved = false;
					timerLabel.setText("Time - 0:15");
					seconds = 15;
					timerThread.needToRestartRandom = true;
					textArea.setText(textArea.getText() + "\nRound " + roundNumber);
					roundNumber++;
				}
			}
		}
		public void mouseEntered(MouseEvent e) {
		}
		public void mouseExited(MouseEvent e) {
		}
		public void mousePressed(MouseEvent e) {
		}
		public void mouseReleased(MouseEvent e) {
		}
	}

	private void getTheMenuBar() {
		//the top menu bar of the GUI
		JMenuBar menuBar = new JMenuBar();
		JMenu infoMenu = new JMenu("Info");
		infoMenu.setMnemonic('I');
		menuBar.add(infoMenu);
		
		JMenuItem howItem = new JMenuItem("How To");
		howItem.setMnemonic('H');
		howItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
		howItem.addActionListener(new howToAction());
		JMenuItem aboutItem = new JMenuItem("About");
		aboutItem.setMnemonic('A');
		aboutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		aboutItem.addActionListener(new aboutAction());
		
		infoMenu.add(howItem);
		infoMenu.add(aboutItem);
		setJMenuBar(menuBar);
	}
	
	class howToAction implements ActionListener {
		//How To Play pop-up from the menu
		JFrame howToFrame;
		howToAction() {
			howToFrame = new JFrame("Battleship Instructions");
			howToFrame.setSize(400, 200);
			howToFrame.setLocation(200, 250);
			howToFrame.setResizable(false);
			howToFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			JPanel jp = new JPanel(new BorderLayout());
			JTextPane textPane = new JTextPane();
			textPane.setEditable(false);
			String newText = "";
			try {
				FileReader fr = new FileReader("about.txt");
				BufferedReader br = new BufferedReader(fr);
				String line = br.readLine();
				while (line != null) {
					newText = newText + line + "\n";
					line = br.readLine();
				}
				textPane.setText(newText);
			}  catch (FileNotFoundException fnfe) {
				System.out.println("FileNotFoundException: " + fnfe.getMessage());
				System.out.println("The file was not found. Try again.");
				System.exit(0);
			} catch (IOException ioe) {
				System.out.println("IOException: " + ioe.getMessage());
				System.out.println("The file was not found. Try again.");
				System.exit(0);
			}
			JScrollPane scrollPane = new JScrollPane(textPane);
			jp.add(scrollPane, BorderLayout.CENTER);
			howToFrame.add(jp);
		}
		public void actionPerformed(ActionEvent e) {
			howToFrame.setVisible(true);
		}
	}
	
	class aboutAction implements ActionListener {
		//About pop-up from the menu
		JFrame aboutFrame;
		aboutAction() {
			aboutFrame = new JFrame("About");
			aboutFrame.setSize(400, 250);
			aboutFrame.setLocation(200, 250);
			aboutFrame.setResizable(false);
			aboutFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			JPanel jp = new JPanel();
			jp.setLayout(new BorderLayout());
			JPanel madePanel = new JPanel(new GridBagLayout());
			JLabel madeLabel = new JLabel("Made by Nikita Zolotykh");
			madePanel.add(madeLabel);
			JPanel imagePanel = new JPanel(new GridBagLayout());
			ImageIcon imageIcon = new ImageIcon("photo.jpg");
			JLabel imageLabel = new JLabel();
			imageLabel.setIcon(imageIcon);
			imagePanel.add(imageLabel);
			JPanel infoPanel = new JPanel(new GridBagLayout());
			JLabel infoLabel = new JLabel("March 1, 2015 CSCI 201 Assignment #3");
			infoPanel.add(infoLabel);
			jp.add(madePanel, BorderLayout.NORTH);
			jp.add(imagePanel, BorderLayout.CENTER);
			jp.add(infoPanel, BorderLayout.SOUTH);
			aboutFrame.add(jp);
		}
		public void actionPerformed(ActionEvent e) {
			aboutFrame.setVisible(true);
		}
	}

	public String getTime() {
		//convenient formatting for printing out the current timer time
		if (seconds >= 10) {
			return new String("0:" + (seconds));
		} else
		if (seconds < 10) {
			return new String("0:0" + (seconds));
		}
		return new String ("impossible time"); //technically this should never happen, used to please Java
	}	
}
