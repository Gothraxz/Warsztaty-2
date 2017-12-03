package Zadania;

import java.sql.Connection;
import java.sql.SQLException;

import Models.Group;
import Models.User;

public class Main {

	public static void main(String[] args) {
		
		try (Connection conn = (new Connect()).getConnect()) {
			
			// tworzy tabelę
//			Group.createTable(conn);
			
			// towrzy nową grupę, ustawia nazwę i zapisuje w bazie danych
//			Group group = new Group();
//			group.setName("Nazwa2");
//			group.saveToDB(conn);
			
			// wczytuje grupę o podanym id
//			Group group = Group.loadById(conn, 1);
//			System.out.println(group.getName());
			
			// tworzy użytkownika w grupie i pokazuje hash hasła
//			Group group = Group.loadById(conn, 1);
//			User usr = new User("John", "j@j.tv", group, "test");
//			System.out.println(usr.getPassword());
			
			// tworzy tabelę Users
//			User.createTable(conn);
			
			// dodaje użytkownika do bazy
//			Group group = Group.loadById(conn, 1);
//			User usr = new User("John", "j@j.tv", group, "test");
//			usr.saveToDB(conn);
			
			// wczytuje email użytkownika o id 1
//			User usr = User.loadById(conn, 1);
//			User.loadById(conn, 1);
//			System.out.println(usr.getEmail());
//			System.out.println(usr.getUsername());
//			System.out.println(usr.getPassword());
			
			// czytuje wszystkich userów
//			User[] users = User.loadAllUsers(conn);
//			for (int i = 0; i < users.length; i++) {
//				System.out.println(users[i].getUsername());
//				System.out.println(users[i].getEmail());
//			}
			
			// tworzenie i edycja użytkownika
//			Group group = Group.loadById(conn, 1);
//			User usr = new User("John 2", "j@j3.tv", group, "test");
//			usr.saveToDB(conn);
//			Group group = Group.loadById(conn, 1);
//			usr.setEmail("t@t.pl");
//			usr.saveToDB(conn);
			
			// usuwanie użytkownika
//			Group group = Group.loadById(conn, 1);
//			User usr = User.loadById(conn, 3);
//			usr.delete(conn);
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

}
