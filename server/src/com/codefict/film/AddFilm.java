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
 * Servlet implementation class AddFilm
 */
@WebServlet("/AddFilm")
public class AddFilm extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddFilm() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Database db = (Database) getServletContext().getAttribute("film_database");
		PrintWriter out = response.getWriter();

		String name = request.getParameter("name");
		String maker = request.getParameter("maker");
		String iso = request.getParameter("iso");
		String InsertSQLFILM = String.format(
				"INSERT into FILM VALUES (film_ai.NEXTVAL, '%s', '%s',(SELECT iso.iso_no FROM iso WHERE iso.iso=%s))",
				name, maker, iso);
		try {
			db.runSql(InsertSQLFILM);
			out.print("success");
		} catch (SQLException e) {
			out.print("failed");
		}
	}

}
