package com.android.server.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.mysql.jdbc.Statement;

public class PartyMatchingService extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		PrintWriter out = response.getWriter();
		JSONObject jObject = new JSONObject();
		String result = "fail";
		String jsonData = null;
		
		String rt_name = request.getParameter("rt_name");
		String user_name = request.getParameter("user_name");
		String user_menu = request.getParameter("user_menu");
		String user_menu_price = request.getParameter("user_menu_price");
		String collage_addr = request.getParameter("collage_addr");
		String room_num = request.getParameter("room_num");
		
		try{
			Dbconnect(jObject, result, rt_name, user_name, user_menu, user_menu_price, collage_addr, room_num);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
				
		jsonData = jObject.toString();
		System.out.print(jObject.toString());
		
		out.print(jsonData);
	}
	
	@SuppressWarnings("unchecked")
	public void Dbconnect(JSONObject jObject, String result, String rt_name, String user_name, String user_menu, String user_menu_price, String collage_addr, String room_num) throws ClassNotFoundException, SQLException {
		Connection conn = getConnection();
		PreparedStatement ps = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		Statement stmt = null;
		int i = 0;
		
		Random random = new Random();
		
		int randint = random.nextInt(999999999) + 100000000;
		String randomId = Integer.toString(randint);
		String mainAddr = null;
		int partyID = 0;
		int max_user = 0;
		int goal_price = 0;
		int present_user = 0;
		int present_price = 0;
		int menu_price = 0;
		
		menu_price = Integer.valueOf(user_menu_price);
		
		String selectAddressSQL = "SELECT DISTINCT main_address FROM addressList WHERE sub_address='" + collage_addr + "'";
		
		try {
			ps = conn.prepareStatement(selectAddressSQL);
			rs1 = ps.executeQuery();
			
			while(rs1.next()) {
				mainAddr = rs1.getString("main_address");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} rs1.close();
		
		//String sql = "insert into " + rt_name + "(" + randomId + ", " + user_name + ", " + user_menu + ", " + user_menu_price + ", " + user_address + ", ";
		String selectPartySQL = "SELECT DISTINCT party_id, party_max_numOfuser, party_goal_price, party_present_price, party_present_numOfuser "
								+ "FROM " + rt_name + "_party "
								+ "WHERE main_address='" + mainAddr + "' AND party_present_numOfuser/party_max_numOfuser < 1";
		
		try {
			ps = conn.prepareStatement(selectPartySQL);
			rs2 = ps.executeQuery();
		
			if(rs2 == null){
				return;
			}
			else {
				while(rs2.next()) {
					partyID = rs2.getInt("party_id");
					max_user = rs2.getInt("party_max_numOfuser");
					goal_price = rs2.getInt("party_goal_price");
					present_price = rs2.getInt("party_present_price");
					present_user = rs2.getInt("party_present_numOfuser");
				}
				
				stmt = (Statement) conn.createStatement();
				
				present_price += menu_price;
				present_user += 1;
				
				String joinPartySQL = "INSERT INTO "  + rt_name + "_party" + "(party_id, user_name, user_menu, user_menu_price, room_num, party_max_numOfuser, party_goal_price, main_address) "
									  + "VALUES(" + partyID + ", '" + user_name + "', '" + user_menu + "', " + user_menu_price + ", "+  room_num + ", " + max_user + ", " + goal_price + ", '" + mainAddr + "')" ;
				
				i = stmt.executeUpdate(joinPartySQL);
				
				stmt.clearBatch();
				
				String updateSQL = "UPDATE " + rt_name + "_party SET party_present_price = " + present_price + ", party_present_numOfuser = " + present_user + " WHERE party_id = " + partyID;
				
				i = stmt.executeUpdate(updateSQL);
				
				result = "succeed";
				jObject.put("Result", result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				conn.close();
				rs2.close();
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		//데이터베이스 설정
		Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurant","root","1111");
		return c;
	}
}