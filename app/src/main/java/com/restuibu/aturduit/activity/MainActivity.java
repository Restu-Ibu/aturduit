package com.restuibu.aturduit.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.restuibu.aturduit.fragment.AddTransactionFragment;
import com.restuibu.aturduit.fragment.HistoryFragment;
import com.restuibu.aturduit.R;
import com.restuibu.aturduit.fragment.StatisticFragment;
import com.restuibu.aturduit.adapter.TabListener;
import com.restuibu.aturduit.model.Budget;
import com.restuibu.aturduit.model.MySQLiteHelper;
import com.restuibu.aturduit.util.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.widget.ArrayAdapter.createFromResource;
import static com.restuibu.aturduit.util.Util.changeBudgetTitle;
import static com.restuibu.aturduit.util.Util.exportDB;
import static com.restuibu.aturduit.util.Util.importDB;
import static com.restuibu.aturduit.util.Util.verifyStoragePermissions;

public class MainActivity extends Activity {
    private ActionBar.Tab AddTransactionFragmentTab, HistoryFragmentTab,
            StatisticFragmentTab;
    private Fragment addTransactionFragment = new AddTransactionFragment();
    private Fragment historyFragment = new HistoryFragment();
    private Fragment statisticFragment = new StatisticFragment();
    private MySQLiteHelper helper;
    private Menu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        setContentView(R.layout.activity_main);




        // add interstitial
        // if (SplashActivity.mInterstitialAd.isLoaded()) {
        // SplashActivity.mInterstitialAd.show();
        // }
        // SplashActivity.mInterstitialAd.setAdListener(new AdListener() {
        // @Override
        // public void onAdClosed() {
        // Util.loadInterstitial(MainActivity.this);
        // }
        // });


        // Asking for the default ActionBar element that our platform supports.
        ActionBar actionBar = getActionBar();
        helper = new MySQLiteHelper(MainActivity.this);

        Intent intent = getIntent();
        boolean fromSignIn = intent.getBooleanExtra("fromSignIn", false);

        if(fromSignIn){
            importDB(MainActivity.this, fromSignIn);
        }

        /*
		 * // Screen handling while hiding ActionBar icon.
		 * actionBar.setDisplayShowHomeEnabled(false);
		 * 
		 * // Screen handling while hiding Actionbar title.
		 * actionBar.setDisplayShowTitleEnabled(false);
		 */


        // Creating ActionBar tabs.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setTitle("");
        actionBar.setIcon(android.R.color.transparent);

        // Setting custom tab icons.
        AddTransactionFragmentTab = actionBar.newTab().setIcon(R.drawable.add);
        HistoryFragmentTab = actionBar.newTab().setIcon(R.drawable.history);
        StatisticFragmentTab = actionBar.newTab().setIcon(R.drawable.statistic);

        // Setting tab listeners.
        AddTransactionFragmentTab.setTabListener(new TabListener(
                addTransactionFragment, MainActivity.this));
        HistoryFragmentTab.setTabListener(new TabListener(historyFragment,
                MainActivity.this));
        StatisticFragmentTab.setTabListener(new TabListener(statisticFragment,
                MainActivity.this));

        // Adding tabs to the ActionBar.
        actionBar.addTab(AddTransactionFragmentTab);
        actionBar.addTab(HistoryFragmentTab);
        actionBar.addTab(StatisticFragmentTab);

