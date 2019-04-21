package program;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import domain.AIma;
import domain.Action;
import domain.AlreadySignedException;
import domain.State;

public class Program {

	private static final long EASY = 100;
	private static final long NORMAL = 1000;
	private static final long HARD = 3000;

	private static void cleanScreen() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}

	public static void main(String[] args) {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		boolean wantToPlay = true;

		cleanScreen();

		do {
			boolean player;
			do {
				try {
					System.out.print("Do you want to be Player 1 or Player 2?\nType 1 or 2 to select player: ");
					int p = Integer.parseInt(in.readLine());
					if (p == 1 || p == 2) {
						player = p == 1;
						cleanScreen();
						break;
					}
					cleanScreen();
				} catch (IOException | NumberFormatException e) {
					cleanScreen();
				}
			} while (true);
			String difficulty = null;
			do {
				try {
					System.out.print("Select difficulty (E = easy, N = normal, H = hard): ");
					difficulty = in.readLine().toLowerCase().substring(0, 1);
					if (difficulty.startsWith("e") || difficulty.startsWith("n") || difficulty.startsWith("h")) {
						cleanScreen();
						break;
					}
					cleanScreen();
				} catch (IOException e) {
					cleanScreen();
				}
			} while (true);
			State s = new State();
			System.out.println(s.getTurn() ? "Player 1 turn:\n" : "Player 2 turn:\n");
			System.out.println(s);
			System.out.println();

			while (!s.gameEnded()) {
				if (s.getTurn() == player) {
					int row = -1;
					int col = -1;
					int q = 0;
					do {
						// Read the row
						do {
							try {
								System.out.print("Row (1 -> 6): ");
								String ans = in.readLine().toLowerCase();
								row = Integer.parseInt(ans) - 1;
								if (row >= 0 && row <= 5) {
									break;
								} else {
									cleanScreen();
									System.out.println(s.getTurn() ? "Player 1 turn:\n" : "Player 2 turn:\n");
									System.out.println(s);
									System.out.println();
								}
							} catch (Exception e) {
								cleanScreen();
								System.out.println(s.getTurn() ? "Player 1 turn:\n" : "Player 2 turn:\n");
								System.out.println(s);
								System.out.println();
							}
						} while (true);

						// Read the column
						do {
							try {
								System.out.print("Col (1 -> 6): ");
								String ans = in.readLine().toLowerCase();
								col = Integer.parseInt(ans) - 1;
								if (col >= 0 && col <= 5) {
									break;
								} else {
									cleanScreen();
									System.out.println(s.getTurn() ? "Player 1 turn:\n" : "Player 2 turn:\n");
									System.out.println(s);
									System.out.println();
								}
							} catch (Exception e) {
								cleanScreen();
								System.out.println(s.getTurn() ? "Player 1 turn:\n" : "Player 2 turn:\n");
								System.out.println(s);
								System.out.println();
							}
						} while (true);

						// Read the quantity
						do {
							try {
								System.out.print("Quantity (1 -> 6): ");
								String ans = in.readLine().toLowerCase();
								q = Integer.parseInt(ans);
								if (q >= 1 && q <= 6) {
									break;
								} else {
									cleanScreen();
									System.out.println(s.getTurn() ? "Player 1 turn:\n" : "Player 2 turn:\n");
									System.out.println(s);
									System.out.println();
								}
							} catch (Exception e) {
								cleanScreen();
								System.out.println(s.getTurn() ? "Player 1 turn:\n" : "Player 2 turn:\n");
								System.out.println(s);
								System.out.println();
							}
						} while (true);
						try {
							Action a = new Action(row, col, q);
							s = s.result(a);
							break;
						} catch (Exception e) {
							cleanScreen();
							System.out.println(s.getTurn() ? "Player 1 turn:\n" : "Player 2 turn:\n");
							System.out.println(s);
							System.out.println();
							System.out.println("Move not valid, do not cheat");
						}
					} while (true);
				} else {
					System.out.println("I'm thinking...");
					AIma ai = new AIma(s, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,
							(difficulty == "e") ? EASY : (difficulty == "n" ? NORMAL : HARD));
					Action a = ai.makeDecision(s);
					try {
						s = s.result(a);
					} catch (AlreadySignedException e) {
						System.out.println("Something went really wrong, oops");
						System.exit(1);
					}
				}
				cleanScreen();
				System.out.println(s.getTurn() ? "Player 1 turn:\n" : "Player 2 turn:\n");
				System.out.println(s);
				System.out.println();
			}
			// System.out.println(s.winner());
			if (s.getTurn() == player) {
				System.out.println("You won!\n");
			} else {
				System.out.println("You lose :(\n");
			}
			do {
				try {
					System.out.print("Do you want to play again (y = yes, n = no): ");
					String ans = in.readLine().toLowerCase();
					if (ans.startsWith("y") || ans.startsWith("n")) {
						if (ans.startsWith("n")) {
							wantToPlay = false;
						}
						cleanScreen();
						break;
					}
				} catch (Exception e) {
					cleanScreen();
				}
			} while (true);
		} while (wantToPlay);

	}
}
