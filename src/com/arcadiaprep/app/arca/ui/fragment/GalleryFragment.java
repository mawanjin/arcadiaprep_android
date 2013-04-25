package com.arcadiaprep.app.arca.ui.fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher.ViewFactory;

import com.arcadiaprep.app.arca.R;
import com.arcadiaprep.app.arca.service.SystemService;
import com.arcadiaprep.app.arca.ui.list.ImageAdapter;
import com.arcadiaprep.app.arca.vo.ImageVO;

public class GalleryFragment extends DialogFragment implements
OnItemSelectedListener, ViewFactory{
	
	private ImageSwitcher is;
	private Gallery gallery;
	private int position;
	private Bitmap selectedBitmap;
	private String folderPath;
	private List<Bitmap> thumbList = new ArrayList<Bitmap>();
	private List<Bitmap> bitmapList = new ArrayList<Bitmap>();
	private List<String> bitmapPath = new ArrayList<String>();
	
	private int screenWidth;
	private int containerWidth;
	private int containerHeight;
	
	private Callback callback;
	
	public void setCallback(Callback callback){
		this.callback = callback;
	}

	public static GalleryFragment newInstance(String folderPath) {
		GalleryFragment f = new GalleryFragment();
		Bundle args = new Bundle();
		args.putString("folderPath", folderPath);
		f.setArguments(args);
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		folderPath = getArguments().getString("folderPath");
		screenWidth = SystemService.getScreenWith(getActivity().getWindowManager());
		containerWidth = (int)(screenWidth/2);
		containerHeight = (int)(containerWidth*0.75);
		init();
		setStyle(DialogFragment.STYLE_NO_FRAME, 0);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.gallery_fragment, container, false);
			

		is = (ImageSwitcher) v.findViewById(R.id.switcher);
		is.setFactory(this);

		is.setInAnimation(AnimationUtils.loadAnimation(getActivity(),
				android.R.anim.fade_in));
		is.setOutAnimation(AnimationUtils.loadAnimation(getActivity(),
				android.R.anim.fade_out));
		
		is.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				callback.onSelectImage(new ImageVO(selectedBitmap,bitmapPath.get(position)));
			}
		});

		gallery = (Gallery) v.findViewById(R.id.gallery);

		gallery.setAdapter(new ImageAdapter(getActivity(),thumbList));
		gallery.setOnItemSelectedListener(this);
		
		View layoutContainer = v.findViewById(R.id.container);
		
		android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(containerWidth,containerHeight);
		layoutContainer.setLayoutParams(params);
		
		return v;
	}
	
	@Override
	public View makeView() {
		ImageView i = new ImageView(getActivity());
		i.setBackgroundColor(0xFF000000);
		i.setScaleType(ImageView.ScaleType.FIT_CENTER);
		i.setLayoutParams(new ImageSwitcher.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		return i;
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		
		is.setImageDrawable(new BitmapDrawable(bitmapList.get(position)));
		this.position = position;
		selectedBitmap = bitmapList.get(position);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}
	
	private void init(){
		ArrayList<String> list = imagePath(new File(folderPath));

		for(String path : list){
			InputStream inputStream = null;
			try {
				inputStream = new FileInputStream(path);
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
			
				bitmapPath.add(path);

				thumbList.add(ThumbnailUtils.extractThumbnail(bitmap, (int)(containerWidth/5), (int)(containerHeight*0.2)));
//				bitmapList.add(ThumbnailUtils.extractThumbnail(bitmap, containerWidth, containerHeight));
				bitmapList.add(bitmap);

			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	
	private  ArrayList<String> imagePath(File file){
		ArrayList<String> list = new ArrayList<String>();
		File[] files = file.listFiles();
		for(File f: files){
			list.add(f.getAbsolutePath());
		}
		Collections.sort(list);
		return list;
	}
	
	public interface Callback{
		public void onSelectImage(ImageVO vo);
	}
	
}
