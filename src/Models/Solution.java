package Models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Solution {

	public static void main(String[] args) {

	}
	
	private int id;
	private String created;
	private String updated;
	private String description;
	private Exercise exercise;
	private User user;
	private DateFormat dateTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	public Solution(String description, Exercise exercise, User user) {
		this.description = description;
		this.exercise = exercise;
		this.user = user;
	}
	
	public Solution() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getUpdated() {
		return updated;
	}

	public void setUpdated(String updated) {
		this.updated = updated;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Exercise getExercise() {
		return exercise;
	}

	public void setExercise(Exercise exercise) {
		this.exercise = exercise;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public DateFormat getDateTime() {
		return dateTime;
	}

	public void setDateTime(DateFormat dateTime) {
		this.dateTime = dateTime;
	}

	public static void createTable(Connection conn) {
		String query = "CREATE TABLE Solution(\n" + 
				"	id INT AUTO_INCREMENT,\n" + 
				"    created DATETIME,\n" + 
				"    updated DATETIME,\n" + 
				"    description TEXT,\n" + 
				"    exercise_id INT NOT NULL,\n" + 
				"    users_id BIGINT NOT NULL,\n" + 
				"    PRIMARY KEY(id),\n" + 
				"    FOREIGN KEY(exercise_id) REFERENCES Exercise(id),\n" + 
				"    FOREIGN KEY(users_id) REFERENCES Users(id)\n" + 
				");";
		
		try {
			PreparedStatement stm = conn.prepareStatement(query);
			stm.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Nie można utworzyć tabeli Solution.");
		}
	}
	
	public void saveToDB(Connection conn) throws SQLException {
		Date date = new Date();
		this.updated = dateTime.format(date).toString();
		this.created = dateTime.format(date).toString();
		if	(this.id == 0) {
			String sql = "INSERT INTO Solution(created, updated, description, exercise_id, users_id) VALUES (?, ?, ?, ?, ?)";
			String generatedColumns[] = { "ID" };
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql, generatedColumns);
			preparedStatement.setString(1, this.created);
			preparedStatement.setString(2, this.updated);
			preparedStatement.setString(3, this.description);
			preparedStatement.setInt(4, this.exercise.getId());
			preparedStatement.setInt(5, this.user.getId());
			preparedStatement.executeUpdate();
			ResultSet rs = preparedStatement.getGeneratedKeys();
			if (rs.next())	{
				this.id	= rs.getInt(1);
			}
		} else {
			String sql = "UPDATE Solution SET updated = ?, description = ?, exercise_id = ?, users_id = ? where id = ?";
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, this.updated);
			preparedStatement.setString(2, this.description);
			preparedStatement.setInt(3, this.exercise.getId());
			preparedStatement.setInt(4, this.user.getId());
			preparedStatement.setInt(5, this.id);
			preparedStatement.executeUpdate();
		}
	}
	
	public static Solution loadById(Connection conn, int id) throws SQLException {
		String sql = "SELECT * FROM Solution WHERE id = ?";
		PreparedStatement preparedStatement;
		preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, id);
		ResultSet resultSet = preparedStatement.executeQuery();;
		if (resultSet.next()) {
			Solution loadedSolution = new Solution();
			loadedSolution.id = resultSet.getInt("id");
			loadedSolution.created = resultSet.getString("created");
			loadedSolution.updated = resultSet.getString("updated");
			loadedSolution.description = resultSet.getString("description");
			loadedSolution.exercise = Exercise.loadById(conn, resultSet.getInt("exercise_id"));
			loadedSolution.user = User.loadById(conn, resultSet.getInt("users_id"));
			return loadedSolution;
		}
		return null;
	}
	
	static public Solution[] loadAllSolutions(Connection conn) throws SQLException {
		ArrayList<Solution> solutions = new ArrayList<Solution>();
		String sql = "SELECT * FROM Solution";
		PreparedStatement preparedStatement;
		preparedStatement = conn.prepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			Solution loadedSolution = new Solution();
			loadedSolution.id = resultSet.getInt("id");
			loadedSolution.created = resultSet.getString("created");
			loadedSolution.updated = resultSet.getString("updated");
			loadedSolution.description = resultSet.getString("description");
			loadedSolution.exercise = Exercise.loadById(conn, resultSet.getInt("exercise_id"));
			loadedSolution.user = User.loadById(conn, resultSet.getInt("users_id"));
			solutions.add(loadedSolution);
		}
		Solution[] sArray = new Solution[solutions.size()]; 
		sArray = solutions.toArray(sArray);
		return sArray;
	}
	
	public void delete(Connection conn) throws SQLException {
		if (this.id != 0) {
			String sql = "DELETE FROM Solution WHERE id = ?";
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setInt(1, this.id);
			preparedStatement.executeUpdate();
			this.id = 0;
		}
	}

	static public Solution[] loadAllByExerciseId(Connection conn, int id) throws SQLException {
		ArrayList<Solution> solutions = new ArrayList<>();
		String sql = "SELECT * FROM Solution WHERE exercise_id=? ORDER BY created DESC";
		PreparedStatement preparedStatement;
		preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, id);
		ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			Solution loadedSolution = new Solution();
			loadedSolution = Solution.loadById(conn, resultSet.getInt("id"));
			solutions.add(loadedSolution);
		}
		Solution[] sArray = new Solution[solutions.size()];
		sArray = solutions.toArray(sArray);
		return sArray;
	}
	
}
