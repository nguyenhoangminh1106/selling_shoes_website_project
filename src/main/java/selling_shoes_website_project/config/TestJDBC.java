package selling_shoes_website_project.config;


import java.sql.Connection;
import java.sql.DriverManager;

public class TestJDBC {
	public static void main(String[] args) {
		String jdbcUrl = "jdbc:mysql://localhost:3306/selling_shoes_website_project?useSSL=false";
		String user = "webstudent";
		String password = "webstudent";
		
		try {
			
			System.out.println("Connect to database: " + jdbcUrl);
			Connection myConn = DriverManager.getConnection(jdbcUrl, user, password);
			System.out.println("Connection successfully!");
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
