package com.arcadiaprep.app.arca.ui.list;

import java.io.InputStream;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arcadiaprep.app.arca.R;
import com.arcadiaprep.app.arca.vo.ListItemMyQuestionVO;

public class ListViewQuestionAdapter extends BaseAdapter {

	private Context context;
	private List<ListItemMyQuestionVO> listItems;
	private LayoutInflater listContainer;

	public final class ListItemView {
		public ImageView logoimg;
		public TextView title;
		public TextView info;
		public ImageView arrowIcon;
		public TextView progressTxt;
		public TextView txtPrice;
		public ProgressBar progress;
	}
	
	public ListViewQuestionAdapter(Context context,List<ListItemMyQuestionVO> listItems){
		this.context = context;
		listContainer = LayoutInflater.from(context);
		this.listItems = listItems;
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
		final int selectID = position;
		//custom view
		ListItemView _listItemView = null;
//		if(convertView==null){
			_listItemView = new ListItemView();   
			
			convertView = listContainer.inflate(R.layout.list_item_questions, null);
			 
			_listItemView.logoimg = (ImageView) convertView.findViewById(R.id.imageItem); 
			_listItemView.title = (TextView)convertView.findViewById(R.id.titleItem);   
			_listItemView.info = (TextView)convertView.findViewById(R.id.infoItem);
			_listItemView.progressTxt = (TextView) convertView.findViewById(R.id.progress_txt);
			_listItemView.progress = (ProgressBar) convertView.findViewById(R.id.progressBar);
			_listItemView.txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);
            
            convertView.setTag(_listItemView);   
//		}else{
//			_listItemView = (ListItemView) convertView.getTag();
//		}
		final ListItemView listItemView =  _listItemView;
		// listItemView.logoimg.setBackgroundResource(Integer.parseInt(listItems.get(position).getLogoImg()));   
		
		String img_name = listItems.get(position).getLogoImg();
		
		if (img_name!=null) {
            Bitmap image = null;
            InputStream is = null;
            try {
              is = context.getResources().getAssets().open(img_name);
              image = BitmapFactory.decodeStream(is);

            } catch (Exception e) {
                System.out.println("Failure to get image (" + img_name +") from assets. " + e);
            }
    		listItemView.logoimg.setImageBitmap(image);   

        }
		
		listItemView.title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		listItemView.title.getPaint().setFakeBoldText(true);
        listItemView.title.setText(Html.fromHtml( (String) listItems.get(position)   
                .getTitle()));   
        if(!listItems.get(position).isRecommends())
        listItemView.info.setText(Html.fromHtml((String) listItems.get(position).getInfo()));
        if(listItems.get(position).isRecommends()){
        	listItemView.txtPrice.setText(listItems.get(position).getProgress());
        	listItemView.progressTxt.setVisibility(View.INVISIBLE);
        	listItemView.progress.setVisibility(View.INVISIBLE);
        }else{
        	listItemView.txtPrice.setVisibility(View.INVISIBLE);
        	listItemView.progressTxt.setText(listItems.get(position).getProgress());
        }
        	
        
        
       if(listItems.get(position).isFinish()){
    	   listItemView.progress.setVisibility(View.GONE); 
       }else{
    	   if(listItemView.progressTxt.getText().equals(""))
    		   listItemView.progress.setVisibility(View.GONE); 
    	   else{
    		   listItemView.progress.setMax(listItems.get(position).getProgressMax());
    		   listItemView.progress.setProgress(listItems.get(position).getProgressCount());
    	   }
    		   
    	   
       }
    	     
        	
		return convertView;
	}
	
	private void showDetailInfo(int clickId){
		new AlertDialog.Builder(context).setTitle("product information").setMessage("detail:"+listItems.get(clickId).getTitle()).setPositiveButton("ok", null).show();
	}

	public List<ListItemMyQuestionVO> getListItems() {
		return listItems;
	}

	public void setListItems(List<ListItemMyQuestionVO> listItems) {
		this.listItems = listItems;
	}
	
	

}
