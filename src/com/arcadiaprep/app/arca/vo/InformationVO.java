package com.arcadiaprep.app.arca.vo;

import java.util.ArrayList;
import java.util.List;

public class InformationVO {
	private List<MinItemVO> items = new ArrayList<MinItemVO>();

	public List<MinItemVO> getItems() {
		return items;
	}

	public void setItem(List<MinItemVO> items) {
		this.items = items;
	}
	
	public String[] getItemName(){
		String [] rs = new String[items.size()];
		
		for(int i=0;i<items.size();i++)
			rs[i] = items.get(i).getTitle();
		
		return rs;
	}

	public MinItemVO getItemByItemName(String name) {
		for(MinItemVO i : items){
			if(i.getTitle().equals(name))return i;
		}
		return null;
	}

}
