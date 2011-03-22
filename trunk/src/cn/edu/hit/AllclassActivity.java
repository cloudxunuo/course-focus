package cn.edu.hit;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AllclassActivity extends Activity {
    /** Called when the activity is first created. */
	private ArrayList<Button> buttons = null;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.allclass);
        buttons = new ArrayList<Button>();
        buttons.add((Button)findViewById(R.id.button101));
        buttons.add((Button)findViewById(R.id.button201));
        buttons.add((Button)findViewById(R.id.button301));
        buttons.add((Button)findViewById(R.id.button401));
        buttons.add((Button)findViewById(R.id.button501));
        buttons.add((Button)findViewById(R.id.button601));
        buttons.add((Button)findViewById(R.id.button701));

        buttons.add((Button)findViewById(R.id.button102));
        buttons.add((Button)findViewById(R.id.button202));
        buttons.add((Button)findViewById(R.id.button302));
        buttons.add((Button)findViewById(R.id.button402));
        buttons.add((Button)findViewById(R.id.button502));
        buttons.add((Button)findViewById(R.id.button602));
        buttons.add((Button)findViewById(R.id.button702));
        
        buttons.add((Button)findViewById(R.id.button103));
        buttons.add((Button)findViewById(R.id.button203));
        buttons.add((Button)findViewById(R.id.button303));
        buttons.add((Button)findViewById(R.id.button403));
        buttons.add((Button)findViewById(R.id.button503));
        buttons.add((Button)findViewById(R.id.button603));
        buttons.add((Button)findViewById(R.id.button703));
        
        buttons.add((Button)findViewById(R.id.button104));
        buttons.add((Button)findViewById(R.id.button204));
        buttons.add((Button)findViewById(R.id.button304));
        buttons.add((Button)findViewById(R.id.button404));
        buttons.add((Button)findViewById(R.id.button504));
        buttons.add((Button)findViewById(R.id.button604));
        buttons.add((Button)findViewById(R.id.button704));
        
        buttons.add((Button)findViewById(R.id.button105));
        buttons.add((Button)findViewById(R.id.button205));
        buttons.add((Button)findViewById(R.id.button305));
        buttons.add((Button)findViewById(R.id.button405));
        buttons.add((Button)findViewById(R.id.button505));
        buttons.add((Button)findViewById(R.id.button605));
        buttons.add((Button)findViewById(R.id.button705));
        
        buttons.add((Button)findViewById(R.id.button106));
        buttons.add((Button)findViewById(R.id.button206));
        buttons.add((Button)findViewById(R.id.button306));
        buttons.add((Button)findViewById(R.id.button406));
        buttons.add((Button)findViewById(R.id.button506));
        buttons.add((Button)findViewById(R.id.button606));
        buttons.add((Button)findViewById(R.id.button706));
        
        buttons.add((Button)findViewById(R.id.button107));
        buttons.add((Button)findViewById(R.id.button207));
        buttons.add((Button)findViewById(R.id.button307));
        buttons.add((Button)findViewById(R.id.button407));
        buttons.add((Button)findViewById(R.id.button507));
        buttons.add((Button)findViewById(R.id.button607));
        buttons.add((Button)findViewById(R.id.button707));
        
        buttons.add((Button)findViewById(R.id.button108));
        buttons.add((Button)findViewById(R.id.button208));
        buttons.add((Button)findViewById(R.id.button308));
        buttons.add((Button)findViewById(R.id.button408));
        buttons.add((Button)findViewById(R.id.button508));
        buttons.add((Button)findViewById(R.id.button608));
        buttons.add((Button)findViewById(R.id.button708));
        
        buttons.add((Button)findViewById(R.id.button109));
        buttons.add((Button)findViewById(R.id.button209));
        buttons.add((Button)findViewById(R.id.button309));
        buttons.add((Button)findViewById(R.id.button409));
        buttons.add((Button)findViewById(R.id.button509));
        buttons.add((Button)findViewById(R.id.button609));
        buttons.add((Button)findViewById(R.id.button709));
        
        buttons.add((Button)findViewById(R.id.button110));
        buttons.add((Button)findViewById(R.id.button210));
        buttons.add((Button)findViewById(R.id.button310));
        buttons.add((Button)findViewById(R.id.button410));
        buttons.add((Button)findViewById(R.id.button510));
        buttons.add((Button)findViewById(R.id.button610));
        buttons.add((Button)findViewById(R.id.button710));
        
        buttons.add((Button)findViewById(R.id.button111));
        buttons.add((Button)findViewById(R.id.button211));
        buttons.add((Button)findViewById(R.id.button311));
        buttons.add((Button)findViewById(R.id.button411));
        buttons.add((Button)findViewById(R.id.button511));
        buttons.add((Button)findViewById(R.id.button611));
        buttons.add((Button)findViewById(R.id.button711));
    }
    class listener implements View.OnClickListener
    {

		public void onClick(View v) 
		{
			// TODO Auto-generated method stub
			
		}
    	
    }
}
