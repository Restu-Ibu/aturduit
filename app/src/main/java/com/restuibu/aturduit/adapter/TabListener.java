package com.restuibu.aturduit.adapter;

import com.restuibu.aturduit.R;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;

public class TabListener implements ActionBar.TabListener {

	private Fragment fragment;
	private Activity m;
	// The contructor.
	public TabListener(Fragment fragment, Activity a) {
		this.fragment = fragment;
		m=a;
	}

	// When a tab is tapped, the FragmentTransaction replaces
	// the content of our main layout with the specified fragment;
	// that's why we declared an id for the main layout.
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		ft.replace(R.id.frameLayout1, fragment);
		m.getActionBar().setTitle("Dashboard");
//		if(tab.getPosition()==0){
//			m.getActionBar().setTitle(m.getResources().getString(R.string.Tambah));
//			//Toast.makeText(m.getApplicationContext(), "0", Toast.LENGTH_SHORT).show();
//		}else if (tab.getPosition()==1){
//			m.getActionBar().setTitle(m.getResources().getString(R.string.Riwayat));
//			//Toast.makeText(m.getApplicationContext(), "1", Toast.LENGTH_SHORT).show();
//		}else if(tab.getPosition()==2){
//			m.getActionBar().setTitle(m.getResources().getString(R.string.Statistik));
//			//Toast.makeText(m.getApplicationContext(), "2", Toast.LENGTH_SHORT).show();
//		}
	}

	// When a tab is unselected, we have to hide it from the user's view.
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		ft.remove(fragment);
	}

	// Nothing special here. Fragments already did the job.
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {

	}
}
