package com.codefict.film;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/NotInsertedCameraList")
public class NotInsertedCameraList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public NotInsertedCameraList() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		Database db = (Database) getServletContext().getAttribute("film_database");
		PrintWriter out = response.getWriter();
		
		Token token = Token.ServerInstance( db );
		
		String sendToken = MessageTool.ConvertToSQLSafeString(request.getParameter("token"));
		
		String UserId;
		
		try {
			UserId = token.getUserID(sendToken);
		}
		catch (Exception e) {
			out.print("Invalid Token");
			return;
		}
		
		String getListSql;
		getListSql = String.format("SELECT uc.uc_no, ca.c_name, ca.maker FROM user_camera uc JOIN camera ca ON uc.camera=ca.c_no WHERE uc.usr=(SELECT u_no FROM usr WHERE usr.id='%s') AND uc.uc_no NOT IN (SELECT uflm.camera FROM inserted_film uflm WHERE uflm.f_end IS NULL AND uflm.usr=(SELECT u_no FROM usr WHERE usr.id='%s'))", UserId, UserId);
		
		try {
			String IdList = "";
			ResultSet rs = db.runSql(getListSql);
			int count = 0;
			while(rs.next()) {
				int itemId = rs.getInt(1);
				String name = rs.getString(2).trim();
				String maker = rs.getString(3).trim();
				IdList += String.format( (count++>0?",{\"id\":%d,\"name\":\"%s\",\"maker\":\"%s\"}":"{\"id\":%d,\"name\":\"%s\",\"maker\":\"%s\"}") , itemId, name, maker);
			}
			out.print( String.format("[%s]", IdList) );
		}
		catch(SQLException e) {
			out.print("failed");
		}
	}
}
