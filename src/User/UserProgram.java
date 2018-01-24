package User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Models.Exercise;
import Models.Solution;
import Models.User;
import Zadania.Connect;

public class UserProgram {

	private static int userID; 
	
	public static void main(String[] args) {
		try (Connection conn = (new Connect()).getConnect()) {
	
			//obiekt up w celu wywołania metod nie statycznych
			UserProgram up = new UserProgram();
			Scanner scan = new Scanner(System.in);

			System.out.println("Zaloguj się podając ID:\n");
			userID = up.userId();
			
			if (!up.userExists(conn, userID)) {
				while (!up.userExists(conn, userID)) {
					System.out.println("Podany użytkownik nie istnieje.");
					userID = up.userId();
				}
			}
			
			System.out.println("\n"
					+ " - - - Program użytkownika - - -"
					+ "\n"
					+ "\n"
					+ "Dostępne komendy:\n"
					+ "add - utworzenie rozwiązania do zadania\n"
					+ "view - przeglądanie rozwiązań użytkownika\n"
					+ "quit - zakończenie działania programu");

			String command = "";

			do {
				command = scan.nextLine();
				if (command.equalsIgnoreCase("add")) {
					System.out.println("\nUtworzenie rozwiązania:\n");
					up.add(conn);
					System.out.println("\nKomenda wykonana, wpisz kolejne polecenie:\n");
				} else if (command.equalsIgnoreCase("view")) {
					System.out.println("\nPrzeglądanie rozwiązań:\n");
					up.view(conn);
					System.out.println("\nKomenda wykonana, wpisz kolejne polecenie:\n");
				} else if (command.equalsIgnoreCase("quit")) {
					System.out.println("\n- - - - - - - - - - - - - - -"
							+ "\nZakończono działanie programu"
							+ "\n- - - - - - - - - - - - - - -");
				} else {
					System.out.println("Nie rozpoznano komendy.");
				}
			} while (!command.equalsIgnoreCase("quit"));

		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	private String scanInt() {
		Scanner scan = new Scanner(System.in);
		String tmp = "";
		do {
			tmp = scan.nextLine();
			if (tmp.isEmpty()) {
				break;
			}
			if (!tmp.matches("-?\\d+(\\.\\d+)?")) {
				System.out.println("Wprowadź poprawne id:");
			}
		} while (!tmp.matches("-?\\d+(\\.\\d+)?"));
		return tmp;
	}
	
	private boolean filled(String[] userForm) {
		boolean filled = true;
		
		for (int i = 0; i < userForm.length; i++) {
			if (userForm[i].isEmpty()) {
				filled = false;
			}
		}
		
		if (filled == false) {
			System.out.println("\nNie wprowadzono wszystkich wymaganych danych!"
					+ "\nWprowadź ponownie dane:\n");
		}
		
		return filled;
	}
	
	private String[] solutionDetails() {
		Scanner scan = new Scanner(System.in);
		String[] solutionDetails = new String[1];
		
		System.out.println("Wprowadź rozwiązanie:");
		solutionDetails[0] = scan.nextLine();
		return solutionDetails;
	}
	
	private int userId() {
		Scanner scan = new Scanner(System.in);

		System.out.println("Wprowadź id użytkownika:");
		int userID;
		do {
			while (!scan.hasNextInt()) {
				System.out.println("Wprowadź liczbę większą od 0:");
				scan.next();
			}
			userID = scan.nextInt();
			if (userID < 1) {
				System.out.println("Liczba musi być większa od 0:");
			}
		} while (userID < 1);
		return userID;
	}
	
	private boolean userExists(Connection conn, int userId) throws SQLException {
		User[] users = User.loadAllUsers(conn);
		for (int i = 0; i < users.length; i++) {
			if (userId == users[i].getId()) {
				return true;
			}
		}
		return false;
	}
	
public void add(Connection conn) throws SQLException {
		
		Solution[] userSolutions = Exercise.loadAllByUserId(conn, userID);
		List<Integer> missingSolutions = new ArrayList<>();
		
		for (Solution solution : userSolutions) {
			if (solution.getDescription() == null ||
					solution.getDescription().isEmpty() || 
					solution.getUpdated() == null ||
					solution.getUpdated().isEmpty()) {
				missingSolutions.add(solution.getId());
			}
		}
		
		System.out.println("Zadania bez przypisanego rozwiązania:\n");
		for (int i = 0; i < missingSolutions.size(); i++) {
			System.out.print(missingSolutions.get(i) + ", ");
		}
		
		System.out.println("\nWprowadź id do którego chcesz przypisać rozwiązanie:\n");
		String solutionId = scanInt();
		
		boolean idOnList = false;
		
		do {
			for (int i = 0; i < missingSolutions.size(); i++) {
				if (missingSolutions.get(i) == Integer.parseInt(solutionId)) {
					idOnList = true;
					break;
				}
			}
			if (idOnList == false) {
				System.out.println("Wprowadziłeś id spoza zakresu!");
				solutionId = scanInt();
			}
			
		} while (idOnList == false);
		
		
		String[] details = solutionDetails();

		while (!filled(details)) {
			details = solutionDetails();
		}
		
		Solution updatedSolution = Solution.loadById(conn, Integer.parseInt(solutionId));
		updatedSolution.setDescription(details[0]);
		
		updatedSolution.saveToDB(conn);
	}
	
	public void view(Connection conn) throws SQLException {
		
		Solution[] solutions = Exercise.loadAllByUserId(conn, userID);
		
		System.out.println("Lista rozwiązań:\n");
		for (Solution solution : solutions) {
			System.out.println(solution.getId() + ". - " 
					+ solution.getCreated() + ",\n" 
					+ solution.getUpdated() + ":\n" 
					+ solution.getDescription()
					+ "\n- - - - - - - - - - - - -");
		}
		
	}
// done
}
