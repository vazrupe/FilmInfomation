package com.codefict.film;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ItemData
 */
@WebServlet("/ItemData")
public class ItemData extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ItemData() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		Database db = (Database) getServletContext().getAttribute("film_database");
		PrintWriter out = response.getWriter();
		
		String id = request.getParameter("id");
		String type = request.getParameter("type");

		String getCameraSql = String.format("SELECT ca.c_name, ca.maker, mnt.m_name, sht_min.speed, sht_max.speed FROM camera ca JOIN mount mnt ON ca.mount=mnt.m_no JOIN shutter_speed sht_min ON ca.minss=sht_min.ss_no JOIN shutter_speed sht_max ON ca.maxss=sht_max.SS_NO WHERE ca.c_no='%s'"
                , id);
		String getlensSql = String.format("select ls.l_name, ls.maker, mnt.m_name, ls.zoom, dim_minf.f_size, dim_maxf.f_size, ls.minfcs, ls.maxfcs, ls.macfcs, ls.inffcs from lens ls JOIN mount mnt on ls.mount=mnt.m_no JOIN diaphragm dim_minf on ls.minf=dim_minf.f_no JOIN diaphragm dim_maxf on ls.maxf=dim_maxf.f_no WHERE ls.l_no='%s'"
				, id);
		String getfilmSql = String.format("select fm.f_name, fm.maker, iso.iso from film fm join iso on fm.iso=iso.iso_no where fm.f_no='%s'"
				, id);

		String text = "";
		
		try {
			if (type.equals("camera")) {
				ResultSet rs_camera = db.runSql(getCameraSql);
				if (rs_camera.next()) {
					String c_name = rs_camera.getString(1).trim();
					String maker = rs_camera.getString(2).trim();
					String mount = rs_camera.getString(3).trim();
					String minss = rs_camera.getString(4).trim();
					String maxss = rs_camera.getString(5).trim();

					text = "{\"name\":\"" + c_name + "\", \"maker\":\"" + maker+ "\", \"mount\":\"" + mount + "\", \"shutter_min\":\"" + minss + "\", \"shutter_max\":\"" + maxss + "\"}";
				}
			} else if (type.equals("lens")) {
				ResultSet rs_lens = db.runSql(getlensSql);
				if (rs_lens.next()) {
					String l_name = rs_lens.getString(1).trim();
					String maker = rs_lens.getString(2).trim();
					String mount = rs_lens.getString(3).trim();
					Float zoom = rs_lens.getFloat(4);
					String minf = rs_lens.getString(5).trim();
					String maxf = rs_lens.getString(6).trim();
					Float minfcs = rs_lens.getFloat(7);
					Float maxfcs = rs_lens.getFloat(8);
					String macfcs = rs_lens.getString(9).trim();
					String inffcs = rs_lens.getString(10).trim();

					text = "{\"name\":\"" + l_name + "\", \"maker\":\"" + maker + "\", \"mount\":\"" + mount + "\", \"zoom\":" + zoom + ", \"f_min\":\"" + minf + "\", \"f_max\":\"" + maxf + "\", \"focus_min\":" + minfcs + ", \"focus_max\":" + maxfcs + ", \"focus_macro\":\"" + macfcs + "\", \"focus_unlimited\":\"" + inffcs + "\"}";
				}
			}

			else if (type.equals("film")) {
				ResultSet rs_film = db.runSql(getfilmSql);
				if (rs_film.next()) {
					String f_name = rs_film.getString(1);
					String maker = rs_film.getString(2);
					int iso = rs_film.getInt(3);
					
					text = "{\"name\":\"" + f_name.trim() + "\", \"maker\":\""+ maker.trim() + "\", \"iso\":" + iso + "}";
				}
			}
			else {
				out.print("failed");
				return;
			}

			out.print(text);

		} catch (Exception e) {
			out.print("failed");
		}
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
