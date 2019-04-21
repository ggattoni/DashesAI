package domain;

import java.util.ArrayList;
import java.util.List;

public class State {

	private String[] board;
	private boolean turn; // Player 1:: true; Player 2:: false;

	public State() {
		this.board = new String[6];

		this.board[0] = "     |     ";
		this.board[1] = "    | |    ";
		this.board[2] = "   | | |   ";
		this.board[3] = "  | | | |  ";
		this.board[4] = " | | | | | ";
		this.board[5] = "| | | | | |";

		this.turn = true;
	}

	public State(String[] board, boolean turn) {
		this.board = new String[6];
		for (int i = 0; i < board.length; i++) {
			this.board[i] = board[i];
		}
		this.turn = turn;
	}
	
	public boolean getTurn() {
		return this.turn;
	}

	private boolean isSigned(int row, int col) {
		int c = 0;
		String r = this.board[row];
		for (int i = 0; i < r.length(); i++) {
			if (r.charAt(i) == '|') {
				if (c == col) {
					return false;
				}
				c++;
			} else if (r.charAt(i) == '+') {
				if (c == col) {
					return true;
				}
				c++;
			}
		}
		return false;
	}
	
	public boolean isPossible(int row, int col, int q) {
		for (int i = col; i < col + q; i++) {
			if (isSigned(row, i)) {
				return false;
			}
		}
		return true;
	}

	public State result(Action a) throws AlreadySignedException {
		for (int i = a.getCol(); i < a.getCol() + a.getQuantity(); i++) {
			if (this.isSigned(a.getRow(), i)) {
				throw new AlreadySignedException("You're trying to delete dashes already signed");
			}
		}
		String oldRow = this.board[a.getRow()];
		StringBuilder newRow = new StringBuilder();
		int col = 0;
		boolean working = false;
		for (int i = 0; i < oldRow.length(); i++) {
			// Se nella vecchia riga c'è uno spazio e non sto segnando
			if (oldRow.charAt(i) == ' ' && !working) {
				if ((col == a.getCol() && isSigned(a.getRow(), a.getCol() - 1))
						|| col == a.getCol() + a.getQuantity() && isSigned(a.getRow(), a.getCol() + a.getQuantity())) {
					newRow.append('-');
				} else {
					newRow.append(' ');
				}
			}
			// Se nella vecchia riga c'è uno spazio e sto segnando
			else if (oldRow.charAt(i) == ' ' && working) {
				newRow.append('-');
			}
			// Se nella vecchia riga c'è un trattino e non sto segnando
			else if (oldRow.charAt(i) == '|' && !working) {
				// Se sono arrivato alla colonna
				if (col == a.getCol()) {
					newRow.append('+');
					if (a.getQuantity() > 1) {
						working = !working;
					}
				} else {
					newRow.append('|');
				}
				col++;
			}
			// Se nella vecchia riga c'è un trattino e sto segnando
			else if (oldRow.charAt(i) == '|' && working) {
				// Se sono arrivato all'ultima colonna
				if (col == a.getCol() + a.getQuantity() - 1) {
					working = !working;
				}
				newRow.append('+');
				col++;
			}
			// Se nella vecchia riga c'è un trattino segnato
			else if (oldRow.charAt(i) == '+') {
				newRow.append('+');
				col++;
			}
			// Se nella vecchia riga c'è un segno
			else if (oldRow.charAt(i) == '-') {
				newRow.append('-');
			}
		}
		String[] newBoard = new String[6];
		for (int i = 0; i < newBoard.length; i++) {
			if (i == a.getRow()) {
				newBoard[i] = newRow.toString();
			} else {
				newBoard[i] = this.board[i];
			}
		}

		return new State(newBoard, !this.turn);
	}

//	public List<Action> actions() {
//		ArrayList<Action> result = new ArrayList<>();
//
//		for (int row = 0; row < this.board.length; row++) {
//			for (int col = 0; col <= row; col++) {
//				for (int quantity = 1; quantity <= row + 1 - (col); quantity++) {
//					try {
//						Action a = new Action(row, col, quantity);
//						System.out.println(result(a));
//						result.add(a);
//					} catch (AlreadySignedException e) {
//					}
//				}
//			}
//		}
//
//		return result;
//	}

