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
 * Servlet implementation class CameraInitData
 */
@WebServlet("/CameraInitData")
public class CameraInitData extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CameraInitData() {
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
		String getSpeedSQL = "select DISTINCT SPEED from SHUTTER_SPEED ORDER BY ss_no";
		String getMakerSQL = "select DISTINCT MAKER from CAMERA";
		try {

			ResultSet rs_maker = db.runSql(getMakerSQL);
			String makerList = "";
			int count = 0;
			while (rs_maker.next()) {
				String maker = rs_maker.getString(1);
				makerList += String.format((count++ > 0 ? ",\"%s\"" : "\"%s\""), maker.trim());
			}

			ResultSet rs_mount = db.runSql(getMountSQL);
			String nameList = "";
			count = 0;
			while (rs_mount.next()) {
				String name = rs_mount.getString(1);
				nameList += String.format((count++ > 0 ? ",\"%s\"" : "\"%s\""), name.trim());
			}

			ResultSet rs_speed = db.runSql(getSpeedSQL);
			String speedList = "";
			count = 0;
			while (rs_speed.next()) {
				String speed = rs_speed.getString(1);
				speedList += String.format((count++ > 0 ? ",\"%s\"" : "\"%s\""), speed.trim());
			}

			out.print("{\"maker\":[" + makerList + "], \"mount\":[" + nameList + "], \"speed\":[" + speedList + "]}");

		} catch (SQLException e) {
			out.print("오류가있음");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

}
