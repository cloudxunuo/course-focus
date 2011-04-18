package org.gaara.db;

import java.sql.*;
import java.util.*;

import org.gaara.table.Course;

public class DataBaseMgr {
	public ArrayList<Course> getCoursesList (String sql){
		ArrayList<Course> list = new ArrayList<Course>();
		Connection conn = getConnection();
		if(conn!=null){
			try{
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				while(result.next()){
					Course course = new Course();
					course.setCourseNum(result.getString("courseNum"));
					course.setcourseName(result.getString("courseName"));
					course.setTeacherName(result.getString("teacherName"));
					course.setLocation(result.getString("location"));
					list.add(course);
				}
				conn.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}else{
			System.out.println("������ݿ���� !");
		}
		return list;
	}
	
	public String getDateDay()
	{
		String dateDay = "";
		String sql = "select * from dateday";
		Connection conn = getConnection();
		if(conn!=null){
			try{
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				result.next();
				dateDay = result.getString("date") + result.getString("day");
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}else{
			System.out.println("������ݿ���� !");
		}
		return dateDay;
	}
	
	public Connection getConnection(){
		
		Connection conn = null;
		try{
			Class.forName(driver);
			conn = DriverManager.getConnection(url, sqluser,sqlpassword); 
		}catch(Exception e){
			e.printStackTrace();
		}
		return conn;
	}
	
	private String driver = "com.mysql.jdbc.Driver"; // �������
	private String url = "jdbc:mysql://localhost/coursefocus?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true"; // URLָ��Ҫ���ʵ���ݿ���hit
	private String sqluser = "root"; // MySQL����ʱ���û���
	private String sqlpassword = "aijie1314"; // MySQL����ʱ������
}
