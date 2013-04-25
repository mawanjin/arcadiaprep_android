package com.arcadiaprep.app.arca.ui.list;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.arcadiaprep.app.arca.R;
import com.arcadiaprep.app.arca.mo.BookMark;

public class ListViewBookMarkAdapter extends BaseAdapter{
	
	LayoutInflater inflater;
	List<BookMark> listItems;
	Context context;
	
	public void setListItems(List<BookMark> listItems){
		this.listItems = listItems;
	}
	
	public ListViewBookMarkAdapter(Context context,List<BookMark> listItems){
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.listItems = listItems;
	}
	
	public final class ListItemView{
		public TextView txtTitle;
		public TextView txtDate;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(listItems==null)return 0;
		return listItems.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return listItems.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {

		ListItemView listItemView = null;
		if (convertView == null) {
			listItemView = new ListItemView(); 
			convertView = inflater.inflate(R.layout.list_item_bookmark, null);
			
			listItemView.txtTitle =  (TextView) convertView.findViewById(R.id.txtTitle);
			listItemView.txtDate = (TextView) convertView.findViewById(R.id.txtDate);
			
			convertView.setTag(listItemView);
			
		}else
			listItemView = (ListItemView) convertView.getTag();
		
		//set content
		listItemView.txtTitle.setText(listItems.get(   
                position).getTitle()+": #"+listItems.get(   
                        position).getPosition());
		
		listItemView.txtDate.setText(listItems.get(   
                position).getDate());
		
		return convertView;
	}
	
	

}
