package org.mstc.coursefocus;

import java.util.List;

import org.mstc.coursefocus.utils.AsyncImageLoader;
import org.mstc.coursefocus.utils.AsyncImageLoader.ImageCallback;
import org.mstc.coursefocus.utils.BlogHolder;
import org.mstc.coursefocus.utils.BlogInfo;
import org.mstc.coursefocus.utils.WeiboConstant;

import weibo4android.Paging;
import weibo4android.Status;
import weibo4android.Weibo;
import weibo4android.WeiboException;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class HomeComment extends Activity{
	
	private LinearLayout loadingLayout;

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homecomment);
        
        loadingLayout = (LinearLayout)findViewById(R.id.loadingLayout);
        try {
			loadList();
		} catch (WeiboException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	
	private List<BlogInfo> wbList;
    
    private void loadList() throws WeiboException{
    	
        // load from the Internet
    	Paging paging = new Paging();
    	paging.setCount(20);
    	paging.setPage(1);
    	
    	Weibo weibo = WeiboConstant.getInstance().getWeibo();
    	
    	List<Status> userTimeLine = weibo.getUserTimeline(paging);
    	
    	StringBuilder stringBuilder = new StringBuilder("");
		for (Status status : userTimeLine) {
			stringBuilder.append(status.getUser().getScreenName() + "说:"
					+ status.getText() + "\n");
		}
        if(wbList!=null)
        {
            WeiBoAdapater adapater = new WeiBoAdapater();
            ListView Msglist=(ListView)findViewById(R.id.Msglist);
            /*
            Msglist.setOnItemClickListener(new OnItemClickListener(){
                public void onItemClick(AdapterView<?> arg0, View view,int arg2, long arg3) {
                    Object obj=view.getTag();
                    if(obj!=null){
                        String id=obj.toString();
                        Intent intent = new Intent(HomeComment.this,ViewActivity.class);
                        Bundle b=new Bundle();
                        b.putString("key", id);
                        intent.putExtras(b);
                        startActivity(intent);
                    }
                }
            });*/
            Msglist.setAdapter(adapater);
            loadingLayout.setVisibility(View.GONE);
        }
    }
    
  //微博列表Adapater
    public class WeiBoAdapater extends BaseAdapter{

        private AsyncImageLoader asyncImageLoader;
        
        public int getCount() {
            return wbList.size();
        }

        public Object getItem(int position) {
            return wbList.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            asyncImageLoader = new AsyncImageLoader();
            convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.blog, null);
            BlogHolder wh = new BlogHolder();
            wh.wbicon = (ImageView) findViewById(R.id.wbicon);
            wh.wbtext = (TextView) findViewById(R.id.wbtext);
            wh.wbtime = (TextView) findViewById(R.id.wbtime);
            wh.wbuser = (TextView) findViewById(R.id.wbuser);
            wh.wbimage=(ImageView) findViewById(R.id.wbimage);
            BlogInfo wb = wbList.get(position);
            if(wb!=null){
                convertView.setTag(wb.getId());
                wh.wbuser.setText(wb.getUserName());
                wh.wbtime.setText(wb.getTime());
                wh.wbtext.setText(wb.getText(), TextView.BufferType.SPANNABLE);
//                textHighlight(wh.wbtext,new char[]{'#'},new char[]{'#'});
//                textHighlight(wh.wbtext,new char[]{'@'},new char[]{':',' '});
//                textHighlight2(wh.wbtext,"http://"," ");
                
                if(wb.getHaveImage()){
                    wh.wbimage.setImageResource(R.drawable.icon);
                }
                Drawable cachedImage = asyncImageLoader.loadDrawable(wb.getUserIcon(),wh.wbicon, new ImageCallback(){

                    public void imageLoaded(Drawable imageDrawable,ImageView imageView, String imageUrl) {
                        imageView.setImageDrawable(imageDrawable);
                    }
                    
                });
                 if (cachedImage == null) {
                     wh.wbicon.setImageResource(R.drawable.icon);
                    }else{
                        wh.wbicon.setImageDrawable(cachedImage);
                    }
            }
            return convertView;
        }
    }
}
