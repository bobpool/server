package com.android.server.servlet;

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

public class RestaurantService extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		PrintWriter out = response.getWriter();
		JSONObject jObject = new JSONObject();
		JSONArray jArray = new JSONArray();
		String jsonData = null;
		
		String rt = request.getParameter("restaurant");
		
		System.out.print(rt);
		
		try{
			Dbconnect(jArray, rt);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		jObject.put("Rdata", jArray);
		
		jsonData = jObject.toString();
		System.out.print(jObject.toString());
		
		out.print(jsonData);
	}
	
	@SuppressWarnings("unchecked")
	public void Dbconnect(JSONArray jArray, String rt) throws ClassNotFoundException, SQLException {
		Connection conn = getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String sql = "select * from " + rt;
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			
			int i = 0;
			while(rs.next()){
				JSONObject sObject = new JSONObject();
				sObject.put("Restaurant_Name", rs.getString("Restaurant_Name"));
				sObject.put("Restaurant_Address", rs.getString("Restaurant_Address"));
				sObject.put("Restaurant_TellNum", rs.getString("Restaurant_TellNum"));
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
		//데이터베이스 설정
		Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/Restaurant","root","1111");
		return c;
	}
}