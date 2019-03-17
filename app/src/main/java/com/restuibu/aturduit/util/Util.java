package com.restuibu.aturduit.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.restuibu.aturduit.activity.DashboardActivity;
import com.restuibu.aturduit.activity.GoogleSignInActivity;
import com.restuibu.aturduit.activity.HistoryAndStatisticBudgetActivity;
import com.restuibu.aturduit.activity.TestActivity;
import com.restuibu.aturduit.adapter.DashboardCategoryAdapter;
import com.restuibu.aturduit.broadcast.NotificationPublisher;
import com.restuibu.aturduit.fragment.AddTransactionFragment;
import com.restuibu.aturduit.activity.MainActivity;
import com.restuibu.aturduit.MyWidgetProvider;
import com.restuibu.aturduit.R;
import com.restuibu.aturduit.activity.SplashActivity;
import com.restuibu.aturduit.adapter.OptionAdapter;
import com.restuibu.aturduit.model.About;
import com.restuibu.aturduit.model.Alarm;
import com.restuibu.aturduit.model.Budget;
import com.restuibu.aturduit.model.DashboardCategory;
import com.restuibu.aturduit.model.MySQLiteHelper;
import com.restuibu.aturduit.model.OptionItem;

import static android.content.Context.MODE_PRIVATE;
import static com.restuibu.aturduit.activity.DashboardActivity.button1;
import static com.restuibu.aturduit.util.Constant.DATABASE_NAME;
import static com.restuibu.aturduit.util.Constant.NOTIFICATION_CHANNEL_ID;
import static com.restuibu.aturduit.util.Constant.NOTIFICATION_CHANNEL_NAME;
import static com.restuibu.aturduit.util.Constant.NOTIFICATION_MORNING;
import static com.restuibu.aturduit.util.Constant.NOTIFICATION_NIGHT;
import static com.restuibu.aturduit.util.Constant.NOTIFICATION_NOON;
import static com.restuibu.aturduit.util.Constant.PERMISSIONS_STORAGE;
import static com.restuibu.aturduit.util.Constant.REQUEST_EXTERNAL_STORAGE;


public class Util {
    private static ProgressDialog pd;

    // menu
    public static Menu menu;


    // db
    public static MySQLiteHelper helper;
    public static SQLiteDatabase db;

    // export import db
    public static File currentDB = null;
    public static File backupDB = null;

    public static FirebaseAuth mAuth;
    public static GoogleSignInClient mGoogleSignInClient;

    public static GestureDetectorCompat gestureDetectorCompat;

