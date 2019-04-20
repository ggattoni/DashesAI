package domain;

public class Action {

	private int row;
	private int col;
	private int quantity;
	
	public Action(int row, int col, int quantity) {
		if (row == 0 && (col != 0 || quantity != 1) ||
				row == 1 && ((col < 0 && col > 1) || (quantity < 1 && quantity > 2)) ||
				row == 2 && ((col < 0 && col > 2) || (quantity < 1 && quantity > 3)) ||
				row == 3 && ((col < 0 && col > 3) || (quantity < 1 && quantity > 4)) ||
				row == 4 && ((col < 0 && col > 4) || (quantity < 1 && quantity > 5)) ||
				row == 5 && ((col < 0 && col > 5) || (quantity < 1 && quantity > 6))) {
			throw new IllegalArgumentException("Mossa non valida");
		}
		this.row = row;
		this.col = col;
		this.quantity = quantity;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public int getQuantity() {
		return quantity;
	}

}
