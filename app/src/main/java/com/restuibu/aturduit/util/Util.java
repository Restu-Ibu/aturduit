package com.restuibu.aturduit.util;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.restuibu.aturduit.fragment.AddTransactionFragment;
import com.restuibu.aturduit.activity.MainActivity;
import com.restuibu.aturduit.MyWidgetProvider;
import com.restuibu.aturduit.R;
import com.restuibu.aturduit.activity.SplashActivity;
import com.restuibu.aturduit.adapter.OptionAdapter;
import com.restuibu.aturduit.model.About;
import com.restuibu.aturduit.model.Alarm;
import com.restuibu.aturduit.model.Budget;
import com.restuibu.aturduit.model.MySQLiteHelper;
import com.restuibu.aturduit.model.OptionItem;

public class Util {
    public static ArrayList<About> getAllAbout() {
        ArrayList<About> list = new ArrayList<>();
        int max = 0;
        max = Constant.konten_desc_about.length;


        for (int i = 0; i < max; i++) {
            About a = new About();

            a.setImg(Constant.konten_img_about[i]);
            a.setDesc(Constant.konten_desc_about[i]);
            a.setUrl(Constant.konten_url_about[i]);


            list.add(a);
        }

        return list;
    }

    public static int getDayOfYear(String datestr) {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        int CurrentDayOfYear = 0;
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        int multipier = currentYear - 2016;


        try {
            Date date = format.parse(datestr);
            Calendar localCalendar = Calendar.getInstance();
            localCalendar.setTime(date);

            CurrentDayOfYear = localCalendar.get(Calendar.DAY_OF_YEAR);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return CurrentDayOfYear;
    }

    public static void loadInterstitial(Context c) {
        SplashActivity.mInterstitialAd = new InterstitialAd(c);
        SplashActivity.mInterstitialAd.setAdUnitId(c.getResources().getString(
                R.string.interstitial_ad_unit_id));

        AdRequest adRequest = new AdRequest.Builder().build();
        SplashActivity.mInterstitialAd.loadAd(adRequest);
    }

    public static void alertInfoBackup(final Context c) {

        LayoutInflater inflater = LayoutInflater.from(c);
        View dialogview = inflater.inflate(R.layout.alertdialog_infoimport,
                null);
        final AlertDialog alert = new AlertDialog.Builder(c).create();

        Button bImport = (Button) dialogview.findViewById(R.id.button1);

        bImport.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                MySQLiteHelper.importDB(c);
                Util.restart(c);
                alert.dismiss();
            }
        });

        alert.setView(dialogview);
        alert.show();
    }

    public static void alertTimer(final Context c) {

        LayoutInflater inflater = LayoutInflater.from(c);
        View dialogview = inflater.inflate(R.layout.alertdialog_timer, null);
        final AlertDialog alert = new AlertDialog.Builder(c).create();

        Button bAdd = (Button) dialogview.findViewById(R.id.button1);
        final ListView lvAlarm = (ListView) dialogview
                .findViewById(R.id.listView1);

        Util.loadAlarmList(c, lvAlarm);

        bAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Util.alertAddTimer(c, lvAlarm);
            }
        });

        alert.setView(dialogview);
        alert.show();
    }

    public static void alertAddTimer(final Context c, final ListView lvAlarm) {

        LayoutInflater inflater = LayoutInflater.from(c);
        View dialogview = inflater
                .inflate(R.layout.alertdialog_add_timer, null);
        final AlertDialog alert = new AlertDialog.Builder(c).create();

        // ArrayList<String> values = new ArrayList<String>();

        Button bSet = (Button) dialogview.findViewById(R.id.button1);
        final TimePicker tpTime = (TimePicker) dialogview
                .findViewById(R.id.timePicker1);
        final CheckBox cbIsRepeat = (CheckBox) dialogview
                .findViewById(R.id.checkBox1);

        bSet.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                MySQLiteHelper helper = new MySQLiteHelper(c);
                Calendar calendar = Calendar.getInstance();

                calendar.set(Calendar.HOUR_OF_DAY, tpTime.getCurrentHour());
                calendar.set(Calendar.MINUTE, tpTime.getCurrentMinute());
                calendar.set(Calendar.SECOND, 0);

                helper.addAlarm(new Alarm(0, calendar.getTimeInMillis(),
                        cbIsRepeat.isChecked() ? 1 : 0));
                loadAlarmList(c, lvAlarm);

                alert.dismiss();
            }
        });

        alert.setView(dialogview);
        alert.show();
    }

    public static void loadAlarmList(Context c, ListView list) {
        MySQLiteHelper helper = new MySQLiteHelper(c);
        ArrayList<Alarm> alarms = helper.getAlarms();
        ArrayList<String> str_alarm = new ArrayList<String>();

        for (Alarm alarm : alarms) {
            String time = new SimpleDateFormat("kk:mm").format(new Date(alarm
                    .getTime()));

            if (alarm.getIsRepeat() == 1) {
                time = time + " (diulang setiap hari)";
            }

            str_alarm.add(time);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c,
                android.R.layout.simple_list_item_1, android.R.id.text1,
                str_alarm);
        list.setAdapter(adapter);

    }

    public static void alertOptionBackup(final Context c) {

        LayoutInflater inflater = LayoutInflater.from(c);
        View dialogview = inflater.inflate(R.layout.alertdialog_menu_backup,
                null);
        final AlertDialog alert = new AlertDialog.Builder(c).create();

        Button bOnline = (Button) dialogview.findViewById(R.id.button1);
        Button bOffline = (Button) dialogview.findViewById(R.id.button2);

        bOnline.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

            }
        });

        bOffline.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

            }
        });

        alert.setView(dialogview);
        alert.show();
    }

    public static void alertOption(final Context c) {

        LayoutInflater inflater = LayoutInflater.from(c);
        View dialogview = inflater.inflate(R.layout.alert_menu_options, null);
        final AlertDialog alert = new AlertDialog.Builder(c).create();

        GridView grid = (GridView) dialogview.findViewById(R.id.gridView1);

        ArrayList<OptionItem> options = new ArrayList<OptionItem>();
        options.add(new OptionItem("Restore Database", R.mipmap.ic_restore));
        options.add(new OptionItem("Backup Database", R.mipmap.ic_backup));
        options.add(new OptionItem("Reminder", R.mipmap.ic_reminder));
        options.add(new OptionItem("Reset Database", R.mipmap.ic_reset));
        OptionAdapter adapter = new OptionAdapter(c, options);

        grid.setAdapter(adapter);

        grid.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                switch (arg2) {
                    case 0:
                        Util.alertInfoBackup(c);

                        break;
                    case 1:
                        MySQLiteHelper.exportDB(c);
                        break;
                    case 2:
                        //Util.alertTimer(c);
                        //currency = "Rp";
                        Toast.makeText(c, "Coming soon", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        AlertDialog.Builder builder = new AlertDialog.Builder(c);

                        builder.setTitle("Reset Database");

                        builder.setIcon(android.R.drawable.ic_delete);
                        builder.setMessage("Anda yakin ingin Reset Database?");

                        builder.setPositiveButton("YES",
                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface arg0,
                                                        int arg1) {
                                        // TODO Auto-generated method stub
                                        MySQLiteHelper helper = new MySQLiteHelper(
                                                c);
                                        helper.resetDatabase();
                                        Util.restart(c);

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

                        break;
                }

                alert.dismiss();
            }
        });

        alert.setView(dialogview);
        alert.show();
    }

    public static String getDateString(long timeStamp, SimpleDateFormat sdf) {

        try {
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        } catch (Exception ex) {
            return ex.toString();
        }
    }

    public static String formatUang(String nominal) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat format = NumberFormat.getCurrencyInstance(localeID);
        String currency = format.format(Double.parseDouble(nominal));

        return currency;
    }

    public static void refreshTimePickerAtAddTransactionFragment() {
        AddTransactionFragment.bTimePicker.setText(new SimpleDateFormat(
                "kk:mm:ss").format(new Date()));
        // AddTransactionFragment.bDatePicker.setText(new
        // SimpleDateFormat("dd/MM/yyyy").format(new Date()));
    }

    public static boolean checkTransactionDateInBudget(long tglTime,
                                                       Budget budget) {

        long startDate = budget.getTimeStartDate();
        long endDate = budget.getTimeEndDate();

        return (tglTime <= endDate) && (tglTime >= startDate);

    }

    public static long getTimeStamp(String dateStr, SimpleDateFormat sdf) {

        try {
            return sdf.parse(dateStr).getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            return 0;
        }

    }

    public static boolean budgetIsMoreThanZero(int idBudget, long amount,
                                               Context context) {
        MySQLiteHelper helper = new MySQLiteHelper(context);

        Budget budget = helper.getDetailBudget(idBudget);

        if ((Long.parseLong(budget.getLeft()) - amount) > 0) {
            return true;
        }

        return false;
    }

    public static boolean checkBudget(Context context) {

        MySQLiteHelper helper = new MySQLiteHelper(context);
        if (helper.getDetailLastBudget() != null) {
            Budget budget = helper.getDetailLastBudget();

            long nowDate = (new Date()).getTime();
            long startDate = budget.getTimeStartDate();
            long endDate = budget.getTimeEndDate();

            return (nowDate <= endDate) && (nowDate >= startDate);

        } else {
            return false;
        }

        // MySQLiteHelper helper = new MySQLiteHelper(context);
        // if (helper.getDetailLastBudget() != null) {
        // Budget budget = helper.getDetailLastBudget();
        //
        //
        // Date nowDate;
        // try {
        // nowDate = sdf.parse(sdf.format((new Date())));
        // Date startDate = sdf.parse(budget.getStartDate());
        // Date endDate = sdf.parse(budget.getEndDate());
        //
        // return (nowDate.after(startDate) && nowDate.before(endDate))
        // || (nowDate.equals(startDate))
        // || (nowDate.equals(endDate));
        // } catch (ParseException e) {
        // // TODO Auto-generated catch block
        // return false;
        // }
        //
        // } else {
        // return false;
        // }
    }

    public static void updateWidget(Context context) {
        MySQLiteHelper helper = new MySQLiteHelper(context);
        AppWidgetManager appWidgetManager = AppWidgetManager
                .getInstance(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.widget_layout);
        ComponentName thisWidget = new ComponentName(context,
                MyWidgetProvider.class);

        remoteViews.setTextViewText(R.id.textView1, "Sisa budget");
        remoteViews.setTextViewText(R.id.textView2,
                Util.formatUang(helper.getDetailLastBudget().getLeft()));
        remoteViews.setTextViewText(R.id.textView3, Util.getDateString(helper
                        .getDetailLastBudget().getTimeStartDate(),
                new SimpleDateFormat("dd/MM/yyyy kk:mm:ss")));
        remoteViews.setTextViewText(R.id.textView4, Util.getDateString(helper
                .getDetailLastBudget().getTimeEndDate(), new SimpleDateFormat(
                "dd/MM/yyyy kk:mm:ss")));

        appWidgetManager.updateAppWidget(thisWidget, remoteViews);

    }

    public static Date addMonths(Date date, int month) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, month); // minus number would decrement the days
        return cal.getTime();
    }

    public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); // minus number would decrement the days
        return cal.getTime();
    }

    public static void restart(Context c) {
        ((MainActivity) c).finish();

        Intent i = new Intent(c, MainActivity.class);
        c.startActivity(i);

    }

    public static TextWatcher onTextChangedListener(final EditText editText) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                editText.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    editText.setText(formattedString);
                    editText.setSelection(editText.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                editText.addTextChangedListener(this);
            }
        };
    }
}
