package tictactoe;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Menu extends JFrame implements Runnable{

	//double buffering variables
	Image dbImage;
	Graphics dbGraphics;
	
	final int SCREENWIDTH = 500;
	final int SCREENHEIGHT = 500;
	
	ImageIcon singleplayer;
	
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
	
	//default constructor
	public Menu() {
		
		addKeyListener(new Keyboard());
		addWindowListener(new Window());
		
		//images
		singleplayer = new ImageIcon("src/tictactoe/Singleplayer.png");
		
		//JFrame properties
		this.setSize(SCREENWIDTH, SCREENHEIGHT);
		this.setVisible(true);
		this.setResizable(false);
		this.setTitle("");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
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
	
	public void paint(Graphics g) {		
		//double buffering
		dbImage = createImage(getWidth(), getHeight());
		dbGraphics = dbImage.getGraphics();
		paintComponent(dbGraphics);
		g.drawImage(dbImage, 0, 0, this);
		
	}
	
	//prints stuff to the JFrame
	public void paintComponent(Graphics g) {
		
		g.drawRect(50, 50, 400, 100);
		g.drawImage(singleplayer.getImage(), 50, 50, 400, 100, this);
		
	}
	
}

