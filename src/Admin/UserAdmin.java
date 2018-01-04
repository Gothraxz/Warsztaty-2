package Admin;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import Models.Group;
import Models.User;
import Zadania.Connect;

public class UserAdmin {
	
//	public static void main(String[] args) {
//		try (Connection conn = (new Connect()).getConnect()) {
//			
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
	
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
		
		System.out.println("Wprowadź nazwę użytkownika");
		userDetails[0] = scan.nextLine();
		System.out.println("Wprowadź e-mail:");
		userDetails[1] = scan.nextLine();
		System.out.println("Wprowadź hasło:");
		userDetails[2] = scan.nextLine();
		System.out.println("Wprowadź id grupy:");
		userDetails[3] = scan.nextLine();
		// dopisać zabezpieczenie wprwadzania tylko liczb dodatnich
		return userDetails;
	}
	
	public void add(Connection conn) throws SQLException {
		String[] details = userDetails();
		
		User newUser = new User(details[0], 
				details[1], 
				Group.loadById(conn, Integer.parseInt(details[3])), 
				details[2]);
		
		newUser.saveToDB(conn);
	}
	
	
	
}
