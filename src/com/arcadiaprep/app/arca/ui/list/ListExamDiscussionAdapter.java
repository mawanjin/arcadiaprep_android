package com.arcadiaprep.app.arca.ui.list;

import java.util.List;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arcadiaprep.app.arca.QuestionViewActivity;
import com.arcadiaprep.app.arca.R;
import com.arcadiaprep.app.arca.constants.ConstantQuestion;
import com.arcadiaprep.app.arca.service.AsyncImageLoader;
import com.arcadiaprep.app.arca.ui.dialog.DiscussionShowImageDialogFragment;
import com.arcadiaprep.app.arca.vo.CacheImage;
import com.arcadiaprep.app.arca.vo.DiscussionUserVO;
import com.arcadiaprep.app.arca.vo.DiscussionVO;

public class ListExamDiscussionAdapter extends BaseAdapter {
	
	LayoutInflater inflater;
	List<DiscussionVO> listItems;
	Context context;
	Callback callback;
	private AsyncImageLoader loader = AsyncImageLoader.getInstance();
	
	public final class ListItemView{
		public ProgressBar progressBar;
		public ProgressBar progressBar1;
		public ImageButton imgPortrait;
		public TextView txtTitle;
		public ImageView imgAttach;
		public TextView comment;
		public TextView date;
		private ImageButton btnReply;
	}
	
	public ListExamDiscussionAdapter(Context context,List<DiscussionVO> listItems,Callback callback){
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.listItems = listItems;
		this.callback = callback;
	}
	
	
	public void setListItems(List<DiscussionVO> listItems){
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
		final int p = position;
		ListItemView listItemView =  null;
		if (convertView == null) {
			listItemView = new ListItemView();
			convertView = inflater.inflate(R.layout.list_item_discussion, null);
			
			listItemView.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
			listItemView.progressBar1 = (ProgressBar) convertView.findViewById(R.id.progressBar1);
			listItemView.imgPortrait =  (ImageButton) convertView.findViewById(R.id.imgPortrait);
			listItemView.txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
			listItemView.imgAttach = (ImageView) convertView.findViewById(R.id.imgAttach);
			listItemView.imgAttach.setDrawingCacheEnabled(true);
			listItemView.imgPortrait.setDrawingCacheEnabled(true);
			listItemView.comment = (TextView) convertView.findViewById(R.id.comment);
			listItemView.date = (TextView) convertView.findViewById(R.id.date);
			listItemView.btnReply = (ImageButton) convertView.findViewById(R.id.btnReply);
			
			convertView.setTag(listItemView);
			
		}else
			listItemView = (ListItemView) convertView.getTag();
		
		final ListItemView iteview =  listItemView;
		
		listItemView.imgPortrait.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				DiscussionUserVO vo = new DiscussionUserVO();
				
//				vo.setHead();
//				vo.setImgAttach(iteview.imgAttach.getDrawingCache());
				CacheImage.currentHead = iteview.imgPortrait.getDrawingCache();
				CacheImage.currentAttach = iteview.imgAttach.getDrawingCache();
				vo.setImgAttach(listItems.get(p).getAttachIamge());
				vo.setUserID(listItems.get(p).getUserId());
				
				callback.onUserInfo(p,vo);
			}
		});
		
		listItemView.btnReply.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				callback.onReply(p);
			}
		});
		
		listItemView.imgAttach.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(((ImageView)v).getDrawable()!=null){
					int a = p;
					FragmentTransaction f = ((Activity)context).getFragmentManager().beginTransaction();
					if(((Activity)context).getFragmentManager().findFragmentByTag("show_img")!=null){
						f.remove(((Activity)context).getFragmentManager().findFragmentByTag("show_img"));
					}
					
					f.add(DiscussionShowImageDialogFragment.newInstance(((ImageView)v).getDrawable()), "show_img");
					f.commit();
				}
			}
		});
		
		
		
		//set content
		Bitmap cacheImage = loader.loadDrawable(listItems.get(position).getProfilePic(), new AsyncImageLoader.ImageCallback() {
			@Override
			public void imageLoaded(Bitmap imageDrawable) {
				iteview.imgPortrait.setImageBitmap(imageDrawable);
				iteview.progressBar.setVisibility(View.GONE);
				notifyDataSetChanged();
			}
		}); 
		if(cacheImage!=null){listItemView.imgPortrait.setImageBitmap(cacheImage);listItemView.progressBar.setVisibility(View.GONE);notifyDataSetChanged();}
		if(listItems.get(position).getAttachIamge()!=null&&!listItems.get(position).getAttachIamge().equals("")&&!listItems.get(position).getAttachIamge().equals("null")){
			listItemView.progressBar1.setVisibility(View.VISIBLE);
			
			Bitmap cacheImageAttach = loader.loadDrawable(listItems.get(position).getAttachIamge(), new AsyncImageLoader.ImageCallback() {
				@Override
				public void imageLoaded(Bitmap imageDrawable) {
					iteview.imgAttach.setImageBitmap(imageDrawable);
					iteview.progressBar1.setVisibility(View.GONE);
					notifyDataSetChanged();
				}
			}); 
			if (cacheImageAttach != null) {
				listItemView.imgAttach.setImageBitmap(cacheImageAttach);
				listItemView.progressBar1.setVisibility(View.GONE);
				notifyDataSetChanged();
			}
			listItemView.imgAttach.setVisibility(View.VISIBLE);
			
		}else{
			listItemView.progressBar1.setVisibility(View.GONE);
			listItemView.imgAttach.setVisibility(View.GONE);
			notifyDataSetChanged();
		}
			
			
		listItemView.txtTitle.setText(listItems.get(position).getUname());
//		listItemView.comment.setText(Html.fromHtml(listItems.get(position).getMessage()));
		listItemView.comment.setText(listItems.get(position).getMessage());
		listItemView.date.setText(listItems.get(position).getDate());
		TextView	gotoProblem =(TextView) convertView.findViewById(R.id.gotoProblem);
		if(listItems.get(p).getQuestionId()!=0){
			gotoProblem.setVisibility(View.VISIBLE);
			gotoProblem.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context,QuestionViewActivity.class);
					intent.putExtra("show_discussion", true);
					intent.putExtra("VIEW_TYPE", ConstantQuestion.QUESTION_VIEW_TYPE_DISCUSSION);
					intent.putExtra("ex_app_id", true);
					intent.putExtra("question_id", listItems.get(p).getQuestionId());
					
					context.startActivity(intent);
				}
			});
		}else
			gotoProblem.setVisibility(View.INVISIBLE);
		
		
		
		
		
		return convertView;
	}
	
	public interface Callback{
		public void onReply(int position);
		public void onUserInfo(int position,DiscussionUserVO vo);
	}
	

}
