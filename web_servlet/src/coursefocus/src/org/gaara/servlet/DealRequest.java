package org.gaara.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.gaara.db.DataBaseMgr;
import org.gaara.table.Course;

import java.io.*;
import java.sql.ResultSet;
import java.util.*;

public class DealRequest extends HttpServlet{
	public DealRequest() {
		super();}

	public void destroy() {
		super.destroy();
	}
	public void doGet(HttpServletRequest request,HttpServletResponse response)
	{
		try {
			doPost(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void doPost(HttpServletRequest request,HttpServletResponse response)
		throws ServletException,IOException
		{
			String classNum = request.getParameter("classNum");
			System.out.println(classNum);
			String clientAddr = request.getRemoteAddr();
			classNum = "c" + classNum;
			/*
			File file=new File("C:/Program Files/Apache Software Foundation/Tomcat 6.0/webapps/coursefocus/debug.log");
	        if(!file.exists())
	            file.createNewFile();
	        FileOutputStream outfile=new FileOutputStream(file,true);
     		StringBuffer sb=new StringBuffer();
     		sb.append(clientAddr);
     		outfile.write(sb.toString().getBytes("utf-8"));
     		outfile.close();
     		*/
			
			response.setContentType("text/html");
			DataBaseMgr db = new DataBaseMgr();
			String sql = "select * from " + classNum + ";";
			System.out.println(sql);
			ArrayList<Course> courses = db.getCoursesList(sql);
			
			String dateDay = db.getDateDay();
			
			HttpSession session = request.getSession();

			session.setAttribute("courses", courses);
//			for (int i = 0; i< courses.size(); i++)
//			{
//				session.setAttribute("courses" + i, courses.get(i));
//			}
//			session.setAttribute("size", "" + courses.size());
//			response.sendRedirect("/coursefocus/index.jsp");
			DataOutputStream out = new DataOutputStream(response.getOutputStream());
			
			for (int i = 0; i < courses.size(); i++)
			{
				out.writeUTF(courses.get(i).getCourseNum());
				out.writeUTF(courses.get(i).getCourseName());
				out.writeUTF(courses.get(i).getTeacherName());
				out.writeUTF(courses.get(i).getLocation());
				out.writeUTF("endline");
			}
			out.writeUTF("endoffile");
			
			System.out.println(dateDay);
			out.writeUTF(dateDay);
			
			out.close();
		}
}
