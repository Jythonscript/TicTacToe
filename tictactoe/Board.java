package tictactoe;

public class Board {
	
	//board state final fields
	public final int EMPTY = 0;
	public final int X = 1;
	public final int O = 2;
	
	private int[][] board;
	public final int WIDTH;
	public final int HEIGHT;
	
	
	//number of like tiles you need in a row to win
	final int VICTORYCONDITION = 3;
	
	public Board(int width, int height) {
		
		WIDTH = width;
		HEIGHT = height;
		
		board = new int[width][height];
		
		//clears the board from the start
		for (int r = 0; r < WIDTH; r++) {
			for (int c = 0; c < HEIGHT; c++) {
				
				board[r][c] = EMPTY;
				
			}
		}
		
	}
	
	public void clear() {
		
		for (int r = 0; r < WIDTH; r++) {
			for (int c = 0; c < HEIGHT; c++) {
				
				board[r][c] = EMPTY;
				
			}
		}
		
	}
	
	//returns the state of the item at the given location on the board, if it exists
	public int get(int row, int column) {
		
		if (row < WIDTH && row >= 0 
		&& column < HEIGHT && column >= 0) {
			return board[row][column];
		}
		return -1;
		
	}
	
	//sets the [row][column] location on the board to the given state
	public void set(int row, int column, int state) {
		
		if (state < 0 || state > this.O) {
			return;
		}
		else if (get(row, column) != -1) {
			board[row][column] = state;
		}
		
	}
	
	//returns true if the parameter state has VICTORYCONDITION in a row
	public boolean isVictory(int state) {
		
		for (int r = 0; r < WIDTH; r++) {
			for (int c = 0; c < HEIGHT; c++) {
				
				//horizontal VICTORYCONDITION in a row
				try {
					for (int i = 0; i < VICTORYCONDITION; i++) {
						
						if (board[r + i][c] != state) {
							break;
						}
						if (i == VICTORYCONDITION - 1) {
							return true;
						}
						
					}
					
				} catch (Exception e) {
					//yeet
				}
				
				//vertical VICTORYCONDITION in a row
				try {
					for (int i = 0; i < VICTORYCONDITION; i++) {
						
						if (board[r][c + i] != state) {
							break;
						}
						if (i == VICTORYCONDITION - 1) {
							return true;
						}
						
					}
					
				} catch (Exception e) {
					//yeet
				}
				
				//diagional (up-left to down-right) VICTORYCONDITION in a row
				try {
					for (int i = 0; i < VICTORYCONDITION; i++) {
						
						if (board[r + i][c + i] != state) {
							break;
						}
						if (i == VICTORYCONDITION - 1) {
							return true;
						}
						
					}
					
				} catch (Exception e) {
					//yeet
				}
				
				//diagional (up-right to down-left) VICTORYCONDITION in a row
				try {
					for (int i = 0; i < VICTORYCONDITION; i++) {
						
						if (board[r - i][c + i] != state) {
							break;
						}
						if (i == VICTORYCONDITION - 1) {
							return true;
						}
						
					}
					
				} catch (Exception e) {
					//yeet
				}
				
			}
		}
		
		return false;
		
	}
	
	public boolean isTieGame(int state, int enemystate) {
		
		return (numBlankSpaces() == 0 
				&& !isVictory(state)
				&& !isVictory(enemystate));
				
	}
	
	public int numBlankSpaces() {
		
		int blanks = 0;
		
		for (int r = 0; r < WIDTH; r++) {
			for (int c = 0; c < HEIGHT; c++) {
				
				if (this.get(r, c) == EMPTY) {
					
					blanks ++;
					
				}
				
			}
		}
		return blanks;
	}
	
	//returns a board where the given state was placed in the nth blank space available, checking in the following order:
	/* 1 2 3
	   4 5 6
	   7 8 9 */
	public Board fillNthBlankWithState(int n, int state) {
		
		Board b = this;
		
		int currentBlankNum = 0;
		
		//for loop modified for correct order checking
		for (int c = 0; c < b.HEIGHT; c++) {
			for (int r = 0; r < b.WIDTH; r++) {
				
				if (b.get(r, c) == EMPTY) {
					currentBlankNum ++;
				}
				
				if (currentBlankNum == n) {
					b.set(r, c, state);
					return b;
				}
				
			}
		}
		
		return b;
		
	}
	
