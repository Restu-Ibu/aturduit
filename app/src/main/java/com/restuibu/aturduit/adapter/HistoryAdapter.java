package com.restuibu.aturduit.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.restuibu.aturduit.fragment.HistoryFragment;
import com.restuibu.aturduit.R;
import com.restuibu.aturduit.model.History;
import com.restuibu.aturduit.model.Transaksi;
import com.restuibu.aturduit.util.Util;
import static com.restuibu.aturduit.util.Util.helper;

public class HistoryAdapter extends ArrayAdapter<History> implements Filterable {

	private Context context;
	public static String css = null;
	private ArrayList<History> itemsArrayList;
	private ArrayList<History> oriItemsArrayList;

	private Filter planetFilter;

	public HistoryAdapter(Context context, ArrayList<History> itemsArrayList) {

		super(context, R.layout.rowhistory_layout, itemsArrayList);

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

	public History getItem(int position) {
		return itemsArrayList.get(position);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		// 1. Create inflater
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// 2. Get rowView from inflater
		View rowView = inflater.inflate(R.layout.rowhistory_layout, parent,
				false);
		RelativeLayout selector = (RelativeLayout) rowView
				.findViewById(R.id.relativeLayout1);

		TextView tanggal = (TextView) rowView.findViewById(R.id.textView1);
		TextView total = (TextView) rowView.findViewById(R.id.textView2);
		TextView jumlah = (TextView) rowView.findViewById(R.id.textView3);
		LinearLayout llKategori;
		TextView tvKategori;

		tanggal.setText((itemsArrayList.get(position).getTanggal()));
		total.setText(Util.formatUang(itemsArrayList.get(position).getTotal()));
		jumlah.setText(itemsArrayList.get(position).getJumlah());

		if (!itemsArrayList.get(position).getTotal_food().equals("0")){
			llKategori = (LinearLayout) rowView.findViewById(R.id.llKategori1);
			llKategori.setVisibility(View.VISIBLE);
			tvKategori = (TextView) rowView.findViewById(R.id.tvKategori1);
			tvKategori.setText("("+itemsArrayList.get(position).getTotal_food()+")");
		}if (!itemsArrayList.get(position).getTotal_transport().equals("0")){
			llKategori = (LinearLayout) rowView.findViewById(R.id.llKategori2);
			llKategori.setVisibility(View.VISIBLE);
			tvKategori = (TextView) rowView.findViewById(R.id.tvKategori2);
			tvKategori.setText("("+itemsArrayList.get(position).getTotal_transport()+")");
		}if (!itemsArrayList.get(position).getTotal_entertain().equals("0")){
			llKategori = (LinearLayout) rowView.findViewById(R.id.llKategori3);
			llKategori.setVisibility(View.VISIBLE);
			tvKategori = (TextView) rowView.findViewById(R.id.tvKategori3);
			tvKategori.setText("("+itemsArrayList.get(position).getTotal_entertain()+")");
		}if (!itemsArrayList.get(position).getTotal_groceries().equals("0")){
			llKategori = (LinearLayout) rowView.findViewById(R.id.llKategori4);
			llKategori.setVisibility(View.VISIBLE);
			tvKategori = (TextView) rowView.findViewById(R.id.tvKategori4);
			tvKategori.setText("("+itemsArrayList.get(position).getTotal_groceries()+")");
		}if (!itemsArrayList.get(position).getTotal_other().equals("0")){
			llKategori = (LinearLayout) rowView.findViewById(R.id.llKategori5);
			llKategori.setVisibility(View.VISIBLE);
			tvKategori = (TextView) rowView.findViewById(R.id.tvKategori5);
			tvKategori.setText("("+itemsArrayList.get(position).getTotal_other()+")");
		}

		total.setGravity(Gravity.RIGHT);

		selector.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				ArrayList<Transaksi> listTransaksi = new ArrayList<Transaksi>();

				listTransaksi = helper.getAllTransaksiByTanggal(itemsArrayList
						.get(position).getTanggal());
				HistoryFragment.adapterTransaksi = new TransaksiAdapter(
						context, listTransaksi);

				HistoryFragment.list
						.setAdapter(HistoryFragment.adapterTransaksi);
				HistoryFragment.bBack.setVisibility(View.VISIBLE);
				HistoryFragment.adapterStatus = 2;

				HistoryFragment.tTitle1.setText(context.getResources().getString(R.string.jam));
				HistoryFragment.tTitle2.setText(context.getResources().getString(R.string.Deskripsi));
				HistoryFragment.tTitle2.setGravity(Gravity.LEFT);
				HistoryFragment.tTitle3.setText(context.getResources().getString(R.string.Harga));
				HistoryFragment.tTitle4.setText(context.getResources().getString(R.string.Kategori));
			}
		});

		selector.setOnLongClickListener(new OnLongClickListener() {

			public boolean onLongClick(View arg0) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(context);

				builder.setTitle("Hapus Transaksi");

				builder.setIcon(android.R.drawable.ic_delete);
				builder.setMessage("Anda yakin ingin menghapus transaksi?");

				builder.setPositiveButton("YES",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								helper.updateBudgetByDateHistory(itemsArrayList.get(
										position).getTanggal());
								helper.deleteHistory(itemsArrayList.get(
										position).getTanggal());
								

								// /
								ArrayList<History> listHistory = new ArrayList<History>();
								HistoryAdapter adapterHistory;

								listHistory = helper.getAllHistory();
								adapterHistory = new HistoryAdapter(context,
										listHistory);

								HistoryFragment.list.setAdapter(adapterHistory);
								HistoryFragment.adapterStatus = 1;

							}

						});

				builder.setNegativeButton("NO",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

							}

						});

				AlertDialog alert = builder.create();
				alert.show();

				return true;
			}
		});

		return rowView;
	}
	
	

	@Override
	public Filter getFilter() {
		if (planetFilter == null)
			planetFilter = new PlanetFilter();

		return planetFilter;
	}

	private class PlanetFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();
			// We implement here the filter logic
			if (constraint == null || constraint.length() == 0) {
				// No filter implemented we return all the list
				results.values = oriItemsArrayList;
				results.count = oriItemsArrayList.size();
			} else {
				// We perform filtering operation
				List<History> nPlanetList = new ArrayList<History>();

				for (int i = 0; i < itemsArrayList.size(); i++) {
					if (itemsArrayList.get(i).getTanggal()
							.toUpperCase()
							.startsWith(constraint.toString().toUpperCase())
							|| itemsArrayList
									.get(i)
									.getTotal()
									.toUpperCase()
									.startsWith(
											constraint.toString().toUpperCase())
							|| itemsArrayList
									.get(i)
									.getJumlah()
									.toUpperCase()
									.startsWith(
											constraint.toString().toUpperCase())
							) {
						nPlanetList.add(itemsArrayList.get(i));

					}
				}

				results.values = nPlanetList;
				results.count = nPlanetList.size();

			}
			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {

			// Now we have to inform the adapter about the new list filtered
			if (results.count == 0)
				notifyDataSetInvalidated();
			else {

				itemsArrayList = (ArrayList<History>) results.values;
				notifyDataSetChanged();
			}

		}

	}

	

}
