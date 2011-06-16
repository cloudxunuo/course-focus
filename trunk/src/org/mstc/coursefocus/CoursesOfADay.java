package org.mstc.coursefocus;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.mstc.coursefocus.db.DBHelper;
import org.mstc.coursefocus.utils.CourseMsg;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CoursesOfADay extends Activity implements OnTouchListener,
		OnGestureListener {
	String classNum = null;
	int curWeekofSchool = 0;
	int today = 0;
	
	GestureDetector mGestureDetector;
	private DBHelper dbHelper = new DBHelper(this,"coursefocus");
	private static final int FLING_MIN_DISTANCE = 100;
	private static final int FLING_MIN_VELOCITY = 200;
	private MyLayout[] weekLayout = new MyLayout[7];
	private LinearLayout flayout = null;
	private SQLiteDatabase db = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.coursesofaday);
		
		Intent intent = this.getIntent();
		classNum = intent.getStringExtra("classNum");
		
		mGestureDetector = new GestureDetector(this);
		flayout =(LinearLayout)findViewById(R.id.layout);
		flayout.setOnTouchListener(this);
		flayout.setLongClickable(true);
		for(int i=0;i<7;i++)
		{
			weekLayout[i] = new MyLayout(this);
		}
		init();
		flayout.addView(weekLayout[today]);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, 0, getString(R.string.viewComment));
		menu.add(0, 1, 1, getString(R.string.logout));
		menu.add(0, 2, 2, getString(R.string.contactUs));
		return true;
	}

	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) { 
		   case 0:
			   viewComment();
		       return true; 
		   case 1:
			   logout();
		       return true; 
		   case 2: 
			   contactUs();
		       return true; 
		   } 
		   return false; 
	}

	public boolean init()
	{		
		db = dbHelper.getReadableDatabase();
		
		setTodayIndex();
		
		String select = "select * from c" + classNum;
    	try
    	{
    		Cursor cursor = db.rawQuery(select, null);
    		while (cursor.moveToNext()) 
    		{
    			CourseMsg msg = new CourseMsg();
    			msg.setCourseNum(cursor.getString(0));
    			msg.setCourseName(cursor.getString(1));
    			msg.setTeacherName(cursor.getString(2));
    			msg.setLocation(cursor.getString(3));
    			msg.setEditable(cursor.getString(4));
    			msg.setReminding(cursor.getString(5));
    			AddCourse(msg);
    		}
    		db.close();
    		return true;
    	}
    	catch(Exception e)
    	{
    		db.close();
    		e.printStackTrace();
    		return false;
    	}
	}
	
	
	private void viewComment()
	{
		Intent intent = new Intent();
		intent.setClass(CoursesOfADay.this, HomeComment.class);
		startActivity(intent);
	}
	
	private void logout()
	{
		Intent intent = new Intent();
		intent.setClass(CoursesOfADay.this, Login.class);
		SharedPreferences preference = getSharedPreferences("person",Context.MODE_PRIVATE);
		Editor edit = preference.edit();
		edit.remove("rememberMe");
		edit.commit();
		
		startActivity(intent);
		CoursesOfADay.this.finish();
	}
	
	private void contactUs()
	{
		Intent intent = new Intent();
		intent.setClass(CoursesOfADay.this, ContactUs.class);
		startActivity(intent);
	}
	
	private void setTodayIndex()
	{		
		String select = "select * from dateday";
		db.rawQuery(select, null);
		try
    	{
    		Cursor cursor = db.rawQuery(select, null);
    		cursor.moveToNext();
    		String date = cursor.getString(0);
    		String day = cursor.getString(1);
    		
    		int year2 = Integer.parseInt(date.substring(0,4));
    		int month2 = Integer.parseInt(date.substring(4,6));
    		int day2 = Integer.parseInt(date.substring(6,8));
    		
    		int week = Integer.parseInt(day.substring(0,2));
    		
    		//构造当前时间的日历
    		String nowDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
    		Calendar cur = new GregorianCalendar();
    		int year1 = Integer.parseInt(nowDate.substring(0, 4));
    		int month1 = Integer.parseInt(nowDate.substring(4, 6));
    		int day1 = Integer.parseInt(nowDate.substring(6, 8));
    		cur.set(year1,month1,day1);
    		
    		//构造开学时间的日历
    		Calendar start = new GregorianCalendar();
    		start.set(year2, month2, day2);
    		
    		int curWeek = cur.get(Calendar.WEEK_OF_YEAR);
    		int startWeek = start.get(Calendar.WEEK_OF_YEAR);
    		
    		this.curWeekofSchool = week + curWeek - startWeek;
    		
    		Date date_ = new Date();
    		cur.setTime(date_);
    		int curWeekday = cur.get(Calendar.DAY_OF_WEEK);
    		if (curWeekday == 1)
    		{
    			today = 6;
    		}
    		else
    		{
    			today = curWeekday - 2;
    		}
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
	}

	private void AddCourse(CourseMsg msg)
	{
	    	String num = msg.getCourseNum();
	    	String sstartWeek = num.substring(0, 2);
	    	String sendWeek = num.substring(2, 4);
	    	int startWeek = Integer.parseInt(sstartWeek);
	    	int endWeek = Integer.parseInt(sendWeek);
	    	if (curWeekofSchool >= startWeek && curWeekofSchool <= endWeek)
	    	{
	    		String sday = String.valueOf(num.charAt(4));
		    	String stime = String.valueOf(num.charAt(5));
	    		int day = Integer.parseInt(sday);
		    	day--;
		    	int time = Integer.parseInt(stime);
		    	MyView view = new MyView(this,msg);
				view.setGravity(Gravity.CENTER);
				weekLayout[day].addView(time,msg);
	    	}
	}
	public boolean onTouch(View v, MotionEvent event) {
		// OnGestureListener will analyzes the given motion event
		return mGestureDetector.onTouchEvent(event);
	}
	private void clearBackColor()
	{
		int i = 0;
		do
		{
			MyView myView = (MyView)weekLayout[today].getChildAt(i);
			if (myView == null)
					break;
			myView.setBackgroundColor(Color.BLACK);
			i++;
		}while(true);
	}

	public boolean onDown(MotionEvent e) {
		// do nothing
		return false;
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// 参数解释：
		// e1：第1个ACTION_DOWN MotionEvent
		// e2：最后一个ACTION_MOVE MotionEvent
		// velocityX：X轴上的移动速度，像素/秒
		// velocityY：Y轴上的移动速度，像素/秒
		// 触发条件 ：
		// X轴的坐标位移大于FLING_MIN_DISTANCE，且移动速度大于FLING_MIN_VELOCITY个像素/秒
		if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE/3
				&& Math.abs(velocityX) > FLING_MIN_VELOCITY/3) {
			// Fling left
			flayout.removeAllViews();
			today=(today+1)%7;
			System.out.println(today);
			flayout.addView(weekLayout[today]);
		} else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE/3
				&& Math.abs(velocityX) > FLING_MIN_VELOCITY/3) {
			// Fling right
			flayout.removeAllViews();
			today=(today+6)%7;
			System.out.println(today);
			flayout.addView(weekLayout[today]);
		}
		return false;
	}

	public void onLongPress(MotionEvent e) {
		// do nothing

	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// do nothing
		return false;
	}

	public void onShowPress(MotionEvent e) {
		// do nothing

	}

	public boolean onSingleTapUp(MotionEvent e) {
		// do nothing
		return false;
	}
	
	
	class MyView extends TextView
	{
		CourseMsg course;
		boolean courseFlag = false;
		int index;
		public MyView(Context context,int index) {
			super(context);
			this.setHeight(70);
			this.setWidth(120);
			this.index = index;
		}
		public MyView(Context context,CourseMsg msg)
		{
			super(context);
			this.courseFlag = true;
			this.setGravity(Gravity.CENTER);
			this.setBackgroundResource(R.drawable.bg);
			this.setHeight(70);
			this.setWidth(120);
			this.addMsg(msg);
			String courseNum = msg.getCourseNum();
			this.index = Integer.parseInt(courseNum.substring(5, 6));
		}
		
		@Override
		public boolean onTouchEvent(MotionEvent event) 
		{
			// TODO Auto-generated method stub
			this.setBackgroundColor(Color.YELLOW);
			if (courseFlag)
			{
				showCourseInfo();
			}
			else
			{
				Intent intent = new Intent();
				intent.setClass(CoursesOfADay.this, EditCourse.class);
				intent.putExtra("classNum", classNum);
				intent.putExtra("weekday", today);
				intent.putExtra("time", index);
				finish();
				startActivity(intent);
			}
			return super.onTouchEvent(event);
		}
		public void addMsg(CourseMsg msg)
		{
			String reminding = null;
			if (msg.getReminding().equals("1"))
			{
				reminding = "闹钟提醒";
			}
			if(msg.getReminding().equals("0"))
			{
				reminding = "";
			}
			this.setText(msg.getCourseName()+"\n"+msg.getTeacherName()+"\n"+msg.getLocation() + "\n" + reminding);
			course = msg;
		}
		protected void onDraw(Canvas canvas)
		{
			super.onDraw(canvas);
			Paint paint = new Paint();
			paint.setColor(Color.WHITE);
			canvas.drawLine(0,0 , this.getWidth(), 0, paint);
			canvas.drawLine(0,0 , 0,this.getHeight()-1, paint);
			canvas.drawLine(this.getWidth()-1,0 , this.getWidth()-1, this.getHeight()-1, paint);
			canvas.drawLine(0,this.getHeight()-1, this.getWidth()-1, this.getHeight()-1, paint);
		}
		public void bigger()
		{
			this.setHeight(120);
			this.setWidth(120);
		}
		
		private void showCourseInfo()
		{
			LayoutInflater inflater = LayoutInflater.from(CoursesOfADay.this);
			final View textEntryView = inflater.inflate(R.layout.confirmeditcourse,
					null);
			final TextView CourseNameD = (TextView) textEntryView
					.findViewById(R.id.Every_CourseNameE);
			final TextView TeacherNameD = (TextView) textEntryView
					.findViewById(R.id.Every_TeacherNameE);
			final TextView CourseTimeD = (TextView) textEntryView
					.findViewById(R.id.Every_CourseTimeE);
			final TextView CourseAddressD = (TextView) textEntryView
					.findViewById(R.id.Every_CourseAddressE);
			final TextView CourseWeeksD = (TextView) textEntryView
					.findViewById(R.id.Every_CourseWeeksE);
			final TextView CourseDateD = (TextView) textEntryView
					.findViewById(R.id.Every_CourseDateE);
			CourseNameD.setText(course.getCourseName());
			TeacherNameD.setText(course.getTeacherName());
			CourseAddressD.setText(course.getLocation());
			String courseNum = course.getCourseNum();
			String time="";
	    	switch(courseNum.charAt(5))
	    	{
	        	case '1':time = getString(R.string.CourseT_1);break;
	        	case '2':time = getString(R.string.CourseT_2);break;
	        	case '3':time = getString(R.string.CourseT_3);break;
	        	case '4':time = getString(R.string.CourseT_4);break;
	        	case '5':time = getString(R.string.CourseT_5);break;
	    	}
	    	CourseTimeD.setText(time);
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
	    	CourseDateD.setText(weekday);
	    	CourseWeeksD.setText(courseNum.substring(0, 2) + getString(R.string.to) + courseNum.substring(2, 4) +
	        		getString(R.string.week));
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
        	
			AlertDialog.Builder builder = new AlertDialog.Builder(CoursesOfADay.this);
			builder.setCancelable(false);
			builder.setIcon(R.drawable.icon);
			builder.setTitle(R.string.CourseMessage);
			builder.setView(textEntryView);
			builder.setPositiveButton(R.string.comment,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							clearBackColor();
							Intent intent = new Intent();
							intent.setClass(CoursesOfADay.this, CommentCourse.class);
							intent.putExtra("classNum", classNum);
							intent.putExtra("courseNum", course.getCourseNum());
							startActivity(intent);
						}
						});
			builder.setNeutralButton(R.string.more,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							clearBackColor();
							Intent intent = new Intent();
							intent.setClass(CoursesOfADay.this, CourseOperation.class);
							intent.putExtra("classNum", classNum);
							intent.putExtra("courseNum", course.getCourseNum());
							intent.putExtra("courseName", course.getCourseName());
							startActivity(intent);
							CoursesOfADay.this.finish();
						}
						});
			builder.setNegativeButton(R.string.back,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							clearBackColor();
						}
						});
			builder.show();
		}
	}
	
	class MyLayout extends LinearLayout
	{
		public MyLayout(Context context) {
			super(context);
			this.setOnTouchListener(CoursesOfADay.this);
			this.setLongClickable(true);
			this.setOrientation(LinearLayout.VERTICAL);
			for(int i=0;i<5;i++)
			{
				MyView views = new MyView(CoursesOfADay.this,i + 1);
		
				this.addView(views);
			}
		}
		
		public void addView(int time,CourseMsg msg)
		{
			MyView view = new MyView(CoursesOfADay.this,msg);
			time--;
			this.removeViews(time, 1);
			this.addView(view,time);
		}
	}
}