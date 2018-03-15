package tictactoe;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;


public class TicTacScreen extends JFrame implements Runnable{

	//double buffering variables
	Image dbImage;
	Graphics dbGraphics;
	
	Board board;
	
	final int BOARDWIDTH;
	final int BOARDHEIGHT;
	
	//pixel width of each slot on the board
	final int BOXWIDTH = 100;
	
	//x and y offset of the board and everything on it from the point (0, 0)
	final int XOFFSET = 50;
	final int YOFFSET = 50;
	
	//screen dimensions
	final int SCREENWIDTH;
	final int SCREENHEIGHT;
	
	int gamestate;
	//game states
	public static final int XTURN = 1;
	public static final int OTURN = 2;
	public static final int XVICTORY = 3;
	public static final int OVICTORY = 4;
	public static final int TIEGAME = 5;
	String gameStateString;
	
	//whether or not the window is active
	boolean isActive = true;
	
	//29.4 fps if active
	int activeMsDelay = 34;

	//desired fps when not active
	int backgroundMsDelay = 1000;
	
	//main thread
	public void run() {
		
		try {
			while (true) {
				
				Thread.sleep(activeMsDelay);
				if (!isActive) {
					Thread.sleep(backgroundMsDelay - activeMsDelay);
				}
				
				repaint();
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public TicTacScreen(int width, int height, int state) {
		
		//starts event listeners
		addMouseListener(new Mouse());
		addKeyListener(new Keyboard());
		addWindowListener(new Window());

		//sets board and screen dimensions
		BOARDWIDTH = width;
		BOARDHEIGHT = height;
		SCREENWIDTH = BOXWIDTH * BOARDWIDTH + (2 * XOFFSET);
		SCREENHEIGHT = BOXWIDTH * BOARDHEIGHT + (2 * YOFFSET);
		
		//sets up the state of the game
		gamestate = state;
		
		//creates board
		board = new Board(BOARDWIDTH, BOARDHEIGHT);
		
		//JFrame properties
		this.setSize(SCREENWIDTH, SCREENHEIGHT);
		this.setVisible(true);
		this.setResizable(true);
		this.setTitle("Tic Tac Toe");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	//mouse input
	public class Mouse extends MouseAdapter {
		
		public void mouseReleased(MouseEvent e) {
			
			//places an X or a O based on the mouse position 
			
			//gets mouse coordinates
			int x = e.getX();
			int y = e.getY();
			
			//converts coords into a selected block
			int blockX = (x - XOFFSET) / BOXWIDTH;
			int blockY = (y - YOFFSET) / BOXWIDTH;
			
			//bounds checking
			if (blockX < 0) {
				blockX = 0;
				return;
			}
			if (blockX > board.WIDTH - 1) {
				blockX = board.WIDTH - 1;
				return;
			}
			
			if (blockY < 0) {
				blockY = 0;
				return;
			}
			if (blockY > board.HEIGHT - 1) {
				blockY = board.HEIGHT - 1;
				return;
			}
			
//			System.out.println(blockX + ", " + blockY);
			
			if (e.isShiftDown() && board.get(blockX, blockY) == board.EMPTY) {
				board.set(blockX, blockY, board.O);
			}
			else if (!e.isShiftDown() && board.get(blockX, blockY) == board.EMPTY) {
				board.set(blockX, blockY, board.X);				
			}
			
			if (board.isVictory(board.X)) {
				gamestate = XVICTORY;
//				System.out.println("X Wins\n");
				return;
			}
			else if (board.isVictory(board.O)) {
				gamestate = OVICTORY;
//				System.out.println("O Wins\n");
				return;
			}
			else if (board.isTieGame(board.X, board.O)) {
//				System.out.println("Tie game\n");
				gamestate = TIEGAME;
				return;
			}
			
		}
		
	}
	
	//key input
	public class Keyboard extends KeyAdapter {
		
		public void keyPressed(KeyEvent e) {
			
			//gets the keycode when a key is pressed
			int key = e.getKeyCode();
			
			switch (key) {
			
			case KeyEvent.VK_Q:
				System.exit(0);
				break;
			
			case KeyEvent.VK_R:
				gamestate = XTURN;
				board.clear();
				break;
			
			case KeyEvent.VK_X:
				board.makeTurn(board.X);
				if (board.isVictory(board.X)) {
					System.out.println("X Wins\n");
				}
				else if (board.isVictory(board.O)) {
					System.out.println("O Wins\n");
				}
				break;
				
			case KeyEvent.VK_O:
				board.makeTurn(board.O);
				if (board.isVictory(board.X)) {
					System.out.println("X Wins\n");
				}
				else if (board.isVictory(board.O)) {
					System.out.println("O Wins\n");
				}
				break;
				
			}
			
		}
		
	}
	
	public class Window extends WindowAdapter {
		
		public void windowActivated(WindowEvent e) {
			
			isActive = true;
			
		}
		
		public void windowDeactivated(WindowEvent e) {
			
			isActive = false;
			
		}
		
	}
	
	//double buffering
	public void paint(Graphics g) {
		
		dbImage = createImage(getWidth(), getHeight());
		dbGraphics = dbImage.getGraphics();
		paintComponent(dbGraphics);
		g.drawImage(dbImage, 0, 0, this);
		
	}
	
	//prints stuff to the JFrame
	public void paintComponent(Graphics g) {
		
		int ovalOffset = BOXWIDTH / 8;
		int crossOffset = BOXWIDTH / 4;
		int ovalWidth = (BOXWIDTH * 3) / 4;
		
		//prints the board
		for (int r = 0; r < board.WIDTH; r++) {
			
			int cellXLocation = XOFFSET + r * BOXWIDTH;
			
			//draws the horizontal lines that make up the grid
			if (r != 0) {
				g.drawLine(cellXLocation, YOFFSET, cellXLocation, YOFFSET + board.HEIGHT * BOXWIDTH);
			}
			for (int c = 0; c < board.HEIGHT; c++) {
				
				int cellYLocation = YOFFSET + c * BOXWIDTH;
				
				//draws the vertical lines that make up the grid
				if (c != 0) {
					g.drawLine(XOFFSET, cellYLocation, XOFFSET + board.WIDTH * BOXWIDTH, cellYLocation);
				}
				
				if (board.get(r, c) == board.O) {
					//draws O's on the board
					g.drawOval(cellXLocation + ovalOffset, cellYLocation + ovalOffset, ovalWidth, ovalWidth);
				}
				else if (board.get(r, c) == board.X) {
					//draws two lines that make up the X
					g.drawLine(cellXLocation + crossOffset, YOFFSET + (c) * BOXWIDTH + crossOffset,
							XOFFSET + (r + 1) * BOXWIDTH - crossOffset, YOFFSET + (c + 1) * BOXWIDTH - crossOffset);
					
					g.drawLine(cellXLocation + crossOffset, YOFFSET + (c + 1) * BOXWIDTH - crossOffset,
							XOFFSET + (r + 1) * BOXWIDTH - crossOffset, YOFFSET + (c) * BOXWIDTH + crossOffset);
				}
				
			}
		}
		
		
		
		switch (gamestate) {
		
		case XTURN: 	gameStateString = "X's Turn";
			break;
		case OTURN: 	gameStateString = "O's Turn";
			break;
		case XVICTORY: 	gameStateString = "X Wins!";
			break;
		case OVICTORY: 	gameStateString = "O Wins!";
			break;
		case TIEGAME: 	gameStateString = "Tie Game";
			break;
		default: 		gameStateString = "";
			break;
			
		}
		
		g.drawString(gameStateString, XOFFSET, YOFFSET);
		
	}
	
}
