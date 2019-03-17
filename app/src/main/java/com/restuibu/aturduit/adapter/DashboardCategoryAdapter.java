package com.restuibu.aturduit.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.restuibu.aturduit.R;
import com.restuibu.aturduit.model.DashboardCategory;
import com.restuibu.aturduit.model.Transaksi;
import com.restuibu.aturduit.util.Util;

import java.util.ArrayList;
import java.util.List;

import static com.restuibu.aturduit.util.Util.getCategoryIcon;
import static com.restuibu.aturduit.util.Util.helper;

public class DashboardCategoryAdapter extends ArrayAdapter<DashboardCategory> implements Filterable {

    private Context context;
    public static String css = null;
    private ArrayList<DashboardCategory> itemsArrayList;
    private ArrayList<DashboardCategory> oriItemsArrayList;

    private Filter planetFilter;

    public DashboardCategoryAdapter(Context context, ArrayList<DashboardCategory> itemsArrayList) {

        super(context, R.layout.rowlayout_category_dashboard, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
        this.oriItemsArrayList = itemsArrayList;
    }

    public void resetData() {
        this.itemsArrayList = this.oriItemsArrayList;
    }

    public int getCount() {
        return itemsArrayList.size();
    }

    public DashboardCategory getItem(int position) {
        return itemsArrayList.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.rowlayout_category_dashboard, parent,
                false);

        TextView rank = (TextView) rowView.findViewById(R.id.textView1);
        ImageView category = (ImageView) rowView.findViewById(R.id.imageView1);
        TextView avg = (TextView) rowView.findViewById(R.id.textView2);
        TextView min = (TextView) rowView.findViewById(R.id.textView3);
        TextView max = (TextView) rowView.findViewById(R.id.textView4);
        TextView total = (TextView) rowView.findViewById(R.id.textView5);

        rank.setText("#"+Integer.toString(position+1));
        category.setImageResource(Util.getCategoryIcon(context,itemsArrayList.get(position).getKategori()));
        avg.setText(Util.formatUang2(itemsArrayList.get(position).getAvg_harga()));
        min.setText(Util.formatUang2((itemsArrayList.get(position).getMin_harga())));
        max.setText(Util.formatUang2((itemsArrayList.get(position).getMax_harga())));
        total.setText(Util.formatUang2((itemsArrayList.get(position).getSum_harga())));

        return rowView;
    }
}
