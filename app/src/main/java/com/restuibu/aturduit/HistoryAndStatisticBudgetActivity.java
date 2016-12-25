package com.restuibu.aturduit;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.restuibu.aturduit.adapter.BudgetAdapter;
import com.restuibu.aturduit.model.MySQLiteHelper;

public class HistoryAndStatisticBudgetActivity extends Activity {
	private ListView list;
	private Spinner spinner;

	private BudgetAdapter budgetAdapter;
	private MySQLiteHelper helper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_and_statistic_budget);
		helper = new MySQLiteHelper(HistoryAndStatisticBudgetActivity.this);

		getActionBar().setIcon(android.R.color.transparent);
		getActionBar().setTitle(R.string.RiwayatBudget);

		// add interstitial
		if (SplashActivity.mInterstitialAd.isLoaded()) {
			SplashActivity.mInterstitialAd.show();
		}
		SplashActivity.mInterstitialAd.setAdListener(new AdListener() {
			@Override
			public void onAdClosed() {
				MainActivity.loadInterstitial(HistoryAndStatisticBudgetActivity.this);
			}
		});

		 AdView mAdView = (AdView) findViewById(R.id.adView);
		 AdRequest adRequest = new AdRequest.Builder().build();
		 mAdView.loadAd(adRequest);

		// // add interstitial
		// if (SplashActivity.mInterstitialAd.isLoaded()) {
		// SplashActivity.mInterstitialAd.show();
		// }
		// SplashActivity.mInterstitialAd.setAdListener(new AdListener() {
		// @Override
		// public void onAdClosed() {
		// Util.loadInterstitial(HistoryAndStatisticBudgetActivity.this);
		// }
		// });

		list = (ListView) findViewById(R.id.listView1);
		spinner = (Spinner) findViewById(R.id.spinner1);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				HistoryAndStatisticBudgetActivity.this, R.array.budget_array,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub

				switch (arg2) {
				case 1:
					budgetAdapter = new BudgetAdapter(
							HistoryAndStatisticBudgetActivity.this, helper
									.getAllWeeklyBudget());
					list.setAdapter(budgetAdapter);
					break;

				case 2:
					budgetAdapter = new BudgetAdapter(
							HistoryAndStatisticBudgetActivity.this, helper
									.getAllMonthlyBudget());
					list.setAdapter(budgetAdapter);
					break;
				case 3:
					budgetAdapter = new BudgetAdapter(
							HistoryAndStatisticBudgetActivity.this, helper
									.getAllBudget());
					list.setAdapter(budgetAdapter);
					break;

				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		// list.setAdapter(budgetAdapter);

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

}
