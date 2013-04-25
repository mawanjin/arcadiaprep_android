package com.arcadiaprep.app.arca.util;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ListHeightUtils {

		private final static String LOG_TAG="ListHeightUtils";
		
		private final static int LIST_HEIGHT=220;
		
        public static void setListViewHeightBasedOnChildren(ListView listView) { 
        	Log.d(LOG_TAG, "This is 'setListViewHeightBasedOnChildren'");
        	 ListAdapter listAdapter = listView.getAdapter(); 
		        if (listAdapter == null) {
		            return;
		        }		

		        int totalHeight = 0;
		        for (int i = 0; i < listAdapter.getCount(); i++) {
		            View listItem = listAdapter.getView(i, null, listView);
		            listItem.measure(0, 0);
		            totalHeight += listItem.getMeasuredHeight();
		        }
		        ViewGroup.LayoutParams params = listView.getLayoutParams();
		        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));  ;
		        params.height += 5;//if without this statement,the listview will be a little short
		        if(params.height<LIST_HEIGHT)
		        	params.height=LIST_HEIGHT;
		        listView.setLayoutParams(params);
        } 
} 


