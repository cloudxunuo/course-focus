package org.mstc.coursefocus;

import org.mstc.coursefocus.db.DBHelper;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SetLocalReminding extends Activity{
	private String classNum;
	private String courseNum;
	private String courseName;
	
	private RadioGroup gb;
	private RadioButton rbOn;
	private RadioButton rbOff;
	
	boolean reminded;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setlocalreminding);
		
		Intent intent = new Intent();
		intent = this.getIntent();
		classNum = intent.getStringExtra("classNum");
		courseNum = intent.getStringExtra("courseNum");
		
		reminded = reminding();
		
		gb = (RadioGroup)findViewById(R.id.localRemindingGB);
		
		rbOn = (RadioButton)gb.getChildAt(0);
		rbOff = (RadioButton)gb.getChildAt(1);
		
		if (reminded)
		{
			rbOn.setChecked(true);
			rbOff.setChecked(false);
		}
		else
		{
			rbOn.setChecked(false);
			rbOff.setChecked(true);
		}
		
		Button saveBtn = (Button)findViewById(R.id.localRemindingSaveButton);
		saveBtn.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (rbOn.isChecked() && reminded)
				{
					finish();
				}
				else if (rbOff.isChecked() && !reminded)
				{
					finish();
				}
				else
				{
					resetReminding();
					finish();
				}
			}});
		Button backBtn = (Button)findViewById(R.id.localRemindingBackButton);
		backBtn.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				finish();
			}});
		
	}
	private boolean reminding()
	{
		DBHelper dbHelper =  new DBHelper(SetLocalReminding.this, "coursefocus");
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query("c" + classNum, new String[]{"courseName,reminding"}, "courseNum=?", 
				new String[]{courseNum+""}, null, null, null);
		cursor.moveToNext();
		String remindingStr = cursor.getString(cursor.getColumnIndex("reminding"));
		courseName = cursor.getString(cursor.getColumnIndex("courseName"));
		db.close();
		if (remindingStr.equals("0"))
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	private void resetReminding()
	{
		String remindOrNot = "";
		if (reminded)
		{
			remindOrNot = "0";
		}
		else
		{
			remindOrNot = "1";
		}
		DBHelper dbHelper =  new DBHelper(SetLocalReminding.this, "coursefocus");
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("reminding", remindOrNot);
		db.update("c" + classNum, values, "courseNum=?", new String[]{courseNum+""});
		db.close();
	}
}
