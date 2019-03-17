package com.restuibu.aturduit.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.restuibu.aturduit.activity.MainActivity;
import com.restuibu.aturduit.util.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.restuibu.aturduit.util.Constant.DATABASE_NAME;
import static com.restuibu.aturduit.util.Constant.DATABASE_VERSION;
import static com.restuibu.aturduit.util.Constant.TBL_TRANSAKSI;
import static com.restuibu.aturduit.util.Constant.TBL_BUDGET;
import static com.restuibu.aturduit.util.Constant.TBL_ALARM;
import static com.restuibu.aturduit.util.Util.currentDB;
import static com.restuibu.aturduit.util.Util.backupDB;
import static com.restuibu.aturduit.util.Util.createDirIfNotExists;
import static com.restuibu.aturduit.util.Util.exportDB;
import static com.restuibu.aturduit.util.Util.mAuth;
import static com.restuibu.aturduit.util.Util.updateBudgetActionBar;

public class MySQLiteHelper extends SQLiteOpenHelper {


    private Context context;


    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;


        // init when start activity
        //exportDB(context, 0);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create book table

        // timeStartDate dan timeEndDate disini untuk membandingkan
        String tbl_budget = "CREATE TABLE IF not exists tbl_budget ("
                + " idBudget integer PRIMARY KEY AUTOINCREMENT,"
                + " startDate date NOT NULL," + " endDate date NOT NULL,"
                + " category text NOT NULL," + " amount text NOT NULL,"
                + " left text NOT NULL, "
                + " timeStartDate timestamp NOT NULL,"
                + " timeEndDate timestamp NOT NULL)";

        // time disini untuk mengurutkan ketika display history
        String tbl_transaksi = "CREATE TABLE IF not exists tbl_transaksi ("
                + " idTransaksi integer PRIMARY KEY AUTOINCREMENT,"
                + " deskripsi text NOT NULL," + " harga text NOT NULL,"
                + " jam text NOT NULL," + " tanggal date NOT NULL,"
                + " time timestamp NOT NULL)";

        // ID = 1 utk pagi
        // ID = 2 utk siang
        // ID = 3 utk malem
        String tbl_alarm = "CREATE TABLE IF not exists tbl_alarm ("
                + " idAlarm integer PRIMARY KEY AUTOINCREMENT,"
                + " time integer NOT NULL," + " isRepeat integer NOT NULL)";

        // create table
        db.execSQL(tbl_budget);
        db.execSQL(tbl_transaksi);
        db.execSQL(tbl_alarm);

        // after update but for new users (newVersion == 2)
        db.execSQL("ALTER TABLE tbl_transaksi ADD COLUMN kategori TEXT");
        db.execSQL("UPDATE tbl_transaksi SET kategori = 'Lainnya' WHERE kategori IS NULL OR kategori =''");

        // after update but for new users (newVersion == 3)
        db.execSQL("INSERT INTO tbl_alarm VALUES (1, 9, 0)");
        db.execSQL("INSERT INTO tbl_alarm VALUES (2, 14, 0)");
        db.execSQL("INSERT INTO tbl_alarm VALUES (3, 20, 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion == 2){
            db.execSQL("ALTER TABLE tbl_transaksi ADD COLUMN kategori TEXT");
            db.execSQL("UPDATE tbl_transaksi SET kategori = 'Lainnya' WHERE kategori IS NULL OR kategori =''");
        }

        if(newVersion == 3){
            db.execSQL("INSERT INTO tbl_alarm VALUES (1, 9, 0)");
            db.execSQL("INSERT INTO tbl_alarm VALUES (2, 14, 0)");
            db.execSQL("INSERT INTO tbl_alarm VALUES (3, 20, 0)");
        }

