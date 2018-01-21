package com.android.bobpool.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Servlet implementation class AndroidConnection
 */
@WebServlet("/BobDBConnection")
public class AndroidConnection extends HttpServlet {
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		JSONObject jObject = new JSONObject();
		JSONArray jArray = new JSONArray();
		String jsonData = null;
		System.out.println("called");
		try {
			DBConnectionTest(jArray);
		} catch(ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		jObject.put("Mdata", jArray);
		jsonData = jObject.toString();
		System.out.println(jObject.toString());
		out.print(jsonData);
	}
	@SuppressWarnings("unchecked")
	public void DBConnectionTest(JSONArray jArray) throws ClassNotFoundException, SQLException {
		Connection conn = getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String sql = "select * from UserInfo";
		try {
			System.out.println("mysql connected");
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
//			int i = 0;
			while(rs.next()) {
				JSONObject sObject = new JSONObject();
				sObject.put("Age",rs.getString("Age"));
				sObject.put("name",rs.getString("name"));
				sObject.put("address",rs.getString("address"));
				jArray.add(sObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				conn.close();
				rs.close();
				System.out.println("mysql disconnected");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private Connection getConnection() throws ClassNotFoundException, SQLException{
		Class.forName("com.mysql.jdbc.Driver"); 
		Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/스키마이름", "아이디","비밀번호");
		return c;
	}
}
