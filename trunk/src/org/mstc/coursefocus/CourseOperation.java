package org.mstc.coursefocus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mstc.coursefocus.db.DBHelper;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class CourseOperation extends ListActivity{
	static final int DIALOG_PAUSED_ID = 0;
	
	private String classNum = null;
	private String courseNum = null;
	private String courseName = null;
    // private List<String> data = new ArrayList<String>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Intent intent = this.getIntent();
        classNum = intent.getStringExtra("classNum");
        courseNum = intent.getStringExtra("courseNum");
        courseName = intent.getStringExtra("courseName");
        
        SimpleAdapter adapter = new SimpleAdapter(this,getData(),R.layout.coursedetails,
                new String[]{"title","img"},
                new int[]{R.id.title,R.id.img});
        setListAdapter(adapter);
    }

	private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("title", getString(R.string.setLocalReminding));
        map.put("img", R.drawable.icon);
        list.add(map);
        
        map = new HashMap<String, Object>();
        map.put("title", getString(R.string.setGCReminding));
        map.put("img", R.drawable.icon);
        list.add(map);
        
        map = new HashMap<String, Object>();
        map.put("title", getString(R.string.findCourseWithSameName));
        map.put("img", R.drawable.icon);
        list.add(map);
        
        DBHelper dbHelper = new DBHelper(CourseOperation.this, "coursefocus");
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query("c" + classNum, new String[]{"editable"}, "courseNum=?", new String[]{courseNum + ""}, null, null, null);
		cursor.moveToNext();
		String editable = cursor.getString(cursor.getColumnIndex("editable"));
		db.close();
		if (editable.equals("1"))
		{
			map = new HashMap<String, Object>();
	        map.put("title", getString(R.string.editThis));
	        map.put("img", R.drawable.icon);
	        list.add(map);
	        
			map = new HashMap<String, Object>();
	        map.put("title", getString(R.string.deleteThis));
	        map.put("img", R.drawable.icon);
	        list.add(map);
		}
        return list;
    }

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		if(position == 0)
		{
			Intent intent = new Intent();
			intent.setClass(CourseOperation.this, SetLocalReminding.class);
			intent.putExtra("classNum", classNum);
			intent.putExtra("courseNum", courseNum);
			startActivity(intent);
		}
		if (position == 2)
		{
			Intent intent = new Intent();
			intent.setClass(CourseOperation.this, CourseDetails.class);
			intent.putExtra("classNum", classNum);
			intent.putExtra("courseName", courseName);
			startActivity(intent);
		}
		if (position == 3)
		{
			DBHelper dbHelper = new DBHelper(CourseOperation.this, "coursefocus");
			String select = "select * from c" + classNum+ " where courseNum = '"+courseNum+"'";
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			try
			{
				Cursor cursor = db.rawQuery(select, null);
				cursor.moveToNext();
				String courseName = cursor.getString(cursor.getColumnIndex("courseName"));
				String teacherName = cursor.getString(cursor.getColumnIndex("teacherName"));
				String location = cursor.getString(cursor.getColumnIndex("location"));
				String editable = cursor.getString(cursor.getColumnIndex("editable"));
				String reminding =cursor.getString(cursor.getColumnIndex("reminding"));
				db.close();
				if(!editable.equals("0"))
				{
					Intent intent = new Intent();
					intent.setClass(CourseOperation.this, EditCourse.class);
					intent.putExtra("courseName", courseName);
					intent.putExtra("classNum", classNum);
					intent.putExtra("courseNum", courseNum);
					intent.putExtra("teacherName",teacherName);
					intent.putExtra("location",location);
					intent.putExtra("reminding",reminding);
					startActivity(intent);	
				}
				else
				{
					AlertDialog.Builder builder = new Builder(CourseOperation.this);
					builder.setMessage("此课程不可编辑");
					builder.setTitle("提示");
					builder.create().show();
				}
			}
			catch(Exception e){}
		}
		if (position == 4)
		{
			showDialog(DIALOG_PAUSED_ID);
		}
		super.onListItemClick(l, v, position, id);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			Intent intent = new Intent();
			intent.setClass(CourseOperation.this, CoursesOfADay.class);
			intent.putExtra("classNum", classNum);
			startActivity(intent);
			CourseOperation.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void searchCourse()
	{
		
	}
	
	private void deleteCourse()
	{
		DBHelper dbHelper = new DBHelper(CourseOperation.this, "coursefocus");
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.delete("c" + classNum, "courseNum=?", new String[]{courseNum+""});
		db.close();
	}
	
	 @Override  
	 protected Dialog onCreateDialog(int id)  
	  
	 {  
	  
	  AlertDialog.Builder builder = new AlertDialog.Builder(this);  
	  
	  builder.setMessage(getString(R.string.confirmMsg))
	  .setCancelable(false)  
	  .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {  
	   public void onClick(DialogInterface dialog, int id) {
		    deleteCourse();
		    Intent intent = new Intent();
			intent.setClass(CourseOperation.this, CoursesOfADay.class);
			intent.putExtra("classNum", classNum);
			startActivity(intent);
			CourseOperation.this.finish();
	   }  
	  })
	  .setNegativeButton(getString(R.string.Cancle), new DialogInterface.OnClickListener() {  
	   public void onClick(DialogInterface dialog, int id) {  
		   dialog.cancel();
	   }  
	  }); 
	  
	  AlertDialog alert = builder.create();  
	  
	  return alert;  
	  
	 }
}
