package com.arcadiaprep.app.arca.ui.list;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.AbsListView.LayoutParams;
import android.widget.TextView;

import com.arcadiaprep.app.arca.R;
import com.arcadiaprep.app.arca.service.SystemService;

public class ListInformationAdapter extends BaseAdapter {

	ListItemView listItemView = null;
	LayoutInflater inflater;
	String listItems[];
	Activity context;

	public final class ListItemView {
		public TextView txtName;
	}

	public ListInformationAdapter(Activity context, String[] listItems) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.listItems = listItems;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (listItems != null)
			return listItems.length;
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return listItems[arg0];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			listItemView = new ListItemView();
			convertView = inflater
					.inflate(R.layout.list_item_information, null);
			listItemView.txtName = (TextView) convertView
					.findViewById(R.id.txtName);
			LayoutParams pl = new LayoutParams(LayoutParams.FILL_PARENT,(int)(SystemService.getScreenHeight(context.getWindowManager())*0.27/listItems.length));
			convertView.setLayoutParams(pl);
			convertView.setTag(listItemView);
			

		} else
			listItemView = (ListItemView) convertView.getTag();

		if (listItems != null && listItems.length > 0) {
			listItemView.txtName.setText(listItems[position]);
		}

		return convertView;
	}

	public void setListItems(String[] listitems) {
		this.listItems = listitems;
	}

}
