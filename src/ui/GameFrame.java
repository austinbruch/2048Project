package ui;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import logic.Game;
import logic.GameBoard;
import logic.Move;
import util.Colors;
import config.Constants;

public class GameFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton[][] gridButtons;
	private JPanel pane;
	private Game game;
	private JMenu scoreMenu = new JMenu("Score: ");
	private JMenu bestMenu = new JMenu("Best: ");
	private JMenu movesMenu = new JMenu("Moves: ");
	
	
	private static boolean displayedWinningDialog = false;

	public GameFrame(Game game) {
		this.game = game;
		initUI();
	}

	private void initUI() {
		final GameFrame gameFrameReference = this;
		final Game gameReference = this.game;
		pane = (JPanel) getContentPane();
		pane.setToolTipText("2048");

		JMenuBar menuBar = new JMenuBar();

		JMenu file = new JMenu("File");

		JMenuItem exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W | KeyEvent.VK_Q, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())); // CMD+W closes the application
		exitMenuItem.setToolTipText("Exit application");
		exitMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				gameReference.endGame(true);
				System.exit(0);
			}
		});

		JMenuItem newGameMenuItem = new JMenuItem("New Game");
		newGameMenuItem.setToolTipText("Start a new game");
		newGameMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())); // CMD+N starts a new game
		newGameMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO start a new game
				gameReference.startNewGame();
			}
		});

		JMenuItem highScoresMenuItem = new JMenuItem("High Scores");
		highScoresMenuItem.setToolTipText("View your high scores");
		highScoresMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO view high scores
			}
		});

		// add the three options to the file menu dropdown
		file.add(newGameMenuItem);
		file.add(highScoresMenuItem);
		file.add(exitMenuItem);

		// add the file menu option to the menu bar
		menuBar.add(file);

		menuBar.add(Box.createHorizontalGlue());

		menuBar.add(movesMenu);
		menuBar.add(scoreMenu);
		menuBar.add(bestMenu);

		// set which menubar we are using for the frame
		setJMenuBar(menuBar);

		addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {

				switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					gameFrameReference.game.move(Move.LEFT);
					break;
				case KeyEvent.VK_RIGHT:
					gameFrameReference.game.move(Move.RIGHT);
					break;
				case KeyEvent.VK_UP: 
					gameFrameReference.game.move(Move.UP);
					break;
				case KeyEvent.VK_DOWN: 
					gameFrameReference.game.move(Move.DOWN);
					break;
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		});


		JPanel gameBoard = new JPanel();
		int borderWidth = 4;
		int gapBetweenCells = 4;
		
		gameBoard.setBorder(BorderFactory.createEmptyBorder(borderWidth, borderWidth, borderWidth, borderWidth));
		
		gameBoard.setLayout(new GridLayout(Constants.NUM_COLUMNS, Constants.NUM_ROWS, gapBetweenCells, gapBetweenCells));

		gridButtons = new JButton[Constants.NUM_ROWS][Constants.NUM_COLUMNS];
		for (int i = 0; i < Constants.NUM_ROWS; i++) {
			for (int j = 0; j < Constants.NUM_COLUMNS; j++) {
				final JButton button = new JButton();
				button.setText("");
				button.setEnabled(false);
				button.setOpaque(true);
				button.setFont(new Font(Constants.TILE_FONT, Font.BOLD, Constants.TILE_FONT_LARGE));
				gridButtons[i][j] = button;
				gameBoard.add(button);
			}
		}

		add(gameBoard);

		pack();

		int windowWidth = 500, windowHeight = 500;
		
		setSize(windowWidth, windowHeight);
		setTitle(Constants.GAME_TITLE);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void updateUI(GameBoard gameBoard) {
		boolean reached2048 = false;
		for (int i = 0; i < Constants.NUM_ROWS; i++) {
			for (int j = 0; j < Constants.NUM_COLUMNS; j++) {
				if(gameBoard.needUpdate(i, j)) {
					String text = translateButtonText(gameBoard.getValueAtPosition(i, j));
					if(text.length() > 3) {
						gridButtons[i][j].setFont(new Font(Constants.TILE_FONT, Font.BOLD, Constants.TILE_FONT_SMALL));
						if(text.equals("2048")) {
							reached2048 = true;
						}
					}
					if(text.length() <= 3) {
						gridButtons[i][j].setFont(new Font(Constants.TILE_FONT, Font.BOLD, Constants.TILE_FONT_LARGE));
					}
					gridButtons[i][j].setText(text);
					gridButtons[i][j].setBackground(new Color(getColorForNumber(gameBoard.getValueAtPosition(i, j))));
					gameBoard.clearUpdate(i, j);
				}
			}
		}
		
		if(!displayedWinningDialog & reached2048) {
			reached2048Dialog();
			displayedWinningDialog = true;
		}
	}

	private String translateButtonText(int value) {
		if(value == 0) { 
			return "";
		} else {
			return Integer.toString(value);
		}
	}

	private int getColorForNumber(int value) {
		switch(value) {
		case 0: 
			return Color.DARK_GRAY.getRGB();
		case 2: 
			return Colors._2;
		case 4: 
			return Colors._4;
		case 8:
			return Colors._8;
		case 16: 
			return Colors._16;
		case 32: 
			return Colors._32;
		case 64: 
			return Colors._64;
		case 128:
			return Colors._128;
		case 256: 
			return Colors._256;
		case 512: 
			return Colors._512;
		case 1024:
			return Colors._1024;
		case 2048: 
			return Colors._2048;
		case 4096:
			return Colors._4096;
		case 8192: 
			return Colors._8192;
		case 16384:
			return Colors._16384;
		case 32768:
			return Colors._32768;
		case 65536:
			return Colors._65536;
		default: 
			return Color.DARK_GRAY.getRGB();
		}
	}
	
	public void setMoves(int moves) {
		this.movesMenu.setText("Moves: " + Integer.toString(moves));
	}
	
	public void setScore(int score) {
		this.scoreMenu.setText("Score: " + Integer.toString(score));
	}
	
	public void setBest(int best) {
		this.bestMenu.setText("Best: " + Integer.toString(best));
	}

	public void endGameDialog() {
		JOptionPane.showMessageDialog(null, "Game Over", "Game Over", DO_NOTHING_ON_CLOSE);
	}
	
	private void reached2048Dialog() {
		JOptionPane.showMessageDialog(null, "You Win!", "Congratulations, you've reached 2048!", DO_NOTHING_ON_CLOSE);
	}
	
	public void run() {
		final GameFrame reference = this;
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				reference.setVisible(true);
			}
		});
	}

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				GameFrame ex = new GameFrame(null);
				ex.setVisible(true);
			}
		});
	}
}
