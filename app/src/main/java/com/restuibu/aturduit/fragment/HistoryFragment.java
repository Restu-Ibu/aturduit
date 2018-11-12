package com.restuibu.aturduit.fragment;

import java.util.ArrayList;

import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.restuibu.aturduit.R;
import com.restuibu.aturduit.adapter.HistoryAdapter;
import com.restuibu.aturduit.adapter.TransaksiAdapter;
import com.restuibu.aturduit.model.History;
import com.restuibu.aturduit.model.MySQLiteHelper;

public class HistoryFragment extends Fragment {
	// ///
	public static ListView list;
	public static ImageButton bBack;
	public static TextView tTitle1, tTitle2, tTitle3;
	public static HistoryAdapter adapterHistory;
	public static TransaksiAdapter adapterTransaksi;
	public static int adapterStatus;

	private boolean searching = false;
	private EditText eSearch;
	private ImageButton bSearch;
	private MySQLiteHelper helper;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_history, container,
				false);

//		// add interstitial
//		if (SplashActivity.mInterstitialAd.isLoaded()) {
//			SplashActivity.mInterstitialAd.show();
//		}
//		SplashActivity.mInterstitialAd.setAdListener(new AdListener() {
//			@Override
//			public void onAdClosed() {
//				MainActivity.loadInterstitial(getActivity());
//			}
//		});
		

		helper = new MySQLiteHelper(getActivity());

		bBack = (ImageButton) rootView.findViewById(R.id.button1);
		bSearch = (ImageButton) rootView.findViewById(R.id.button2);
		eSearch = (EditText) rootView.findViewById(R.id.editText1);

		tTitle1 = (TextView) rootView.findViewById(R.id.title1);
		tTitle2 = (TextView) rootView.findViewById(R.id.title2);
		tTitle3 = (TextView) rootView.findViewById(R.id.title3);
		bBack.setVisibility(View.GONE);
		bBack.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				ArrayList<History> listHistory = new ArrayList<History>();
				HistoryAdapter adapterHistory;

				MySQLiteHelper helper = new MySQLiteHelper(getActivity());
				SQLiteDatabase db = helper.getReadableDatabase();
				helper.onCreate(db);

				listHistory = helper.getAllHistory();
				adapterHistory = new HistoryAdapter(getActivity(), listHistory);

				HistoryFragment.list.setAdapter(adapterHistory);
				HistoryFragment.adapterStatus = 1;

				HistoryFragment.tTitle1.setText(R.string.title1);
				HistoryFragment.tTitle2.setText(R.string.title2);
				HistoryFragment.tTitle3.setText(R.string.title3);

				HistoryFragment.bBack.setVisibility(View.GONE);
			}
		});

		bSearch.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (searching) {
					eSearch.setVisibility(View.GONE);
					bSearch.setImageResource(android.R.drawable.ic_menu_search);
					searching = false;
				} else {
					eSearch.setVisibility(View.VISIBLE);
					eSearch.requestFocus();
					bSearch.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
					searching = true;
				}

			}
		});

		eSearch.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				if (HistoryFragment.adapterStatus == 1)
					HistoryFragment.adapterHistory.getFilter().filter(arg0);
				else
					HistoryFragment.adapterTransaksi.getFilter().filter(arg0);
			}

			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});

		list = (ListView) rootView.findViewById(R.id.listView1);
		fill_list();

		return rootView;
	}

	private void fill_list() {
		// TODO Auto-generated method stub
		ArrayList<History> listHistory = new ArrayList<History>();

		listHistory = helper.getAllHistory();
		HistoryFragment.adapterHistory = new HistoryAdapter(getActivity(),
				listHistory);
		HistoryFragment.adapterStatus = 1;

		HistoryFragment.tTitle1.setText(R.string.title1);
		HistoryFragment.tTitle2.setText(R.string.title2);
		HistoryFragment.tTitle3.setText(R.string.title3);

		HistoryFragment.list.setAdapter(adapterHistory);

	}

}
