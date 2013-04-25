package com.arcadiaprep.app.arca.ui.list;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.arcadiaprep.app.arca.R;
import com.arcadiaprep.app.arca.vo.QuestionExamStatus;

public class ListExamReviewAdapter extends BaseAdapter {
	LayoutInflater inflater;
	List<QuestionExamStatus> listItems;
	Context context;
	
	public final class ListItemView{
		public ImageView judge;
		public TextView no;
		public TextView choice;
		public TextView answer;
	}
	
	public ListExamReviewAdapter(Context context,List<QuestionExamStatus> listItems){
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
		ListItemView listItemView = null;
		if (convertView == null) {
			listItemView = new ListItemView();
			convertView = inflater.inflate(R.layout.list_item_exam_review, null);
			
			listItemView.judge = (ImageView) convertView.findViewById(R.id.judge);
			listItemView.no = (TextView) convertView.findViewById(R.id.no);
			listItemView.choice = (TextView) convertView.findViewById(R.id.choice);
			listItemView.answer = (TextView) convertView.findViewById(R.id.answer);
			
			convertView.setTag(listItemView);
			
		}else
			listItemView = (ListItemView) convertView.getTag();
		
		//set content
		String correctChoice = convertChoice(listItems.get(position).getCorrectChoice());
		listItemView.answer.setText("("+correctChoice+")");
		
		listItemView.choice.setText(convertChoice(listItems.get(   
                position).getChoice()));
		
		listItemView.no.setText((position+1>9)?position+1+"":(position+1)+"  ");
		
		if(listItems.get(   
                position).getChoice().equals(listItems.get(   
                        position).getCorrectChoice())){
			listItemView.judge.setImageResource(R.drawable.select);
		}else{
			listItemView.judge.setImageResource(R.drawable.delete);
		}
		
		
		return convertView;
	}
	
	private String convertChoice(String c){
		if(c.equals("-1"))return "--";
		String rs ="";
		char[] cs =  c.toCharArray();
		for(int i=0;i<cs.length;i++){
			if(cs[i]=='0')rs+="A,";
			else if(cs[i]=='1')rs+="B,";
			else if(cs[i]=='2')rs+="C,";
			else if(cs[i]=='3')rs+="D,";
			else if(cs[i]=='4')rs+="E,";
			else if(cs[i]=='5')rs+="F,";
			else if(cs[i]=='6')rs+="G,";
		}
		if(rs.equals(""))
			return "--";
		else
			return rs.substring(0,rs.lastIndexOf(','));
	}

}
