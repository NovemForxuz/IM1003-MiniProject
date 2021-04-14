import java.awt.*; // Uses AWT's Layout Managers
import java.awt.event.*; // Uses AWT's Event Handlers
import javax.swing.*; // Uses Swing's Container/Components

import java.util.Calendar;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.border.BevelBorder;

public class Sudoku extends JFrame {
	// Name-constants for the game properties
	public static final int GRID_SIZE = 9; // Size of the board
	public static final int SUBGRID_SIZE = 3; // Size of the sub-grid
	
	// Name-constants for UI control (sizes, colors and fonts)
	public static final int CELL_SIZE = 60; // Cell width/height in pixels
	public static final int CANVAS_WIDTH = CELL_SIZE * GRID_SIZE;
	public static final int CANVAS_HEIGHT = CELL_SIZE * GRID_SIZE;
	// Board width/height in pixels
	public static final Color OPEN_CELL_BGCOLOR = Color.YELLOW;
	public static final Color OPEN_CELL_TEXT_YES = new Color(0, 255, 0); // RGB
	public static final Color OPEN_CELL_TEXT_NO = Color.RED;
	public static final Color CLOSED_CELL_BGCOLOR = new Color(240, 240, 240); // RGB
	public static final Color CLOSED_CELL_TEXT = Color.BLACK;
	public static final Font FONT_NUMBERS = new Font("Monospaced", Font.BOLD, 20);

	public static Color color1b = new Color(132, 179, 255);
	public static Color color2b = new Color(51, 255, 255);
	public static Color color1p = new Color(102, 204, 0);
	public static Color color2p = new Color(204, 255, 153);
	public static Color color1pl = new Color(160, 160, 160);
	public static Color color2pl = new Color(224, 224, 224);
	public static Color color1pp = new Color(255, 0, 127);
	public static Color color2pp = new Color(255, 102, 255);
	public static Color theme1 = color1p;
	public static Color theme2 = color2p;
	private boolean[][] OriginalMasks = new boolean[9][9];
	private int[][] OriginalPuzzle = new int[9][9];

	// The game board composes of 9x9 JTextFields,
	// each containing String "1" to "9", or empty String
	private JTextField[][] tfCells = new JTextField[GRID_SIZE][GRID_SIZE];

	// Puzzle to be solved and the mask (which can be used to control the
	// difficulty level).
	private int[][] puzzle = { { 5, 3, 4, 6, 7, 8, 9, 1, 2 }, { 6, 7, 2, 1, 9, 5, 3, 4, 8 },
			{ 1, 9, 8, 3, 4, 2, 5, 6, 7 }, { 8, 5, 9, 7, 6, 1, 4, 2, 3 }, { 4, 2, 6, 8, 5, 3, 7, 9, 1 },
			{ 7, 1, 3, 9, 2, 4, 8, 5, 6 }, { 9, 6, 1, 5, 3, 7, 2, 8, 4 }, { 2, 8, 7, 4, 1, 9, 6, 3, 5 },
			{ 3, 4, 5, 2, 8, 6, 1, 7, 9 } };
	// For testing, open only 2 cells.
	private boolean[][] masks = { { false, false, false, false, false, true, false, false, false },
			{ false, false, false, false, false, false, false, false, true },
			{ false, false, false, false, false, false, false, false, false },
			{ false, false, false, false, false, false, false, false, false },
			{ false, false, false, false, false, false, false, false, false },
			{ false, false, false, false, false, false, false, false, false },
			{ false, false, false, false, false, false, false, false, false },
			{ false, false, false, false, false, false, false, false, false },
			{ false, false, false, false, false, false, false, false, false } };

	private boolean[][] colour = { { false, false, false, true, true, true, false, false, false },
			{ false, false, false, true, true, true, false, false, false },
			{ false, false, false, true, true, true, false, false, false },
			{ true, true, true, false, false, false, true, true, true },
			{ true, true, true, false, false, false, true, true, true },
			{ true, true, true, false, false, false, true, true, true },
			{ false, false, false, true, true, true, false, false, false },
			{ false, false, false, true, true, true, false, false, false },
			{ false, false, false, true, true, true, false, false, false } };

