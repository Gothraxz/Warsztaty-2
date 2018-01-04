package Models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Exercise {

	public static void main(String[] args) {

	}
	
	private int id;
	private String title;
	private String description;
	// zmienne na potrzeby pobrania rozwiązań
	private int solutionId;
	private String solutionCreated;
	private String solutionUpdated;
	private String solutionDescription;
	
	public Exercise(String title) {
		this.title = title;
		this.description = "N/A";
	}
	
	public Exercise(String title, String description) {
		this.title = title;
		this.description = description;
	}
	
	public Exercise() {};
	
	public int getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	// gettery na potrzeby pobrania rozwiązań
	public int getSolutionId() {
		return solutionId;
	}

	public String getSolutionCreated() {
		return solutionCreated;
	}

	public String getSolutionUpdated() {
		return solutionUpdated;
	}

	public String getSolutionDescription() {
		return solutionDescription;
	}

	public static void createTable(Connection conn) {
		String query = "CREATE TABLE Exercise(\n" + 
				"	id INT AUTO_INCREMENT,\n" + 
				"    title VARCHAR(255) NOT NULL,\n" + 
				"    description TEXT,\n" + 
				"    PRIMARY KEY(id)\n" + 
				");";
		
		try {
			PreparedStatement stm = conn.prepareStatement(query);
			stm.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Nie można utworzyć tabeli Exercise");
		}
	}
	
	public void saveToDB(Connection conn) throws SQLException {
		if	(this.id == 0) {
			String sql = "INSERT INTO Exercise(title, description) VALUES (?, ?)";
			String generatedColumns[] = { "ID" };
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql, generatedColumns);
			preparedStatement.setString(1, this.title);
			preparedStatement.setString(2, this.description);
			preparedStatement.executeUpdate();
			ResultSet rs = preparedStatement.getGeneratedKeys();
			if (rs.next())	{
				this.id	= rs.getInt(1);
			}
		}
			// edycja - nie wymagana
//		} else {
//			String sql = "UPDATE Exercise SET title = ?, description = ? where id = ?";
//			PreparedStatement preparedStatement;
//			preparedStatement = conn.prepareStatement(sql);
//			preparedStatement.setString(1, this.title);
//			preparedStatement.setString(2, this.description);
//			preparedStatement.setInt(3, this.id);
//			preparedStatement.executeUpdate();
//		}
	}
	
	public static Exercise loadById(Connection conn, int id) throws SQLException {
		String sql = "SELECT * FROM Exercise WHERE id = ?";
		PreparedStatement preparedStatement;
		preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, id);
		ResultSet resultSet = preparedStatement.executeQuery();;
		if (resultSet.next()) {
			Exercise loadedExercise = new Exercise();
			loadedExercise.id = resultSet.getInt("id");
			loadedExercise.title = resultSet.getString("title");
			loadedExercise.description = resultSet.getString("description");
			return loadedExercise;
		}
		return null;
	}
	
	static public Exercise[] loadAllExercises(Connection conn) throws SQLException {
		ArrayList<Exercise> exercises = new ArrayList<Exercise>();
		String sql = "SELECT * FROM Exercise";
		PreparedStatement preparedStatement;
		preparedStatement = conn.prepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			Exercise loadedExercise = new Exercise();
			loadedExercise.id = resultSet.getInt("id");
			loadedExercise.title = resultSet.getString("title");
			loadedExercise.description = resultSet.getString("description");
			exercises.add(loadedExercise);
		}
		Exercise[] eArray = new Exercise[exercises.size()]; 
		eArray = exercises.toArray(eArray);
		return eArray;
	}
	
	public void delete(Connection conn) throws SQLException {
		if (this.id != 0) {
			String sql = "DELETE FROM Exercise WHERE id = ?";
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setInt(1, this.id);
			preparedStatement.executeUpdate();
			this.id = 0;
		}
	}
	

	static public Solution[] loadAllByUserId(Connection conn, int id) throws SQLException {
		ArrayList<Solution> solutions = new ArrayList<Solution>();
		String sql = "SELECT * FROM Solution WHERE users_id = ?";
		PreparedStatement preparedStatement;
		preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, id);
		ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			Solution loadedSolutions = new Solution();
			loadedSolutions.setCreated(resultSet.getString("created"));
			loadedSolutions.setUpdated(resultSet.getString("updated"));
			loadedSolutions.setDescription(resultSet.getString("description"));
			loadedSolutions.setExercise(Exercise.loadById(conn, resultSet.getInt("exercise_id")));
			loadedSolutions.setUser(User.loadById(conn, resultSet.getInt("users_id")));
			solutions.add(loadedSolutions);
		}
		Solution[] sArray = new Solution[solutions.size()]; 
		sArray = solutions.toArray(sArray);
		return sArray;
	}
	
	// pobranie rozwiązań jako obiekt ćwiczeń - alternatywne z powyższym
//	static public Exercise[] loadAllByUserId(Connection conn, int id) throws SQLException {
//		ArrayList<Exercise> exercises = new ArrayList<Exercise>();
//		String sql = "SELECT Exercise.id AS e_id, Exercise.title AS e_title, Exercise.description AS e_description, \n" + 
//				"Solution.id, Solution.created, Solution.updated, Solution.description, \n" + 
//				"Solution.users_id, Solution.exercise_id FROM Solution\n" + 
//				"JOIN Exercise ON Solution.exercise_id = Exercise.id\n" + 
//				"WHERE Solution.users_id = ?;";
//		PreparedStatement preparedStatement;
//		preparedStatement = conn.prepareStatement(sql);
//		preparedStatement.setInt(1, id);
//		ResultSet resultSet = preparedStatement.executeQuery();
//		while (resultSet.next()) {
//			Exercise loadedSolutions = new Exercise();
//			loadedSolutions.id = resultSet.getInt("e_id");
//			loadedSolutions.title = resultSet.getString("e_title");
//			loadedSolutions.description = resultSet.getString("e_description");
//			loadedSolutions.solutionId = resultSet.getInt("id");
//			loadedSolutions.solutionCreated = resultSet.getString("created");
//			loadedSolutions.solutionUpdated = resultSet.getString("updated");
//			loadedSolutions.solutionDescription = resultSet.getString("description");
//			exercises.add(loadedSolutions);
//		}
//		Exercise[] eArray = new Exercise[exercises.size()]; 
//		eArray = exercises.toArray(eArray);
//		return eArray;
//	}
	
}
