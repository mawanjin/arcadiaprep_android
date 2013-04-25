package com.arcadiaprep.app.arca.ui.list;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Gallery.LayoutParams;

import com.arcadiaprep.app.arca.R;

public class ImageAdapter extends BaseAdapter {
	
	private List<Bitmap> thumbList;
	
	public ImageAdapter(Context c,List<Bitmap> thumbList) {
		mContext = c;
		this.thumbList = thumbList;
	}

	public int getCount() {
		return thumbList.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView i = new ImageView(mContext);
		i.setImageBitmap(thumbList.get(position));
		i.setAdjustViewBounds(true);
		i.setLayoutParams(new Gallery.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		i.setBackgroundResource(R.drawable.e);
		return i;
	}

	private Context mContext;

}
