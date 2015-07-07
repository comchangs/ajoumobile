/*******************************************************************************
 * Copyright (c) 2011 A-MiCøM. All rights reserved.
 *******************************************************************************/
package ajou.amicom.ajouInfo.twitter;

import twitter4j.*;
import twitter4j.http.*;

import java.io.*;
import java.util.*;

import ajou.amicom.ajouInfo.*;
import android.app.Activity;
import android.content.*;
import android.net.*;
import android.os.Bundle;
import android.util.*;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class twitter extends Activity {
	
	private static final String TAG = null;
	public static String consumerKey = "qvSen79CLMR3nXkh8vfiw"; //"발급받은consumerKey ";
	public static String consumerSecret = "Bd9VgpxnakVPUSnTnz56GlP6zA0FGv3OSxx0IU1FuE"; //"발급받은consumeSecret";
	public static Uri CALLBACK_URL = Uri.parse("wefu://twitter");
	 
	private Twitter twitter;
	private AccessToken acToken;
	private RequestToken rqToken;
	private Status status = null;
	 
	public void onCreate(Bundle savedInstanceState) {
	         twitter = new TwitterFactory().getInstance();
	         twitter.setOAuthConsumer(consumerKey, consumerSecret);
	         try {
				rqToken = twitter.getOAuthRequestToken(CALLBACK_URL.toString());
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	         startActivity(new Intent(Intent.ACTION_VIEW,  Uri.parse(
	rqToken.getAuthorizationURL())));
	}
	 
	protected void onNewIntent(Intent intent) {
	super.onNewIntent(intent);
	Uri uri = intent.getData();
	if(uri != null && CALLBACK_URL.getScheme().equals(uri.getScheme())){
	String oauth_verifier = uri.getQueryParameter("oauth_verifier");
	try {
	             acToken = twitter.getOAuthAccessToken(rqToken, oauth_verifier);
	             status = twitter.updateStatus("test twitter4j web application oauth");
	} catch (TwitterException e) {
	             
				Log.e(TAG, e.getMessage());
	}
	          }}
}