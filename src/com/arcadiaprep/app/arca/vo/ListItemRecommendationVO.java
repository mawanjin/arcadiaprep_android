package com.arcadiaprep.app.arca.vo;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ListItemRecommendationVO implements Parcelable {
	private String logoImg;
	private String title;
	private String info;
	private String price;
	private MinItemVO aboutPage = new MinItemVO();

	public ListItemRecommendationVO() {
	}

	public ListItemRecommendationVO(String logoImg, String title, String info,
			String price, MinItemVO aboutPage) {
		super();
		this.logoImg = logoImg;
		this.title = title;
		this.info = info;
		this.price = price;
		this.aboutPage = aboutPage;
	}
	
	public static final Parcelable.Creator<ListItemRecommendationVO> CREATOR = new Creator<ListItemRecommendationVO>() {
			
			@Override
			public ListItemRecommendationVO[] newArray(int size) {
				// TODO Auto-generated method stub
				return new ListItemRecommendationVO[size];
			}
			
			@Override
			public ListItemRecommendationVO createFromParcel(Parcel source) {
				// TODO Auto-generated method stub
				return new ListItemRecommendationVO(source);
			}
		};
		public static Parcelable.Creator<ListItemRecommendationVO> getCreator() {
			return CREATOR;
		}
	public String getLogoImg() {
		return logoImg;
	}

	public void setLogoImg(String logoImg) {
		this.logoImg = logoImg;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public MinItemVO getAboutPage() {
		return aboutPage;
	}

	public void setAboutPage(MinItemVO aboutPage) {
		this.aboutPage = aboutPage;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	ListItemRecommendationVO(Parcel p) {
		logoImg = p.readString();
		title = p.readString();
		info = p.readString();
		price = p.readString();
		aboutPage.setTitle(p.readString());
		aboutPage.setText(p.readString());
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(logoImg);
		dest.writeString(title);
		dest.writeString(info);
		dest.writeString(price);
		dest.writeString(aboutPage.getTitle());
		dest.writeString(aboutPage.getText());
	}

}
