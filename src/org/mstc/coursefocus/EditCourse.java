package org.mstc.coursefocus;

import org.mstc.coursefocus.db.DBHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EditCourse extends Activity {
	/** Called when the activity is first created. */
	private String classNum = null;
	private int weekday;
	private int classtime;
	private Button OK, Cancel;
	private EditText CourseName, TeacherName, CourseAddress;
	private Spinner CourseTime, CourseWeeks1, CourseWeeks2, CourseDate;
	int Edward_Movie_Dialog = 1;

	String[] time = null;
	String[] week = null;
	private String[] Date = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editcourse);
		Intent intent = this.getIntent();
		classNum = intent.getStringExtra("classNum");
		weekday = intent.getIntExtra("weekday",2);
		classtime = intent.getIntExtra("time", 1);
		
		initWidget();
		Date = new String[] { getString(R.string.Mon), getString(R.string.Tue),
				getString(R.string.Wed), getString(R.string.Thur),
				getString(R.string.Fri), getString(R.string.Sat),
				getString(R.string.Sun) };
		week = new String[] { getString(R.string.No_1),getString(R.string.No_2),
				getString(R.string.No_3),getString(R.string.No_4),
				getString(R.string.No_5),getString(R.string.No_6),
				getString(R.string.No_7),getString(R.string.No_8),
				getString(R.string.No_9),getString(R.string.No_10),
				getString(R.string.No_11),getString(R.string.No_12),
				getString(R.string.No_13),getString(R.string.No_14),
				getString(R.string.No_15),getString(R.string.No_16),
				getString(R.string.No_17),getString(R.string.No_18),
				getString(R.string.No_19),getString(R.string.No_20),
				getString(R.string.No_21)};
		time = new String[] { getString(R.string.CourseT_1),getString(R.string.CourseT_2),
				getString(R.string.CourseT_3),getString(R.string.CourseT_4),
				getString(R.string.CourseT_5),getString(R.string.CourseT_6)};
		initListAdapters();
	}

	private void initListAdapters() {
		ArrayAdapter<Object> arrayAdapter = new ArrayAdapter<Object>(this,
				android.R.layout.simple_spinner_item, time);
		ArrayAdapter<Object> arrayAdapter1 = new ArrayAdapter<Object>(this,
				android.R.layout.simple_spinner_item, week);
		ArrayAdapter<Object> arrayAdapter2 = new ArrayAdapter<Object>(this,
				android.R.layout.simple_spinner_item, Date);

		CourseTime.setAdapter(arrayAdapter);
		CourseTime.setSelection(classtime - 1);
		CourseWeeks1.setAdapter(arrayAdapter1);
		CourseWeeks2.setAdapter(arrayAdapter1);
		CourseWeeks2.setSelection(20);
		CourseDate.setAdapter(arrayAdapter2);
		CourseDate.setSelection(weekday);
	}

	private void initWidget() {
		CourseName = (EditText) findViewById(R.id.CourseNameE);
		TeacherName = (EditText) findViewById(R.id.TeacerNameE);
		CourseAddress = (EditText) findViewById(R.id.CourseAddressE);
		CourseTime = (Spinner) findViewById(R.id.CourseSpinner);
		CourseWeeks1 = (Spinner) findViewById(R.id.CourseWeeksSpinner1);
		CourseWeeks2 = (Spinner) findViewById(R.id.CourseWeeksSpinner2);
		CourseDate = (Spinner) findViewById(R.id.CourseDateSpinner);
		OK = (Button) findViewById(R.id.OK);
		Cancel = (Button) findViewById(R.id.Cancle);

		OK.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				boolean infoComplete = true;
				String tip = "";
				// TODO Auto-generated method stub
				if (CourseName.getText().toString().equals(""))
				{
					infoComplete = false;
					tip = "请输入课程名";
				}
				if (TeacherName.getText().toString().equals(""))
				{
					infoComplete = false;
					tip = "请输入教师名";
				}
				if (CourseAddress.getText().toString().equals(""))
				{
					infoComplete = false;
					tip = "请输入上课地点";
				}
				if (infoComplete)
				{
					showDialog(EditCourse.this);
				}
				else
				{
					Toast toast = Toast.makeText(getApplicationContext(),tip, Toast.LENGTH_SHORT);
				    toast.setGravity(Gravity.CENTER, 0, 0);
				    toast.show();
				}
			}
		});
		
		Cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(EditCourse.this, CoursesOfADay.class);
				intent.putExtra("classNum", classNum);
				startActivity(intent);
				EditCourse.this.finish();
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			Intent intent = new Intent();
			intent.setClass(EditCourse.this, CoursesOfADay.class);
			intent.putExtra("classNum", classNum);
			startActivity(intent);
			EditCourse.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void showDialog(Context context) {
		LayoutInflater inflater = LayoutInflater.from(this);
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
		CourseDateD.setText(CourseDate.getSelectedItem().toString());
		CourseNameD.setText(CourseName.getText().toString());
		TeacherNameD.setText(TeacherName.getText().toString());
		CourseTimeD.setText(CourseTime.getSelectedItem().toString());
		CourseAddressD.setText(CourseAddress.getText().toString());
		CourseWeeksD.setText(CourseWeeks1.getSelectedItem().toString() + "-"
				+ CourseWeeks2.getSelectedItem().toString() + getString(R.string.week));
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(true);
		builder.setIcon(R.drawable.icon);
		builder.setTitle(R.string.CourseMessage);
		builder.setView(textEntryView);
		builder.setPositiveButton(R.string.OK,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						DBHelper dbHelper = new DBHelper(EditCourse.this, "coursefocus");
						SQLiteDatabase db = dbHelper.getWritableDatabase();
						ContentValues values = new ContentValues();
						
						String courseNum = "";
						String DateNum = "";
						for (int j = 0; j < 7; j++) {
							if (CourseDate.getSelectedItem().toString()
									.equals(Date[j])) {
								DateNum = (j + 1) + "";
							}
						}
						String classTime = "0";
						for (int i = 0; i < 6; i++) {
							if (CourseTime.getSelectedItem().toString()
									.equals(time[i])) {
								if (i == 5)
									classTime = i + "";
								else
									classTime = (i + 1) + "";
								break;
							}
						}
						courseNum = CourseWeeks1.getSelectedItem().toString()
								+ CourseWeeks2.getSelectedItem().toString()
								+ DateNum + classTime;
						values.put("courseNum", courseNum);
						values.put("courseName", CourseName.getText().toString());
						values.put("teacherName", TeacherName.getText().toString());
						values.put("location", CourseAddress.getText().toString());
						values.put("editable", "1");
						values.put("reminding", "1");
						try
						{
							db.insert("c" + classNum, null, values);
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
						db.close();
						
						Intent intent = new Intent();
						intent.setClass(EditCourse.this, CoursesOfADay.class);
						intent.putExtra("classNum", classNum);
						startActivity(intent);
						EditCourse.this.finish();
					}
				});
		builder.setNegativeButton(R.string.Cancle,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				});

		builder.show();

	}
}