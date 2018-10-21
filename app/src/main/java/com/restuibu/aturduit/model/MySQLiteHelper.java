package com.restuibu.aturduit.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MySQLiteHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "costtracker";
    public static final String TBL_TRANSAKSI = "tbl_transaksi";
    public static final String TBL_BUDGET = "tbl_budget";
    public static final String TBL_ALARM = "tbl_alarm";

    private Context context;

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
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

        String tbl_alarm = "CREATE TABLE IF not exists tbl_alarm ("
                + " idAlarm integer PRIMARY KEY AUTOINCREMENT,"
                + " time integer NOT NULL," + " isRepeat integer NOT NULL)";

        // create table
        db.execSQL(tbl_budget);
        db.execSQL(tbl_transaksi);
        db.execSQL(tbl_alarm);
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
                    // Toast.makeText(context,
                    // "diff" + budgets.get(i).getIdBudget(),
                    // Toast.LENGTH_SHORT).show();
                } else {
                    amount = Math.abs(amount);
                    updateBudgetSum(budgets.get(i).getIdBudget(), amount);
                    // Toast.makeText(context,
                    // "sum" + budgets.get(i).getIdBudget(),
                    // Toast.LENGTH_SHORT).show();
                }

                Toast.makeText(context, "Budget telah diupdate",
                        Toast.LENGTH_SHORT).show();
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
        Toast.makeText(context, "Database berhasil direset", Toast.LENGTH_LONG)
                .show();
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
                        Long.parseLong(cursor.getString(5)));

            } while (cursor.moveToNext());
        }

        return trans;
    }

    public ArrayList<History> getAllHistory() {
        ArrayList<History> history = new ArrayList<History>();
        // 1. build the query
        // String query = "select * from tbl_friendlist";

        String query = "select distinct tanggal, sum(harga) total, count(*) jumlah from tbl_transaksi GROUP BY tanggal order by time asc";
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
                        Long.parseLong(cursor.getString(5)));

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
        db.execSQL("update tbl_transaksi set jam='" + transaksi.getJam()
                + "',tanggal='" + transaksi.getTanggal() + "',harga='"
                + transaksi.getHarga() + "',deskripsi='"
                + transaksi.getDeskripsi() + "',time=" + transaksi.getTime()
                + " where idTransaksi='" + transaksi.getIdTransaksi() + "'");
        Toast.makeText(context, "Transaksi berhasil diedit", Toast.LENGTH_SHORT)
                .show();
    }

    public static void importDB(Context c) {
        FileChannel source = null;
        FileChannel destination = null;

        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        String currentDBPath = "/data/" + c.getPackageName() + "/databases/"
                + DATABASE_NAME;
        String backupDBPath = DATABASE_NAME + "_backup";
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);

        try {

            if (currentDB.exists()) {
                source = new FileInputStream(backupDB).getChannel();
                destination = new FileOutputStream(currentDB).getChannel();
                destination.transferFrom(source, 0, source.size());
                source.close();
                destination.close();
                Toast.makeText(c, "Database berhasil diimport",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(c, "Database tidak ditemukan di folder root",
                        Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
        }
    }

    public static void exportDB(Context c) {
        FileChannel source = null;
        FileChannel destination = null;

        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        String currentDBPath = "/data/" + c.getPackageName() + "/databases/"
                + DATABASE_NAME;
        String backupDBPath = DATABASE_NAME + "_backup";
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);

        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(c, "Database dibackup ke\n" + backupDB.toString(),
                    Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed

        this.onCreate(db);
    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }

}
