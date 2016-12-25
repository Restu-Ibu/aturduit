package com.restuibu.aturduit.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.restuibu.aturduit.R;
import com.restuibu.aturduit.model.OptionItem;

public class OptionAdapter extends BaseAdapter implements Filterable {

	private Context context;
	private List<OptionItem> OptionItems;
	private List<OptionItem> OriOptionItems;

	private Filter planetFilter;

	public OptionAdapter(Context context, List<OptionItem> OptionItems) {
		this.context = context;
		this.OptionItems = OptionItems;
		this.OriOptionItems = OptionItems;
	}

	public void resetData() {
		this.OptionItems = this.OriOptionItems;
	}

	public int getCount() {
		return OptionItems.size();
	}

	public OptionItem getItem(int position) {
		return OptionItems.get(position);
	}

	private class ViewHolder {

		TextView tvLabel;
		ImageView ivIcon;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			convertView = inflater
					.inflate(R.layout.item_applist, parent, false);

			viewHolder = new ViewHolder();

			viewHolder.ivIcon = (ImageView) convertView
					.findViewById(R.id.imageView1);
			viewHolder.tvLabel = (TextView) convertView
					.findViewById(R.id.textView1);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final OptionItem row = OptionItems.get(position);

		viewHolder.tvLabel.setTextSize(TypedValue.COMPLEX_UNIT_PT, 5);
		viewHolder.tvLabel.setText(row.getName());

		viewHolder.ivIcon.setImageResource(row.getImg());

		return convertView;
	}

	@Override
	public long getItemId(int position) {
		return OptionItems.indexOf(getItem(position));
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
				results.values = OriOptionItems;
				results.count = OriOptionItems.size();
			} else {
				// We perform filtering operation
				List<OptionItem> nPlanetList = new ArrayList<OptionItem>();

				for (int i = 0; i < OptionItems.size(); i++) {
					if (OptionItems.get(i).getName().toUpperCase()
							.contains(constraint.toString().toUpperCase())) {
						nPlanetList.add(OptionItems.get(i));

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

				OptionItems = (ArrayList<OptionItem>) results.values;
				notifyDataSetChanged();
			}

		}

	}
}
