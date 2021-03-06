package com.restuibu.aturduit.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.restuibu.aturduit.HistoryFragment;
import com.restuibu.aturduit.R;
import com.restuibu.aturduit.model.MySQLiteHelper;
import com.restuibu.aturduit.model.Transaksi;
import com.restuibu.aturduit.model.Util;

public class TransaksiAdapter extends ArrayAdapter<Transaksi> implements
		Filterable {

	private Context context;
	public static String css = null;
	private ArrayList<Transaksi> itemsArrayList;
	private ArrayList<Transaksi> oriItemsArrayList;

	private Filter planetFilter;

	public TransaksiAdapter(Context context, ArrayList<Transaksi> itemsArrayList) {

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

	public Transaksi getItem(int position) {
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

		TextView jam = (TextView) rowView.findViewById(R.id.textView1);
		TextView deskripsi = (TextView) rowView.findViewById(R.id.textView2);
		TextView harga = (TextView) rowView.findViewById(R.id.textView3);

		// 3. get
		jam.setText(itemsArrayList.get(position).getJam());
		deskripsi.setText(itemsArrayList.get(position).getDeskripsi());
		harga.setText(Util.formatUang(itemsArrayList.get(position).getHarga()));

		selector.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				LayoutInflater inflater = LayoutInflater.from(context);
				View dialogview = inflater.inflate(
						R.layout.alertdialog_detailhistory, null);
				AlertDialog.Builder alert = new AlertDialog.Builder(context);

				alert.setView(dialogview);

				TextView idTransaksi = (TextView) dialogview
						.findViewById(R.id.textView11);
				TextView deskripsi = (TextView) dialogview
						.findViewById(R.id.textView15);
				TextView harga = (TextView) dialogview
						.findViewById(R.id.textView14);
				TextView jam = (TextView) dialogview
						.findViewById(R.id.textView12);
				TextView tanggal = (TextView) dialogview
						.findViewById(R.id.textView13);

				idTransaksi.setText(Integer.toString(itemsArrayList.get(
						position).getIdTransaksi()));
				deskripsi.setText(itemsArrayList.get(position).getDeskripsi());
				harga.setText(Util.formatUang(itemsArrayList.get(position)
						.getHarga()));
				jam.setText(itemsArrayList.get(position).getJam());

				tanggal.setText(itemsArrayList.get(position).getTanggal());

				alert.show();
			}
		});

		selector.setOnLongClickListener(new OnLongClickListener() {

			public boolean onLongClick(View arg0) {
				// TODO Auto-generated method stub
				LayoutInflater inflater = LayoutInflater.from(context);
				View dialogview = inflater.inflate(
						R.layout.alertdialog_deleteedit, null);
				final AlertDialog alert = new AlertDialog.Builder(context)
						.create();

				alert.setMessage("Ubah atau hapus transaksi?");
				alert.setView(dialogview);

				final Button bEdit = (Button) dialogview
						.findViewById(R.id.button1);
				final Button bDelete = (Button) dialogview
						.findViewById(R.id.button2);

				bEdit.setOnClickListener(new OnClickListener() {

					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						alert.dismiss();

						final MySQLiteHelper helper = new MySQLiteHelper(
								context);

						LayoutInflater inflater = LayoutInflater.from(context);
						View dialogview = inflater.inflate(
								R.layout.alertdialog_edit, null);
						final AlertDialog alert = new AlertDialog.Builder(
								context).create();

						final EditText eId = (EditText) dialogview
								.findViewById(R.id.editText1);
						// final EditText eTime = (EditText) dialogview
						// .findViewById(R.id.editText2);
						// final EditText eDate = (EditText) dialogview
						// .findViewById(R.id.editText3);
						final Button bTimePicker = (Button) dialogview
								.findViewById(R.id.button3);
						final Button bDatePicker = (Button) dialogview
								.findViewById(R.id.button2);

						final EditText ePrice = (EditText) dialogview
								.findViewById(R.id.editText4);
						final EditText eDescription = (EditText) dialogview
								.findViewById(R.id.editText5);

						eId.setText(Integer.toString(itemsArrayList.get(
								position).getIdTransaksi()));
						eId.setEnabled(false);

						final Transaksi transaksi = helper
								.getDetailTransaksi(itemsArrayList
										.get(position).getIdTransaksi());

						bTimePicker.setText(transaksi.getJam());
						bDatePicker.setText(transaksi.getTanggal());
						// eTime.setText(transaksi.getJam());
						// eDate.setText(transaksi.getTanggal());
						ePrice.setText(transaksi.getHarga());
						eDescription.setText(transaksi.getDeskripsi());

						final long tempAmount = Long.parseLong(transaksi
								.getHarga());

						bTimePicker
								.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View arg0) {
										// TODO Auto-generated method stub
										LayoutInflater inflater = LayoutInflater
												.from(context);
										View dialogview = inflater
												.inflate(
														R.layout.alertdialog_timepicker,
														null);
										final AlertDialog alert = new AlertDialog.Builder(
												context).create();
										alert.setTitle("Atur Jam");

										final TimePicker timePicker = (TimePicker) dialogview
												.findViewById(R.id.timePicker1);
										Button bSet = (Button) dialogview
												.findViewById(R.id.button1);
										Button bCancel = (Button) dialogview
												.findViewById(R.id.button2);

										alert.setView(dialogview);
										alert.show();

										timePicker.setIs24HourView(true);

										bSet.setOnClickListener(new View.OnClickListener() {

											public void onClick(View arg0) {
												// TODO Auto-generated method
												// stub

												if ((timePicker
														.getCurrentHour() < 10)
														&& (timePicker
																.getCurrentMinute() > 9)) {
													bTimePicker.setText("0"
															+ timePicker
																	.getCurrentHour()
															+ ":"
															+ timePicker
																	.getCurrentMinute()
															+ ":"
															+ new SimpleDateFormat(
																	"ss")
																	.format(new Date()));
												} else if ((timePicker
														.getCurrentHour() > 9)
														&& (timePicker
																.getCurrentMinute() < 10)) {
													bTimePicker.setText(timePicker
															.getCurrentHour()
															+ ":0"
															+ timePicker
																	.getCurrentMinute()
															+ ":"
															+ new SimpleDateFormat(
																	"ss")
																	.format(new Date()));

												} else if ((timePicker
														.getCurrentHour() < 10)
														&& (timePicker
																.getCurrentMinute() < 10)) {
													bTimePicker.setText("0"
															+ timePicker
																	.getCurrentHour()
															+ ":0"
															+ +timePicker
																	.getCurrentMinute()
															+ ":"
															+ new SimpleDateFormat(
																	"ss")
																	.format(new Date()));
												} else {
													bTimePicker.setText(""
															+ timePicker
																	.getCurrentHour()
															+ ":"
															+ +timePicker
																	.getCurrentMinute()
															+ ":"
															+ new SimpleDateFormat(
																	"ss")
																	.format(new Date()));
												}
												alert.dismiss();

											}
										});

										bCancel.setOnClickListener(new View.OnClickListener() {

											@Override
											public void onClick(View v) {
												// TODO Auto-generated method
												// stub
												alert.dismiss();
											}
										});

									}
								});

						bDatePicker
								.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										Util.refreshTimePickerAtAddTransactionFragment();

										LayoutInflater inflater = LayoutInflater
												.from(context);
										View dialogview = inflater
												.inflate(
														R.layout.alertdialog_datepicker,
														null);
										final AlertDialog alert = new AlertDialog.Builder(
												context).create();
										alert.setTitle("Atur Tanggal");

										final DatePicker datePicker = (DatePicker) dialogview
												.findViewById(R.id.datePicker1);
										Button bSet = (Button) dialogview
												.findViewById(R.id.button1);
										Button bCancel = (Button) dialogview
												.findViewById(R.id.button2);

										alert.setView(dialogview);
										alert.show();

										bSet.setOnClickListener(new View.OnClickListener() {

											@Override
											public void onClick(View v) {
												// TODO Auto-generated method
												// stub
												if ((datePicker.getDayOfMonth() < 10)
														&& (datePicker
																.getMonth()+ 1 > 9))
													bDatePicker.setText("0"
															+ Integer
																	.toString(datePicker
																			.getDayOfMonth())
															+ "/"
															+ Integer
																	.toString(datePicker
																			.getMonth() + 1)
															+ "/"
															+ Integer
																	.toString(datePicker
																			.getYear()));
												else if ((datePicker
														.getDayOfMonth() > 9)
														&& (datePicker
																.getMonth()+ 1 < 10))
													bDatePicker.setText(Integer
															.toString(datePicker
																	.getDayOfMonth())
															+ "/0"
															+ Integer
																	.toString(datePicker
																			.getMonth() + 1)
															+ "/"
															+ Integer
																	.toString(datePicker
																			.getYear()));
												else if ((datePicker
														.getDayOfMonth() < 10)
														&& (datePicker
																.getMonth()+ 1 < 10))
													bDatePicker.setText("0"
															+ Integer
																	.toString(datePicker
																			.getDayOfMonth())
															+ "/0"
															+ Integer
																	.toString(datePicker
																			.getMonth() + 1)
															+ "/"
															+ Integer
																	.toString(datePicker
																			.getYear()));
												else {
													bDatePicker.setText(Integer
															.toString(datePicker
																	.getDayOfMonth())
															+ "/"
															+ Integer
																	.toString(datePicker
																			.getMonth() + 1)
															+ "/"
															+ Integer
																	.toString(datePicker
																			.getYear()));
												}

												alert.dismiss();
											}
										});
										bCancel.setOnClickListener(new View.OnClickListener() {

											@Override
											public void onClick(View v) {
												// TODO Auto-generated method
												// stub
												alert.dismiss();
											}
										});
									}
								});

						final Button bEdit = (Button) dialogview
								.findViewById(R.id.button1);
						bEdit.setOnClickListener(new OnClickListener() {

							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								int idBudgetBefore = helper.getIdBudgetByDateTransaction(Util
										.getTimeStamp(transaksi.getTanggal()
												+ " " + transaksi.getJam(),
												new SimpleDateFormat(
														"dd/MM/yyyy kk:mm:ss")));
								int idBudgetAfter = helper.getIdBudgetByDateTransaction(Util
										.getTimeStamp(bDatePicker.getText()
												.toString()
												+ " "
												+ bTimePicker.getText()
														.toString(),
												new SimpleDateFormat(
														"dd/MM/yyyy kk:mm:ss")));

								if ((idBudgetBefore != idBudgetAfter)) {
									helper.updateBudgetSum(idBudgetBefore, Long
											.parseLong(transaksi.getHarga()));
									helper.updateBudgetDifference(
											idBudgetAfter, Long
													.parseLong(ePrice.getText()
															.toString()));
									if ((idBudgetBefore != -1)
											|| (idBudgetAfter != -1)) {
										Toast.makeText(context,
												"Budget telah diupdate",
												Toast.LENGTH_SHORT).show();
									}

								}

								ArrayList<Transaksi> listTransaksi = new ArrayList<Transaksi>();
								TransaksiAdapter adapterTransaksi;

								Transaksi transaksiUpdate = new Transaksi(
										itemsArrayList.get(position)
												.getIdTransaksi(), eDescription
												.getText().toString(), ePrice
												.getText().toString(),
										bTimePicker.getText().toString(),
										bDatePicker.getText().toString(),
										Util.getTimeStamp(bDatePicker.getText()
												.toString()
												+ " "
												+ bTimePicker.getText()
														.toString(),
												new SimpleDateFormat(
														"dd/MM/yyyy kk:mm:ss")));
								helper.updateTransaksi(transaksiUpdate);

								// update amount budget yang transaksinya masih
								// di tanggal budget tersebut

								helper.updateBudgetByDate(
										bDatePicker.getText().toString()
												+ " "
												+ bTimePicker.getText()
														.toString(),
										tempAmount
												- Long.parseLong(ePrice
														.getText().toString()));

								listTransaksi = helper
										.getAllTransaksiByTanggal(itemsArrayList
												.get(position).getTanggal());
								adapterTransaksi = new TransaksiAdapter(
										context, listTransaksi);

								HistoryFragment.list
										.setAdapter(adapterTransaksi);
								HistoryFragment.adapterStatus = 2;

								alert.dismiss();

							}
						});

						alert.setTitle("Edit Transaction");
						alert.setView(dialogview);

						alert.show();
					}
				});

				bDelete.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						// TODO Auto-generated method stub
						alert.dismiss();

						ArrayList<Transaksi> listTransaksi = new ArrayList<Transaksi>();
						TransaksiAdapter adapterTransaksi;

						MySQLiteHelper helper = new MySQLiteHelper(context);

						helper.deleteTransaksi(itemsArrayList.get(position)
								.getIdTransaksi());
						helper.updateBudgetByDate(
								itemsArrayList.get(position).getTanggal() + " "
										+ itemsArrayList.get(position).getJam(),
								Long.parseLong(itemsArrayList.get(position)
										.getHarga()));

						listTransaksi = helper
								.getAllTransaksiByTanggal(itemsArrayList.get(
										position).getTanggal());
						adapterTransaksi = new TransaksiAdapter(context,
								listTransaksi);

						HistoryFragment.list.setAdapter(adapterTransaksi);
						HistoryFragment.adapterStatus = 2;

					}
				});

				alert.show();
				return false;

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
				List<Transaksi> nPlanetList = new ArrayList<Transaksi>();

				for (int i = 0; i < itemsArrayList.size(); i++) {
					if (itemsArrayList.get(i).getJam().toUpperCase()
							.startsWith(constraint.toString().toUpperCase())
							|| itemsArrayList
									.get(i)
									.getDeskripsi()
									.toUpperCase()
									.startsWith(
											constraint.toString().toUpperCase())
							|| itemsArrayList
									.get(i)
									.getHarga()
									.toUpperCase()
									.startsWith(
											constraint.toString().toUpperCase())) {
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

				itemsArrayList = (ArrayList<Transaksi>) results.values;
				notifyDataSetChanged();
			}

		}

	}

}
