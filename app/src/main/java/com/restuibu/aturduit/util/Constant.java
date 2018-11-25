package com.restuibu.aturduit.util;

import android.Manifest;
import android.os.Environment;

import com.restuibu.aturduit.R;
import com.restuibu.aturduit.model.MySQLiteHelper;

import java.io.File;

public class Constant {
    // notif
    public static final String NOTIFICATION_CHANNEL_ID = "12345";
    public static final String NOTIFICATION_CHANNEL_NAME = "12345-name";
    public static final int NOTIFICATION_MORNING = 1;
    public static final int NOTIFICATION_NOON = 2;
    public static final int NOTIFICATION_NIGHT = 3;

    // Database Version
    public static final int DATABASE_VERSION = 3;
    // Database Name
    public static final String DATABASE_NAME = "costtracker";
    public static final String TBL_TRANSAKSI = "tbl_transaksi";
    public static final String TBL_BUDGET = "tbl_budget";
    public static final String TBL_ALARM = "tbl_alarm";

    // swipe
    public static float MIN_DISTANCE = 100;

    // Storage Permissions
    public static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static final String privacyPolicyUrl = "http://guekeloe.blogspot.com/2018/10/atur-duit.html";

    public static final String[] konten_desc_about = {
            "Informasi Aplikasi", "Bagikan", "Kebijakan Privasi"
    };
    public static final Integer[] konten_img_about = {
            R.mipmap.ic_information, R.mipmap.ic_share, R.mipmap.ic_lock
    };
    public static final String[] konten_url_about = {
            "https://www.facebook.com/majelistaklimtelkomsel", "https://twitter.com/mttelkomsel", "https://twitter.com/mttelkomsel"
    };


}
