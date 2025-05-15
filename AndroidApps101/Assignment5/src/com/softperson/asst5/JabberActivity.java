package com.softperson.asst5;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.*;
import android.webkit.*;

public class JabberActivity extends Activity {

	private WebView webView1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jabber);
		webView1=(WebView)findViewById(R.id.webView1);
		webView1.getSettings().setBuiltInZoomControls(true);
		webView1.getSettings().setJavaScriptEnabled(true);
		webView1.getSettings().setDomStorageEnabled(true);
		webView1.loadUrl("file:///android_asset/jabberwocky.html");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webView1.canGoBack())
		{
			webView1.goBack();
			return true;
		}
		
		return super.onKeyDown(keyCode,  event);
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.jabber, menu);
		return true;
	}

	
	public void wikipedia(View view)
	{
		String url = "http://en.wikipedia.org/wiki/Jabberwocky";
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		startActivity(i);
	}
	
	public void jabberImage(View view)
	{
		webView1.loadUrl("file:///android_asset/Jabberwocky.jpg");
		// webView1.loadUrl("http://upload.wikimedia.org/wikipedia/commons/d/d0/Jabberwocky.jpg");
		
	}
}
