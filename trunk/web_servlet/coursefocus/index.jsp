<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="org.gaara.table.*" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%
	int size = 0;
	if (session.getAttribute("size") != null)
	{
		String str = (String) session.getAttribute("size");
		size = Integer.parseInt(str);
	}
	System.out.println(size);
	for (int i = 0; i < size; i++)
	{
		Course course = (Course)session.getAttribute("courses" + i);
		if(course!=null)
		{
			out.println(course.getCourseNum());
			out.println(course.getCourseName());
			out.println(course.getTeacherName());
			out.println(course.getLocation());
			out.println("<br/>");
		}
	}
 %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
  
  <body>
    <form action="myrequest" method="post">
    	<div>
    		<input type="text" name="classNum"/>
    	</div>
    	<div>
    		<input type="submit" value="测试" id="btn_submit">
    	</div>
    </form>
  </body>
</html>
