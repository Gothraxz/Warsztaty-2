package Zadania;

import java.sql.Connection;
import java.sql.SQLException;

import Models.Exercise;
import Models.Group;
import Models.Solution;
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
			
			// wczytuje wszystkich userów
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
			
			// tworzenie i usuwanie grupy
//			Group group1 = Group.loadById(conn, 3);
//			group1.saveToDB(conn);
//			group1.delete(conn);
//			Group[] groups = Group.loadAllGroups(conn);
//			for (int i = 0; i < groups.length; i++) {
//				System.out.println(groups[i].getId());
//				System.out.println(groups[i].getName());
//			}
			
			// tworzy tabelę Exercise oraz dodaje obiekt
//			Exercise.createTable(conn);
//			Exercise exercise1 = new Exercise("Title", "Description");
//			Exercise exercise2 = new Exercise("Title 2");
//			exercise1.saveToDB(conn);
//			exercise2.saveToDB(conn);
			
			// wczytuje ćwiczenie, listę ćwiczeń oraz usuwa
//			Exercise exercise1 = Exercise.loadById(conn, 2);
//			System.out.println(exercise1.getTitle());
//			exercise1.delete(conn);
//			Exercise[] exercises = Exercise.loadAllExercises(conn);
//			for (int i = 0; i < exercises.length; i++) {
//				System.out.println(exercises[i].getTitle());
//				System.out.println(exercises[i].getDescription());
//			}
			
			// tworzenie tabeli rozwiązań
//			Solution.createTable(conn);	
			
			// tworzenie nowych rozwiązań
//			User user1 = User.loadById(conn, 1);
//			User user4 = User.loadById(conn, 4);
//			Exercise exercise1 = Exercise.loadById(conn, 1);
//			Solution solution1 = new Solution("Opis", exercise1, user1);
//			Solution solution2 = new Solution("Opis2", exercise1, user4);
//			solution1.saveToDB(conn);
//			solution2.saveToDB(conn);
			
			// edycja, wczytywanie oraz usuwanie
//			Solution solution1 = Solution.loadById(conn, 1);
//			Solution solution2 = Solution.loadById(conn, 2);
//			solution1.setDescription("Zmieniony opis");
//			solution1.saveToDB(conn);
//			Solution[] solutions = Solution.loadAllSolutions(conn);
//			for (int i = 0; i < solutions.length; i++) {
//				System.out.println(solutions[i].getDescription());
//				System.out.println(solutions[i].getUpdated());
//			}
//			solution2.delete(conn);
//			System.out.println(solution2.getDescription());
//			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

}
