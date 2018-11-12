package com.restuibu.aturduit.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.restuibu.aturduit.R;
import com.restuibu.aturduit.adapter.AboutListViewAdapter;

public class AboutActivity extends Activity {
	private AboutListViewAdapter adapter;
	private ListView konten_list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		konten_list = (ListView) findViewById(R.id.listView);

		adapter = new AboutListViewAdapter(AboutActivity.this);
		konten_list.setAdapter(adapter);


		getActionBar().setTitle("Tentang");
	}

	
	@Override
	public void onBackPressed(){
		super.onBackPressed();
		finish();
		
	}

}
