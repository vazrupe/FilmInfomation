package com.codefict.film;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/InsertUserFilm")
public class InsertUserFilm extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public InsertUserFilm() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Database db = (Database) getServletContext().getAttribute("film_database");
		PrintWriter out = response.getWriter();
		
		Token token = Token.ServerInstance( db );
		
		String sendToken = MessageTool.ConvertToSQLSafeString(request.getParameter("token"));
		String userCameraId = MessageTool.ConvertToSQLSafeString(request.getParameter("uc_id"));
		String filmId = MessageTool.ConvertToSQLSafeString(request.getParameter("f_id"));
		
		String UserId;
		
		try {
			UserId = token.getUserID(sendToken);
		}
		catch (Exception e) {
			out.print("Invalid Token");
			return;
		}
		
		String insertFilmSql = String.format("INSERT INTO inserted_film (uf_no, usr, camera, film) VALUES (inserted_film_ai.NEXTVAL, (SELECT u_no FROM usr WHERE id='%s'), %s, %s)", UserId, userCameraId, filmId);
		
		try {
			db.runSql(insertFilmSql);
			out.print("success");
		}
		catch(SQLException e) {
			out.print("failed");
		}
	}
}