    public static void signOut(final Activity activity) {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(activity,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        backupDB.delete();
                        activity.deleteDatabase(DATABASE_NAME);
                        Intent i = new Intent(activity, GoogleSignInActivity.class);
                        activity.startActivity(i);
                        ((MainActivity) activity).finish();
                    }
                });
    }


    public static void showProgress(Context c, String msg) {
        pd = new ProgressDialog(c);
        pd.setMessage(msg);
        pd.show();
    }

    public static void stopProgress() {
        if (pd.isShowing())
            pd.dismiss();
    }

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
                importDB(c, false);
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

    public static String parseDateCategory(String time) {
        String inputPattern = "MM/yyyy";
        String outputPattern = "MMM yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String parseDateCategory2(String time) {
        String inputPattern = "MMM yyyy";
        String outputPattern = "MM/yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
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
        //alert.setTitle("Menu");

        ArrayList<OptionItem> options = new ArrayList<OptionItem>();
        options.add(new OptionItem(c.getString(R.string.option_restore_db), R.mipmap.ic_restore));
        options.add(new OptionItem(c.getString(R.string.option_backup_db), R.mipmap.ic_backup));
        options.add(new OptionItem(c.getString(R.string.option_reminder), R.mipmap.ic_reminder));
        options.add(new OptionItem(c.getString(R.string.option_reset_db), R.mipmap.ic_reset));
        OptionAdapter adapter = new OptionAdapter(c, options);

        grid.setAdapter(adapter);

        grid.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                switch (arg2) {
                    case 0:
                        importDB(c, false);
                        break;
                    case 1:
                        exportDB(c, 1, false);
                        break;
                    case 2:
                        alertReminder(c);

                        break;
                    case 3:
                        AlertDialog.Builder builder = new AlertDialog.Builder(c);

                        builder.setTitle(c.getString(R.string.option_reset_db));

                        builder.setIcon(android.R.drawable.ic_delete);
                        builder.setMessage(c.getString(R.string.option_reset_db2));

                        builder.setPositiveButton(c.getString(R.string.btn_yes),
                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface arg0,
                                                        int arg1) {
                                        // TODO Auto-generated method stub
                                        helper.resetDatabase();

                                        Toast.makeText(c, c.getString(R.string.toast_reset_db), Toast.LENGTH_LONG)
                                                .show();

                                        Util.restart(c);

                                    }

                                });

                        builder.setNegativeButton(c.getString(R.string.btn_no),
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

    public static String formatUang2(String nominal) {
        //return currency;
        Locale localeID = new Locale("in", "ID");
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(localeID);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setCurrencySymbol("");
        formatter.setDecimalFormatSymbols(symbols);

        return formatter.format(Double.parseDouble(nominal));
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
        if (helper.getDetailLastBudget() != null) {
            Budget budget = helper.getDetailLastBudget();

            long nowDate = (new Date()).getTime();
            long startDate = budget.getTimeStartDate();
            long endDate = budget.getTimeEndDate();

            return (nowDate <= endDate) && (nowDate >= startDate);

        } else {
            return false;
        }

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


    /**
     * Checks if the app has permission to write to device storage
     * <p>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public static void importDB(final Context c, final boolean fromSignIn) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        String userDBName = mAuth.getCurrentUser().getUid();
        StorageReference backupDBRef = storageRef.child(c.getPackageName() + "/backupDB/" + userDBName);

        Util.showProgress(c, c.getString(R.string.toast_progress_restore_db));

        backupDBRef.getFile(backupDB).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                try {
                    FileChannel source = new FileInputStream(backupDB).getChannel();
                    FileChannel destination = new FileOutputStream(currentDB).getChannel();
                    destination.transferFrom(source, 0, source.size());
                    source.close();
                    destination.close();

                    Toast.makeText(c, c.getString(R.string.toast_progress_restore_db2),
                            Toast.LENGTH_SHORT).show();

                    if (fromSignIn) {
                        helper = new MySQLiteHelper(c);
                        db = helper.getWritableDatabase();
                        helper.updateBlankCategory();
                        setInitReminder(c);
                        changeBudgetTitle(c);
                        addEmailInSignOutMenu(c);
                    } else {
                        Util.restart(c);
                    }


                } catch (Exception e) {
                    Toast.makeText(c, e.toString(),
                            Toast.LENGTH_SHORT).show();
                }
                Util.stopProgress();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                FileChannel source = null;
                FileChannel destination = null;
                try {

                    // check if backupDB not yet exist means its first time user
                    if(fromSignIn){
                        if(!backupDB.exists()){
                            helper = new MySQLiteHelper(c);
                            db = helper.getWritableDatabase();
                            changeBudgetTitle(c);
                            addEmailInSignOutMenu(c);
                        }
                    }

                    Toast.makeText(c, c.getString(R.string.toast_progress_restore_db3),
                            Toast.LENGTH_SHORT).show();

                }
                catch (Exception e) {
                    Toast.makeText(c, e.toString(),
                            Toast.LENGTH_SHORT).show();
                }
                Util.stopProgress();
            }
        });


    }

    public static boolean createDirIfNotExists(File file) {
        boolean ret = true;

        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.e("TravellerLog :: ", "Problem creating folder");
                ret = false;
            }
        }
        return ret;
    }

    public static void exportDB(Context c, int alsoCloud, boolean isSignOut) {
        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            if (alsoCloud == 1) {
                uploadToCloudFirebaseStorage(c, isSignOut);
            }
        } catch (IOException e) {
            Toast.makeText(c, e.toString(),
                    Toast.LENGTH_LONG).show();
        }
    }

    public static void uploadToCloudFirebaseStorage(final Context c, final boolean isSignOut) {
        //// Firebase storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();

        Uri file = Uri.fromFile(backupDB);
        String userDBName = mAuth.getCurrentUser().getUid();
        StorageReference backupDBRef = storageRef.child(c.getPackageName() + "/backupDB/" + userDBName);
        UploadTask uploadTask = backupDBRef.putFile(file);

        Util.showProgress(c, c.getString(R.string.toast_progress_backup_db));

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(c, c.getString(R.string.toast_progress_backup_db3),
                        Toast.LENGTH_SHORT).show();
                Util.stopProgress();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(c, c.getString(R.string.toast_progress_backup_db2),
                        Toast.LENGTH_SHORT).show();

                if (isSignOut) {
                    Util.signOut((Activity) c);
                }

                Util.stopProgress();
            }
        });
    }

    public static void changeBudgetTitle(Context c) {
        if (menu != null) {
            MenuItem budgetMenu = menu.findItem(R.id.budget);

            if (!Util.checkBudget(c)) {
                SpannableString s = new SpannableString(c.getString(R.string.menu_budget2));
                s.setSpan(new ForegroundColorSpan(Color.RED), 0, s.length(), 0);
                budgetMenu.setTitle(s);

            } else {
                updateBudgetActionBar();
            }
        }
    }

    public static void addEmailInSignOutMenu(Context c) {

        if (menu != null) {
            MenuItem budgetMenu = menu.findItem(R.id.action_signout);

            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(c);

            SpannableString s = new SpannableString("Sign Out (" + acct.getEmail() + ")");
            //s.setSpan(new ForegroundColorSpan(Color.RED), 0, s.length(), 0);
            budgetMenu.setTitle(s);
        }
    }

    public static void updateBudgetActionBar() {
        MenuItem budgetMenu = menu.findItem(R.id.budget);
        String budgetLeft = helper.getDetailLastBudget().getLeft();
        //Log.e("budget left", budgetLeft);

        SpannableString s = new SpannableString(Util.formatUang(budgetLeft));

        s.setSpan(new RelativeSizeSpan(1.3f), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        if (Long.parseLong(budgetLeft) < 0) {
            s.setSpan(new ForegroundColorSpan(Color.RED), 0, s.length(), 0);
        }

        budgetMenu.setTitle(s);
    }

    public static void removeOtherCategories(Context c, ImageView[] iv, int id) {
        for (int i = 0; i < 5; i++) {
            if (i != id)
                iv[i].setBackgroundColor(0);
        }
    }

    public static void alertReminder(final Context c) {

        LayoutInflater inflater = LayoutInflater.from(c);
        View dialogview = inflater.inflate(R.layout.alertdialog_reminder,
                null);
        final AlertDialog alert = new AlertDialog.Builder(c).create();
        alert.setTitle(c.getString(R.string.option_reminder2));

        final Spinner spinPagi = dialogview.findViewById(R.id.spinner1);
        final Spinner spinSiang = dialogview.findViewById(R.id.spinner2);
        final Spinner spinMalam = dialogview.findViewById(R.id.spinner3);

        final Switch sPagi = dialogview.findViewById(R.id.switch1);
        final Switch sSiang = dialogview.findViewById(R.id.switch2);
        final Switch sMalam = dialogview.findViewById(R.id.switch3);
        Button bSimpan = dialogview.findViewById(R.id.bSimpan);
        Button bBatal = dialogview.findViewById(R.id.bBatal);

        setSpinnerValue(c, spinPagi, "pagi");
        setSpinnerValue(c, spinSiang, "siang");
        setSpinnerValue(c, spinMalam, "malam");


        ArrayList<Alarm> alarms = helper.getAllReminder();
        Alarm alarm_pagi = alarms.get(0);
        Alarm alarm_siang = alarms.get(1);
        Alarm alarm_malam = alarms.get(2);

        // INIT
        sPagi.setChecked((alarm_pagi.getIsRepeat() == 1) ? true : false);
        sSiang.setChecked((alarm_siang.getIsRepeat() == 1) ? true : false);
        sMalam.setChecked((alarm_malam.getIsRepeat() == 1) ? true : false);

        spinPagi.setEnabled((alarm_pagi.getIsRepeat() == 1) ? true : false);
        spinSiang.setEnabled((alarm_siang.getIsRepeat() == 1) ? true : false);
        spinMalam.setEnabled((alarm_malam.getIsRepeat() == 1) ? true : false);

        spinPagi.setSelection(getSpinnerIndex(spinPagi, getStringInt((int) alarm_pagi.getTime())));
        spinSiang.setSelection(getSpinnerIndex(spinSiang, getStringInt((int) alarm_siang.getTime())));
        spinMalam.setSelection(getSpinnerIndex(spinMalam, getStringInt((int) alarm_malam.getTime())));
        // end INIT

        sPagi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                spinPagi.setEnabled(b);
            }
        });

        sSiang.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                spinSiang.setEnabled(b);
            }
        });

        sMalam.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                spinMalam.setEnabled(b);
            }
        });

        bSimpan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpdateReminder(c, NOTIFICATION_MORNING, spinPagi, sPagi);
                setUpdateReminder(c, NOTIFICATION_NOON, spinSiang, sSiang);
                setUpdateReminder(c, NOTIFICATION_NIGHT, spinMalam, sMalam);

                Toast.makeText(c, c.getString(R.string.toast_reminder), Toast.LENGTH_SHORT).show();

                alert.dismiss();
            }
        });

        bBatal.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });

        alert.setView(dialogview);
        alert.show();
    }

    public static void setInitReminder(Context c){
        ArrayList<Alarm> alarms = helper.getAllReminder();
        Alarm alarm_pagi = alarms.get(0);
        Alarm alarm_siang = alarms.get(1);
        Alarm alarm_malam = alarms.get(2);

        setUpdateReminder2(c, alarm_pagi.getIdAlarm(), (int) alarm_pagi.getTime(), alarm_pagi.getIsRepeat());
        setUpdateReminder2(c, alarm_siang.getIdAlarm(), (int) alarm_siang.getTime(), alarm_siang.getIsRepeat());
        setUpdateReminder2(c, alarm_malam.getIdAlarm(), (int) alarm_malam.getTime(), alarm_malam.getIsRepeat());
    }

    private static void setUpdateReminder(Context c, int waktu, Spinner spin, Switch sw) {
        int idAlarm = waktu;
        int time = Integer.parseInt(spin.getSelectedItem().toString());
        int isChecked = (sw.isChecked()) ? 1 : 0;

        // Update DB
        Alarm alarm = new Alarm(idAlarm, time, isChecked);
        helper.updateReminder(alarm);

        setUpdateReminder2(c, idAlarm, time, isChecked);
    }

    private static void setUpdateReminder2(Context c, int idAlarm, int time, int isChecked) {
        // Update alarm
        String title = "";
        switch (idAlarm) {
            case 1:
                title = c.getString(R.string.reminder_title1);
                break;
            case 2:
                title = c.getString(R.string.reminder_title2);
                break;
            case 3:
                title = c.getString(R.string.reminder_title3);
                break;

        }
        if (isChecked == 1){
            cancelReminder(c, idAlarm);
            scheduleNotification(c, getNotification(c, title), idAlarm, time);
        }
        else {
            cancelReminder(c, idAlarm);
        }
    }

    private static void cancelReminder(Context c, int idAlarm) {
        AlarmManager alarmManager = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent(c, NotificationPublisher.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(c, idAlarm, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
    }

    public static void cancelAllReminder(Context c) {
        AlarmManager alarmManager = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);

        PendingIntent pendingIntent;
        Intent notificationIntent;
        notificationIntent = new Intent(c, NotificationPublisher.class);

        pendingIntent = PendingIntent.getBroadcast(c, NOTIFICATION_MORNING, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);

        pendingIntent = PendingIntent.getBroadcast(c, NOTIFICATION_NOON, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);

        pendingIntent = PendingIntent.getBroadcast(c, NOTIFICATION_NIGHT, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
    }


    private static int getSpinnerIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }

        return 0;
    }

    private static String getStringInt(int i) {
        String result = Integer.toString(i);

        if (i < 10) {
            result = "0" + Integer.toString(i);
        }

        return result;
    }

    //private method of your class
    private static void setSpinnerValue(Context c, Spinner spin, String waktu) {
        String[] pagi = new String[]{"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11"};
        String[] siang = new String[]{"12", "13", "14", "15", "16", "17"};
        String[] malam = new String[]{"18", "19", "20", "21", "22", "23"};

        ArrayList list = null;

        switch (waktu) {
            case "pagi":
                list = new ArrayList<>(Arrays.asList(pagi));
                break;
            case "siang":
                list = new ArrayList<>(Arrays.asList(siang));
                break;
            case "malam":
                list = new ArrayList<>(Arrays.asList(malam));
                break;
        }

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                c, android.R.layout.simple_spinner_item, list);

        spin.setAdapter(spinnerArrayAdapter);
        //spin.setSelection(defaultPosition);
    }


    private static Notification getNotification(Context c, String title) {
        Intent intent = new Intent(c, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(c, 0, intent, 0);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(c, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_icon)
                .setContentTitle(title)
                .setContentText("Jangan lupa catet transaksi!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setSound(alarmSound)
                .setAutoCancel(true);

        return mBuilder.build();
    }

    private static void scheduleNotification(Context c, Notification notification, int ID, int hour) {
        // Create notification channel
        NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        NotificationManager notificationManager = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.RED);
        notificationChannel.enableVibration(true);

        Intent notificationIntent = new Intent(c, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, ID);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        int RequestCode = ID;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(c, RequestCode, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, getTime(hour), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public static long getTime(int hour) {
        // Quote in Morning at 08:32:00 AM
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Calendar cur = Calendar.getInstance();

        if (cur.after(calendar)) {
            calendar.add(Calendar.DATE, 1);
        }

        return calendar.getTimeInMillis();
    }


    public static void alertDashboardPeriod(final Context c){
        LayoutInflater inflater = LayoutInflater.from(c);
        View dialogview = inflater.inflate(
                R.layout.alertdialog_list, null);
        final AlertDialog alert = new AlertDialog.Builder(c)
                .create();
        alert.setTitle("Pilih Periode");
        final ListView listView1 = (ListView) dialogview.findViewById(R.id.listView1);
        final MySQLiteHelper helper = new MySQLiteHelper(c);

        ArrayList<String> list = helper.getMonthsHistory();
        list.add("Semua Periode");
        for (int i=0; i<list.size(); i++){
            if(i != list.size() - 1)
                list.set(i, parseDateCategory(list.get(i)));
        }

        ArrayAdapter adapter = new ArrayAdapter(c,
                android.R.layout.simple_list_item_1, list);
        listView1.setAdapter(adapter);
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                DashboardActivity.button1.setText(selectedItem);

                ArrayList<DashboardCategory> list;
                if (!selectedItem.equals("Semua Periode")) {
                    list = helper.getAllCategoriesByDate(parseDateCategory2(selectedItem));
                }
                else {
                    list = helper.getAllCategories();
                }

                DashboardCategoryAdapter adapter = new DashboardCategoryAdapter(c, list);
                DashboardActivity.lvCategory.setAdapter(adapter);
                DashboardActivity.setData(c, list);

                alert.dismiss();
            }
        });

        alert.setView(dialogview);
        alert.show();
    }

    public static int getCategoryIcon(Context context, String label){
        int res = 0;

        if (label.equals(context.getString(R.string.kategori1))) {
            res = R.mipmap.ic_food;
        } else if (label.equals(context.getString(R.string.kategori2))) {
            res = R.mipmap.ic_transport;
        } else if (label.equals(context.getString(R.string.kategori3))) {
            res = R.mipmap.ic_entertain;
        } else if (label.equals(context.getString(R.string.kategori4))) {
            res = R.mipmap.ic_groceries;
        } else if (label.equals(context.getString(R.string.kategori5))) {
            res = R.mipmap.ic_others;
        } else if (label.equals(context.getString(R.string.kategori6))) {
            res = R.mipmap.ic_hutang;
        } else if (label.equals(context.getString(R.string.kategori7))) {
            res = R.mipmap.ic_zis;
        }

        return res;
    }

}
