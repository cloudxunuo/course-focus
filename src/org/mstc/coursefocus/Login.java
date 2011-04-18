package org.mstc.coursefocus;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.mstc.coursefocus.db.DBHelper;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
	private Toast toast = null;
	
	private Button login = null;
	
	SQLiteDatabase db = null;
	private HttpPost httpRequest = null;
	private HttpResponse httpResponse = null;
	
	private String courseInfo = "";
	
	private static final int MAX_RESPONSE_SIZE = 3486;
	private static final String httpUri = "http://10.0.2.2:8080/coursefocus/myrequest";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.login);
        login = (Button)findViewById(R.id.loginButton);
        login.setOnClickListener(new OnClickListener(){
        	public void onClick(View view)
        	{
        		loginClick();
        	}
        });
    }
    
    public void loginClick()
    {
    	CharSequence classNum = "";
    	
		EditText classNum_et = (EditText)findViewById(R.id.classNum);
		classNum = classNum_et.getText();
		
		//判断是否输入班号
		if (classNum.toString().equals(""))
		{
			toast = Toast.makeText(getApplicationContext(),"请输入您的班号", Toast.LENGTH_SHORT);
		    toast.setGravity(Gravity.CENTER, 0, 0);
		    toast.show();
		    return;
		}
		
		//检查该班课表信息是否存在于本地数据库
		if (!checkTableExist(classNum.toString()))
		{
			getClassInfoFromWebServer(classNum.toString());
		}
		else
		{
			//数据存在的话，直接跳转到课表显示界面
			turnAllClass(classNum.toString());
		}
    }
    
    public boolean checkTableExist(String classNum)
    {
    	DBHelper dbHelper = new DBHelper(Login.this, "coursefocus");
		db = dbHelper.getWritableDatabase();
		Cursor cursor = db.query("sqlite_master", new String[]{"name"}, "name=?", new String[]{"c" + classNum}, null, null, null);
		while(cursor.moveToNext())
		{
			String name = cursor.getString(cursor.getColumnIndex("name"));
			if (name.equals("c" + classNum.toString()))
			{
				db.close();
				return true;
			}
		}
		db.close();
		return false;
    }
    
    public void getClassInfoFromWebServer(String classNum)
    {
    	httpRequest = new HttpPost(httpUri);
		
		HttpParams httpParams = new BasicHttpParams();
		int timeoutConnection = 3000; 
		HttpConnectionParams.setConnectionTimeout(httpParams, timeoutConnection); 
		//Set the default socket timeout (SO_TIMEOUT)  
		// in milliseconds which is the timeout for waiting for data. 
		int timeoutSocket = 5000; 
		HttpConnectionParams.setSoTimeout(httpParams, timeoutSocket);
		
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("classNum", classNum.toString()));
		try
		{
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		} 
		catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
		try {
			httpResponse = httpClient.execute(httpRequest);
		} 
		catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (httpResponse == null)
		{
			toast = Toast.makeText(getApplicationContext(),"对不起！连接超时，网络无响应，数据无法获得", Toast.LENGTH_LONG);
		    toast.setGravity(Gravity.CENTER, 0, 0);
		    toast.show();
			return;
		}
		//如果网络连接正常，返回200
		if (httpResponse.getStatusLine().getStatusCode() == 200)
		{
			byte[] data = new byte[MAX_RESPONSE_SIZE];
			try {
				data = EntityUtils.toByteArray((HttpEntity)httpResponse.getEntity());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("error");
				e.printStackTrace();
			}
			
			//检查班级信息是否存在于WEB服务器的数据库中
			boolean classExists = false;
			ByteArrayInputStream bais = new ByteArrayInputStream(data);
			DataInputStream dis = new DataInputStream(bais);
			try {
				courseInfo = dis.readUTF();
				if (!courseInfo.equals("endoffile"))
				{
					classExists = true;
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (classExists)
			{	//将数据存储到本地数据库
				DBHelper dbHelper = new DBHelper(Login.this, "coursefocus");
				db = dbHelper.getWritableDatabase();
				
				String[] colName = {"courseNum","courseName","teacherName","location","editable","reminding"};
				
				String sql = "create table c" + classNum.toString() + "(courseNum varchar(6) primary key, courseName varchar(30), teacherName varchar(30), location varchar(16), editable varchar(1), reminding varchar(1))";
				db.execSQL(sql);
				
				// 将课表信息存入本地数据库
				try {
					while(!courseInfo.equals("endoffile"))
					{
						int  i = 0;
						ContentValues values = new ContentValues();
						while (!courseInfo.equals("endline") && !courseInfo.equals("endoffile"))
						{
							values.put(colName[i], courseInfo);
							i++;										
							
							courseInfo = dis.readUTF();
						}
						if (!courseInfo.equals("endoffile"))
						{
							values.put("editable", "0");
							values.put("reminding", "1");
							db.insert("c" + classNum.toString(), null, values);
						}
						courseInfo = dis.readUTF();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("error");
					e.printStackTrace();
					return ;
				}
				
				//将日期与周数的对应关系写入数据库
				String dateDay = "";
				try {
					dateDay = dis.readUTF();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					return ;
				}
				try
				{	
					sql = "create table dateday(date varchar(8) primary key, day varchar(3))";
					db.execSQL(sql);
					ContentValues values = new ContentValues();
					values.put("date", dateDay.substring(0,8));
					values.put("day", dateDay.substring(8,11));
					db.insert("dateday", null, values);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				
				db.close();
				
				//并跳转到课表显示界面
				turnAllClass(classNum.toString());
			}
			else
			{
				toast = Toast.makeText(getApplicationContext(),"对不起！我们的服务器中没有你的数据", Toast.LENGTH_LONG);
			    toast.setGravity(Gravity.CENTER, 0, 0);
			    toast.show();
			}
		}
		else 
		{
			toast = Toast.makeText(getApplicationContext(),"网络异常 ，请稍后再试", Toast.LENGTH_LONG);
		    toast.setGravity(Gravity.CENTER, 0, 0);
		    toast.show();
		}
    }
    
    public void turnAllClass(String classNum)
    {
    	Intent intent = new Intent();
		intent.setClass(Login.this, CoursesOfADay.class);
		intent.putExtra("classNum", classNum);
		startActivity(intent);
		Login.this.finish();
    }
}