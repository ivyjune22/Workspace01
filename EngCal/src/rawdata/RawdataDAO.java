package rawdata;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RawdataDAO {
	private Connection conn = null;
	private ResultSet rs = null;
	private PreparedStatement pstmt = null;
	
	public Connection getConnection() {
		String url = "jdbc:oracle:thin:@localhost:1521";
		String name = "test";
		String password = "1234";
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");			
			conn = DriverManager.getConnection(url, name, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return conn;
	}
	
	public void close() {
		try {
			if(pstmt != null) pstmt.close();
			if(rs != null) rs.close();
			if(conn != null) conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Double> read(String data) {
		Double value = 0.0;
		List<Double> datavalue = new ArrayList<Double>();
		String table = data.split("\\.")[0];
		String column = data.split("\\.")[1];
		String sql = "select " + column + " from " + table;
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				value = rs.getDouble(1);
				datavalue.add(value);
			}
		} catch (SQLException e) {
			
		} finally {
			close();
		}
		
		return datavalue;
	}
}
