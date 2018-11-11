package com.restuibu.aturduit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.restuibu.aturduit.R;
import com.restuibu.aturduit.model.About;
import com.restuibu.aturduit.model.Util;

import java.util.ArrayList;

public class AboutListViewAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<About> Abouts = new ArrayList<>();
    private String jenis;

    public AboutListViewAdapter(Context c) {
        mContext = c;
        this.jenis = jenis;
        Abouts = Util.getAllAbout();

    }

    public int getCount() {
        return Abouts.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;


        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.layout_about, null);
            holder = new ViewHolder();
            holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.linearLayout1);
            holder.text = (TextView) convertView.findViewById(R.id.textView1);
            holder.image = (ImageView) convertView.findViewById(R.id.imageView1);

            // if it's not recycled, initialize some attributes
            final About a = Abouts.get(position);

            holder.image.setImageResource(a.getImg());
            holder.text.setText(a.getDesc());

            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    // references to our images


    static class ViewHolder {
        LinearLayout linearLayout;
        ImageView image;
        TextView text;
    }

}

