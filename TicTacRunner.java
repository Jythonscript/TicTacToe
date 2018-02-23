package tictactoe;

public class TicTacRunner {

	public static void main(String[] args) {
		
		TicTacScreen scr = new TicTacScreen();
		Thread blankThread = new Thread(scr);
		blankThread.start();
		
//		Menu m = new Menu();
		
	}
	
}
