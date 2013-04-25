package com.arcadiaprep.app.arca.vo;

import android.content.ClipData.Item;
import android.os.Parcel;
import android.os.Parcelable;

public class ListItemMyQuestionVO implements Parcelable{
	private int exAppId;
	private String section;
	private String logoImg;
	private String title;
	private String info;
	private String progress;
	private boolean finish;
	private int progressCount;
	private int progressMax;
	//0 false 1 true
	private int resume=0;
    private String totalTime;
        
    private boolean isRecommends =false;
    private String recommendId;
    private String packageSetName;
	
	public ListItemMyQuestionVO(){}
	
	ListItemMyQuestionVO(Parcel p){
		exAppId = p.readInt();
		logoImg = p.readString();
		title = p.readString();
		info = p.readString();
		progress = p.readString();
		progressCount = p.readInt();
		resume = p.readInt();
		finish = Boolean.parseBoolean(p.readString());
		section = p.readString();
		totalTime = p.readString();
	}
	
	public static final Parcelable.Creator<ListItemMyQuestionVO> CREATOR = new Creator<ListItemMyQuestionVO>() {
		
		@Override
		public ListItemMyQuestionVO[] newArray(int size) {
			// TODO Auto-generated method stub
			return new ListItemMyQuestionVO[size];
		}
		
		@Override
		public ListItemMyQuestionVO createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new ListItemMyQuestionVO(source);
		}
	};

	public ListItemMyQuestionVO(int exAppId,String logoImg, String title, String info,
			String progress, int progressCount,int resume,String section) {
		super();
		this.exAppId=exAppId;
		this.logoImg = logoImg;
		this.title = title;
		this.info = info;
		this.progress = progress;
		this.progressCount = progressCount;
		this.resume = resume;
		this.section = section;
	}
	
	public ListItemMyQuestionVO(int exAppId,String logoImg, String title, String info,
			String progress, int progressCount,boolean finish,String section) {
		super();
		this.exAppId=exAppId;
		this.logoImg = logoImg;
		this.title = title;
		this.info = info;
		this.progress = progress;
		this.progressCount = progressCount;
		this.finish = finish;
		this.section = section;
	}



	public int getExAppId() {
		return exAppId;
	}

	public void setExAppId(int exAppId) {
		this.exAppId = exAppId;
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

	public String getProgress() {
		return progress;
	}

	public void setProgress(String progress) {
		this.progress = progress;
	}

	public int getProgressCount() {
		return progressCount;
	}

	public void setProgressCount(int progressCount) {
		this.progressCount = progressCount;
	}
	
	


	public int getResume() {
		if(finish){
			resume=0;
		}else{
			resume = progressCount;
		}
		
		return resume;
	}

	public void setResume(int resume) {
		this.resume = resume;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}



	public boolean isFinish() {
		return finish;
	}

	public void setFinish(boolean finish) {
		this.finish = finish;
	}
	
	

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public static Parcelable.Creator<ListItemMyQuestionVO> getCreator() {
		return CREATOR;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(exAppId);
		dest.writeString(logoImg);
		dest.writeString(title);
		dest.writeString(info);
		dest.writeString(progress);
		dest.writeInt(progressCount);
		dest.writeInt(resume);
		dest.writeString(finish+"");
		dest.writeString(section);
		dest.writeString(totalTime);
		
	}

	public int getProgressMax() {
		return progressMax;
	}

	public void setProgressMax(int progressMax) {
		this.progressMax = progressMax;
	}
	
	public String getTotalTime() {
	    return totalTime;
	}

    public void setTotalTime(String totalTime)
    {
        this.totalTime = totalTime;        
    }

	public boolean isRecommends() {
		return isRecommends;
	}

	public void setRecommends(boolean isRecommends) {
		this.isRecommends = isRecommends;
	}

	public String getRecommendId() {
		return recommendId;
	}

	public void setRecommendId(String recommendId) {
		this.recommendId = recommendId;
	}

	public String getPackageSetName() {
		return packageSetName;
	}

	public void setPackageSetName(String packageSetName) {
		this.packageSetName = packageSetName;
	}
	
	

}
