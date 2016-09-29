<%@page import="java.sql.*"%>
<%@page import="com.codefict.film.Token.NotAuthToken"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@page import="com.codefict.film.*"%>


<% out.print( request.getParameter("id")==null );out.print( request.getParameter("pwd") );



Database DB = (Database)getServletContext().getAttribute("film_database"); String UserID;/*
try {
	String myToken = request.getParameter("token");
	myToken = MessageTool.ConvertToSQLSafeString(myToken);
	Token tokenManager = Token.ServerInstance( DB );
	UserID = tokenManager.getUserID(myToken);
	tokenManager.UpdateTokenValidTime(myToken);
} catch(NotAuthToken e) { out.print("Unvalid Token"); return; }*/


try {
	String TestSql = "select * from nls_session_parameters";
	ResultSet rs = DB.runSql(TestSql);
	while (rs.next()) {
		String PARAM = rs.getString(1);
		String VALUE = rs.getString(2);
		
		out.print(String.format("%s: %s<br/>", PARAM, VALUE));
	}
} catch(Exception e) {
	
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
</head>
<body>
test

<form action="UserToken" method="POST">
	<input name="id" type="text" />
	<input name="pwd" type="password" />
	<input type="submit" />
</form>
</body>
</html>