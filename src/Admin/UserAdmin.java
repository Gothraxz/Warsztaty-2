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
			
			//test - obiekt ua w celu wywołania metod nie statycznych
			UserAdmin ua = new UserAdmin();
			ua.welcomeScreen(conn);
//			String[] test = ua.userDetails();
//			System.out.println("." + test[0]);
//			System.out.println("." + test[1]);
//			System.out.println("." + test[2]);
//			System.out.println("." + test[3]);
//			ua.add(conn); // wymagane poprawki
//			ua.edit(conn); // wymagane poprawki
//			ua.delete(conn); // działa
			
			// DO POPRAWY:
			// - komunikaty ze sprawdzania grup oraz użytkownika
			// - tworzenie użytkownika wymaga podania nr grupy 2x
			// - przy edycji, podanie nieistniejącej grupy zwraca nullPointerException
			// - stworzyć klasę do wczytania samego nr grupy
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void welcomeScreen (Connection conn) throws SQLException {
		User[] users = User.loadAllUsers(conn);
		
		for (int i = 0; i < users.length; i++) {
			System.out.println("Id: " + users[i].getId());
			System.out.println("Name: " + users[i].getUsername());
			System.out.println("E-mail: " + users[i].getEmail());
			System.out.println("Group id: " + users[i].getGroup().getId());
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
				System.out.println("(Id powinno być większe od 0)");
				temp = scanInt();
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
			System.out.println("Wprowadź poprawne id:");
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
		return filled;
	}
	
	private boolean groupExists(Connection conn, String groupId) throws SQLException {
		Group[] groups = Group.loadAllGroups(conn);
		for (int i = 0; i < groups.length; i++) {
			groupId.equals(groups[i].getId());
			return true;
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
			System.out.println("Liczba musi być większa od 0:");
			userID = scan.nextInt(); 
		} while (userID < 1);
		return userID;
	}
	
	private boolean userExists(Connection conn, int userId) throws SQLException {
		User[] users = User.loadAllUsers(conn);
		for (int i = 0; i < users.length; i++) {
			String.valueOf(userId).equals(users[i].getId());
			return true;
		}
		return false;
	}
	
	public void add(Connection conn) throws SQLException {
		String[] details = userDetails();
		
		while (!filled(details)) {
			details = userDetails();
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
			System.out.println("Podany użytkownik nie istnieje.");
			userID = userId();
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
			if (groupExists(conn, details[3])) {
				editedUser.setGroup(Group.loadById(conn, Integer.parseInt(details[3])));
			}
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
