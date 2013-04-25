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
import com.arcadiaprep.app.arca.mo.Questions;
import com.arcadiaprep.app.arca.service.BookMarkService;
import com.arcadiaprep.app.arca.util.ExamUtil;
import com.arcadiaprep.app.arca.vo.ExamResultInfo;

public class ListViewMenuQuestionSetAdapter extends BaseAdapter {

	private List<Questions> listItems;
	private ExamResultInfo examResultInfo;
	private LayoutInflater listContainer;


	public final class ListItemView {
		public TextView title;
		public TextView choice;
		public ImageView bookmark;
	}
	
	public ListViewMenuQuestionSetAdapter(Context context,List<Questions> listItems,ExamResultInfo examResultInfo ){
		listContainer = LayoutInflater.from(context);
		this.listItems = listItems;
		this.examResultInfo = examResultInfo;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return listItems.size();
	}

	public Object getItem(int position) {
		if(listItems!=null&&listItems.size()>0)return listItems.get(position);
		return null;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//custom view
		ListItemView listItemView = null;
		if(convertView==null){
			listItemView = new ListItemView();   
			convertView = listContainer.inflate(R.layout.list_item_questions_set, null);
			listItemView.title = (TextView)convertView.findViewById(R.id.titleItem);
			listItemView.bookmark =  (ImageView) convertView.findViewById(R.id.imgBookmark);
			listItemView.choice = (TextView)convertView.findViewById(R.id.choice);
			
            convertView.setTag(listItemView);   
		}else{
			listItemView = (ListItemView) convertView.getTag();
		}
		
		
//		listItemView.title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
//		listItemView.title.getPaint().setFakeBoldText(true);
		Questions q = listItems.get(position);
		
		if((BookMarkService.get(convertView.getContext(), q.getExAppID(), q.getId())!=null)){
			listItemView.bookmark.setBackgroundDrawable(convertView.getResources().getDrawable(R.drawable.bookmark2));
			listItemView.bookmark.setVisibility(View.VISIBLE);
		}else{
//			listItemView.bookmark.setBackgroundDrawable(convertView.getResources().getDrawable(R.drawable.bookmark));
			listItemView.bookmark.setVisibility(View.INVISIBLE);
		}
		String choices = examResultInfo.getQuestions().get(position).getChoice();
		if(!choices.equals("-1"))
			listItemView.choice.setText("("+ExamUtil.convertChoice(choices)+")");
		
        listItemView.title.setText("Question "+(position+1) );   
		return convertView;
	}
	

	public List<Questions> getListItems() {
		return listItems;
	}

	public void setListItems(List<Questions> listItems) {
		this.listItems = listItems;
	}
	
	

}
