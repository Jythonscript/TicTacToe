package tictactoe;

public class Tree {

	int[] index;
	final int initialSize;
	
	public Tree(int size) {
		
		initialSize = size;
		index = new int[initialSize];
		
		//fills the list with all 1s
		for (int i = 0; i < index.length; i++) {
			index[i] = 1;
		}
		
	}
	
	//incerements the list to go through all combinations of numbers
	//eg. [1,1,1,1,1] through [5,4,3,2,1,]
	public void increment() {
		
		for (int i = 0; i < index.length - 1; i++) {
			
			//increments the index if it isn't big enough
			if (index[i] < (initialSize - i)) {
				index[i] ++;
				break;
			}
			else {
				index[i] = 1;
			}
			
		}
		
	}

	public int get(int i) {
		return index[i] - 1;
	}
	
	public String toString() {
		
		String s = "[";
		
		for (int i = 0; i < index.length; i++) {
			
			s += index[i];
			
			if (i != index.length - 1) {
				s += ", ";
			}
			
		}
		
		s += "]";
		
		return s;
		
	}
	
}
