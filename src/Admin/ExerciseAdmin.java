package Admin;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import Models.Exercise;
import Models.Group;
import Models.User;
import Zadania.Connect;

public class ExerciseAdmin {

	public static void main(String[] args) {
		try (Connection conn = (new Connect()).getConnect()) {
			
			//obiekt ea w celu wywołania metod nie statycznych
			ExerciseAdmin ea = new ExerciseAdmin();
			Scanner scan = new Scanner(System.in);
			
			System.out.println("Lista istniejących ćwiczeń:\n");
			
			ea.welcomeScreen(conn);
			
			System.out.println("\n"
					+ " - - - Zarządzanie ćwiczeniami - - -"
					+ "\n"
					+ "\n"
					+ "Dostępne komendy:\n"
					+ "add - tworzenie nowego ćwiczenia\n"
					+ "edit - edycja istniejącego ćwiczenia\n"
					+ "delete - usunięcie ćwiczenia\n"
					+ "quit - zakończenie działania programu");
			
			String command = "";
			
			do {
				command = scan.nextLine();
				if (command.equalsIgnoreCase("add")) {
					System.out.println("\nTworzenie ćwiczenia:\n");
					ea.add(conn);
					System.out.println("\nKomenda wykonana, wpisz kolejne polecenie:\n");
				} else if (command.equalsIgnoreCase("edit")) {
					System.out.println("\nEdycja ćwiczenia:\n");
					ea.edit(conn);
					System.out.println("\nKomenda wykonana, wpisz kolejne polecenie:\n");
				} else if (command.equalsIgnoreCase("delete")) {
					System.out.println("\nUsunięcie ćwiczenia:\n");
					ea.delete(conn);
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

	public void welcomeScreen (Connection conn) throws SQLException {
		Exercise[] exercises = Exercise.loadAllExercises(conn);
		
		for (int i = 0; i < exercises.length; i++) {
			System.out.println("Id: " + exercises[i].getId());
			System.out.println("Tytuł: " + exercises[i].getTitle());
			System.out.println("Opis: " + exercises[i].getDescription());
			System.out.println("- - - - - - -");
		}
	}
	
	private String[] exerciseDetails() {
		Scanner scan = new Scanner(System.in);
		String[] exerciseDetails = new String[2];
		
		System.out.println("Wprowadź tytuł ćwiczenia:");
		exerciseDetails[0] = scan.nextLine();
		System.out.println("Wprowadź opis ćwiczenia:");
		exerciseDetails[1] = scan.nextLine();
		
		return exerciseDetails;
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
		String[] details = exerciseDetails();
		
		while (!filled(details)) {
			details = exerciseDetails();
		}
		
		
		Exercise newExercise = new Exercise(details[0], details[1]);
		
		newExercise.saveToDB(conn);
	}
	
	public void edit(Connection conn) throws SQLException {
		int exerciseID = exerciseId();

		if (!exerciseExists(conn, exerciseID)) {
			while (!exerciseExists(conn, exerciseID)) {
				System.out.println("Podane ćwiczenie nie istnieje.");
				exerciseID = exerciseId();
			}
		}

		Exercise editedExercise = Exercise.loadById(conn, exerciseID);
		
		String[] details = exerciseDetails();
		
		if (!details[0].isEmpty()) {
			editedExercise.setTitle(details[0]);
		}
		if (!details[1].isEmpty()) {
			editedExercise.setDescription(details[1]);
		}
		
		editedExercise.saveToDB(conn);
	}
	
	public void delete(Connection conn) throws SQLException {
		int exerciseID = exerciseId();

		if (!exerciseExists(conn, exerciseID)) {
			System.out.println("Podane ćwiczenie nie istnieje.");
			exerciseID = exerciseId();
		}
		
		Exercise deletedExercise = Exercise.loadById(conn, exerciseID);
		
		deletedExercise.delete(conn);
	}
	
}
