package Admin;

import java.awt.List;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import Models.Group;
import Models.User;
import Zadania.Connect;

public class UserAdmin {
	
	public static void main(String[] args) {
		try (Connection conn = (new Connect()).getConnect()) {
			
			//obiekt ua w celu wywołania metod nie statycznych
			UserAdmin ua = new UserAdmin();
			Scanner scan = new Scanner(System.in);
			
			System.out.println("Lista istniejących użytkowników:\n");
			
			ua.welcomeScreen(conn);
			
			System.out.println("\n"
					+ " - - - Zarządzanie użytkownikami - - -"
					+ "\n"
					+ "\n"
					+ "Dostępne komendy:\n"
					+ "add - tworzenie nowego użytkownika\n"
					+ "edit - edycja istniejącego użytkownika\n"
					+ "delete - usunięcie użytkownika\n"
					+ "quit - zakończenie działania programu");
			
			String command = "";
			
			do {
				command = scan.nextLine();
				if (command.equalsIgnoreCase("add")) {
					System.out.println("\nTworzenie użytkownika:\n");
					ua.add(conn);
					System.out.println("\nKomenda wykonana, wpisz kolejne polecenie:\n");
				} else if (command.equalsIgnoreCase("edit")) {
					System.out.println("\nEdycja użytkownika:\n");
					ua.edit(conn);
					System.out.println("\nKomenda wykonana, wpisz kolejne polecenie:\n");
				} else if (command.equalsIgnoreCase("delete")) {
					System.out.println("\nUsunięcie użytkownika:\n");
					ua.delete(conn);
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
		User[] users = User.loadAllUsers(conn);
		
		for (int i = 0; i < users.length; i++) {
			System.out.println("Id: " + users[i].getId());
			System.out.println("Nazwa: " + users[i].getUsername());
			System.out.println("E-mail: " + users[i].getEmail());
			System.out.println("Id grupy: " + users[i].getGroup().getId());
			System.out.println("- - - - - - -");
		}
	}
	
	private String[] userDetails() {
		Scanner scan = new Scanner(System.in);
		String[] userDetails = new String[4];
		
		System.out.println("Wprowadź nazwę użytkownika:");
		userDetails[0] = scan.nextLine();
		System.out.println("Wprowadź e-mail:");
		userDetails[1] = scan.nextLine();
		System.out.println("Wprowadź hasło:");
		userDetails[2] = scan.nextLine();
		System.out.println("Wprowadź id grupy:");
		String temp = scanInt();
		do {
			if (temp.isEmpty()) {
				break;
			} else {
				if (Integer.valueOf(temp) < 1) {
					System.out.println("(Id powinno być większe od 0)");
					temp = scanInt();
				}
			}
		} while (Integer.parseInt(temp) < 1);
		userDetails[3] = String.valueOf(temp);
		return userDetails;
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
	
	private int groupId() {
		Scanner scan = new Scanner(System.in);

		System.out.println("Wprowadź id grupy:");
		int groupID;
		do {
			while (!scan.hasNextInt()) {
				System.out.println("Wprowadź liczbę większą od 0:");
				scan.next();
			}
			groupID = scan.nextInt();
			if (groupID < 1) {
				System.out.println("Liczba musi być większa od 0:");
			}
		} while (groupID < 1);
		return groupID;
	}
	
	private boolean groupExists(Connection conn, int groupId) throws SQLException {
		Group[] groups = Group.loadAllGroups(conn);
		for (int i = 0; i < groups.length; i++) {
			if (groupId == groups[i].getId()) {
				return true;
			}
		}
		return false;
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
		String[] details = userDetails();
		
		while (!filled(details)) {
			details = userDetails();
		}
		
		while (!groupExists(conn, Integer.valueOf(details[3]))) {
			System.out.println("Grupa o podanym id nie istnieje.");
			details[3] = String.valueOf(groupId());
		}
		
		User newUser = new User(details[0], 
				details[1], 
				Group.loadById(conn, Integer.parseInt(details[3])), 
				details[2]);
		
		newUser.saveToDB(conn);
	}
	
	public void edit(Connection conn) throws SQLException {
		int userID = userId();

		if (!userExists(conn, userID)) {
			while (!userExists(conn, userID)) {
				System.out.println("Podany użytkownik nie istnieje.");
				userID = userId();
			}
		}

		User editedUser = User.loadById(conn, userID);
		
		String[] details = userDetails();
		
		if (!details[0].isEmpty()) {
			editedUser.setUsername(details[0]);
		}
		if (!details[1].isEmpty()) {
			editedUser.setEmail(details[1]);
		}
		if (!details[2].isEmpty()) {
			editedUser.setPassword(details[2]);
		}
		if (!details[3].isEmpty()) {
			while (!groupExists(conn, Integer.valueOf(details[3]))) {
				System.out.println("Grupa o podanym id nie istnieje.");
				details[3] = String.valueOf(groupId());
			}
			editedUser.setGroup(Group.loadById(conn, Integer.valueOf(details[3])));
		}
		
		editedUser.saveToDB(conn);
	}
	
	public void delete(Connection conn) throws SQLException {
		int userID = userId();

		if (!userExists(conn, userID)) {
			System.out.println("Podany użytkownik nie istnieje.");
			userID = userId();
		}
		
		User deletedUser = User.loadById(conn, userID);
		
		deletedUser.delete(conn);
	}
	
}
