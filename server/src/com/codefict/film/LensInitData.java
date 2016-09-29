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

/**
 * Servlet implementation class LensInitData
 */
@WebServlet("/LensInitData")
public class LensInitData extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LensInitData() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		Database db = (Database) getServletContext().getAttribute("film_database");
		PrintWriter out = response.getWriter();

		String getMountSQL = "select DISTINCT M_NAME from MOUNT";
		String getFlist = "select DISTINCT F_SIZE from DIAPHRAGM";
		String getMakerSQL = "select DISTINCT MAKER from LENS";
		try {
			ResultSet rs_maker = db.runSql(getMakerSQL);
			String makerList = "";
			int count = 0;
			while (rs_maker.next()) {
				String maker = rs_maker.getString(1);
				makerList += String.format((count++ > 0 ? ",\"%s\"" : "\"%s\""), maker.trim());
			}

			ResultSet rs_name = db.runSql(getMountSQL);
			String nameList = "";
			count = 0;
			while (rs_name.next()) {
				String name = rs_name.getString(1);
				nameList += String.format((count++ > 0 ? ",\"%s\"" : "\"%s\""), name.trim());
			}

			ResultSet rs_f = db.runSql(getFlist);
			String FList = "";
			count = 0;
			while (rs_f.next()) {
				String F = rs_f.getString(1);
				FList += String.format((count++ > 0 ? ",\"%s\"" : "\"%s\""), F.trim());
			}

			out.print("{\"maker\":[" + makerList + "], \"mount\":[" + nameList + "], \"flist\":[" + FList + "]}");

		} catch (SQLException e) {
			out.print("오류가있음");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

}
