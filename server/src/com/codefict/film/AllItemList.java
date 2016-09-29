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

@WebServlet("/AllItemList")
public class AllItemList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public AllItemList() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		
		Database db = (Database) getServletContext().getAttribute("film_database");
		PrintWriter out = response.getWriter();

		String type = request.getParameter("type");

		String getCameraSql = "select c_no, c_name, maker from camera";
		String getlensSql = "select l_no, l_name, maker from lens";
		String getfilmSql = "select f_no, f_name, maker from film";

		String text = "";

		try {
			if (type.equals("camera")) {
				ResultSet rs_camera = db.runSql(getCameraSql);
				int count = 0;
				while (rs_camera.next()) {
					int c_no = rs_camera.getInt(1);
					String c_name = rs_camera.getString(2);
					String maker = rs_camera.getString(3);

					text += (count++ > 0 ? "," : "") + ("{\"id\":" + c_no + ", \"name\":\"" + c_name.trim() + "\", \"maker\":\""
							+ maker.trim() + "\"}");
				}
			} else if (type.equals("lens")) {
				ResultSet rs_lens = db.runSql(getlensSql);
				int count = 0;
				while (rs_lens.next()) {
					int l_no = rs_lens.getInt(1);
					String l_name = rs_lens.getString(2);
					String maker = rs_lens.getString(3);

					text += (count++ > 0 ? "," : "") + ("{\"id\":" + l_no + ", \"name\":\"" + l_name.trim() + "\", \"maker\":\""
							+ maker.trim() + "\"}");
				}
			}

			else if (type.equals("film")) {
				ResultSet rs_film = db.runSql(getfilmSql);
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

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}
}
