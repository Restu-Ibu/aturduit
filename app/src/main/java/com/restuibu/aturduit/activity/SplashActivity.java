package com.restuibu.aturduit.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.google.android.gms.ads.InterstitialAd;
import com.restuibu.aturduit.R;

public class SplashActivity extends Activity {
	public static InterstitialAd mInterstitialAd;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// remove title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// load interstitial ad
		// Util.loadInterstitial(SplashActivity.this);
		// load interstitial ad
		MainActivity.loadInterstitial(SplashActivity.this);

		// this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash);

		TimerTask task = new TimerTask() {
			@Override
			public void run() {

				// go to the main activity
				Intent i = new Intent(SplashActivity.this, GoogleSignInActivity.class);
				startActivity(i);

				SplashActivity.this.finish();

			}

		};

		// Schedule a task for single execution after a specified delay.
		// Show splash screen for 4 seconds
		new Timer().schedule(task, 1000);

	}

}
