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

@WebServlet("/PictureInitData")
public class PictureInitData extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public PictureInitData() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		Database db = (Database) getServletContext().getAttribute("film_database");
		PrintWriter out = response.getWriter();
		
		Token token = Token.ServerInstance( db );

		String sendToken = MessageTool.ConvertToSQLSafeString(request.getParameter("token"));
		String insertedFilmId = MessageTool.ConvertToSQLSafeString(request.getParameter("id"));
		
		String UserId;
		try {
			UserId = token.getUserID(sendToken);
		}
		catch (Exception e) {
			out.print("Invalid Token");
			return;
		}
		
		
		// getEtcItems
		String getEtcItemSql = String.format("SELECT zoom, minfcs, maxfcs, macfcs, inffcs FROM lens WHERE l_no=(SELECT DISTINCT lens FROM user_lens WHERE ul_no=(SELECT DISTINCT lens FROM user_camera WHERE usr=(SELECT u_no FROM usr WHERE id='%s') AND camera=(SELECT camera FROM inserted_film WHERE uf_no=%s)))"
											, UserId
											, insertedFilmId);
		String maxZoom = "";
		String minFocus = "";
		String maxFocus = "";
		String macroFocus = "";
		String unlimitedFocus = "";
		try {
			ResultSet rs_etc_items = db.runSql(getEtcItemSql);
			if(rs_etc_items.next()) {
				maxZoom = rs_etc_items.getString(1).trim();
				minFocus = rs_etc_items.getString(2).trim();
				maxFocus = rs_etc_items.getString(3).trim();
				macroFocus = rs_etc_items.getString(4).trim();
				unlimitedFocus = rs_etc_items.getString(5).trim();

				if (maxZoom.indexOf('.')==0) maxZoom = "0" + maxZoom;
				if (minFocus.indexOf('.')==0) minFocus = "0" + minFocus;
				if (maxFocus.indexOf('.')==0) maxFocus = "0" + maxFocus;
			}
			else throw new SQLException();
		} catch (SQLException e) {
			out.print("Not Mount Lens");
			return;
		}
		
		
		// getISO
		String getISOSql = String.format("SELECT iso FROM iso WHERE iso_no=(SELECT iso FROM film WHERE f_no=(SELECT film FROM inserted_film WHERE uf_no=%s))", insertedFilmId);
		String getISOListSql = "SELECT iso FROM iso ORDER BY iso";
		int ISO = 0;
		String ISOList = "";
		try {
			ResultSet rs_iso = db.runSql(getISOSql);
			rs_iso.next();
			ISO = rs_iso.getInt(1);
			
			int count = 0;
			ResultSet rs_iso_list = db.runSql(getISOListSql);
			while(rs_iso_list.next()) {
				int ISOTemp = rs_iso_list.getInt(1);
				ISOList += String.format((count++>0?",%d":"%d"), ISOTemp);
			}
		} catch (SQLException e) {
			out.print("failed");
			return;
		}
		
		// getSpeedList
		String ShutterSpeedSql = "SELECT %s FROM camera WHERE c_no=(SELECT DISTINCT camera FROM user_camera WHERE uc_no=(SELECT camera FROM inserted_film WHERE uf_no=%s))";
		String minShutterSpeedSql = String.format(ShutterSpeedSql, "minss", insertedFilmId);
		String maxShutterSpeedSql = String.format(ShutterSpeedSql, "maxss", insertedFilmId);
		String getShutterSpeedListSql = String.format("SELECT speed FROM shutter_speed WHERE ss_no BETWEEN (%s) AND (%s) ORDER BY ss_no"
														, minShutterSpeedSql, maxShutterSpeedSql);
		String ShutterSpeedList = "";
		try {
			int count = 0;
			ResultSet rs_shutter_speed_list = db.runSql(getShutterSpeedListSql);
			while(rs_shutter_speed_list.next()) {
				String shutterSpeed = rs_shutter_speed_list.getString(1).trim();
				ShutterSpeedList += String.format((count++>0?",\"%s\"":"\"%s\""), shutterSpeed);
			}
		} catch (SQLException e) {
			out.print("failed");
			return;
		}
		
		// getFSizeList
		String DiaphragmSql = "SELECT %s FROM lens WHERE l_no=(SELECT DISTINCT lens FROM user_lens WHERE ul_no=(SELECT DISTINCT lens FROM user_camera WHERE usr=(SELECT u_no FROM usr WHERE id='%s') AND camera=(SELECT camera FROM inserted_film WHERE uf_no=%s)))";
		String minDiaphragmSql = String.format(DiaphragmSql, "minf", UserId, insertedFilmId);
		String maxDiaphragmSql = String.format(DiaphragmSql, "maxf", UserId, insertedFilmId);
		String getDiaphragmListSql = String.format("SELECT f_size FROM diaphragm WHERE f_no BETWEEN (%s) AND (%s) ORDER BY f_no"
													, minDiaphragmSql, maxDiaphragmSql);
		String DiaphragmList = "";
		try {
			int count = 0;
			ResultSet rs_diaphragm_list = db.runSql(getDiaphragmListSql);
			while(rs_diaphragm_list.next()) {
				String Diaphragm = rs_diaphragm_list.getString(1).trim();
				DiaphragmList += String.format((count++>0?",\"%s\"":"\"%s\""), Diaphragm);
			}
		} catch (SQLException e) {
			out.print("failed");
			return;
		}
		
		out.print(String.format("{\"iso\":%d,\"iso_list\":[%s],\"shutter_speed_list\":[%s],\"f_list\":[%s],\"max_zoom\":%s,\"min_focus\":%s,\"max_focus\":%s,\"macro_focus\":\"%s\",\"unlimited_focus\":\"%s\"}"
				, ISO, ISOList, ShutterSpeedList, DiaphragmList, maxZoom, minFocus, maxFocus, macroFocus, unlimitedFocus));
	}

}
