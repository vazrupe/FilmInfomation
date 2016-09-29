package com.codefict.film;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/InsertedFilmList")
public class InsertedFilmList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public InsertedFilmList() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		Database db = (Database) getServletContext().getAttribute("film_database");
		PrintWriter out = response.getWriter();
		
		Token token = Token.ServerInstance( db );
		
		String sendToken = MessageTool.ConvertToSQLSafeString(request.getParameter("token"));
		
		String UserId;
		
		try {
			UserId = token.getUserID(sendToken);
		}
		catch (Exception e) {
			out.print("Invalid Token");
			return;
		}
		
		String getInsertedFilmList = String.format("SELECT cif.uf_no, c.c_name, film.f_name, cif.uf_count FROM count_inserted_films cif JOIN film ON film.f_no=cif.film JOIN user_camera uc ON cif.camera=uc.uc_no JOIN camera c ON c.c_no=uc.camera WHERE cif.usr=(SELECT u_no FROM usr WHERE id='%s') AND cif.f_end IS NULL ORDER BY cif.uf_count"
										, UserId);
		
		try {
			ResultSet rs = db.runSql(getInsertedFilmList);
			int count = 0;
			String InsertedFilmList = "";
			while(rs.next()) {
				int uf_no = rs.getInt(1);
				String c_name = rs.getString(2).trim();
				String f_name = rs.getString(3).trim();
				int f_count = rs.getInt(4);
				
				InsertedFilmList += String.format((count++>0?
						",{\"id\":%d,\"c_name\":\"%s\",\"f_name\":\"%s\",\"f_count\":%d}"
						:"{\"id\":%d,\"c_name\":\"%s\",\"f_name\":\"%s\",\"f_count\":%d}")
						, uf_no, c_name, f_name, f_count);
			}
			
			out.print( String.format("[%s]", InsertedFilmList) );
		} catch (Exception e) {
			out.print("failed");
		}
	}

}