	private static int gameMode = 1; // Default Easy mode (1)
	private static String gameModeText;
	
	private int second = 0;
	private int minute = 0;
	private int hour = 0;
	private String time2display;
	private boolean stopTimerFlag = false;
	private boolean resetTimerFlag = false;
	
	private static JLabel lblTimer;
	private static JLabel lblMode;
	private static JButton btnHint;
	
	/*Use a singleton design pattern*/
	private static final Sudoku INSTANCE = null;
	private static Timer TIMER_INSTANCE = null;

	/**
	 * Constructor to setup the game and the UI Components
	 */
	public Sudoku() {
		JFrame f1 = new JFrame();
		f1.setLayout(new BorderLayout());
		JPanel gameBoard = new JPanel(new GridLayout(GRID_SIZE, GRID_SIZE));

		// Generate a random puzzle
		SudokuPuzzleGenerator puz = new SudokuPuzzleGenerator();
		int[][] board = puz.generate();
		puzzle = board.clone();

		// Generate a random mask, initialize with easy mode
		setGameMode();
		if (gameMode != 0) {
			SudokuMaskGenerator mask = new SudokuMaskGenerator();

			boolean[][] musk = mask.generate(gameMode); /** Game-mode: ez-1, med-2, hard-3, extreme-4 */
			masks = musk.clone();
		}
		// initiate start time
		startTimer();

		// Container cp = getContentPane();
		// cp.setLayout(new GridLayout(GRID_SIZE, GRID_SIZE)); // 9x9 GridLayout

		JMenuBar MenuBar = new JMenuBar();
		MenuBar.setBackground(CLOSED_CELL_BGCOLOR);
		JMenu JMenu1 = new JMenu("File");
		JMenuItem JMenuItem2 = new JMenuItem("Reset Game");
		JMenuItem JMenuItem3 = new JMenuItem("Exit");
		JMenuItem2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int a = JOptionPane.showConfirmDialog(null, "Do you want to restart game?");
				if (a == JOptionPane.YES_OPTION) {
					stopTimerFlag = true;
					resetPuzzle();
				}
			}
		});
		JMenuItem3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int a = JOptionPane.showConfirmDialog(null, "Do you want to exit game?");
				if (a == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});
		JMenu JMenu2 = new JMenu("New Game");
		JMenuItem JMenuItem4 = new JMenuItem("Easy");
		JMenuItem JMenuItem5 = new JMenuItem("Medium");
		JMenuItem JMenuItem6 = new JMenuItem("Hard");
		JMenuItem JMenuItem8 = new JMenuItem("Test");
		JMenuItem4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int a = JOptionPane.showConfirmDialog(null, "Do you want to start a new game?");
				if (a == JOptionPane.YES_OPTION) {
					stopTimerFlag = true; 						// trigger timer stop func
					lblMode.setText("Easy");
					gameMode = 1;
					
					resetTimerFlag = true;
					startTimer();
					newPuzzle();
				}
			}
		});
		JMenuItem5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int a = JOptionPane.showConfirmDialog(null, "Do you want to start a new game?");
				if (a == JOptionPane.YES_OPTION) {
					stopTimerFlag = true; 						// trigger timer stop func
					lblMode.setText("Medium");
					gameMode = 2;
					
					newPuzzle();
				}
			}
		});
		JMenuItem6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int a = JOptionPane.showConfirmDialog(null, "Do you want to start a new game?");
				if (a == JOptionPane.YES_OPTION) {
					stopTimerFlag = true; 						// trigger timer stop func
					lblMode.setText("Hard");
					gameMode = 3;
					
					newPuzzle();
				}
			}
		});
		JMenuItem8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int a = JOptionPane.showConfirmDialog(null, "Do you want to start a new game?");
				if (a == JOptionPane.YES_OPTION) {
					stopTimerFlag = true; 						// trigger timer stop func
					lblMode.setText("Test");
					gameMode = 0;
					
					newPuzzle();
				}
			}
		});

		JMenu JMenu3 = new JMenu("Help");
		JMenuItem JMenuItem7 = new JMenuItem("Techniques");
		JMenuItem7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String url = "https://www.kristanix.com/sudokuepic/sudoku-solving-techniques.php";
					java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});
		JMenuItem instruction = new JMenuItem("Instructions");

		instruction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame f = new JFrame();
				JOptionPane.showMessageDialog(f, "Instructions:\r\n" + "\r\n"
						+ "· Key a number in the yellow boxes. Only numbers from 1 to 9 is allowed.\r\n" + "\r\n"
						+ "· Press \"Enter\" to key your answer in. You have to press \"Enter\" once for each yellow box that is filled. \r\n"
						);

			}
		});
		JMenu1.add(JMenu2);
		JMenu1.add(JMenuItem2);
		JMenu1.add(JMenuItem3);
		JMenu2.add(JMenuItem4);
		JMenu2.add(JMenuItem5);
		JMenu2.add(JMenuItem6);
		JMenu2.add(JMenuItem8);
		JMenu3.add(JMenuItem7);
		JMenu3.add(instruction);
		MenuBar.add(JMenu1);
		MenuBar.add(JMenu3);
		f1.add(MenuBar, BorderLayout.NORTH);
		f1.setJMenuBar(MenuBar);

		JMenu mnOptions = new JMenu("Options");
		MenuBar.add(mnOptions);

		JMenu Theme = new JMenu("Theme");

		JMenuItem Default = new JMenuItem("Default");
		Default.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				theme1 = color1p;
				theme2 = color2p;
				gameBoard.setForeground(theme1);
				// ResetPuzzle();
				for (int row = 0; row < GRID_SIZE; ++row) {
					for (int col = 0; col < GRID_SIZE; ++col) {
						if (masks[row][col] == false && colour[row][col] == false) {
							tfCells[row][col].setBackground(theme1);
						} else if (masks[row][col] == false && colour[row][col] == true) {
							tfCells[row][col].setBackground(theme2);
						}
					}
				}
				// time.setTime(0, 0, 0);
				/*
				 * for (int row = 0; row < 9; row++) { for (int col = 0; col < 9; col++) {
				 * OriginalPuzzle[row][col] = puzzle[row][col]; OriginalMasks[row][col] =
				 * masks[row][col]; } }
				 */

			}
		});
		Theme.add(Default);

		JMenuItem Blue = new JMenuItem("Ocean");
		Blue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				theme1 = color1b;
				theme2 = color2b;
				gameBoard.setForeground(theme1);
				// ResetPuzzle();
				for (int row = 0; row < GRID_SIZE; ++row) {
					for (int col = 0; col < GRID_SIZE; ++col) {
						if (masks[row][col] == false && colour[row][col] == false) {
							tfCells[row][col].setBackground(theme1);
						} else if (masks[row][col] == false && colour[row][col] == true) {
							tfCells[row][col].setBackground(theme2);
						}
					}
				}
				// time.setTime(0, 0, 0);
				/*
				 * for (int row = 0; row < 9; row++) { for (int col = 0; col < 9; col++) {
				 * OriginalPuzzle[row][col] = puzzle[row][col]; OriginalMasks[row][col] =
				 * masks[row][col]; } }
				 */
			}

		});
		Theme.add(Blue);

		JMenuItem Plain = new JMenuItem("Newspaper");
		Plain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				theme1 = color1pl;
				theme2 = color2pl;
				gameBoard.setForeground(theme1);
				// ResetPuzzle();
				for (int row = 0; row < GRID_SIZE; ++row) {
					for (int col = 0; col < GRID_SIZE; ++col) {
						if (masks[row][col] == false && colour[row][col] == false) {
							tfCells[row][col].setBackground(theme1);
						} else if (masks[row][col] == false && colour[row][col] == true) {
							tfCells[row][col].setBackground(theme2);
						}
					}
				}
				// time.setTime(0, 0, 0);
				/*
				 * for (int row = 0; row < 9; row++) { for (int col = 0; col < 9; col++) {
				 * OriginalPuzzle[row][col] = puzzle[row][col]; OriginalMasks[row][col] =
				 * masks[row][col]; } }
				 */

			}
		});
		Theme.add(Plain);

		JMenuItem Pink = new JMenuItem("Sakura");
		Pink.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				theme1 = color1pp;
				theme2 = color2pp;
				gameBoard.setForeground(theme1);
				// ResetPuzzle();
				for (int row = 0; row < GRID_SIZE; ++row) {
					for (int col = 0; col < GRID_SIZE; ++col) {
						if (masks[row][col] == false && colour[row][col] == false) {
							tfCells[row][col].setBackground(theme1);
						} else if (masks[row][col] == false && colour[row][col] == true) {
							tfCells[row][col].setBackground(theme2);
						}
					}
				}
				// time.setTime(0, 0, 0);
				/*
				 * for (int row = 0; row < 9; row++) { for (int col = 0; col < 9; col++) {
				 * OriginalPuzzle[row][col] = puzzle[row][col]; OriginalMasks[row][col] =
				 * masks[row][col]; } }
				 */

			}
		});
		Theme.add(Pink);
		mnOptions.add(Theme);
		JCheckBoxMenuItem chckbxmntmMusic = new JCheckBoxMenuItem("Music");
		chckbxmntmMusic.setSelected(true);
		chckbxmntmMusic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AbstractButton MusicButton = (AbstractButton) e.getSource();
				boolean selected = MusicButton.getModel().isSelected();
				if (MusicButton.isSelected()) {

					// CORRECT("correctans.wav"), // explosion
					// WRONG("wrongans.wav"), // gong
					// BOOT("Boot.wav"), // bullet
					/*
					 * INIT("Gameinit.wav"), WIN("winwait.wav"), BGM("BGM.wav"),
					 * SILENT("Silent.wav");
					 */
					SoundBGM.BGM.play();
				} else {

					SoundBGM.BGM.stop();
				}

			}
		});
		mnOptions.add(chckbxmntmMusic);
		JCheckBoxMenuItem hintOption = new JCheckBoxMenuItem("Hint");
		hintOption.setSelected(false);
		hintOption.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AbstractButton hintButton = (AbstractButton) e.getSource();
				//boolean selected = hintButton.getModel().isSelected();
				if(hintButton.isSelected()) {
					//enable hint
					btnHint.setVisible(true);
				}else {
					//disable hint
					btnHint.setVisible(false);
				}
			}
		});
		mnOptions.add(hintOption);
		SoundBGM.init();
		SoundBGM.volume = SoundBGM.Volume.LOW;
		SoundBGM.BGM.play();

		// Allocate a common listener as the ActionEvent listener for all the
		// JTextFields
		InputListener listener = new InputListener();

		// Construct 9x9 JTextFields and add to the content-pane
		for (int row = 0; row < GRID_SIZE; ++row) {
			for (int col = 0; col < GRID_SIZE; ++col) {
				tfCells[row][col] = new JTextField(); // Allocate element of array
				gameBoard.add(tfCells[row][col]); // ContentPane adds JTextField
				if (masks[row][col]) {
					tfCells[row][col].setText(""); // set to empty string
					tfCells[row][col].setEditable(true);
					tfCells[row][col].setBackground(OPEN_CELL_BGCOLOR);

					// Add ActionEvent listener to process the input
					// ... [TODO 4] (Later) ...
					tfCells[row][col].addActionListener(listener);

				} else {
					tfCells[row][col].setText(puzzle[row][col] + "");
					tfCells[row][col].setEditable(false);
					tfCells[row][col].setBackground(CLOSED_CELL_BGCOLOR);
					tfCells[row][col].setForeground(CLOSED_CELL_TEXT);
				}

				OriginalPuzzle[row][col] = puzzle[row][col];
				OriginalMasks[row][col] = masks[row][col];
				// Beautify all the cells
				tfCells[row][col].setHorizontalAlignment(JTextField.CENTER);
				tfCells[row][col].setFont(FONT_NUMBERS);
				if (masks[row][col] == false && colour[row][col] == false) {
					tfCells[row][col].setBackground(theme1);
				} else if (masks[row][col] == false && colour[row][col] == true) {
					tfCells[row][col].setBackground(theme2);
				}
			}
		}

		JPanel status = new JPanel(new FlowLayout());
		lblMode = new JLabel(gameModeText);
		status.add(lblMode);
		lblTimer = new JLabel("Time:");
		status.add(lblTimer);
		btnHint = new JButton("Display hint");
		btnHint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showHint(); //takes in row & col of selected JTextField
			}
		});
		btnHint.setVisible(false);
		status.add(btnHint);
		
		// Set the size of the content-pane and pack all the components
		// under this container.
		f1.add(gameBoard, BorderLayout.CENTER);
		f1.add(status, BorderLayout.SOUTH);
		f1.pack();
		
		f1.setSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
		f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 			// Handle window closing
		f1.setTitle("Sudoku");
		f1.setVisible(true);

	}
	
	public static Sudoku getInstance() {
		if(INSTANCE == null) {
			new Sudoku();
		}
		
		return INSTANCE;
	}

	/** The entry main() entry method */
	public static void main(String[] args) {
		// [TODO 1] (Now)
		// Check Swing program template on how to run the constructor
		//Sudoku.getInstance();
		new Sudoku();
	}

	// Define the Listener Inner Class
	// ... [TODO 2] (Later) ...
	private class InputListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// All the 9*9 JTextFileds invoke this handler. We need to determine
			// which JTextField (which row and column) is the source for this invocation.
			int rowSelected = -1;
			int colSelected = -1;

			// Get the source object that fired the event
			JTextField source = (JTextField) e.getSource();
			// Scan JTextFileds for all rows and columns, and match with the source object
			boolean found = false;
			for (int row = 0; row < GRID_SIZE && !found; ++row) {
				for (int col = 0; col < GRID_SIZE && !found; ++col) {
					if (tfCells[row][col] == source) {
						rowSelected = row;
						colSelected = col;
						found = true; // break the inner/outer loops
					}
				}
			}

			/*
			 * [TODO 5] 1. Get the input String via
			 * tfCells[rowSelected][colSelected].getText() 2. Convert the String to int via
			 * Integer.parseInt(). 3. Assume that the solution is unique. Compare the input
			 * number with the number in the puzzle[rowSelected][colSelected]. If they are
			 * the same, set the background to green (Color.GREEN); otherwise, set to red
			 * (Color.RED).
			 */
			found = false;
			String input1 = tfCells[rowSelected][colSelected].getText();
			int input = Integer.parseInt(input1);
			for (int row = 0; row < GRID_SIZE && !found; ++row) {
				for (int col = 0; col < GRID_SIZE && !found; ++col) {
					if (puzzle[rowSelected][colSelected] == input) {
						tfCells[rowSelected][colSelected].setBackground(OPEN_CELL_TEXT_YES);
						masks[rowSelected][colSelected] = false;
						SoundFX.CORRECT.play();
					} else {
						tfCells[rowSelected][colSelected].setBackground(OPEN_CELL_TEXT_NO);
						SoundFX.WRONG.play();
					}
					found = true;
				}
			}
			/*
			 * [TODO 6] Check if the player has solved the puzzle after this move. You could
			 * update the masks[][] on correct guess, and check the masks[][] if any input
			 * cell pending.
			 */
			
			int count = 0; //number of puzzle to solve left
			for (int row = 0; row < GRID_SIZE; ++row) {
				for (int col = 0; col < GRID_SIZE; ++col) {
					if (masks[row][col] == true) {
						count++;
					}
				}
			}
			if (count == 0) {
				stopTimerFlag = true; // trigger timer stop
				SoundBGM.BGM.stop();
				SoundFX.WIN.play();
				// will return 0 is ok, -1 if dialog closed
				int res = JOptionPane.showOptionDialog(null, "Congratulations!\n" + time2display, "Test",
						JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
				SoundFX.WIN.stop();

				//start new game when close or user press 'ok'
				if (res == 0 || res == -1) {
					//new Sudoku();
				}
			}

		}

	}

	public void resetPuzzle() {
		for (int row = 0; row < GRID_SIZE; ++row) {
			for (int col = 0; col < GRID_SIZE; ++col) {
				if (masks[row][col] == false && colour[row][col] == false) {
					tfCells[row][col].setBackground(theme1);
				} else if (masks[row][col] == false && colour[row][col] == true) {
					tfCells[row][col].setBackground(theme2);
				}
			}
		}
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				puzzle[row][col] = OriginalPuzzle[row][col];
				masks[row][col] = OriginalMasks[row][col];
				if (masks[row][col]) {
					tfCells[row][col].setText(""); // set to empty string
					tfCells[row][col].setEditable(true);
					tfCells[row][col].setBackground(OPEN_CELL_BGCOLOR);

				} else {
					tfCells[row][col].setText(puzzle[row][col] + "");
					tfCells[row][col].setEditable(false);
					// tfCells[row][col].setBackground(CLOSED_CELL_BGCOLOR);
					tfCells[row][col].setForeground(CLOSED_CELL_TEXT);
				}
			}
		}
		startTimer();
		SoundBGM.init();
		SoundBGM.volume = SoundBGM.Volume.LOW;
		SoundBGM.BGM.play();
	}

	public void newPuzzle() {
		SudokuPuzzleGenerator puz = new SudokuPuzzleGenerator();
		int[][] board = puz.generate();
		puzzle = board.clone();
		for (int row = 0; row < GRID_SIZE; ++row) {
			for (int col = 0; col < GRID_SIZE; ++col) {
				if (masks[row][col] == false && colour[row][col] == false) {
					tfCells[row][col].setBackground(theme1);
				} else if (masks[row][col] == false && colour[row][col] == true) {
					tfCells[row][col].setBackground(theme2);
				}
			}
		}
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				if (masks[row][col]) {
					tfCells[row][col].setText(""); // set to empty string
					tfCells[row][col].setEditable(true);
					tfCells[row][col].setBackground(OPEN_CELL_BGCOLOR);

				} else {
					tfCells[row][col].setText(puzzle[row][col] + "");
					tfCells[row][col].setEditable(false);
					// tfCells[row][col].setBackground(CLOSED_CELL_BGCOLOR);
					tfCells[row][col].setForeground(CLOSED_CELL_TEXT);
				}
			}
		}

		startTimer();
		
		SoundBGM.init();
		SoundBGM.volume = SoundBGM.Volume.LOW;
		SoundBGM.BGM.play();
	}
	
	/* Time elapsed function */
	public void startTimer() {
		//Only one instance of timer
		if(TIMER_INSTANCE == null) {
			TIMER_INSTANCE = new Timer();
		}
		if(resetTimerFlag) {
			second = 0;
			minute = 0;
			hour = 0;
			resetTimerFlag = false;
		}
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				// To execute every second
				try {
					SwingUtilities.invokeAndWait(new Runnable() {

						@Override
						public void run() {
							second++;
							if (second >= 60) {
								minute++;
								second = 0;
								if (minute >= 60) {
									hour++;
									minute = 0;
								}
							}
							time2display = String.format("Time: %02d:%02d:%02d", hour, minute, second);
							System.out.println(time2display); //debug

							// stop current timer, start a new one
							if (stopTimerFlag) {
								TIMER_INSTANCE.cancel();
								TIMER_INSTANCE.purge();
								stopTimerFlag = false;
								//return;
							}
							
							

							lblTimer.setText(time2display);
						}
					});
				} catch (InvocationTargetException | InterruptedException e) {
					e.printStackTrace();
				}
			}
		};

		TIMER_INSTANCE.scheduleAtFixedRate(task, 1000, 1000);
		
	}
	
	/*Game-mode: ez-1, med-2, hard-3*/
	public void setGameMode() {
		switch(gameMode) {
		case 0:{
			gameModeText = "Test";
			break;
		}
		case 1: {
			gameModeText = "Easy";
			break;
		}
		case 2: {
			gameModeText = "Medium";
			break;
		}
		case 3: {
			gameModeText = "Hard";
			break;
		}
		}
	}
	
	public int showHint(int row, int col) {
		/*
		 * Menu that enables hint options
		 * Once enabled, hint button visible
		 * Select hint button to auto-fill current JTextField
		 * 
		 * */
		return puzzle[row][col];
	}
}
