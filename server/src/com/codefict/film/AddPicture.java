package com.codefict.film;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/AddPicture")
public class AddPicture extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public AddPicture() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Database db = (Database) getServletContext().getAttribute("film_database");
		PrintWriter out = response.getWriter();
		
		Token token = Token.ServerInstance( db );
		
		String sendToken = MessageTool.ConvertToSQLSafeString(request.getParameter("token"));
		String insertedFilmId = MessageTool.ConvertToSQLSafeString(request.getParameter("id"));
		String target = MessageTool.ConvertToSQLSafeString(request.getParameter("target"));
		String shutterSpeed = MessageTool.ConvertToSQLSafeString(request.getParameter("shutter_speed"));
		String diaphragm = MessageTool.ConvertToSQLSafeString(request.getParameter("diaphragm"));
		String range = MessageTool.ConvertToSQLSafeString(request.getParameter("range"));
		String zoom = MessageTool.ConvertToSQLSafeString(request.getParameter("zoom"));
		String EV = MessageTool.ConvertToSQLSafeString(request.getParameter("ev"));
		String ISO = MessageTool.ConvertToSQLSafeString(request.getParameter("iso"));

		String UserId;
		try {
			UserId = token.getUserID(sendToken);
		}
		catch (Exception e) {
			out.print("Invalid Token");
			return;
		}

		String getCameraIdSql = String.format("SELECT camera FROM user_camera WHERE uc_no=(SELECT camera FROM inserted_film WHERE uf_no=%s)", insertedFilmId);
		String getLensIdSql = String.format("SELECT lens FROM user_lens WHERE ul_no=(SELECT lens FROM user_camera WHERE uc_no=(SELECT camera FROM inserted_film WHERE uf_no=%s))", insertedFilmId);
		String getFilmIdSql = String.format("SELECT film FROM inserted_film WHERE uf_no=%s", insertedFilmId);
		
		
		String AddPictureSql = String.format("INSERT INTO picture_meta (p_no, usr, camera, lens, film, target, shut, f_size, range, zoom, ev, iso) VALUES ("
											+ "picture_meta_ai.NEXTVAL"
											+", (SELECT u_no FROM usr WHERE id='%s')"
											+", (%s)"
											+", (%s)"
											+", (%s)"
											+", '%s'"
											+", (SELECT ss_no FROM shutter_speed WHERE speed='%s')"
											+", (SELECT f_no FROM diaphragm WHERE f_size=%s)"
											+", '%s'"
											+", %s"
											+", %s"
											+", (SELECT iso_no FROM iso WHERE iso=%s))"
											, UserId
											, getCameraIdSql
											, getLensIdSql
											, getFilmIdSql
											, target
											, shutterSpeed
											, diaphragm
											, range
											, zoom
											, EV
											, ISO);
		
		try {
			db.runSql(AddPictureSql);
			out.print("success");
		}
		catch(SQLException e) {
			out.print("failed" + AddPictureSql);
		}
	}

}