	//completes a three in a row for the state or prevents one from the opponent if it is one turn away from happening
	//returns 1 if victory, returns 2 if prevented an opponent's three in a row. Returns 3 if there is only one available spot
	//Otherwise, returns 0
	public int chooseBestOption(int state, int enemystate) {
		
		//board that the testing will be done on
		Board b = new Board(this.WIDTH, this.HEIGHT);
		
		//resets the b board
		equalizeBoards(b);
		
		//first, checks if placing a tile in a location will result in victory for the given state
		for (int i = 1; i <= WIDTH * HEIGHT; i++) {
			
			//resets the b board
			equalizeBoards(b);
			
			b.fillNthBlankWithState(i, state);
			
			if (b.isVictory(state)) {
				this.fillNthBlankWithState(i, state);
				return 1;
			}
			
		}
		
		//second, checks if the opponent placing a tile in a location will cause their victory, then places a tile in that location
		
		for (int i = 1; i <= WIDTH * HEIGHT; i++) {
			
			equalizeBoards(b);
			
			b.fillNthBlankWithState(i, enemystate);
			
			if (b.isVictory(enemystate)) {
				this.fillNthBlankWithState(i, state);
				return 2;
			}
			
		}
		//third, checks if there is only one available spot to place a tile
		if (numBlankSpaces() == 1) {
			this.fillNthBlankWithState(1, state);
			return 3;
		}
		return 0;
	}
	
	//returns the same value as chooseBestOption, but doesn't edit the board
	public int bestOption(int state, int enemystate) {
		
		Board b = new Board(WIDTH, HEIGHT);
		
		equalizeBoards(b);
		
		return b.chooseBestOption(state, enemystate);
		
	}
	
	/*
	 * The following is the optimal strategy for playing TicTacToe. The steps are checked from highest to lowest importance when deciding a move.
	 * 
	 * 1. Place a tile that results in a win
	 * 2. Place a tile that blocks an opponent from winning
	 * 3. Place a tile that results in a fork, which is a situation where the player has overlapping two-in-a-row tiles
	 * 4. Place a tile that prevents the opponent from getting a fork, without guaranteeing your own defeat
	 * 5. Place a tile in the center.
	 * 6. If the opponent placed a tile in one corner on the first turn, place a tile in the opposite corner
	 * 7. Place a tile in a corner
	 * 8. Place a tile in one of the four middle squares on the side
	 * 
	 */
	
	//makes a turn for the parameter state. unlike chooseBestOption, it checks all of the eight scenarios
	public void makeTurn(int state) {
		
		//the board that the testing will be done on
		Board b = new Board(WIDTH, HEIGHT);
		
		//makes the board a copy of the current board
		this.equalizeBoards(b);
		
		//determines the enemy state
		int enemystate;
		if (state == 2) {
			enemystate = 1;
		}
		else {
			enemystate = 2;
		}
		
		//fork index used for option 4 
		int enemyForkIndex = getForkIndex(enemystate, state);
		
		//covers optinos 1 and 2
		if (bestOption(state, enemystate) != 0) {
			chooseBestOption(state, enemystate);
//			System.out.println("\nOption 1 or 2\n");
			return;
		}
		//covers option 3
		else if (createFork(state, enemystate) ) {
//			System.out.println("\nOption 3\n");
			return;
		}
		//covers option 4
		else if (enemyForkIndex != -1) {
			b.fillNthBlankWithState(enemyForkIndex, state);
//			System.out.println(b.isGuaranteedWin(enemystate, state, true));
			if (!b.isGuaranteedWin(enemystate, state, true)) {
				this.fillNthBlankWithState(enemyForkIndex, state);
//				System.out.println("\nOption 4\n");
				return;
			}
			this.equalizeBoards(b);
		}
		//covers option 5
		else if (placeInCenter(state)) {
//			System.out.println("\nOption 5\n");
			return;
		}
		//covers option 6
		else if (placeInOppositeCorner(state, enemystate)) {
//			System.out.println("\nOption 6\n");
			return;
		}
		
		//covers option 7
		else if (placeInCorner(state, enemystate)) {
//			System.out.println("\nOption 7\n");
			return;
		}
		//covers option 8
		else if (placeInSide(state, enemystate)) {
//			System.out.println("\nOption 8\n");
			return;
		}
		
//		System.out.println("no options found");
		
	}
	
	//checks if the game is a guaranteed win for the parameter state versus the enemy state. stateTurnStart
	public boolean isGuaranteedWin(int state, int enemystate, boolean stateTurnStart) {
		
		Board b = new Board(WIDTH, HEIGHT);
		equalizeBoards(b);
		boolean stateturn = stateTurnStart;
		while (b.bestOption(state, enemystate) != 0 && !b.isVictory(state) && !b.isVictory(enemystate)) {
			
			if (stateturn) { 
				b.chooseBestOption(state, enemystate);
			}
			else { 
				b.chooseBestOption(enemystate, state);
			}
			stateturn = !stateturn;
		}
		
		return b.isVictory(state);
		
	}
	
