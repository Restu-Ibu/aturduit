package com.restuibu.aturduit;

import android.app.Activity;
import android.os.Bundle;

public class AboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
		getActionBar().setTitle("Tentang");
	}

	
	@Override
	public void onBackPressed(){
		super.onBackPressed();
		finish();
		
	}

}
