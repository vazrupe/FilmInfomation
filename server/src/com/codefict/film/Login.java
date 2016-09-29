package com.codefict.film;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/UserToken")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    public Login() {
        super();
    }
    public void init() throws ServletException { }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { }
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//doGet(request, response);	
		String us_user_id = request.getParameter("id").toLowerCase();
		String us_user_pwd = request.getParameter("pwd");
		
		String s_user_id = MessageTool.ConvertToSQLSafeString(us_user_id);
		String s_user_pwd = MessageTool.ConvertToSQLSafeString(us_user_pwd);
		
		PrintWriter out = response.getWriter();
		
		Token token = Token.ServerInstance( (Database)getServletContext().getAttribute("film_database") );
		
		out.print( token.getLoginToken(s_user_id, s_user_pwd) );
	}
}
