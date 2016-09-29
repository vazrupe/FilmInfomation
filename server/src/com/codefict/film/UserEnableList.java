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

@WebServlet("/UserEnableList")
public class UserEnableList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public UserEnableList() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		Database db = (Database) getServletContext().getAttribute("film_database");
		PrintWriter out = response.getWriter();
		
		Token token = Token.ServerInstance( db );
		
		String sendToken = MessageTool.ConvertToSQLSafeString(request.getParameter("token"));
		String type = request.getParameter("type");
		
		String UserId;
		
		try {
			UserId = token.getUserID(sendToken);
		}
		catch (Exception e) {
			out.print("Invalid Token");
			return;
		}
		
		String getListSql;
		if (type.equals("camera")) getListSql = String.format("SELECT uc.camera FROM user_camera uc WHERE uc.usr=(SELECT u_no FROM usr WHERE id='%s')", UserId);
		else if (type.equals("lens")) getListSql = String.format("SELECT ul.lens FROM user_lens ul WHERE ul.usr=(SELECT u_no FROM usr WHERE id='%s')", UserId);
		else {
			out.print("failed");
			return;
		}
		
		try {
			String IdList = "";
			ResultSet rs = db.runSql(getListSql);
			int count = 0;
			while(rs.next()) {
				int itemId = rs.getInt(1);
				IdList += String.format( (count++>0?",%d":"%d") , itemId);
			}
			out.print( String.format("[%s]", IdList) );
		}
		catch(SQLException e) {
			out.print("failed");
		}
	}

}
