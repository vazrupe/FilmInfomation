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
 * Servlet implementation class AddLens
 */
@WebServlet("/AddLens")
public class AddLens extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddLens() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Database db = (Database) getServletContext().getAttribute("film_database");
		PrintWriter out = response.getWriter();

		String name = request.getParameter("name");
		String maker = request.getParameter("maker");
		String mount = request.getParameter("mount");
		String zoom = request.getParameter("zoom");
		String f_min = request.getParameter("f_min");
		String f_max = request.getParameter("f_max");
		String focus_min = request.getParameter("focus_min");
		String focus_max = request.getParameter("focus_max");
		String focus_macro = request.getParameter("focus_macro");
		String focus_unlimited = request.getParameter("focus_unlimited");

		String InsertSQLLens = String.format(
				"INSERT into LENS VALUES (lens_ai.NEXTVAL, '%s', '%s', (SELECT mount.m_no FROM mount WHERE mount.m_name='%s'), '%s', (SELECT diaphragm.f_no FROM diaphragm WHERE diaphragm.f_size='%s'), (SELECT diaphragm.f_no FROM diaphragm WHERE diaphragm.f_size='%s'), '%s', '%s', '%s', '%s')",
				name, maker, mount, zoom, f_min, f_max, focus_min, focus_max, focus_macro, focus_unlimited);
		String InsertSQLMount = String.format("INSERT INTO mount VALUES (mount_ai.NEXTVAL, '%s')", mount);
		try {
			db.runSql(InsertSQLMount);
		} catch (SQLException e) { }
		
		try {
			db.runSql(InsertSQLLens);
			out.print("success");
		} catch (SQLException e) {
			out.print("failed");
		}
	}

}
