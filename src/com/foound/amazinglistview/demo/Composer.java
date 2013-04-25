package com.foound.amazinglistview.demo;

import java.util.List;

import com.arcadiaprep.app.arca.vo.ListItemMyQuestionVO;

public class Composer {
	public static final String TAG = Composer.class.getSimpleName();

	public String name;
	public String year;
	public List<ListItemMyQuestionVO> listItems;

	public Composer(String name, String year,List<ListItemMyQuestionVO> listItems) {
		this.name = name;
		this.year = year;
		this.listItems = listItems;
	}
}
