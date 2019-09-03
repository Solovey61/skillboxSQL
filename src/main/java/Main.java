import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class Main {
	public static void main(String[] args) throws IOException, SQLException {
		Properties mysqlProperties = new Properties();
		mysqlProperties.load(new FileReader(Main.class.getResource("mysql.properties").getFile()));
		String url = mysqlProperties.getProperty("url");
		String user = mysqlProperties.getProperty("user");
		String password = mysqlProperties.getProperty("password");
		Connection connection = DriverManager.getConnection(url, user, password);
		Statement statement = connection.createStatement();
		System.out.println("Average purchase count for the year: ");
		ResultSet resultSet = statement.executeQuery("SELECT course_name, count_summary/12 FROM (SELECT course_name, COUNT(*) count_summary FROM PurchaseList GROUP BY course_name) subq GROUP BY course_name;");
		while (resultSet.next())
			System.out.printf("%-40s|%10s\n", resultSet.getString(1), resultSet.getString(2));
		System.out.println();
		System.out.println("Average purchase count for the period: ");
		resultSet = statement.executeQuery("SELECT course_name, AVG(count_summary) FROM (SELECT course_name, COUNT(*) count_summary FROM PurchaseList GROUP BY course_name, MONTH(subscription_date)) subq GROUP BY course_name;");
		while (resultSet.next())
			System.out.printf("%-40s|%10s\n", resultSet.getString(1), resultSet.getString(2));
		connection.close();
	}
}
