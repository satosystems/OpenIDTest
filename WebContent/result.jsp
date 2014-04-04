<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Enumeration" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Open ID result</title>
</head>
<body>
<h1>Open ID: <%= request.getParameter("openid.identity") %></h1>
<table border="1">
<tr><th>Name</th><th>Value</th></tr>
<% for (Enumeration<String> names = request.getParameterNames(); names.hasMoreElements(); ) {
	String name = names.nextElement(); %>
<tr><td><%= name %></td><td><%= request.getParameter(name) %></td></tr>
<% } %>
</table>
</body>
</html>