package tictactoe;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Menu extends JFrame implements Runnable{

	//double buffering variables
	Image dbImage;
	Graphics dbGraphics;
	
	final int SCREENWIDTH = 500;
	final int SCREENHEIGHT = 500;
	
	//button images
	ImageIcon singleplayer;
	
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
	
	//default constructor
	public Menu() {
		
		//images
		singleplayer = new ImageIcon("src/tictactoe/Singleplayer.png");
		
		//JFrame properties
		this.setSize(SCREENWIDTH, SCREENHEIGHT);
		this.setVisible(true);
		this.setResizable(false);
		this.setTitle("");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
}

