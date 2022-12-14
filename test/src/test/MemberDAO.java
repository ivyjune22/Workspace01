package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import test.MemberDTO;

public class MemberDAO {
	private Connection conn = null;
	private ResultSet rs = null;
	private PreparedStatement pstmt = null;
	private String insertSQL = "insert into member values (seq_mem.nextval, ?, ?, ?)";
	private String updateSQL = "update member set name=?, rank=?, birthday=? where num=?";
	private String deleteSQL = "delete member where num=?";
	private String listSQL = "select * from member order by num asc";
	private String rank1ListSQL = "select * from member where rank=1 order by num asc";
	private String rank23ListSQL = "select * from member where rank>1 order by num asc";
	private String findOneSQL = "select * from member where num=?";
	
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
	
	public int insert(MemberDTO dto) {
		int result = 0;
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(insertSQL);
			pstmt.setString(1, dto.getName());
			pstmt.setInt(2, dto.getRank());
			pstmt.setString(3, dto.getBirthday());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return result;
	}
	
	public int update(MemberDTO dto) {
		int result = 0;
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(updateSQL);
			pstmt.setString(1, dto.getName());
			pstmt.setInt(2, dto.getRank());
			pstmt.setString(3, dto.getBirthday());
			pstmt.setInt(4, dto.getNum());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return result;
	}
	
	public int delete(int num) {
		int result = 0;
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(deleteSQL);
			pstmt.setInt(1, num);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return result;
	}
	
	public List<MemberDTO> list() {
		List<MemberDTO> list = new ArrayList<MemberDTO>();
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(listSQL);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				MemberDTO dto = new MemberDTO();
				dto.setNum(rs.getInt(1));
				dto.setName(rs.getString(2));
				dto.setRank(rs.getInt(3));
				dto.setBirthday(rs.getString(4).substring(0, 10));
				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return list;
	}
	
	public List<MemberDTO> rank1List() {
		List<MemberDTO> list = new ArrayList<MemberDTO>();
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(rank1ListSQL);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				MemberDTO dto = new MemberDTO();
				dto.setNum(rs.getInt(1));
				dto.setName(rs.getString(2));
				dto.setRank(rs.getInt(3));
				dto.setBirthday(rs.getString(4).substring(0, 10));
				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return list;
	}
	
	public List<MemberDTO> rank23List() {
		List<MemberDTO> list = new ArrayList<MemberDTO>();
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(rank23ListSQL);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				MemberDTO dto = new MemberDTO();
				dto.setNum(rs.getInt(1));
				dto.setName(rs.getString(2));
				dto.setRank(rs.getInt(3));
				dto.setBirthday(rs.getString(4).substring(0, 10));
				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return list;
	}
	
	public String findName(int num) {
		List<MemberDTO> list = new ArrayList<MemberDTO>();
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(findOneSQL);
			pstmt.setInt(1, num);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				MemberDTO dto = new MemberDTO();
				dto.setNum(rs.getInt(1));
				dto.setName(rs.getString(2));
				dto.setRank(rs.getInt(3));
				dto.setBirthday(rs.getString(4));
				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		String name = list.get(0).getName();
		return name;
	}

	public int findRank(int num) {
		List<MemberDTO> list = new ArrayList<MemberDTO>();
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(findOneSQL);
			pstmt.setInt(1, num);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				MemberDTO dto = new MemberDTO();
				dto.setNum(rs.getInt(1));
				dto.setName(rs.getString(2));
				dto.setRank(rs.getInt(3));
				dto.setBirthday(rs.getString(4));
				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		int rank = list.get(0).getRank();
		return rank;
	}
}
