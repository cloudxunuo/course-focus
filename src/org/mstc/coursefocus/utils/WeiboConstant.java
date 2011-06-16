package org.mstc.coursefocus.utils;

import weibo4android.Weibo;
import weibo4android.http.AccessToken;

public class WeiboConstant {
	private static Weibo weibo = null;
	private static WeiboConstant instance = null;
	private AccessToken accessToken = new AccessToken("8e938c6baf297b903f773e79961ccb6a","1c5757e15c86b86d2169132aacb04d04");
	
	WeiboConstant()
	{
		System.setProperty("weibo4j.oauth.consumerKey", "4215586967");
    	System.setProperty("weibo4j.oauth.consumerSecret", "89c7340537b55ab12131db706460e02e");
	}
	
	public static synchronized WeiboConstant getInstance(){
		if(instance==null)
			instance= new WeiboConstant();
		return instance;
	}
	public Weibo getWeibo(){
		if(weibo==null)
		{
			weibo= new Weibo();
			weibo.setToken(accessToken);
			Weibo.CONSUMER_KEY = "4215586967";
	    	Weibo.CONSUMER_SECRET = "89c7340537b55ab12131db706460e02e";
		}
		return weibo;
	}
}
