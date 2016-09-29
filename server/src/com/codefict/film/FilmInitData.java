package com.codefict.film;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;

@WebServlet("/FilmInitData")
public class FilmInitData extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public FilmInitData() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		Database db = (Database) getServletContext().getAttribute("film_database");
		PrintWriter out = response.getWriter();

		String getISOListSql = "select DISTINCT ISO from ISO ORDER BY ISO";
		String getMakerSql = "select DISTINCT MAKER from FILM";
		try {
			ResultSet rs_iso = db.runSql(getISOListSql);
			String isoList = "";
			int count = 0;
			while (rs_iso.next()) {
				int iso = rs_iso.getInt(1);
				isoList += String.format((count++ > 0 ? ",%s" : "%s"), iso);
			}

			ResultSet rs_maker = db.runSql(getMakerSql);
			String makerList = "";
			count = 0;
			while (rs_maker.next()) {
				String maker = rs_maker.getString(1);
				makerList += String.format((count++ > 0 ? ",\"%s\"" : "\"%s\""), maker.trim());
			}

			out.print("{\"maker\":[" + makerList + "], \"iso\":[" + isoList + "]}");

		} catch (SQLException e) {
			out.print("오류가있음");
		}
	}
}
