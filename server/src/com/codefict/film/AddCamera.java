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
 * Servlet implementation class AddCamera
 */
@WebServlet("/AddCamera")
public class AddCamera extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddCamera() {
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
		String shutter_min = request.getParameter("shutter_min");
		String shutter_max = request.getParameter("shutter_max");

		String InsertSQLCamera = String.format(
				"INSERT into CAMERA VALUES (camera_ai.NEXTVAL, '%s', '%s', (SELECT mount.m_no FROM mount WHERE mount.m_name='%s'), (SELECT SHUTTER_SPEED.ss_no FROM SHUTTER_SPEED WHERE SHUTTER_SPEED.speed='%s'), (SELECT SHUTTER_SPEED.ss_no FROM SHUTTER_SPEED WHERE SHUTTER_SPEED.speed='%s'))",
				name, maker, mount, shutter_min, shutter_max);
		String InsertSQLMount = String.format("INSERT INTO mount VALUES (mount_ai.NEXTVAL, '%s')", mount);
		try {
			db.runSql(InsertSQLMount);
		} catch (SQLException e) {

		}

		try {
			db.runSql(InsertSQLCamera);
			out.print("success");
		} catch (SQLException e) {
			out.print("failed");
		}
	}

}
