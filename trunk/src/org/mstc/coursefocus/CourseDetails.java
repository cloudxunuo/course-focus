package org.mstc.coursefocus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mstc.coursefocus.db.DBHelper;
import org.mstc.coursefocus.utils.CourseMsg;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.SimpleAdapter;

public class CourseDetails extends ListActivity {
		private String classNum = null;
		private String courseName = null;
	    // private List<String> data = new ArrayList<String>();
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        Intent intent = this.getIntent();
	        classNum = intent.getStringExtra("classNum");
	        courseName = intent.getStringExtra("courseName");
	        
	        SimpleAdapter adapter = new SimpleAdapter(this,getData(),R.layout.coursedetails,
	                new String[]{"title","info","img"},
	                new int[]{R.id.title,R.id.info,R.id.img});
	        setListAdapter(adapter);
	    }

		private List<Map<String, Object>> getData() {
	        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	        Map<String, Object> map = new HashMap<String, Object>();
	        List<CourseMsg> msgList = getCourseMsg();
	        if (msgList == null || msgList.size() == 0)
	        {
	        	System.out.println("happen");
	        	return null;
	        }
	        
	        map.put("title", getString(R.string.CourseName));
	        map.put("info", courseName);
	        map.put("img", R.drawable.icon);
	        list.add(map);
	        
	        map = new HashMap<String, Object>();
	        map.put("title", getString(R.string.TeacherName));
	        map.put("info", msgList.get(0).getTeacherName());
	        map.put("img", R.drawable.icon);
	        list.add(map);
	        
	        for (int i = 0; i < msgList.size(); i++)
	        {
	        	String courseNum = msgList.get(i).getCourseNum();
	        	String weekday = "";
	        	switch(courseNum.charAt(4))
	        	{
		        	case '1':weekday = getString(R.string.Mon);break;
		        	case '2':weekday = getString(R.string.Tue);break;
		        	case '3':weekday = getString(R.string.Wed);break;
		        	case '4':weekday = getString(R.string.Thur);break;
		        	case '5':weekday = getString(R.string.Fri);break;
		        	case '6':weekday = getString(R.string.Sat);break;
		        	case '7':weekday = getString(R.string.Sun);break;
	        	}
	        	String time="";
	        	switch(courseNum.charAt(5))
	        	{
		        	case '1':time = getString(R.string.CourseT_1);break;
		        	case '2':time = getString(R.string.CourseT_2);break;
		        	case '3':time = getString(R.string.CourseT_3);break;
		        	case '4':time = getString(R.string.CourseT_4);break;
		        	case '5':time = getString(R.string.CourseT_5);break;
	        	}
	        	String coursePlan = "";
	        	coursePlan = courseNum.substring(0, 2) + getString(R.string.to) + courseNum.substring(2, 4) +
	        		getString(R.string.week) + "  " + weekday + " \n" + time + msgList.get(i).getLocation();
	        	map = new HashMap<String, Object>();
		        map.put("title", getString(R.string.coursePlan));
		        map.put("info", coursePlan);
		        map.put("img", R.drawable.icon);
		        list.add(map);
	        }
	        return list;
	    }
	    
	    private List<CourseMsg> getCourseMsg()
	    {	    	
	    	List<CourseMsg> msgList = new ArrayList<CourseMsg>();
	    	DBHelper dbHelper = new DBHelper(CourseDetails.this, "coursefocus");
	    	SQLiteDatabase db = dbHelper.getReadableDatabase();
	    	Cursor cursor = db.query("c" + classNum, new String[]{"courseNum","teacherName","location"}, "courseName=?", 
	    			new String[]{courseName+""},null, null, null);
	    	while (cursor.moveToNext())
    		{
	    		CourseMsg msg = new CourseMsg();
    			msg.setCourseNum(cursor.getString(cursor.getColumnIndex("courseNum")));
    			msg.setTeacherName(cursor.getString(cursor.getColumnIndex("teacherName")));
    			msg.setLocation(cursor.getString(cursor.getColumnIndex("location")));
    			msgList.add(msg);
    		}
	    	db.close();
	    	return msgList;
	    }
	}
