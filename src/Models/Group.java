package Models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Group {

	public static void main(String[] args) {
		
	}
	
	private int id;
	private String name;
	
	public Group(String name) {
		this.name = name;
	}
	
	public Group() {};
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public static void createTable(Connection conn) {
		String query = "CREATE TABLE Groups(\n" + 
				"	id INT AUTO_INCREMENT,\n" + 
				"    name VARCHAR(255) NOT NULL,\n" + 
				"    PRIMARY KEY(id)\n" + 
				");";
		
		try {
			PreparedStatement stm = conn.prepareStatement(query);
			stm.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Nie można utworzyć tabeli Groups");
		}
	}
	
	public void saveToDB(Connection conn) throws SQLException {
		if	(this.id == 0) {
			String sql = "INSERT INTO Groups(name) VALUES (?)";
			String generatedColumns[] = { "ID" };
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql, generatedColumns);
			preparedStatement.setString(1, this.name);
			preparedStatement.executeUpdate();
			ResultSet rs = preparedStatement.getGeneratedKeys();
			if (rs.next())	{
				this.id	= rs.getInt(1);
			}
		}
	}
	
	public static Group loadById(Connection conn, int id) throws SQLException {
			String sql = "SELECT * FROM Groups WHERE id = ?";
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();;
			if (resultSet.next()) {
				Group loadedGroup = new Group();
				loadedGroup.id = resultSet.getInt("id");
				loadedGroup.name = resultSet.getString("name");
				return loadedGroup;
			}
			return null;
		}
	
	
}