	public List<Action> actions() throws AlreadySignedException {
		ArrayList<Action> result = new ArrayList<>();
		
		for (int row = 0; row < this.board.length; row++) {
			for (int col = 0; col <= row; col++) {
				for (int q = 1; q <= row + 1 - col; q++) {
					if (isPossible(row, col, q)) {
//						System.out.println(result(new Action(row, col, q)));
						result.add(new Action(row, col, q));
					}
				}
			}
		}
		
		return result;
	}
	
	public boolean gameEnded() {
		for (int i = 0; i < this.board.length; i++) {
			for (int j = 0; j < i + 1; j++) {
				if (!isSigned(i, j)) {
					return false;
				}
			}
		}
		return true;
	}

	public String winner() {
		if (gameEnded()) {
			return "Player " + (turn ? "1" : "2") + " won";
		} else {
			return "Game not ended yet";
		}
	}
	
	public boolean allLineSigned(int row) {
		for (int col = 0; col <= row; col++) {
			if (!isSigned(row, col))
				return false;
		}		
		return true;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < this.board.length; i++) {
			result.append(this.board[i] + "\n");
		}
		return result.toString();
	}

	public static void main(String[] args) throws AlreadySignedException {
		State s = new State();
		System.out.println(s);
		System.out.println("Mosse possibili: " + s.actions().size());
		System.out.println();
		
		Action a = new Action(1, 1, 1);
		s = s.result(a);
		System.out.println(s);
		System.out.println(s.isSigned(1, 1));

//		Action a = new Action(5, 1, 4);
//		s = s.result(a);
//		System.out.println(s);
//		System.out.println("Mosse possibili: " + s.actions().size());
//		System.out.println();
//
//		a = new Action(0, 0, 1);
//		s = s.result(a);
//		System.out.println(s);
//		System.out.println("Mosse possibili: " + s.actions().size());
//		System.out.println();
//
//		a = new Action(3, 1, 1);
//		s = s.result(a);
//		System.out.println(s);
//		System.out.println("Mosse possibili: " + s.actions().size());
//		System.out.println();
//
//		a = new Action(3, 0, 1);
//		s = s.result(a);
//		System.out.println(s);
//		System.out.println("Mosse possibili: " + s.actions().size());
//		System.out.println();
//
//		a = new Action(3, 2, 2);
//		s = s.result(a);
//		System.out.println(s);
//		System.out.println("Mosse possibili: " + s.actions().size());
//		System.out.println();
//
//		a = new Action(5, 0, 1);
//		s = s.result(a);
//		System.out.println(s);
//		System.out.println("Mosse possibili: " + s.actions().size());
//		System.out.println();
//
//		a = new Action(4, 0, 5);
//		s = s.result(a);
//		System.out.println(s);
//		System.out.println("Mosse possibili: " + s.actions().size());
//		System.out.println();
//
//		a = new Action(5, 5, 1);
//		s = s.result(a);
//		System.out.println(s);
//		System.out.println("Mosse possibili: " + s.actions().size());
//		System.out.println();
//
//		a = new Action(2, 2, 1);
//		s = s.result(a);
//		System.out.println(s);
//		System.out.println("Mosse possibili: " + s.actions().size());
//		System.out.println();
//
//		a = new Action(1, 0, 2);
//		s = s.result(a);
//		System.out.println(s);
//		System.out.println("Mosse possibili: " + s.actions().size());
//		System.out.println();
//
//		a = new Action(2, 0, 2);
//		s = s.result(a);
//		System.out.println(s);
//		System.out.println("Mosse possibili: " + s.actions().size());
//		System.out.println(s.winner());
//		System.out.println();
	}
}
