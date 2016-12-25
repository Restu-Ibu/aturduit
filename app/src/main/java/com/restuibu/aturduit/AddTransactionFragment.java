package com.restuibu.aturduit;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.restuibu.aturduit.model.MySQLiteHelper;
import com.restuibu.aturduit.model.Transaksi;
import com.restuibu.aturduit.model.Util;

public class AddTransactionFragment extends Fragment {
	private EditText ePrice, eDesc;

	public static Button bTimePicker;
	private Button bDatePicker;

	private Button bSave, bReset;

	private MySQLiteHelper helper;
	private boolean value_text;
	private boolean value_number;

	private boolean null_ePrice;
	private boolean null_eDesc;

	@SuppressLint("SimpleDateFormat")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_add, container,
				false);
		


		
		// add interstitial
		if (SplashActivity.mInterstitialAd.isLoaded()) {
			SplashActivity.mInterstitialAd.show();
		}
		SplashActivity.mInterstitialAd.setAdListener(new AdListener() {
			@Override
			public void onAdClosed() {
				MainActivity.loadInterstitial(getActivity());
			}
		});

		AdView mAdView = (AdView) rootView.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);

		// add interstitial
//		if (SplashActivity.mInterstitialAd.isLoaded()) {
//			SplashActivity.mInterstitialAd.show();
//		}
//		SplashActivity.mInterstitialAd.setAdListener(new AdListener() {
//			@Override
//			public void onAdClosed() {
//				Util.loadInterstitial(getActivity());
//			}
//		});

		ePrice = (EditText) rootView.findViewById(R.id.editText1);
		eDesc = (EditText) rootView.findViewById(R.id.editText2);
		// eTgl = (EditText) rootView.findViewById(R.id.editText3);
		// eJam = (EditText) rootView.findViewById(R.id.editText4);
		bSave = (Button) rootView.findViewById(R.id.button1);
		bReset = (Button) rootView.findViewById(R.id.button2);
		bTimePicker = (Button) rootView.findViewById(R.id.button3);
		bDatePicker = (Button) rootView.findViewById(R.id.button4);

		helper = new MySQLiteHelper(getActivity());
		// Toast.makeText(getActivity(),
		// new
		// SimpleDateFormat("kk:mm:ss").format(new Date()),
		// Toast.LENGTH_SHORT).show();

		// di onResume, masih bingung kok bisa gitu
		// eTgl.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
		// eJam.setText(new SimpleDateFormat("kk:mm:ss").format(new Date()));
		bDatePicker.setText(new SimpleDateFormat("dd/MM/yyyy")
				.format(new Date()));

		// tTimePicker.getCurrentMinute();
		bTimePicker.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Util.refreshTimePickerAtAddTransactionFragment();

				LayoutInflater inflater = LayoutInflater.from(getActivity());
				View dialogview = inflater.inflate(
						R.layout.alertdialog_timepicker, null);
				final AlertDialog alert = new AlertDialog.Builder(getActivity())
						.create();
				alert.setTitle("Atur Jam");

				final TimePicker timePicker = (TimePicker) dialogview
						.findViewById(R.id.timePicker1);
				Button bSet = (Button) dialogview.findViewById(R.id.button1);
				Button bCancel = (Button) dialogview.findViewById(R.id.button2);

				alert.setView(dialogview);
				alert.show();

				timePicker.setIs24HourView(true);

				bSet.setOnClickListener(new View.OnClickListener() {

					public void onClick(View arg0) {
						// TODO Auto-generated method stub

						if ((timePicker.getCurrentHour() < 10)
								&& (timePicker.getCurrentMinute() > 9)) {
							bTimePicker.setText("0"
									+ timePicker.getCurrentHour()
									+ ":"
									+ timePicker.getCurrentMinute()
									+ ":"
									+ new SimpleDateFormat("ss")
											.format(new Date()));
						} else if ((timePicker.getCurrentHour() > 9)
								&& (timePicker.getCurrentMinute() < 10)) {
							bTimePicker.setText(timePicker.getCurrentHour()
									+ ":0"
									+ timePicker.getCurrentMinute()
									+ ":"
									+ new SimpleDateFormat("ss")
											.format(new Date()));

						} else if ((timePicker.getCurrentHour() < 10)
								&& (timePicker.getCurrentMinute() < 10)) {
							bTimePicker.setText("0"
									+ timePicker.getCurrentHour()
									+ ":0"
									+ +timePicker.getCurrentMinute()
									+ ":"
									+ new SimpleDateFormat("ss")
											.format(new Date()));
						} else {
							bTimePicker.setText(""
									+ timePicker.getCurrentHour()
									+ ":"
									+ +timePicker.getCurrentMinute()
									+ ":"
									+ new SimpleDateFormat("ss")
											.format(new Date()));
						}
						alert.dismiss();

					}
				});

				bCancel.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						alert.dismiss();
					}
				});

			}
		});

		bDatePicker.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Util.refreshTimePickerAtAddTransactionFragment();

				LayoutInflater inflater = LayoutInflater.from(getActivity());
				View dialogview = inflater.inflate(
						R.layout.alertdialog_datepicker, null);
				final AlertDialog alert = new AlertDialog.Builder(getActivity())
						.create();
				alert.setTitle("Atur Tanggal");

				final DatePicker datePicker = (DatePicker) dialogview
						.findViewById(R.id.datePicker1);
				Button bSet = (Button) dialogview.findViewById(R.id.button1);
				Button bCancel = (Button) dialogview.findViewById(R.id.button2);

				alert.setView(dialogview);
				alert.show();

				bSet.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						if ((datePicker.getDayOfMonth() < 10)
								&& (datePicker.getMonth() + 1 > 9))
							bDatePicker.setText("0"
									+ Integer.toString(datePicker
											.getDayOfMonth()) + "/"
									+ Integer.toString(datePicker.getMonth())
									+ "/"
									+ Integer.toString(datePicker.getYear()));
						else if ((datePicker.getDayOfMonth() > 9)
								&& (datePicker.getMonth() + 1 < 10))
							bDatePicker.setText(Integer.toString(datePicker
									.getDayOfMonth())
									+ "/0"
									+ Integer.toString(datePicker.getMonth() + 1)
									+ "/"
									+ Integer.toString(datePicker.getYear()));
						else if ((datePicker.getDayOfMonth() < 10)
								&& (datePicker.getMonth() + 1 < 10))
							bDatePicker.setText("0"
									+ Integer.toString(datePicker
											.getDayOfMonth())
									+ "/0"
									+ Integer.toString(datePicker.getMonth() + 1)
									+ "/"
									+ Integer.toString(datePicker.getYear()));

						else {
							bDatePicker.setText(Integer.toString(datePicker
									.getDayOfMonth())
									+ "/"
									+ Integer.toString(datePicker.getMonth() + 1)
									+ "/"
									+ Integer.toString(datePicker.getYear()));
						}
						alert.dismiss();
					}
				});
				bCancel.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						alert.dismiss();
					}
				});
			}
		});

		eDesc.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				eDesc.setError(null);
			}

			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});

		bSave.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				null_ePrice = false;
				null_eDesc = false;
				value_text = true;
				value_number = true;

				if ((ePrice.getText().length() == 0)) {
					ePrice.setError("Must be fill.");
					null_ePrice = true;
				}
				if ((eDesc.getText().length() == 0)) {
					eDesc.setError("Must be fill.");
					null_eDesc = true;
				}

				if (!null_ePrice && !null_eDesc) {
					if (!eDesc.getText().toString()
							.matches("[a-zA-Z 0123456789 .,+]+")) {
						eDesc.setError("Accept Alphabets Only.");
						value_text = false;
					}
					if (!ePrice.getText().toString().matches("[0123456789]+")) {
						ePrice.setError("Accept Numeric Only.");
						value_number = false;
					}

					if (value_number && value_text) {

						Date dt = new Date();

						Transaksi trans = new Transaksi(0, eDesc.getText()
								.toString(), ePrice.getText().toString(),
								bTimePicker.getText().toString(), bDatePicker
										.getText().toString(), dt.getTime());
						helper.addTransaksi(trans);

						helper.updateBudgetByDate(bDatePicker.getText()
								.toString()
								+ " "
								+ bTimePicker.getText().toString(),
								Long.parseLong(ePrice.getText().toString())
										* (-1));

						ePrice.setText("");
						eDesc.setText("");
						ePrice.requestFocus();

					}

				}

			}
		});

		bReset.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				ePrice.setText("");
				eDesc.setText("");
				ePrice.requestFocus();

			}
		});
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		// eTgl.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
		Util.refreshTimePickerAtAddTransactionFragment();
	}
}