		/*
		 * // Initilization viewPager = (ViewPager) findViewById(R.id.pager);
		 * actionBar = getActionBar(); mAdapter = new
		 * TabsPagerAdapter(getSupportFragmentManager());
		 * 
		 * viewPager.setAdapter(mAdapter);
		 * actionBar.setHomeButtonEnabled(false);
		 * actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		 * 
		 * actionBar.addTab(actionBar.newTab().setTabListener(this)
		 * .setIcon(R.drawable.add));
		 * actionBar.addTab(actionBar.newTab().setTabListener(this)
		 * .setIcon(R.drawable.history));
		 * actionBar.addTab(actionBar.newTab().setTabListener(this)
		 * .setIcon(R.drawable.statistic));
		 */

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.main, menu);

        changeBudgetTitle(MainActivity.this, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.budget:
                Util.refreshTimePickerAtAddTransactionFragment();
                if (!Util.checkBudget(MainActivity.this)) {
                    showAlertInsertBudget();
                } else {
                    showAlertInfoBudget();
                }

                return true;

            case R.id.action_option:
                Util.alertOption(MainActivity.this);
                return true;
            case R.id.action_about:
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                return true;

            case R.id.action_signout:
                exportDB(MainActivity.this,1, true);
                return true;

            case R.id.rate:
                Uri uri = Uri.parse("market://details?id=" + this.getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id="
                                    + this.getPackageName())));
                }
                return true;
            case R.id.ins:
                Uri uri2 = Uri.parse("market://developer?id=Restu+Ibu");
                Intent goToMarket2 = new Intent(Intent.ACTION_VIEW, uri2);
                try {
                    startActivity(goToMarket2);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/developer?id=Restu+Ibu")));
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showAlertInsertBudget() {
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        View dialogview = inflater.inflate(R.layout.alertdialog_insertbudget,
                null);
        final AlertDialog alert = new AlertDialog.Builder(MainActivity.this)
                .create();
        alert.setTitle("Atur Budget");
        alert.setView(dialogview);

        final EditText eSetBudget = (EditText) dialogview
                .findViewById(R.id.editText1);
        Button bSet = (Button) dialogview.findViewById(R.id.button1);
        Button bCancel = (Button) dialogview.findViewById(R.id.button2);

        eSetBudget.addTextChangedListener(Util.onTextChangedListener(eSetBudget));

        final Spinner spinner = (Spinner) dialogview.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = createFromResource(
                MainActivity.this, R.array.category_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final Spinner spinner2 = (Spinner) dialogview
                .findViewById(R.id.spinner2);
        ArrayList<String> list = new ArrayList<String>();

        list.add("Waktu mulai:");
        list.add((new SimpleDateFormat("dd/MM/yyyy kk:mm:ss"))
                .format(new Date()));
        list.add((new SimpleDateFormat("dd/MM/yyyy")).format(new Date())
                + " 24:00:00");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter);

        bSet.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                try {
                    SimpleDateFormat sdf = new SimpleDateFormat(
                            "dd/MM/yyyy kk:mm:ss");
                    Date startDate = sdf.parse(spinner2.getSelectedItem()
                            .toString());
                    Date endDate = null;

                    if (spinner.getSelectedItem().toString()
                            .equals("Berdasarkan kategori:")
                            || spinner2.getSelectedItem().toString()
                            .equals("Waktu mulai:")) {
                        Toast.makeText(MainActivity.this,
                                "Harap isi semua form", Toast.LENGTH_SHORT)
                                .show();
                    } else {

                        if (spinner.getSelectedItem().toString()
                                .equals("Mingguan")) {
                            endDate = Util.addDays(startDate, 7);
                        } else if (spinner.getSelectedItem().toString()
                                .equals("Bulanan")) {
                            endDate = Util.addMonths(startDate, 1);
                        }

                        // impleDateFormat sdf = new
                        // SimpleDateFormat("dd/MM/yyyy");

                        Budget budget = new Budget(0, (new SimpleDateFormat(
                                "dd/MM/yyyy")).format(startDate),
                                (new SimpleDateFormat("dd/MM/yyyy"))
                                        .format(endDate), spinner
                                .getSelectedItem().toString(),
                                eSetBudget.getText().toString().replaceAll("\\W", ""),
                                eSetBudget.getText().toString().replaceAll("\\W", ""),
                                startDate.getTime(), endDate.getTime());

                        helper.addBudget(budget);

                        changeBudgetTitle(MainActivity.this, menu);

                        alert.dismiss();
                    }
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        });

        bCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                alert.dismiss();
            }
        });

        alert.show();
    }

    public void showAlertEditBudget() {
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        View dialogview = inflater.inflate(R.layout.alertdialog_editbudget,
                null);
        final AlertDialog alert = new AlertDialog.Builder(MainActivity.this)
                .create();
        alert.setTitle("Ubah Budget");
        alert.setView(dialogview);

        final Budget budget = helper.getDetailLastBudget();

        final Spinner sKategori = (Spinner) dialogview.findViewById(R.id.spinner1);
        final TextView tvTotal = (TextView) dialogview.findViewById(R.id.textView1);
        final TextView tvSisa = (TextView) dialogview.findViewById(R.id.textView2);
        final EditText etAmount = (EditText) dialogview.findViewById(R.id.editText1);
        Button bIncrease = (Button) dialogview.findViewById(R.id.button1);
        Button bDecrease = (Button) dialogview.findViewById(R.id.button2);

        etAmount.addTextChangedListener(Util.onTextChangedListener(etAmount));
        tvTotal.setText(Util.formatUang(budget.getAmount()));
        tvSisa.setText(Util.formatUang(budget.getLeft()));

        if(Long.parseLong(budget.getLeft()) < 0){
            tvSisa.setTextColor(Color.parseColor("#FF0000"));
            tvSisa.setText(Util.formatUang(budget.getLeft()));
        }
        else
            tvSisa.setText(Util.formatUang(budget.getLeft()));

        //CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(MainActivity.this, R.array.editBudget_array);

        ArrayAdapter<CharSequence> adapter = createFromResource(
                MainActivity.this, R.array.editBudget_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sKategori.setAdapter(adapter);


        bIncrease.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sKategori.getSelectedItem().toString()
                        .equals("Pilih yang akan diubah:")) {
                    Toast.makeText(MainActivity.this, "Tentukan kategori Budget yang akan diubah", Toast.LENGTH_LONG).show();
                } else if (sKategori.getSelectedItem().toString()
                        .equals("Total Budget")) {
                    helper.updateBudgetTotalSum(budget.getIdBudget(), Long.parseLong(etAmount.getText().toString().replaceAll("\\W", "")));
                    Toast.makeText(MainActivity.this, "Total Budget telah diubah", Toast.LENGTH_LONG).show();
                    alert.dismiss();
                } else if (sKategori.getSelectedItem().toString()
                        .equals("Sisa Budget")) {
                    if ((Long.parseLong(budget.getLeft()) + Long.parseLong(etAmount.getText().toString().replaceAll("\\W", "")) > Long.parseLong(budget.getAmount()))) {
                        Toast.makeText(MainActivity.this, "Sisa Budget tidak boleh melebihi Total Budget", Toast.LENGTH_LONG).show();
                    } else {
                        helper.updateBudgetSum(budget.getIdBudget(), Long.parseLong(etAmount.getText().toString().replaceAll("\\W", "")));
                        Toast.makeText(MainActivity.this, "Sisa Budget telah diubah", Toast.LENGTH_LONG).show();
                        alert.dismiss();
                    }
                } else {
                    helper.updateBudgetBothSum(budget.getIdBudget(), Long.parseLong(etAmount.getText().toString().replaceAll("\\W", "")));
                    Toast.makeText(MainActivity.this, "Total Budget dan sisa Budget telah diubah", Toast.LENGTH_LONG).show();
                    alert.dismiss();
                }
            }
        });

        bDecrease.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sKategori.getSelectedItem().toString()
                        .equals("Pilih yang akan diubah:")) {
                    Toast.makeText(MainActivity.this, "Tentukan kategori Budget yang akan diubah", Toast.LENGTH_LONG).show();
                } else if (sKategori.getSelectedItem().toString()
                        .equals("Total Budget")) {
                    if ((Long.parseLong(budget.getAmount()) - Long.parseLong(etAmount.getText().toString().replaceAll("\\W", "")) < Long.parseLong(budget.getLeft()))) {
                        Toast.makeText(MainActivity.this, "Total Budget tidak boleh kurang dari sisa Budget", Toast.LENGTH_LONG).show();
                    } else {
                        helper.updateBudgetTotalDifference(budget.getIdBudget(), Long.parseLong(etAmount.getText().toString().replaceAll("\\W", "")));
                        Toast.makeText(MainActivity.this, "Total Budget telah diubah", Toast.LENGTH_LONG).show();
                        alert.dismiss();
                    }
                } else if (sKategori.getSelectedItem().toString()
                        .equals("Sisa Budget")) {
                    helper.updateBudgetDifference(budget.getIdBudget(), Long.parseLong(etAmount.getText().toString().replaceAll("\\W", "")));
                    Toast.makeText(MainActivity.this, "Sisa Budget telah diubah", Toast.LENGTH_LONG).show();
                    alert.dismiss();
                } else {
                    helper.updateBudgetBothDifference(budget.getIdBudget(), Long.parseLong(etAmount.getText().toString().replaceAll("\\W", "")));
                    Toast.makeText(MainActivity.this, "Total Budget dan sisa Budget telah diubah", Toast.LENGTH_LONG).show();
                    alert.dismiss();
                }
            }
        });

        alert.show();

    }

    public void showAlertInfoBudget() {
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        View dialogview = inflater.inflate(R.layout.alertdialog_infobudget,
                null);
        final AlertDialog alert = new AlertDialog.Builder(MainActivity.this)
                .create();
        alert.setTitle("Info Budget");
        alert.setView(dialogview);

        final Budget budget = helper.getDetailLastBudget();

        TextView tIdBudget = (TextView) dialogview.findViewById(R.id.textView1);
        TextView tStartDate = (TextView) dialogview
                .findViewById(R.id.textView2);
        TextView tEndDate = (TextView) dialogview.findViewById(R.id.textView3);
        TextView tCategory = (TextView) dialogview.findViewById(R.id.textView4);
        TextView tAmount = (TextView) dialogview.findViewById(R.id.textView5);
        TextView tLeft = (TextView) dialogview.findViewById(R.id.textView6);
        Button bReset = (Button) dialogview.findViewById(R.id.button1);
        Button bList = (Button) dialogview.findViewById(R.id.button2);
        Button bEdit = (Button) dialogview.findViewById(R.id.button3);

        tIdBudget.setText(Integer.toString(budget.getIdBudget()));
        tStartDate.setText(Util.getDateString(budget.getTimeStartDate(),
                new SimpleDateFormat("dd/MM/yy kk:mm:ss")));
        tEndDate.setText(Util.getDateString(budget.getTimeEndDate(),
                new SimpleDateFormat("dd/MM/yy kk:mm:ss")));
        tCategory.setText(budget.getCategory());
        tAmount.setText(Util.formatUang(budget.getAmount()));

        //Toast.makeText(this, budget.getAmount(), Toast.LENGTH_SHORT).show();

        if(Long.parseLong(budget.getLeft()) < 0){
            tLeft.setTextColor(Color.parseColor("#FF0000"));
            tLeft.setText(Util.formatUang(budget.getLeft()));
        }
        else
            tLeft.setText(Util.formatUang(budget.getLeft()));

        bReset.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                alert.dismiss();

                AlertDialog.Builder builder = new AlertDialog.Builder(
                        MainActivity.this);

                builder.setTitle("Reset Budget");

                builder.setIcon(android.R.drawable.ic_delete);
                builder.setMessage("Anda yakin ingin Reset Budget?");

                builder.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                // TODO Auto-generated method stub
                                helper.deleteBudget(budget.getIdBudget());
                                changeBudgetTitle(MainActivity.this, menu);
                                showAlertInsertBudget();

                            }

                        });

                builder.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                                alert.dismiss();
                            }

                        });

                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        bEdit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showAlertEditBudget();

                alert.dismiss();

                // nothing to do

            }
        });

        bList.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                alert.dismiss();

                startActivity(new Intent(MainActivity.this,
                        HistoryAndStatisticBudgetActivity.class));

            }
        });

        alert.show();
    }

    private void setLanguageForApp(String languageToLoad) {
        Locale locale;
        if (languageToLoad.equals("not-set")) { //use any value for default
            locale = Locale.getDefault();
        } else {
            locale = new Locale(languageToLoad);
        }
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    public static void loadInterstitial(Context c) {
        SplashActivity.mInterstitialAd = new InterstitialAd(c);
        SplashActivity.mInterstitialAd.setAdUnitId(c.getResources().getString(
                R.string.interstitial_ad_unit_id));

        AdRequest adRequest = new AdRequest.Builder().build();
        SplashActivity.mInterstitialAd.loadAd(adRequest);
    }

}
