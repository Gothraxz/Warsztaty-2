package Admin;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import Models.Exercise;
import Models.Group;
import Models.User;
import Zadania.Connect;

public class GroupAdmin {

	public static void main(String[] args) {
		try (Connection conn = (new Connect()).getConnect()) {
			
			//obiekt ga w celu wywołania metod nie statycznych
			GroupAdmin ga = new GroupAdmin();
			Scanner scan = new Scanner(System.in);
			
			System.out.println("Lista istniejących grup:\n");
			
			ga.welcomeScreen(conn);
			
			System.out.println("\n"
					+ " - - - Zarządzanie grupami - - -"
					+ "\n"
					+ "\n"
					+ "Dostępne komendy:\n"
					+ "add - tworzenie nowej grupy\n"
					+ "edit - edycja istniejącej grupy\n"
					+ "delete - usunięcie grupy\n"
					+ "quit - zakończenie działania programu");
			
			String command = "";
			
			do {
				command = scan.nextLine();
				if (command.equalsIgnoreCase("add")) {
					System.out.println("\nTworzenie grupy:\n");
					ga.add(conn);
					System.out.println("\nKomenda wykonana, wpisz kolejne polecenie:\n");
				} else if (command.equalsIgnoreCase("edit")) {
					System.out.println("\nEdycja grupy:\n");
					ga.edit(conn);
					System.out.println("\nKomenda wykonana, wpisz kolejne polecenie:\n");
				} else if (command.equalsIgnoreCase("delete")) {
					System.out.println("\nUsunięcie grupy:\n");
					ga.delete(conn);
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
		Group[] groups = Group.loadAllGroups(conn);
		
		for (int i = 0; i < groups.length; i++) {
			System.out.println("Id: " + groups[i].getId());
			System.out.println("Nazwa: " + groups[i].getName());
			System.out.println("- - - - - - -");
		}
	}
	
	private String[] groupDetails() {
		Scanner scan = new Scanner(System.in);
		String[] groupDetails = new String[1];
		
		System.out.println("Wprowadź nazwę grupy:");
		groupDetails[0] = scan.nextLine();
		
		return groupDetails;
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
	
	public void add(Connection conn) throws SQLException {
		String[] details = groupDetails();
		
		while (!filled(details)) {
			details = groupDetails();
		}
		
		
		Group newGroup = new Group(details[0]);
		
		newGroup.saveToDB(conn);
	}
	
	public void edit(Connection conn) throws SQLException {
		int groupID = groupId();

		if (!groupExists(conn, groupID)) {
			while (!groupExists(conn, groupID)) {
				System.out.println("Podana grupa nie istnieje.");
				groupID = groupId();
			}
		}

		Group editedGroup = Group.loadById(conn, groupID);
		
		String[] details = groupDetails();
		
		if (!details[0].isEmpty()) {
			editedGroup.setName(details[0]);
		}
		
		editedGroup.saveToDB(conn);
	}
	
	public void delete(Connection conn) throws SQLException {
		int groupID = groupId();

		if (!groupExists(conn, groupID)) {
			System.out.println("Podana grupa nie istnieje.");
			groupID = groupId();
		}
		
		Group deletedGroup = Group.loadById(conn, groupID);
		
		deletedGroup.delete(conn);
	}
	
}
