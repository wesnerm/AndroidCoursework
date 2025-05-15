package com.softperson.asst5;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.*;
import android.webkit.*;

public class MainActivity extends Activity {

	private WebView webView1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = new Intent();
		intent.setClass(this, RoundballActivity.class);
		startActivity(intent);
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
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	

}
