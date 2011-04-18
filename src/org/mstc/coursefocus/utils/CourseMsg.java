package org.mstc.coursefocus.utils;

import org.mstc.coursefocus.R;

public class CourseMsg 
{
	private String courseNum;
	private String courseName;
	private String teacherName;
	private String location;
	private String editable;
	private String reminding;
	public CourseMsg()
	{
		this.courseNum="";
		this.courseName="";
		this.teacherName="";
		this.location="";
	}
	public String getCourseNum() {
		return this.courseNum;
	}
	public void setCourseNum(String courseNum) {
		this.courseNum = courseNum;
	}
	public String getCourseName() {
		return this.courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getTeacherName() {
		return this.teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getLocation() {
		return this.location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public void setEditable(String editable)
	{
		this.editable = editable;
	}
	public String getEditable()
	{
		return editable;
	}
	public void setReminding(String reminding)
	{
		this.reminding = reminding;
	}
	public String getReminding()
	{
		return this.reminding;
	}
}