	//attempts to create a fork, returns true if successful
	public boolean createFork(int state, int enemystate) {
		
		Board b = new Board(WIDTH, HEIGHT);
		int blanks = numBlankSpaces();
		
		for (int i = 0; i < blanks; i++) {
			equalizeBoards(b);
			b.fillNthBlankWithState(i + 1, state);
			if (b.isGuaranteedWin(state, enemystate, false)) {
				this.fillNthBlankWithState(i + 1, state);
				return true;
			}
		}		
		
		return false;
		
	}
	
	//returns the index of a potential fork for the given state, return -1 if none found
	public int getForkIndex(int state, int enemystate) {
		
		Board b = new Board(WIDTH, HEIGHT);
		int blanks = numBlankSpaces();
		
		for (int i = 0; i < blanks; i++) {
			equalizeBoards(b);
			b.fillNthBlankWithState(i + 1, state);
			if (b.isGuaranteedWin(state, enemystate, false)) {
				return i + 1;
			}
		}	
		
		return -1;
		
	}
	
	//places the given state in the center, returns false if it is filled
	public boolean placeInCenter(int state) {
		
		for (int r = 0; r < board.length; r++) {
			for (int c = 0; c < board[0].length; c++) {
				
				//checks for an unoccupied middle board location
				if ((r > 0 && c > 0)
					&&(r < board.length - 1) && (c < board[0].length - 1)
					&& (get(r,c) == EMPTY)) {
					set(r,c,state);
					return true;
				}
				
			}
		}
		
		return false;
		
	}
	
	//places a tile in the opposite corner from the opponent, returns true if successful
	public boolean placeInOppositeCorner(int state, int enemystate) {
		
		for (int r = 0; r < board.length; r++) {
			for (int c = 0; c < board[0].length; c++) {
				
				if ((r == 0 || r == (board.length - 1)) 
						&& (c == 0 || c == (board[0].length - 1))
						&& (get(r,c) == enemystate)) {
//					System.out.println(r + " " + c);
//					System.out.println((int)(2-r) + " " + (int)(2-c));
					if (get(2-r,2-c) == EMPTY) {
						set(2-r,2-c,state);
						return true;
					}
				}
				
			}
		}
		
		return false;
		
	}
	
	//places a tile in an available corner, unless if it results in a guaranteed defeat. returns true if successful
	public boolean placeInCorner(int state, int enemystate) {
		
		Board b = new Board(WIDTH, HEIGHT);
		this.equalizeBoards(b);
		
		for (int r = 0; r < board.length; r++) {
			for (int c = 0; c < board[0].length; c++) {
				
				if ((r == 0 || r == (board.length - 1)) 
						&& (c == 0 || c == (board[0].length - 1))
						&& (get(r,c) == EMPTY)) {
					
					b.set(r, c, state);
					if (b.isGuaranteedWin(enemystate, state, true)) {
						return false;
					}
					
					set(r,c,state);
					return true;
					
				}
				
			}
		}
		
		return false;
		
	}
	
	//places a tile in one of the side tiles, returns true if successful
	public boolean placeInSide(int state, int enemystate) {
		
		for (int r = 0; r < board.length; r++) {
			for (int c = 0; c < board[0].length; c++) {
				
				if (((r != 0 && r != board.length - 1 && (c == 0 || c == (board[0].length - 1))) 
				|| (c != 0 && c != board[0].length - 1 && (r == 0 || r == (board.length - 1))))
						&& (get(r,c) == EMPTY)) {
					
//					System.out.println(r + " " + c + " is a side");
					set(r,c,state);
					return true;
					
				}
				
			}
		}
		
		return false;
		
	}
	
	//sets the board in the parameter equal to the current board state
	public void equalizeBoards(Board b) {
		
		for (int r = 0; r < WIDTH; r++) {
			for (int c = 0; c < WIDTH; c++) {
				
				b.set(r, c, this.get(r, c));
				
			}
		}
		
	}
	
	//I was told to not put a comment here, so I deleted it. Oh wait...
	public String toString() {
		
		String s = "";
		for (int c = 0; c < HEIGHT; c++) {
			for (int r = 0; r < WIDTH; r++) {
				s += "" + get(r, c) + " ";
			}
			s += "\n";
		}
		return s;
		
	}
	
}
