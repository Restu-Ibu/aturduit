package com.restuibu.aturduit.fragment;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.restuibu.aturduit.R;
import com.restuibu.aturduit.activity.MainActivity;
import com.restuibu.aturduit.activity.SplashActivity;
import com.restuibu.aturduit.model.Transaksi;
import com.restuibu.aturduit.util.Util;

import static com.restuibu.aturduit.util.Util.helper;
import static com.restuibu.aturduit.util.Util.updateBudgetActionBar;

public class AddTransactionFragment extends Fragment {
    private EditText ePrice, eDesc;

    public static Button bTimePicker;
    private Button bDatePicker;

    private Button bSave, bReset;

    private ImageView[] ivKategori = new ImageView[7];
    private String category;

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
                if(getActivity() != null){
                    MainActivity.loadInterstitial(getActivity());
                }

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

        ivKategori[0] = (ImageView) rootView.findViewById(R.id.imageViewKategori1);
        ivKategori[1] = (ImageView) rootView.findViewById(R.id.imageViewKategori2);
        ivKategori[2] = (ImageView) rootView.findViewById(R.id.imageViewKategori3);
        ivKategori[3] = (ImageView) rootView.findViewById(R.id.imageViewKategori4);
        ivKategori[4] = (ImageView) rootView.findViewById(R.id.imageViewKategori5);
        ivKategori[5] = (ImageView) rootView.findViewById(R.id.imageViewKategori6);
        ivKategori[6] = (ImageView) rootView.findViewById(R.id.imageViewKategori7);

        category = "";
        ivKategori[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.soft_grey));
                category = getString(R.string.kategori1);
                Util.removeOtherCategories(getActivity(), ivKategori, 0);
                Toast.makeText(getActivity(), category, Toast.LENGTH_SHORT).show();
            }
        });

        ivKategori[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.soft_grey));
                category = getString(R.string.kategori2);
                Util.removeOtherCategories(getActivity(), ivKategori, 1);
                Toast.makeText(getActivity(), category, Toast.LENGTH_SHORT).show();
            }
        });

        ivKategori[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.soft_grey));
                category = getString(R.string.kategori3);
                Util.removeOtherCategories(getActivity(), ivKategori, 2);
                Toast.makeText(getActivity(), category, Toast.LENGTH_SHORT).show();
            }
        });

        ivKategori[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.soft_grey));
                category = getString(R.string.kategori4);
                Util.removeOtherCategories(getActivity(), ivKategori, 3);
                Toast.makeText(getActivity(), category, Toast.LENGTH_SHORT).show();
            }
        });
        ivKategori[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.soft_grey));
                category = getString(R.string.kategori5);
                Util.removeOtherCategories(getActivity(), ivKategori, 4);
                Toast.makeText(getActivity(), category, Toast.LENGTH_SHORT).show();
            }
        });
        ivKategori[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.soft_grey));
                category = getString(R.string.kategori6);
                Util.removeOtherCategories(getActivity(), ivKategori, 5);
                Toast.makeText(getActivity(), category, Toast.LENGTH_SHORT).show();
            }
        });
        ivKategori[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.soft_grey));
                category = getString(R.string.kategori7);
                Util.removeOtherCategories(getActivity(), ivKategori, 6);
                Toast.makeText(getActivity(), category, Toast.LENGTH_SHORT).show();
            }
        });




        //helper = new MySQLiteHelper(getActivity());
        // Toast.makeText(getActivity(),
        // new
        // SimpleDateFormat("kk:mm:ss").format(new Date()),
        // Toast.LENGTH_SHORT).show();

        // di onResume, masih bingung kok bisa gitu
        // eTgl.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        // eJam.setText(new SimpleDateFormat("kk:mm:ss").format(new Date()));
        bDatePicker.setText(new SimpleDateFormat("dd/MM/yyyy")
                .format(new Date()));

        ePrice.addTextChangedListener(Util.onTextChangedListener(ePrice));
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
                                    + Integer.toString(datePicker.getMonth() + 1)
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

                String ePrice_ = ePrice.getText().toString().replaceAll("\\W", "");

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
                    if (!ePrice_.matches("[0123456789]+")) {
                        ePrice.setError("Accept Numeric Only.");
                        value_number = false;
                    }

                    if (category.equals("")){
                        Toast.makeText(getActivity(), "Mohon pilih kategori terlebih dahulu", Toast.LENGTH_SHORT).show();
                    }

                    if (value_number && value_text && !category.equals("")) {

                        Date dt = new Date();

                        Transaksi trans = new Transaksi(0, eDesc.getText()
                                .toString(), ePrice_,
                                bTimePicker.getText().toString(), bDatePicker
                                .getText().toString(), dt.getTime(), category);
                        helper.addTransaksi(trans);

                        helper.updateBudgetByDate(bDatePicker.getText()
                                        .toString()
                                        + " "
                                        + bTimePicker.getText().toString(),
                                Long.parseLong(ePrice_)
                                        * (-1));



                        ePrice.setText("");
                        eDesc.setText("");
                        category = "";
                        Util.removeOtherCategories(getActivity(), ivKategori, -1);
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
                category = "";
                Util.removeOtherCategories(getActivity(), ivKategori, -1);
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
