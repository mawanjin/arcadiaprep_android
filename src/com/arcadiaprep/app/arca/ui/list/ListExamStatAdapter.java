package com.arcadiaprep.app.arca.ui.list;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.arcadiaprep.app.arca.R;
import com.arcadiaprep.app.arca.service.AsyncImageLoader;
import com.arcadiaprep.app.arca.service.SystemService;
import com.arcadiaprep.app.arca.vo.ListItemExamStatVO;

public class ListExamStatAdapter extends BaseAdapter{

	ListItemView listItemView = null;
	LayoutInflater inflater;
	List<ListItemExamStatVO> listItems;
	Activity context;
	
	public final class ListItemView{
		public TextView txtName;
		public TextView txtScore;
		public TextView txtTime;
		public TextView txtPer;
	}
	
	public ListExamStatAdapter(Activity context,List<ListItemExamStatVO> listItems){
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.listItems = listItems;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(listItems!=null)return listItems.size();
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return listItems.get(arg0);
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
			convertView = inflater.inflate(R.layout.list_item_exam_stat, null);
			listItemView.txtName =  (TextView) convertView.findViewById(R.id.txtName);
			listItemView.txtScore = (TextView) convertView.findViewById(R.id.txtScore);
			listItemView.txtTime = (TextView) convertView.findViewById(R.id.txtTime);
			listItemView.txtPer = (TextView) convertView.findViewById(R.id.txtPer);
			
			View container_content = convertView.findViewById(R.id.container_content);
			LayoutParams para1 = new LayoutParams((int)(SystemService.getScreenWith(context.getWindowManager())/2),LayoutParams.WRAP_CONTENT);
			para1.rightMargin = (int)(SystemService.getScreenWith(context.getWindowManager())*0.05);
			container_content.setLayoutParams(para1);
			
			convertView.setTag(listItemView);
			
		}else
			listItemView = (ListItemView) convertView.getTag();
		
		if(listItems!=null&&listItems.size()>0){
			listItemView.txtName.setText(listItems.get(position).getName());
			int score = Integer.parseInt(listItems.get(position).getScore());
			int total_n = listItems.get(position).getNumQuestion();
			
			if (total_n > 0) {
				score = score * 100 / total_n;
				listItemView.txtScore.setText(Integer.toString(score)+"%");
			}
			else {
				listItemView.txtScore.setText("N/A");
			}
			//listItemView.txtScore.setText(listItems.get(position).getScore());
			listItemView.txtTime.setText(listItems.get(position).getTotalTime());
			listItemView.txtPer.setText(listItems.get(position).getPer());
		}
		
		return convertView;
	}

	public void setListItems(List<ListItemExamStatVO> examStatsBySectionAndType) {
		this.listItems = examStatsBySectionAndType;
	}

}