        exportDB(context, 1, false);
    }

    public void updateBlankCategory() {
        if(DATABASE_VERSION >= 2){
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("UPDATE tbl_transaksi SET kategori = 'Lainnya' WHERE kategori IS NULL OR kategori =''");
        }
    }

    public Budget getDetailBudget(int idBudget) {
        String query = "select * from tbl_budget where idBudget=" + idBudget;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Budget budget = null;
        if (cursor.moveToFirst()) {
            do {
                budget = new Budget(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4),
                        cursor.getString(5),
                        Long.parseLong(cursor.getString(6)),
                        Long.parseLong(cursor.getString(7)));

            } while (cursor.moveToNext());
        }

        return budget;

    }

    public void updateBudgetByDate(String tgl, long amount) {
        ArrayList<Budget> budgets = getAllBudget();

        long tglTime = Util.getTimeStamp(tgl, new SimpleDateFormat(
                "dd/MM/yyyy kk:mm:ss"));

        for (int i = 0; i < budgets.size(); i++) {
            if (Util.checkTransactionDateInBudget(tglTime, budgets.get(i))) {

                if (amount < 0) {
                    amount = Math.abs(amount);
                    updateBudgetDifference(budgets.get(i).getIdBudget(), amount);
                } else {
                    amount = Math.abs(amount);
                    updateBudgetSum(budgets.get(i).getIdBudget(), amount);
                }
                updateBudgetActionBar();

                break;
            }

        }
    }

    public int getIdBudgetByDateTransaction(long tglTime) {
        ArrayList<Budget> budgets = getAllBudget();

        for (int i = 0; i < budgets.size(); i++) {
            if (Util.checkTransactionDateInBudget(tglTime, budgets.get(i))) {
                return budgets.get(i).getIdBudget();
            }

        }

        return -1;
    }

    public void updateBudgetByDateHistory(String tgl) {

        ArrayList<Transaksi> trans = getAllTransaksiByTanggal(tgl);
        /*
         * Toast.makeText(context, tgl + Integer.toString(trans.size()),
         * Toast.LENGTH_SHORT) .show();
         */
        for (int i = 0; i < trans.size(); i++) {
            updateBudgetByDate(trans.get(i).getTanggal() + " "
                            + trans.get(i).getJam(),
                    Long.parseLong(trans.get(i).getHarga()));

        }
        if (trans.size() > 0)
            Toast.makeText(context, "Budget telah diupdate", Toast.LENGTH_SHORT)
                    .show();

    }

    public Budget getDetailLastBudget() {
        String query = "select * from tbl_budget order by idBudget desc limit 1";

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Budget budget = null;
        if (cursor.moveToFirst()) {
            do {
                budget = new Budget(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4),
                        cursor.getString(5),
                        Long.parseLong(cursor.getString(6)),
                        Long.parseLong(cursor.getString(7)));

            } while (cursor.moveToNext());
        }

        return budget;

    }

    public void addBudget(Budget budget) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // values.put("idTransaksi", "");
        values.put("startDate", budget.getStartDate());
        values.put("endDate", budget.getEndDate());
        values.put("category", budget.getCategory());
        values.put("amount", budget.getAmount());
        values.put("left", budget.getLeft());
        values.put("timeStartDate", budget.getTimeStartDate());
        values.put("timeEndDate", budget.getTimeEndDate());
        db.insert(TBL_BUDGET, null, values);
        Toast.makeText(context, "Budget telah ditetapkan", Toast.LENGTH_SHORT)
                .show();
        if (Util.checkBudget(context))
            Util.updateWidget(context);

        db.close();

    }

    public ArrayList<Budget> getAllBudget() {
        ArrayList<Budget> budgets = new ArrayList<Budget>();
        // 1. build the query
        // String query = "select * from tbl_friendlist";

        String query = "select * from tbl_budget order by idBudget asc";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Budget budget = null;
        if (cursor.moveToFirst()) {
            do {
                budget = new Budget(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4),
                        cursor.getString(5),
                        Long.parseLong(cursor.getString(6)),
                        Long.parseLong(cursor.getString(7)));
                budgets.add(budget);

            } while (cursor.moveToNext());
        }

        return budgets;
    }

    public ArrayList<Budget> getAllWeeklyBudget() {
        ArrayList<Budget> budgets = new ArrayList<Budget>();
        // 1. build the query
        // String query = "select * from tbl_friendlist";

        String query = "select * from tbl_budget where category='Mingguan' order by idBudget asc";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Budget budget = null;
        if (cursor.moveToFirst()) {
            do {
                budget = new Budget(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4),
                        cursor.getString(5),
                        Long.parseLong(cursor.getString(6)),
                        Long.parseLong(cursor.getString(7)));
                budgets.add(budget);

            } while (cursor.moveToNext());
        }

        return budgets;
    }

    public ArrayList<Budget> getAllMonthlyBudget() {
        ArrayList<Budget> budgets = new ArrayList<Budget>();
        // 1. build the query
        // String query = "select * from tbl_friendlist";

        String query = "select * from tbl_budget where category='Bulanan' order by idBudget asc";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Budget budget = null;
        if (cursor.moveToFirst()) {
            do {
                budget = new Budget(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4),
                        cursor.getString(5),
                        Long.parseLong(cursor.getString(6)),
                        Long.parseLong(cursor.getString(7)));
                budgets.add(budget);

            } while (cursor.moveToNext());
        }

        return budgets;
    }

    public void deleteBudget(int idBudget) {
        SQLiteDatabase db = this.getWritableDatabase();
        Toast.makeText(context, "Budget terakhir dihapus", Toast.LENGTH_SHORT)
                .show();
        db.execSQL("delete from tbl_budget where idBudget='" + idBudget + "'");
        if (Util.checkBudget(context))
            Util.updateWidget(context);

    }

    public void updateBudgetBothDifference(int idBudget, long amount) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("update tbl_budget set amount=amount-" + amount
                + ", left=left-" + amount + " where idBudget=" + idBudget + "");
        if (Util.checkBudget(context))
            Util.updateWidget(context);
    }

    public void updateBudgetBothSum(int idBudget, long amount) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("update tbl_budget set amount=amount+" + amount
                + ", left=left+" + amount + " where idBudget=" + idBudget + "");
        if (Util.checkBudget(context))
            Util.updateWidget(context);
    }

    public void updateBudgetTotalDifference(int idBudget, long amount) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("update tbl_budget set amount=amount-" + amount
                + " where idBudget=" + idBudget + "");
        if (Util.checkBudget(context))
            Util.updateWidget(context);
    }

    public void updateBudgetTotalSum(int idBudget, long amount) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("update tbl_budget set amount=amount+" + amount
                + " where idBudget=" + idBudget + "");
        if (Util.checkBudget(context))
            Util.updateWidget(context);
    }


    public void updateBudgetDifference(int idBudget, long amount) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("update tbl_budget set left=left-" + amount
                + " where idBudget=" + idBudget + "");
        if (Util.checkBudget(context))
            Util.updateWidget(context);
    }

    public void updateBudgetSum(int idBudget, long amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("update tbl_budget set left=left+" + amount
                + " where idBudget=" + idBudget + "");
        if (Util.checkBudget(context))
            Util.updateWidget(context);
    }


    public void resetDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM tbl_budget");
        db.execSQL("DELETE FROM tbl_transaksi");
        db.execSQL("DELETE FROM tbl_alarm");
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE name='tbl_budget'");
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE name='tbl_transaksi'");
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE name='tbl_alarm'");

        db.execSQL("INSERT INTO tbl_alarm VALUES (1, 9, 0)");
        db.execSQL("INSERT INTO tbl_alarm VALUES (2, 14, 0)");
        db.execSQL("INSERT INTO tbl_alarm VALUES (3, 20, 0)");

    }

    public Transaksi getDetailTransaksi(int idTransaksi) {

        // 1. build the query
        // String query = "select * from tbl_friendlist";

        String query = "select * from tbl_transaksi where idTransaksi="
                + idTransaksi;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Transaksi trans = null;
        if (cursor.moveToFirst()) {
            do {
                trans = new Transaksi(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4),
                        Long.parseLong(cursor.getString(5)),cursor.getString(6));

            } while (cursor.moveToNext());
        }

        return trans;
    }

    public ArrayList<History> getAllHistory() {
        ArrayList<History> history = new ArrayList<History>();

        String query = ""
                + "SELECT "
                + "a.tanggal, "
                + "a.total, "
                + "a.jumlah, "
                + "b.total_food, "
                + "b.total_transport, "
                + "b.total_entertain, "
                + "b.total_groceries, "
                + "b.total_others, "
                + "b.total_hutang, "
                + "b.total_zis "
                + "FROM "
                + "(select distinct tanggal, sum(harga) total, count(*) jumlah from tbl_transaksi GROUP BY tanggal order by time asc) a "
                + "INNER JOIN "
                + "(select tanggal, SUM(case when kategori = 'Makanan dan minuman' then 1 else 0 end) as total_food, SUM(case when kategori = 'Transportasi' then 1  else 0 end) as total_transport, SUM(case when kategori = 'Hiburan' then 1  else 0 end) as total_entertain, SUM(case when kategori = 'Belanjaan' then 1  else 0 end) as total_groceries, SUM(case when kategori = 'Lainnya' then 1  else 0 end) as total_others, SUM(case when kategori = 'Hutang' then 1  else 0 end) as total_hutang, SUM(case when kategori = 'ZIS' then 1  else 0 end) as total_zis from tbl_transaksi GROUP BY tanggal) b "
                + "ON a.tanggal = b.tanggal";


                // "select distinct strftime('%d-%m-%Y', tanggal / 1000, 'unixepoch') tanggal, sum(harga) total, count(*) jumlah from tbl_transaksi GROUP BY tanggal order by tanggal asc";
        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        History hist = null;
        if (cursor.moveToFirst()) {
            do {
                hist = new History(cursor.getString(0), cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9));
                history.add(hist);

            } while (cursor.moveToNext());
        }

        return history;
    }



    public ArrayList<History> getAllMonthlyHistory() {
        ArrayList<History> history = new ArrayList<History>();
        // 1. build the query
        // String query = "select * from tbl_friendlist";

        String query = "select substr(tanggal,4) tanggal, sum(harga) total, count(*) jumlah from tbl_transaksi GROUP BY substr(tanggal,4)  order by time asc";
        // String query =
        // "select distinct strftime('%d-%m-%Y', tanggal / 1000, 'unixepoch') tanggal, sum(harga) total, count(*) jumlah from tbl_transaksi GROUP BY tanggal order by tanggal asc";
        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        History hist = null;
        if (cursor.moveToFirst()) {
            do {
                hist = new History(cursor.getString(0), cursor.getString(1),
                        cursor.getString(2));
                history.add(hist);

            } while (cursor.moveToNext());
        }

        return history;
    }

    public ArrayList<History> getAllYearlyHistory() {
        ArrayList<History> history = new ArrayList<History>();
        // 1. build the query
        // String query = "select * from tbl_friendlist";
        String query = "select substr(tanggal,7,4) tanggal, sum(harga) total, count(*) jumlah from tbl_transaksi GROUP BY substr(tanggal,7,4) order by time asc";
        // String query =
        // "select distinct strftime('%d-%m-%Y', tanggal / 1000, 'unixepoch') tanggal, sum(harga) total, count(*) jumlah from tbl_transaksi GROUP BY tanggal order by tanggal asc";
        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        History hist = null;
        if (cursor.moveToFirst()) {
            do {
                hist = new History(cursor.getString(0), cursor.getString(1),
                        cursor.getString(2));
                history.add(hist);

            } while (cursor.moveToNext());
        }

        return history;
    }

    public void addTransaksi(Transaksi transaksi) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // values.put("idTransaksi", "");
        values.put("deskripsi", transaksi.getDeskripsi());
        values.put("harga", transaksi.getHarga());
        values.put("jam", transaksi.getJam());
        values.put("tanggal", transaksi.getTanggal());
        values.put("time", transaksi.getTime());
        values.put("kategori", transaksi.getKategori());
        db.insert(TBL_TRANSAKSI, null, values);
        Toast.makeText(context, "Transaksi berhasil ditambahkan",
                Toast.LENGTH_SHORT).show();

        db.close();
    }

    public void addAlarm(Alarm alarm) {
        SQLiteDatabase db = this.getWritableDatabase();

        if (isExistAlarm(alarm.getTime())) {
            Toast.makeText(context, "Alarm sudah ada", Toast.LENGTH_SHORT)
                    .show();
        } else {
            ContentValues values = new ContentValues();
            // values.put("idTransaksi", "");
            values.put("time", alarm.getTime());
            values.put("isRepeat", alarm.getIsRepeat());
            db.insert(TBL_ALARM, null, values);
            Toast.makeText(context, "Alarm berhasil ditambahkan",
                    Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    public boolean isExistAlarm(long time) {
        String current = new SimpleDateFormat("kk:mm").format(new Date(time));

        for (Alarm alarm : getAlarms()) {
            String alarm_str = new SimpleDateFormat("kk:mm").format(new Date(
                    alarm.getTime()));
            if (current.equals(alarm_str)) {
                return true;
            }
        }

        return false;
    }

    public ArrayList<Alarm> getAlarms() {
        ArrayList<Alarm> alarms = new ArrayList<Alarm>();

        // 1. build the query
        // String query = "select * from tbl_friendlist";

        String query = "select * from tbl_alarm order by time asc";

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Alarm alarm = null;
        if (cursor.moveToFirst()) {
            do {
                alarm = new Alarm(Integer.parseInt(cursor.getString(0)),
                        Long.parseLong(cursor.getString(1)),
                        Integer.parseInt(cursor.getString(2)));

                alarms.add(alarm);
            } while (cursor.moveToNext());
        }

        return alarms;
    }

    public ArrayList<Transaksi> getAllTransaksiByTanggal(String tanggal) {
        ArrayList<Transaksi> transaksi = new ArrayList<Transaksi>();

        // 1. build the query
        // String query = "select * from tbl_friendlist";

        String query = "select * from tbl_transaksi where tanggal='" + tanggal
                + "' order by jam asc";

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Transaksi trans = null;
        if (cursor.moveToFirst()) {
            do {
                trans = new Transaksi(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4),
                        Long.parseLong(cursor.getString(5)), cursor.getString(6));

                transaksi.add(trans);
            } while (cursor.moveToNext());
        }

        return transaksi;
    }

    public void deleteTransaksi(int idTransaksi) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from tbl_transaksi where idTransaksi=" + idTransaksi);
        Toast.makeText(context, "Transaksi berhasil dihapus",
                Toast.LENGTH_SHORT).show();

    }

    public void deleteAlarm(int idAlarm) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from tbl_alarm where idAlarm =" + idAlarm);
        Toast.makeText(context, "alarm berhasil dihapus", Toast.LENGTH_SHORT)
                .show();

    }

    public void deleteHistory(String tanggal) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from tbl_transaksi where tanggal='" + tanggal + "'");
        Toast.makeText(context, "History berhasil dihapus", Toast.LENGTH_SHORT)
                .show();
    }

    public void updateTransaksi(Transaksi transaksi) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("update tbl_transaksi set " +
                "jam='" + transaksi.getJam() +
                "',tanggal='" + transaksi.getTanggal() +
                "',harga='" + transaksi.getHarga() +
                "',kategori='" + transaksi.getKategori() +
                "',deskripsi='" + transaksi.getDeskripsi() +
                "',time=" + transaksi.getTime() +
                " where idTransaksi='" + transaksi.getIdTransaksi() + "'");
        Toast.makeText(context, "Transaksi berhasil diedit", Toast.LENGTH_SHORT)
                .show();
    }

    public void updateReminder(Alarm alarm) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("update tbl_alarm set " +
                "time =" + alarm.getTime() +
                ",isRepeat =" + alarm.getIsRepeat() +
                " where idAlarm=" + alarm.getIdAlarm() + "");
    }
    public ArrayList<Alarm> getAllReminder() {
        ArrayList<Alarm> alarms = new ArrayList<Alarm>();

        String query = "SELECT * FROM tbl_alarm";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Alarm alarm = null;
        if (cursor.moveToFirst()) {
            do {
                alarm = new Alarm(Integer.parseInt(cursor.getString(0)),
                        Integer.parseInt(cursor.getString(1)),
                        Integer.parseInt(cursor.getString(2)));
                alarms.add(alarm);

            } while (cursor.moveToNext());
        }

        return alarms;
    }

    // added 20190316
    public ArrayList<String> getMonthsHistory(){
        ArrayList<String> S = new ArrayList<String>();

        // month = 03/2019
        // 1. build the query
        String query =
                "select distinct substr(tanggal,4) " +
                        "from tbl_transaksi " +
                        "order by substr(tanggal,4) asc";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        String s = null;
        if (cursor.moveToFirst()) {
            do {
                s = cursor.getString(0);
                S.add(s);

            } while (cursor.moveToNext());
        }

        return S;
    }

    public ArrayList<DashboardCategory> getAllCategories() {
        ArrayList<DashboardCategory> S = new ArrayList<DashboardCategory>();

        // month = 03/2019
        // 1. build the query
        String query =
                "select kategori, avg(harga), min(harga), max(harga), sum(harga) " +
                        "from tbl_transaksi " +
                        "group by kategori " +
                        "order by sum(harga) desc ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        DashboardCategory s = null;
        if (cursor.moveToFirst()) {
            do {
                s = new DashboardCategory(cursor.getString(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3), cursor.getString(4));
                S.add(s);

            } while (cursor.moveToNext());
        }

        return S;
    }

    public ArrayList<DashboardCategory> getAllCategoriesByDate(String month) {
        ArrayList<DashboardCategory> S = new ArrayList<DashboardCategory>();

        // month = 03/2019
        // 1. build the query
        String query =
                "select kategori, avg(harga), min(harga), max(harga), sum(harga) " +
                        "from tbl_transaksi " +
                        "where " +
                        "substr(tanggal,4) = '"+month+"' " +
                        "group by kategori " +
                        "order by sum(harga) desc ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        DashboardCategory s = null;
        if (cursor.moveToFirst()) {
            do {
                s = new DashboardCategory(cursor.getString(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3), cursor.getString(4));
                S.add(s);

            } while (cursor.moveToNext());
        }

        return S;
    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }



}
