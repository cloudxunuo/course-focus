package cn.edu.hit;

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
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import cn.edu.hit.db.DBHelper;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Login extends Activity {
	
	private Button login = null;
	
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
    	boolean tableExists = false;
		EditText classNum_et = (EditText)findViewById(R.id.classNum);
		classNum = classNum_et.getText();
		
		DBHelper dbHelper = new DBHelper(Login.this, "coursefocus");
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.query("sqlite_master", new String[]{"name"}, "name=?", new String[]{"c" + classNum.toString()}, null, null, null);
		while(cursor.moveToNext())
		{
			String name = cursor.getString(cursor.getColumnIndex("name"));
			if (name.equals("c" + classNum.toString()))
			{
				tableExists = true;
			}
		}
		if (classNum != "" && !tableExists)
		{
			httpRequest = new HttpPost(httpUri);
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
			DefaultHttpClient httpClient = new DefaultHttpClient();
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
				{
					String[] colName = {"courseNum","courseName","teacherName","location","editable","reminding"};
					
					String sql = "create table c" + classNum.toString() + "(courseNum varchar(6), courseName varchar(30), teacherName varchar(30), location varchar(16), editable varchar(1), reminding varchar(1))";
					db.execSQL(sql);
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
					}
				}
				else
				{
					System.out.println("there is no information about your class in our database");
				}
			}
			else 
			{
				System.out.println("something error in your web service");
			}
		}
		else
		{
			System.out.println("table exists,or you didn't enter a class number");
		}
    }
}