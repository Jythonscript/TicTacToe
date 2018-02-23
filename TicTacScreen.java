package tictactoe;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;


public class TicTacScreen extends JFrame implements Runnable{

	//double buffering variables
	Image dbImage;
	Graphics dbGraphics;
	
	Board board;
	
	final int BOARDWIDTH  = 3;
	final int BOARDHEIGHT = 3;
	
	//pixel width of each slot on the board
	final int BOXWIDTH = 100;
	
	//x and y offset of the board and everything on in from the point (0, 0)
	final int XOFFSET = 50;
	final int YOFFSET = 50;
	
	//screen dimensions
	final int SCREENWIDTH = BOXWIDTH * BOARDWIDTH + (2 * XOFFSET);
	final int SCREENHEIGHT = BOXWIDTH * BOARDHEIGHT + (2 * YOFFSET);
	
	int gamestate;
	final int XTURN = 1;
	final int OTURN = 2;
	final int XVICTORY = 3;
	final int OVICTORY = 4;
	final int TIEGAME = 5;
	
	//main thread
	public void run() {
		
		try {
			while (true) {
				
				//58.8 fps
				Thread.sleep(17);
				
				repaint();
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public TicTacScreen() {
		
		//sets up the state of the game
		gamestate = XTURN;
		
		//starts event listeners
		addMouseListener(new Mouse());
		addKeyListener(new Keyboard());
		
		//creates board, 3 by 3
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
			
			if (e.isShiftDown()) {
				board.set(blockX, blockY, board.O);
			}
			else {
				board.set(blockX, blockY, board.X);				
			}
			
			if (board.isVictory(board.X)) {
				System.out.println("X Wins\n");
			}
			else if (board.isVictory(board.O)) {
				System.out.println("O Wins\n");
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
				board.clear();
				break;
			
			case KeyEvent.VK_X:
				board.makeTurn(board.X);
				System.out.println();
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
		
	}
	
}
