package com.arcadiaprep.app.arca.service;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;

import com.arcadiaprep.app.arca.R;
import com.arcadiaprep.app.arca.vo.ExamSection;

/**
 * This main function of this class is to supply some dynamic layout drawing
 * services for main.xml. Something like "question category bar" .
 * 
 * 
 * @author lala
 * 
 */
public class MainLayoutService {

	/**
	 * This method will translate the CategoryBarVO to Button,and deposit the buttons 
	 * into the given LinearLayout.
	 * 
	 * @param context
	 *            the context of parent layout.
	 * @param bars
	 * 			  the data of bar items.Each one will be reflected to a Button. 
	 * @param layout
	 * 			  where the dynamic view will be deposited.
	 */
	public static List<Button> generateCategoryBar(Context context,
			List<ExamSection> bars, LinearLayout layout) {
		List<Button> rs = new ArrayList<Button>();
		if (bars == null || bars.size() == 0)
			return null;

		for (int i = 0; i < bars.size(); i++) {
			Button button = new Button(context);
			
			if(context.getString(R.string.device_screen).equals("xhdpi")||context.getString(R.string.device_screen).equals("hdpi")){
				if (context.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
					button.setText(bars.get(i).getSecNameShort());
				}else
					button.setText(bars.get(i).getSecName());
			}else
				button.setText(bars.get(i).getSecName());
			
			button.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT,1f));
			button.setGravity(Gravity.CENTER_HORIZONTAL);
			button.setTextColor(Color.WHITE);

			if (bars.size() == 1) {
				button.setBackgroundDrawable(context.getResources()
						.getDrawable(R.drawable.main_category_bar));
			} else if (bars.size() == 2) {
				if (i == 0) {
					button.setBackgroundDrawable(context.getResources()
							.getDrawable(R.drawable.main_category_bar_left));
				} else {
					button.setBackgroundDrawable(context.getResources()
							.getDrawable(R.drawable.main_category_bar_right));
				}
			} else {
				if (i == 0) {
					button.setBackgroundDrawable(context.getResources()
							.getDrawable(R.drawable.main_category_bar_left));
				} else if (i == bars.size()-1) {
					button.setBackgroundDrawable(context.getResources()
							.getDrawable(R.drawable.main_category_bar_right));
				} else {
					button.setBackgroundDrawable(context.getResources()
							.getDrawable(R.drawable.main_category_bar_middle));
				}
			}
			layout.addView(button, layout.getChildCount());
			rs.add(button);
		}
		return rs;
	}

	public static List<Button> generateCategoryBar(
			Context context, LinearLayout layout) {

		List<Button> rs = new ArrayList<Button>();
		

		for (int i = 0; i < 3; i++) {
			Button button = new Button(context);
			if(i==0){
				button.setText("Last");				
			}else if(i==1){
				button.setText("Average");
			}else if(i==2){
				button.setText("Best");
			}
			
			button.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT,1f));
			button.setGravity(Gravity.CENTER_HORIZONTAL);
			button.setTextColor(Color.WHITE);


			if (i == 0) {
				button.setBackgroundDrawable(context.getResources()
						.getDrawable(R.drawable.main_category_bar_left));
			} else if (i == 2) {
				button.setBackgroundDrawable(context.getResources()
						.getDrawable(R.drawable.main_category_bar_right));
			} else {
				button.setBackgroundDrawable(context.getResources()
						.getDrawable(R.drawable.main_category_bar_middle));
			}
			
			layout.addView(button, layout.getChildCount());
			rs.add(button);
		}
		return rs;
	}
}
