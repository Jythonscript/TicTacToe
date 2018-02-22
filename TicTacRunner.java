package tictactoe;

public class TicTacRunner {

	public static void main(String[] args) {
		
//		try {
//			Tree t = new Tree(9);
//			
//			System.out.println(t);
//			
//			
//			
//			for (int i = 0; i < 362880; i++) {
//				
//				t.increment();
//				
//				System.out.println(t);
//				
//			}
//		}
//		catch (Exception e) {
//			
//			e.printStackTrace();
//			
//		}
		
		TicTacScreen scr = new TicTacScreen();
		Thread blankThread = new Thread(scr);
		blankThread.start();
		
//		Menu m = new Menu();
		
	}
	
}
