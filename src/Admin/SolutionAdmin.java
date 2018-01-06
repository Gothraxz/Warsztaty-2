package Admin;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import Models.Exercise;
import Models.Group;
import Models.Solution;
import Models.User;
import Zadania.Connect;

public class SolutionAdmin {

	public static void main(String[] args) {
		try (Connection conn = (new Connect()).getConnect()) {
			
			// DO POPRAWY:
			// - view wyświetla 0 zamiast kolejnych ID
			
			
			//obiekt ta w celu wywołania metod nie statycznych
			SolutionAdmin sa = new SolutionAdmin();
			Scanner scan = new Scanner(System.in);
			
			System.out.println("\n"
					+ " - - - Przypisywanie zadań - - -"
					+ "\n"
					+ "\n"
					+ "Dostępne komendy:\n"
					+ "add - przypisanie zadania do użytkownika\n"
					+ "view - przeglądanie rozwiązań użytkownika\n"
					+ "quit - zakończenie działania programu");
			
			String command = "";
			
			do {
				command = scan.nextLine();
				if (command.equalsIgnoreCase("add")) {
					System.out.println("\nPrzypisanie zadania:\n");
					sa.add(conn);
					System.out.println("\nKomenda wykonana, wpisz kolejne polecenie:\n");
				} else if (command.equalsIgnoreCase("view")) {
					System.out.println("\nPrzeglądanie rozwiązań:\n");
					sa.view(conn);
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
		String[] solutionDetails = new String[2];
		
		System.out.println("Wprowadź id użytkownika:");
		String tempUser = scanInt();
		do {
			if (tempUser.isEmpty()) {
				break;
			} else {
				if (Integer.valueOf(tempUser) < 1) {
					System.out.println("(Id powinno być większe od 0)");
					tempUser = scanInt();
				}
			}
		} while (Integer.parseInt(tempUser) < 1);
		solutionDetails[0] = tempUser;
		
		System.out.println("Wprowadź id zadania:");
		String tempExercise = scanInt();
		do {
			if (tempExercise.isEmpty()) {
				break;
			} else {
				if (Integer.valueOf(tempExercise) < 1) {
					System.out.println("(Id powinno być większe od 0)");
					tempExercise = scanInt();
				}
			}
		} while (Integer.parseInt(tempExercise) < 1);
		solutionDetails[1] = tempExercise;
		
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

	private int exerciseId() {
		Scanner scan = new Scanner(System.in);

		System.out.println("Wprowadź id ćwiczenia:");
		int exerciseID;
		do {
			while (!scan.hasNextInt()) {
				System.out.println("Wprowadź liczbę większą od 0:");
				scan.next();
			}
			exerciseID = scan.nextInt();
			if (exerciseID < 1) {
				System.out.println("Liczba musi być większa od 0:");
			}
		} while (exerciseID < 1);
		return exerciseID;
	}
	
	private boolean exerciseExists(Connection conn, int exerciseId) throws SQLException {
		Exercise[] exercises = Exercise.loadAllExercises(conn);
		for (int i = 0; i < exercises.length; i++) {
			if (exerciseId == exercises[i].getId()) {
				return true;
			}
		}
		return false;
	}
	
	public void add(Connection conn) throws SQLException {
		
		System.out.println("Lista dostępnych użytkowników:");
		User[] users = User.loadAllUsers(conn);
		for (User user : users) {
			System.out.print(user.getId() + ".: " + user.getUsername() + ", ");
		}
		System.out.println("\n\nLista dostępnych zadań:");
		Exercise[] exercises = Exercise.loadAllExercises(conn);
		for (Exercise exercise : exercises) {
			System.out.print(exercise.getId() + ".: " + exercise.getTitle() + ", ");
		}
		
		System.out.println("\n");
		
		String[] details = solutionDetails();
		
		while (!filled(details)) {
			details = solutionDetails();
		}
		
		int userID = Integer.parseInt(details[0]);
		int exerciseID = Integer.parseInt(details[1]);
		
		if (!userExists(conn, userID)) {
			while (!userExists(conn, userID)) {
				System.out.println("Podany użytkownik nie istnieje.");
				userID = userId();
			}
		}
		
		if (!exerciseExists(conn, exerciseID)) {
			while (!exerciseExists(conn, exerciseID)) {
				System.out.println("Podane ćwiczenie nie istnieje.");
				exerciseID = exerciseId();
			}
		}
		
		User user = User.loadById(conn, userID);
		Exercise exercise = Exercise.loadById(conn, exerciseID);
		
		Solution newSolution = new Solution("", 
				Exercise.loadById(conn, exerciseID), 
				User.loadById(conn, userID));
		
		newSolution.saveToDB(conn);
	}
	
	public void view(Connection conn) throws SQLException {
		
		int userID = userId();
		
		if (!userExists(conn, userID)) {
			while (!userExists(conn, userID)) {
				System.out.println("Podany użytkownik nie istnieje.");
				userID = userId();
			}
		}
		
		Solution[] solutions = Exercise.loadAllByUserId(conn, userID);
		
		System.out.println("Lista rozwiązań dla użytkownika o id " + userID + ":\n");
		for (Solution solution : solutions) {
			System.out.println(solution.getId() + ". - " 
					+ solution.getCreated() + ",\n" 
					+ solution.getUpdated() + ":\n" 
					+ solution.getDescription()
					+ "\n- - - - - - - - - - - - -");
		}
		
	}
	
}
