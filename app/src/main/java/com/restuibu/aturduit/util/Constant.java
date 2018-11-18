package com.restuibu.aturduit.util;

import android.os.Environment;

import com.restuibu.aturduit.R;

import java.io.File;

public class Constant {
    // Database Version
    public static final int DATABASE_VERSION = 2;
    // Database Name
    public static final String DATABASE_NAME = "costtracker";
    public static final String TBL_TRANSAKSI = "tbl_transaksi";
    public static final String TBL_BUDGET = "tbl_budget";
    public static final String TBL_ALARM = "tbl_alarm";

    // export import db
    public static File currentDB = null;
    public static File backupDB = null;


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
