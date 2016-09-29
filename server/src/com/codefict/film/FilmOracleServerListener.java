package com.codefict.film;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Application Lifecycle Listener implementation class FilmOracleServer
 *
 */
@WebListener
public class FilmOracleServerListener implements ServletContextListener {
    public FilmOracleServerListener() {}
    public void contextDestroyed(ServletContextEvent arg0)  {}

    public void contextInitialized(ServletContextEvent event)  { 
    	ServletContext sc = event.getServletContext();
    	
    	String ora_host = sc.getInitParameter("oracle-host");
		String ora_port = sc.getInitParameter("oracle-port");
		String ora_sid = sc.getInitParameter("oracle-sid");
		String ora_user = sc.getInitParameter("oracle-user");
		String ora_password = sc.getInitParameter("oracle-password");
		
		String ora_con_url = String.format("jdbc:oracle:thin:@%s:%s:%s", ora_host, ora_port, ora_sid);
		
		Database oc = new Database(ora_con_url, ora_user, ora_password);
		
		sc.setAttribute("film_database", oc);
    }
}
