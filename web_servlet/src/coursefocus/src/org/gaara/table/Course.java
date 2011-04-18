package org.gaara.table;

public class Course {
	private String courseNum = null;
	public String getCourseNum()
	{
		return courseNum;
	}
	public void setCourseNum(String courseNum)
	{
		this.courseNum = courseNum;
	}
	
	private String courseName = null;
	public String getCourseName()
	{
		return courseName;
	}
	public void setcourseName(String courseName)
	{
		this.courseName = courseName;
	}
	
	private String teacherName = null;
	public String getTeacherName()
	{
		return teacherName;
	}
	public void setTeacherName(String teacherName)
	{
		this.teacherName = teacherName;
	}
	
	private String location = null;
	public String getLocation()
	{
		return location;
	}
	public void setLocation(String location)
	{
		this.location = location;
	}
}
