package com.foound.amazinglistview.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import android.os.SystemClock;
import android.util.Pair;

import com.arcadiaprep.app.arca.vo.ListItemMyQuestionVO;

public class Data {
	public static final String TAG = Data.class.getSimpleName();

	public static List<Pair<String, List<ListItemMyQuestionVO>>> getAllData() {
		List<Pair<String, List<ListItemMyQuestionVO>>> res = new ArrayList<Pair<String, List<ListItemMyQuestionVO>>>();

		for (int i = 0; i < 2; i++) {
			if(getOneSection(i)!=null)
			res.add(getOneSection(i));
		}

		return res;
	}

	public static List<ListItemMyQuestionVO> getFlattenedData() {
		List<ListItemMyQuestionVO> res = new ArrayList<ListItemMyQuestionVO>();

		for (int i = 0; i < 2; i++) {
			res.addAll(getOneSection(i).second);
		}

		return res;
	}

	public static Pair<Boolean, List<ListItemMyQuestionVO>> getRows(int page) {
		List<ListItemMyQuestionVO> flattenedData = getFlattenedData();
		if (page == 1) {
			return new Pair<Boolean, List<ListItemMyQuestionVO>>(true,
					flattenedData.subList(0, 5));
		} else {
			SystemClock.sleep(2000); // simulate loading
			return new Pair<Boolean, List<ListItemMyQuestionVO>>(
					page * 5 < flattenedData.size(), flattenedData.subList(
							(page - 1) * 5,
							Math.min(page * 5, flattenedData.size())));
		}
	}
	
	private static String[] titles = { "My Question Sets", "Recommendations"};
	public static ListItemMyQuestionVO[][] ListItemMyQuestionVOss;
	
	

	public static Pair<String, List<ListItemMyQuestionVO>> getOneSection(int index) {
//		String[] titles = { "Recommendations", "My Question Sets"};
//		ListItemMyQuestionVO[][] ListItemMyQuestionVOss = {};
//		ListItemMyQuestionVO[][] ListItemMyQuestionVOss = {
//				{ new ListItemMyQuestionVO("Thomas Tallis", "1510-1585"),
//						new ListItemMyQuestionVO("Josquin Des Prez", "1440-1521"),
//						new ListItemMyQuestionVO("Pierre de La Rue", "1460-1518"), },
//				{ new ListItemMyQuestionVO("Johann Sebastian Bach", "1685-1750"),
//						new ListItemMyQuestionVO("George Frideric Handel", "1685-1759"),
//						new ListItemMyQuestionVO("Antonio Vivaldi", "1678-1741"),
//						new ListItemMyQuestionVO("George Philipp Telemann", "1681-1767"), }
//				 };
		
		
		if(ListItemMyQuestionVOss==null)return null;
		ArrayList<ListItemMyQuestionVO> qs = new ArrayList<ListItemMyQuestionVO>();
		for(ListItemMyQuestionVO v:ListItemMyQuestionVOss[index]){
			if(v!=null&&!v.equals(""))qs.add(v);
		}
		
		return new Pair<String, List<ListItemMyQuestionVO>>(titles[index],
				qs);
	}
}
