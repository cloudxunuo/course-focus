package cn.edu.hit.ui;

import cn.edu.hit.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;


public class MainActivity extends Activity {
    /** Called when the activity is first created. */
	private Button OK,Cancle;
	private EditText CourseName,TeacherName,CourseAddress;
	private Spinner CourseTime;
	int Edward_Movie_Dialog = 1;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
       Log.w(ACTIVITY_SERVICE, "kaishi");
        CourseName = (EditText)findViewById(R.id.CourseNameE);
        TeacherName = (EditText)findViewById(R.id.TeacerNameE);
        CourseAddress = (EditText)findViewById(R.id.CourseAddressE);
        CourseTime = (Spinner)findViewById(R.id.CourseSpinner);
        String[] str ={"1-2","3-4","5-6","7-8","9-10","9-11"};
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,str);
        CourseTime.setAdapter(arrayAdapter);
        OK = (Button)findViewById(R.id.OK);
        Cancle = (Button)findViewById(R.id.Cancle);
        View.OnClickListener b_ocl= new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				 showDialog(MainActivity.this);
			}
        	
        };
       OK.setOnClickListener(b_ocl);
        
    }
    private void showDialog(Context context){
    	LayoutInflater inflater = LayoutInflater.from(this); 
    	final View textEntryView = inflater.inflate(R.layout.everycourselayout, null);
    	final TextView CourseNameD=(TextView)textEntryView.findViewById(R.id.Every_CourseNameE);
    	final TextView TeacherNameD=(TextView)textEntryView.findViewById(R.id.Every_TeacherNameE);
    	final TextView CourseTimeD=(TextView)textEntryView.findViewById(R.id.Every_CourseTimeE);
    	final TextView CourseAddressD=(TextView)textEntryView.findViewById(R.id.Every_CourseAddressE);
    	CourseNameD.setText(CourseName.getText().toString());
    	TeacherNameD.setText(TeacherName.getText().toString());
    	CourseTimeD.setText(CourseTime.getSelectedItem().toString());
    	CourseAddressD.setText(CourseAddress.getText().toString());
    	AlertDialog.Builder builder = new AlertDialog.Builder(context);   
    	builder.setCancelable(false); 
    	builder.setIcon(R.drawable.icon);
    	builder.setTitle(R.string.CourseMessage);
    	builder.setView(textEntryView); 
    	 builder.setPositiveButton(R.string.OK,   
                 new DialogInterface.OnClickListener() {   
                     public void onClick(DialogInterface dialog, int whichButton) {   
                         setTitle(R.string.OK);   
                     }   
                 });   
         builder.setNegativeButton(R.string.Cancle,   
                 new DialogInterface.OnClickListener() {   
                     public void onClick(DialogInterface dialog, int whichButton) {   
                         setTitle(R.string.Cancle);   
                     }   
                 });   
          
         builder.show(); 

    }
}
