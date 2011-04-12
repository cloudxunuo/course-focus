package org.mstc.coursefocus.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DBHelper extends SQLiteOpenHelper{
	private static final int VERSION = 1;
	
	public DBHelper(Context context,String name, CursorFactory factory, int version)
	{
		super(context, name, factory, version);	
	}
	
	public DBHelper(Context context, String name, int version)
	{
		this(context, name, null, version);
	}
	
	public DBHelper(Context context, String name)
	{
		this(context, name,VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		System.out.println("create a database");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		System.out.println("update a database");
	}
}
