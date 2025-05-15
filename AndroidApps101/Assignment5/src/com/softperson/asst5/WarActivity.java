package com.softperson.asst5;

import android.os.Bundle;
import android.app.Activity;
import android.view.*;
import android.webkit.*;

public class WarActivity extends Activity {

	private WebView webView1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_war);
		webView1=(WebView)findViewById(R.id.webView1);
		webView1.getSettings().setBuiltInZoomControls(true);
		webView1.getSettings().setJavaScriptEnabled(true);
		webView1.loadUrl("file:///android_asset/waroftheworlds.html");
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
		getMenuInflater().inflate(R.menu.war, menu);
		return true;
	}

}
