package com.android.server;

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

public class dbconnection extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		System.out.print("111111111111");
		PrintWriter out = response.getWriter();
		JSONObject jObject = new JSONObject();
		JSONArray jArray = new JSONArray();
		String jsonData = null;
				
		try{
			dbconnectionTest(jArray);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		jObject.put("Mdata", jArray);
		
		jsonData = jObject.toString();
		System.out.print(jObject.toString());
		
		out.print(jsonData);
	}
	
	@SuppressWarnings("unchecked")
	public void dbconnectionTest(JSONArray jArray) throws ClassNotFoundException, SQLException {
		Connection conn = getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String sql = "select * from rt_info";
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			
			int i = 0;
			while(rs.next()){
				JSONObject sObject = new JSONObject();
				sObject.put("name", rs.getString("rt_name"));
				sObject.put("pnum", rs.getString("rt_pnum"));
				jArray.add(sObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				conn.close();
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		//�����ͺ��̽� ����
		Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/restorant","root","1111");
		return c;
	}
}