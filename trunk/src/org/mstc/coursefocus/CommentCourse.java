package org.mstc.coursefocus;

import org.mstc.coursefocus.utils.WeiboConstant;

import weibo4android.Weibo;
import weibo4android.WeiboException;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CommentCourse extends Activity{
	private Button submit = null;
	private EditText comment = null;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commentcourse);
        
        submit = (Button)findViewById(R.id.submitCommentbtn);
        submit.setOnClickListener(new SubmitListener());
        comment = (EditText)findViewById(R.id.comment);
	}
	
	class SubmitListener implements OnClickListener
	{
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (comment.getText().toString().equals(""))
			{
				Toast toast = Toast.makeText(getApplicationContext(),"不能发送空白信息", Toast.LENGTH_LONG);
			    toast.setGravity(Gravity.CENTER, 0, 0);
			    toast.show();
			}
			else
			{
				Weibo weibo = WeiboConstant.getInstance().getWeibo();
				try {
					weibo.updateStatus(comment.getText().toString());
				} catch (WeiboException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast toast = Toast.makeText(getApplicationContext(),"我们的应用出了问题，信息发送失败！", Toast.LENGTH_LONG);
				    toast.setGravity(Gravity.CENTER, 0, 0);
				    toast.show();
				}
				Toast toast = Toast.makeText(getApplicationContext(),"发布成功！", Toast.LENGTH_LONG);
			    toast.setGravity(Gravity.CENTER, 0, 0);
			    toast.show();
			    comment.setText("");
			}
		}
	}
}
