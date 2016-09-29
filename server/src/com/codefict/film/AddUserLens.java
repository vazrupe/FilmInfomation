package com.codefict.film;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AddUserLens
 */
@WebServlet("/AddUserLens")
public class AddUserLens extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddUserLens() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Database db = (Database) getServletContext().getAttribute("film_database");
		PrintWriter out = response.getWriter();
		
		Token token = Token.ServerInstance(db);
		String sendToken = MessageTool.ConvertToSQLSafeString(request.getParameter("token"));
		String id = request.getParameter("id");
		
		String UserId;
		
		try {
			UserId = token.getUserID(sendToken);
		}
		catch (Exception e) {
			out.print("Invalid Token");
			return;
		}
		String InsertSQLlens = String.format("INSERT INTO user_lens values (user_lens_ai.nextval, (SELECT usr.u_no FROM usr where usr.id='%s'), '%s')" , UserId, id);
		try {
			db.runSql(InsertSQLlens);
			out.print("success");
		} catch (SQLException e) {
			out.print("failed");
		}
	}

}
