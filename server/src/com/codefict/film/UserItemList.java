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
 * Servlet implementation class UserItemList
 */
@WebServlet("/UserItemList")
public class UserItemList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserItemList() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		Database db = (Database) getServletContext().getAttribute("film_database");
		PrintWriter out = response.getWriter();

		String type = request.getParameter("type");

		String getUserCameraSql = "SELECT uc_no, ca.c_name, ca.maker FROM user_camera uca JOIN camera ca on uca.camera=ca.c_no";
		String getUserlensSql = "SELECT ul_no, ls.l_name, ls.maker FROM user_lens uls JOIN lens ls on uls.lens=ls.l_no";
		String getUserfilmSql = "select f_no, f_name, maker from film";

		String text = "";

		try {
			if (type.equals("camera")) {
				ResultSet rs_camera = db.runSql(getUserCameraSql);
				int count = 0;
				while (rs_camera.next()) {
					int uc_no = rs_camera.getInt(1);
					String name = rs_camera.getString(2);
					String maker = rs_camera.getString(3);
					

					text += (count++ > 0 ? "," : "") + ("{\"id\":" + uc_no + ", \"name\":\"" + name + "\", \"maker\":\""
							+ maker + "\"}");
				}
			} else if (type.equals("lens")) {
				ResultSet rs_lens = db.runSql(getUserlensSql);
				int count = 0;
				while (rs_lens.next()) {
					int ul_no = rs_lens.getInt(1);
					String name = rs_lens.getString(2);
					String maker = rs_lens.getString(3);

					text += (count++ > 0 ? "," : "") + ("{\"id\":" + ul_no + ", \"name\":\"" + name + "\", \"maker\":\""
							+ maker + "\"}");
				}
			}

			else if (type.equals("film")) {
				ResultSet rs_film = db.runSql(getUserfilmSql);
				int count = 0;
				while (rs_film.next()) {
					int f_no = rs_film.getInt(1);
					String f_name = rs_film.getString(2);
					String maker = rs_film.getString(3);

					text += (count++ > 0 ? "," : "") + ("{\"id\":" + f_no + ", \"name\":\"" + f_name.trim() + "\", \"maker\":\""
							+ maker.trim() + "\"}");
				}
			}
			else {
				out.print("failed");
				return;
			}

			out.print("[" + text + "]");

		} catch (SQLException e) {
			out.print("failed");
		}
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
