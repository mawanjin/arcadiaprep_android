package com.arcadiaprep.app.arca.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.arcadiaprep.app.arca.constants.ConstantSystem;
import com.arcadiaprep.app.arca.util.HTTPUtils;
import com.arcadiaprep.app.arca.util.http.HttpConnection;
import com.arcadiaprep.app.arca.vo.DiscussionUserVO;
import com.arcadiaprep.app.arca.vo.DiscussionVO;
import com.arcadiaprep.app.login.communication.model.EntityHelper;
import com.arcadiaprep.app.login.model.User;

public class DiscussionService {
	Context context ;
	static DiscussionService discussionService;
	
	public synchronized static DiscussionService getInstance(Context context){
		if(discussionService==null)discussionService= new DiscussionService(context);
		return discussionService;
	}
	
	private DiscussionService(Context context){this.context = context;}
	
	
	/**
	 * @param questionid
	 * @return
	 */
	public void findDiscussionsCountByQuestionid(final int _questionid ,final Handler handler1){
		int questionid = _questionid;
		//test questionid = 1800
//		questionid = 1588;
		//get data from web server
		String server_url = SystemService.parseModuelInfo(context).getServerURL()+"/"+ConstantSystem.GET_SERVER_URL_DISCUSSIONS_BY_QUESTION_ID(questionid);
		
		Handler handler = new Handler() {
			public void handleMessage(Message message) {
				Message msg = new Message();
				switch (message.what) {
					case HttpConnection.DID_START: {
						Log.d("Image", "Starting Connection");
						break;
					}
					case HttpConnection.DID_SUCCEED: {
						
						String response = (String) message.obj;
						JSONObject a;
						try {
							a = new JSONObject(response);
							msg.obj = new Integer(a.getJSONArray("discussions").length());
							msg.what = 5;
							handler1.sendMessage(msg);
							
						} catch (Exception e) {
							e.printStackTrace();
							msg.what = 5;
							msg.obj = new Integer(0);
							handler1.sendMessage(msg);
						}
						
						break;
					}
					case HttpConnection.DID_ERROR: {
						Exception e = (Exception) message.obj;
						e.printStackTrace();
						msg.what = 5;
						msg.obj = new Integer(0);
						handler1.sendMessage(msg);
						break;
					}  
				}
			}
		};
		
		new HttpConnection(handler).get(server_url);
		
		//test
//		Message msg = new Message();
//		msg.what = 5;
//		msg.obj = msg.obj = new Integer(3);
//		handler1.sendMessage(msg);
		
		
//		//test
//		JSONObject a;
//		
//		try {
//			a = new JSONObject("{'qid':'1800','discussions':[{'id':'935','discussion_id':'251','user_id':'1425','subject':'test subject','message':'Why is answer C correct? ','date':'2012-01-25 09:13:19','replied_to':'0','pseudonym':'0','cid':'1425','profile':'1','profile_pic':'http://www.arcadiaprep.com/_layouts/images/img_user65.png','uname':'tyrone gibson'},{'id':'936','discussion_id':'251','user_id':'1610','subject':'','message':'Tyrone, the conclusion is based upon the assumption that How people feel depends on the time they have been treated, while answer C is claiming the exact opposite; How people feel will determine the length of treatment and it will make the conclusion proposed(even the whole survey) meaningless :)\r\n\r\nNiels','date':'2012-01-25 14:23:20','replied_to':'0','pseudonym':'0','cid':'1610','profile':'5','profile_pic':'http://www.arcadiaprep.com//_upload/pictures/132735469593830142.jpg','uname':'Niels Krook','member':'Contributor'},{'id':'935','discussion_id':'251','user_id':'1425','subject':'test subject','message':'Why is answer C correct? ','date':'2012-01-25 09:13:19','replied_to':'0','pseudonym':'0','cid':'1425','profile':'1','profile_pic':'http://www.arcadiaprep.com/_layouts/images/img_user65.png','uname':'tyrone gibson'},{'id':'936','discussion_id':'251','user_id':'1610','subject':'','message':'Tyrone, the conclusion is based upon the assumption that How people feel depends on the time they have been treated, while answer C is claiming the exact opposite; How people feel will determine the length of treatment and it will make the conclusion proposed(even the whole survey) meaningless :)\r\n\r\nNiels','date':'2012-01-25 14:23:20','replied_to':'0','pseudonym':'0','cid':'1610','profile':'5','profile_pic':'http://www.arcadiaprep.com//_upload/pictures/132735469593830142.jpg','uname':'Niels Krook','member':'Contributor'},{'id':'935','discussion_id':'251','user_id':'1425','subject':'test subject','message':'Why is answer C correct? ','date':'2012-01-25 09:13:19','replied_to':'0','pseudonym':'0','cid':'1425','profile':'1','profile_pic':'http://www.arcadiaprep.com/_layouts/images/img_user65.png','uname':'tyrone gibson'},{'id':'936','discussion_id':'251','user_id':'1610','subject':'','message':'Tyrone, the conclusion is based upon the assumption that How people feel depends on the time they have been treated, while answer C is claiming the exact opposite; How people feel will determine the length of treatment and it will make the conclusion proposed(even the whole survey) meaningless :)\r\n\r\nNiels','date':'2012-01-25 14:23:20','replied_to':'0','pseudonym':'0','cid':'1610','profile':'5','profile_pic':'http://www.arcadiaprep.com//_upload/pictures/132735469593830142.jpg','uname':'Niels Krook','member':'Contributor'},{'id':'935','discussion_id':'251','user_id':'1425','subject':'test subject','message':'Why is answer C correct? ','date':'2012-01-25 09:13:19','replied_to':'0','pseudonym':'0','cid':'1425','profile':'1','profile_pic':'http://www.arcadiaprep.com/_layouts/images/img_user65.png','uname':'tyrone gibson'},{'id':'936','discussion_id':'251','user_id':'1610','subject':'','message':'Tyrone, the conclusion is based upon the assumption that How people feel depends on the time they have been treated, while answer C is claiming the exact opposite; How people feel will determine the length of treatment and it will make the conclusion proposed(even the whole survey) meaningless :)\r\n\r\nNiels','date':'2012-01-25 14:23:20','replied_to':'0','pseudonym':'0','cid':'1610','profile':'5','profile_pic':'http://www.arcadiaprep.com//_upload/pictures/132735469593830142.jpg','uname':'Niels Krook','member':'Contributor'},{'id':'935','discussion_id':'251','user_id':'1425','subject':'test subject','message':'Why is answer C correct? ','date':'2012-01-25 09:13:19','replied_to':'0','pseudonym':'0','cid':'1425','profile':'1','profile_pic':'http://www.arcadiaprep.com/_layouts/images/img_user65.png','uname':'tyrone gibson'},{'id':'936','discussion_id':'251','user_id':'1610','subject':'','message':'Tyrone, the conclusion is based upon the assumption that How people feel depends on the time they have been treated, while answer C is claiming the exact opposite; How people feel will determine the length of treatment and it will make the conclusion proposed(even the whole survey) meaningless :)\r\n\r\nNiels','date':'2012-01-25 14:23:20','replied_to':'0','pseudonym':'0','cid':'1610','profile':'5','profile_pic':'http://www.arcadiaprep.com//_upload/pictures/132735469593830142.jpg','uname':'Niels Krook','member':'Contributor'},{'id':'935','discussion_id':'251','user_id':'1425','subject':'test subject','message':'Why is answer C correct? ','date':'2012-01-25 09:13:19','replied_to':'0','pseudonym':'0','cid':'1425','profile':'1','profile_pic':'http://www.arcadiaprep.com/_layouts/images/img_user65.png','uname':'tyrone gibson'},{'id':'936','discussion_id':'251','user_id':'1610','subject':'','message':'Tyrone, the conclusion is based upon the assumption that How people feel depends on the time they have been treated, while answer C is claiming the exact opposite; How people feel will determine the length of treatment and it will make the conclusion proposed(even the whole survey) meaningless :)\r\n\r\nNiels','date':'2012-01-25 14:23:20','replied_to':'0','pseudonym':'0','cid':'1610','profile':'5','profile_pic':'http://www.arcadiaprep.com//_upload/pictures/132735469593830142.jpg','uname':'Niels Krook','member':'Contributor'},{'id':'935','discussion_id':'251','user_id':'1425','subject':'test subject','message':'Why is answer C correct? ','date':'2012-01-25 09:13:19','replied_to':'0','pseudonym':'0','cid':'1425','profile':'1','profile_pic':'http://www.arcadiaprep.com/_layouts/images/img_user65.png','uname':'tyrone gibson'},{'id':'936','discussion_id':'251','user_id':'1610','subject':'','message':'Tyrone, the conclusion is based upon the assumption that How people feel depends on the time they have been treated, while answer C is claiming the exact opposite; How people feel will determine the length of treatment and it will make the conclusion proposed(even the whole survey) meaningless :)\r\n\r\nNiels','date':'2012-01-25 14:23:20','replied_to':'0','pseudonym':'0','cid':'1610','profile':'5','profile_pic':'http://www.arcadiaprep.com//_upload/pictures/132735469593830142.jpg','uname':'Niels Krook','member':'Contributor'},{'id':'935','discussion_id':'251','user_id':'1425','subject':'test subject','message':'Why is answer C correct? ','date':'2012-01-25 09:13:19','replied_to':'0','pseudonym':'0','cid':'1425','profile':'1','profile_pic':'http://www.arcadiaprep.com/_layouts/images/img_user65.png','uname':'tyrone gibson'},{'id':'936','discussion_id':'251','user_id':'1610','subject':'','message':'Tyrone, the conclusion is based upon the assumption that How people feel depends on the time they have been treated, while answer C is claiming the exact opposite; How people feel will determine the length of treatment and it will make the conclusion proposed(even the whole survey) meaningless :)\r\n\r\nNiels','date':'2012-01-25 14:23:20','replied_to':'0','pseudonym':'0','cid':'1610','profile':'5','profile_pic':'http://www.arcadiaprep.com//_upload/pictures/132735469593830142.jpg','uname':'Niels Krook','member':'Contributor'},{'id':'935','discussion_id':'251','user_id':'1425','subject':'test subject','message':'Why is answer C correct? ','date':'2012-01-25 09:13:19','replied_to':'0','pseudonym':'0','cid':'1425','profile':'1','profile_pic':'http://www.arcadiaprep.com/_layouts/images/img_user65.png','uname':'tyrone gibson'},{'id':'936','discussion_id':'251','user_id':'1610','subject':'','message':'Tyrone, the conclusion is based upon the assumption that How people feel depends on the time they have been treated, while answer C is claiming the exact opposite; How people feel will determine the length of treatment and it will make the conclusion proposed(even the whole survey) meaningless :)\r\n\r\nNiels','date':'2012-01-25 14:23:20','replied_to':'0','pseudonym':'0','cid':'1610','profile':'5','profile_pic':'http://www.arcadiaprep.com//_upload/pictures/132735469593830142.jpg','uname':'Niels Krook','member':'Contributor'},{'id':'935','discussion_id':'251','user_id':'1425','subject':'test subject','message':'Why is answer C correct? ','date':'2012-01-25 09:13:19','replied_to':'0','pseudonym':'0','cid':'1425','profile':'1','profile_pic':'http://www.arcadiaprep.com/_layouts/images/img_user65.png','uname':'tyrone gibson'},{'id':'936','discussion_id':'251','user_id':'1610','subject':'','message':'Tyrone, the conclusion is based upon the assumption that How people feel depends on the time they have been treated, while answer C is claiming the exact opposite; How people feel will determine the length of treatment and it will make the conclusion proposed(even the whole survey) meaningless :)\r\n\r\nNiels','date':'2012-01-25 14:23:20','replied_to':'0','pseudonym':'0','cid':'1610','profile':'5','profile_pic':'http://www.arcadiaprep.com//_upload/pictures/132735469593830142.jpg','uname':'Niels Krook','member':'Contributor'},{'id':'935','discussion_id':'251','user_id':'1425','subject':'test subject','message':'Why is answer C correct? ','date':'2012-01-25 09:13:19','replied_to':'0','pseudonym':'0','cid':'1425','profile':'1','profile_pic':'http://www.arcadiaprep.com/_layouts/images/img_user65.png','uname':'tyrone gibson'},{'id':'936','discussion_id':'251','user_id':'1610','subject':'','message':'Tyrone, the conclusion is based upon the assumption that How people feel depends on the time they have been treated, while answer C is claiming the exact opposite; How people feel will determine the length of treatment and it will make the conclusion proposed(even the whole survey) meaningless :)\r\n\r\nNiels','date':'2012-01-25 14:23:20','replied_to':'0','pseudonym':'0','cid':'1610','profile':'5','profile_pic':'http://www.arcadiaprep.com//_upload/pictures/132735469593830142.jpg','uname':'Niels Krook','member':'Contributor'},{'id':'935','discussion_id':'251','user_id':'1425','subject':'test subject','message':'Why is answer C correct? ','date':'2012-01-25 09:13:19','replied_to':'0','pseudonym':'0','cid':'1425','profile':'1','profile_pic':'http://www.arcadiaprep.com/_layouts/images/img_user65.png','uname':'tyrone gibson'},{'id':'936','discussion_id':'251','user_id':'1610','subject':'','message':'Tyrone, the conclusion is based upon the assumption that How people feel depends on the time they have been treated, while answer C is claiming the exact opposite; How people feel will determine the length of treatment and it will make the conclusion proposed(even the whole survey) meaningless :)\r\n\r\nNiels','date':'2012-01-25 14:23:20','replied_to':'0','pseudonym':'0','cid':'1610','profile':'5','profile_pic':'http://www.arcadiaprep.com//_upload/pictures/132735469593830142.jpg','uname':'Niels Krook','member':'Contributor'},{'id':'935','discussion_id':'251','user_id':'1425','subject':'test subject','message':'Why is answer C correct? ','date':'2012-01-25 09:13:19','replied_to':'0','pseudonym':'0','cid':'1425','profile':'1','profile_pic':'http://www.arcadiaprep.com/_layouts/images/img_user65.png','uname':'tyrone gibson'},{'id':'936','discussion_id':'251','user_id':'1610','subject':'','message':'Tyrone, the conclusion is based upon the assumption that How people feel depends on the time they have been treated, while answer C is claiming the exact opposite; How people feel will determine the length of treatment and it will make the conclusion proposed(even the whole survey) meaningless :)\r\n\r\nNiels','date':'2012-01-25 14:23:20','replied_to':'0','pseudonym':'0','cid':'1610','profile':'5','profile_pic':'http://www.arcadiaprep.com//_upload/pictures/132735469593830142.jpg','uname':'Niels Krook','member':'Contributor'},{'id':'935','discussion_id':'251','user_id':'1425','subject':'test subject','message':'Why is answer C correct? ','date':'2012-01-25 09:13:19','replied_to':'0','pseudonym':'0','cid':'1425','profile':'1','profile_pic':'http://www.arcadiaprep.com/_layouts/images/img_user65.png','uname':'tyrone gibson'},{'id':'936','discussion_id':'251','user_id':'1610','subject':'','message':'Tyrone, the conclusion is based upon the assumption that How people feel depends on the time they have been treated, while answer C is claiming the exact opposite; How people feel will determine the length of treatment and it will make the conclusion proposed(even the whole survey) meaningless :)\r\n\r\nNiels','date':'2012-01-25 14:23:20','replied_to':'0','pseudonym':'0','cid':'1610','profile':'5','profile_pic':'http://www.arcadiaprep.com//_upload/pictures/132735469593830142.jpg','uname':'Niels Krook','member':'Contributor'},{'id':'935','discussion_id':'251','user_id':'1425','subject':'test subject','message':'Why is answer C correct? ','date':'2012-01-25 09:13:19','replied_to':'0','pseudonym':'0','cid':'1425','profile':'1','profile_pic':'http://www.arcadiaprep.com/_layouts/images/img_user65.png','uname':'tyrone gibson'},{'id':'936','discussion_id':'251','user_id':'1610','subject':'','message':'Tyrone, the conclusion is based upon the assumption that How people feel depends on the time they have been treated, while answer C is claiming the exact opposite; How people feel will determine the length of treatment and it will make the conclusion proposed(even the whole survey) meaningless :)\r\n\r\nNiels','date':'2012-01-25 14:23:20','replied_to':'0','pseudonym':'0','cid':'1610','profile':'5','profile_pic':'http://www.arcadiaprep.com//_upload/pictures/132735469593830142.jpg','uname':'Niels Krook','member':'Contributor'},{'id':'935','discussion_id':'251','user_id':'1425','subject':'test subject','message':'Why is answer C correct? ','date':'2012-01-25 09:13:19','replied_to':'0','pseudonym':'0','cid':'1425','profile':'1','profile_pic':'http://www.arcadiaprep.com/_layouts/images/img_user65.png','uname':'tyrone gibson'},{'id':'936','discussion_id':'251','user_id':'1610','subject':'','message':'Tyrone, the conclusion is based upon the assumption that How people feel depends on the time they have been treated, while answer C is claiming the exact opposite; How people feel will determine the length of treatment and it will make the conclusion proposed(even the whole survey) meaningless :)\r\n\r\nNiels','date':'2012-01-25 14:23:20','replied_to':'0','pseudonym':'0','cid':'1610','profile':'5','profile_pic':'http://www.arcadiaprep.com//_upload/pictures/132735469593830142.jpg','uname':'Niels Krook','member':'Contributor'},{'id':'935','discussion_id':'251','user_id':'1425','subject':'test subject','message':'Why is answer C correct? ','date':'2012-01-25 09:13:19','replied_to':'0','pseudonym':'0','cid':'1425','profile':'1','profile_pic':'http://www.arcadiaprep.com/_layouts/images/img_user65.png','uname':'tyrone gibson'},{'id':'936','discussion_id':'251','user_id':'1610','subject':'','message':'Tyrone, the conclusion is based upon the assumption that How people feel depends on the time they have been treated, while answer C is claiming the exact opposite; How people feel will determine the length of treatment and it will make the conclusion proposed(even the whole survey) meaningless :)\r\n\r\nNiels','date':'2012-01-25 14:23:20','replied_to':'0','pseudonym':'0','cid':'1610','profile':'5','profile_pic':'http://www.arcadiaprep.com//_upload/pictures/132735469593830142.jpg','uname':'Niels Krook','member':'Contributor'},{'id':'935','discussion_id':'251','user_id':'1425','subject':'test subject','message':'Why is answer C correct? ','date':'2012-01-25 09:13:19','replied_to':'0','pseudonym':'0','cid':'1425','profile':'1','profile_pic':'http://www.arcadiaprep.com/_layouts/images/img_user65.png','uname':'tyrone gibson'},{'id':'936','discussion_id':'251','user_id':'1610','subject':'','message':'Tyrone, the conclusion is based upon the assumption that How people feel depends on the time they have been treated, while answer C is claiming the exact opposite; How people feel will determine the length of treatment and it will make the conclusion proposed(even the whole survey) meaningless :)\r\n\r\nNiels','date':'2012-01-25 14:23:20','replied_to':'0','pseudonym':'0','cid':'1610','profile':'5','profile_pic':'http://www.arcadiaprep.com//_upload/pictures/132735469593830142.jpg','uname':'Niels Krook','member':'Contributor'},{'id':'935','discussion_id':'251','user_id':'1425','subject':'test subject','message':'Why is answer C correct? ','date':'2012-01-25 09:13:19','replied_to':'0','pseudonym':'0','cid':'1425','profile':'1','profile_pic':'http://www.arcadiaprep.com/_layouts/images/img_user65.png','uname':'tyrone gibson'},{'id':'936','discussion_id':'251','user_id':'1610','subject':'','message':'Tyrone, the conclusion is based upon the assumption that How people feel depends on the time they have been treated, while answer C is claiming the exact opposite; How people feel will determine the length of treatment and it will make the conclusion proposed(even the whole survey) meaningless :)\r\n\r\nNiels','date':'2012-01-25 14:23:20','replied_to':'0','pseudonym':'0','cid':'1610','profile':'5','profile_pic':'http://www.arcadiaprep.com//_upload/pictures/132735469593830142.jpg','uname':'Niels Krook','member':'Contributor'},{'id':'935','discussion_id':'251','user_id':'1425','subject':'test subject','message':'Why is answer C correct? ','date':'2012-01-25 09:13:19','replied_to':'0','pseudonym':'0','cid':'1425','profile':'1','profile_pic':'http://www.arcadiaprep.com/_layouts/images/img_user65.png','uname':'tyrone gibson'},{'id':'936','discussion_id':'251','user_id':'1610','subject':'','message':'Tyrone, the conclusion is based upon the assumption that How people feel depends on the time they have been treated, while answer C is claiming the exact opposite; How people feel will determine the length of treatment and it will make the conclusion proposed(even the whole survey) meaningless :)\r\n\r\nNiels','date':'2012-01-25 14:23:20','replied_to':'0','pseudonym':'0','cid':'1610','profile':'5','profile_pic':'http://www.arcadiaprep.com//_upload/pictures/132735469593830142.jpg','uname':'Niels Krook','member':'Contributor'}]}");
//			
//			Message msg = new Message();
//			msg.obj = a.getJSONArray("discussions").length();
//			msg.what = 5;
//			handler1.sendMessage(msg);
//			
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
	
		
		//parse data to discussion
		//test begin
//		DiscussionVO d = new DiscussionVO();
//		discussions.add(d);
//		d.setComment("this is comment");
//		d.setDate("2012-12-12 12:12:12");
//		d.setPortrait("image");
//		d.setTitle("this is title");
		//test end

	}
	
	
	/**
	 * @param questionid
	 * @return
	 */
	public void findDiscussionsByQuestionid(int questionid ,final Handler handler1){
		final List<DiscussionVO> discussions = new ArrayList<DiscussionVO>();
		//test questionid = 1800
//		questionid = 1588;
		//get data from web server
		String server_url = SystemService.parseModuelInfo(context).getServerURL()+"/"+ConstantSystem.GET_SERVER_URL_DISCUSSIONS_BY_QUESTION_ID(questionid);
		
		Handler handler = new Handler() {
			public void handleMessage(Message message) {
				Message msg = new Message();
				switch (message.what) {
					case HttpConnection.DID_START: {
						Log.d("Image", "Starting Connection");
						break;
					}
					case HttpConnection.DID_SUCCEED: {
						
						String response = (String) message.obj;
						JSONObject a;
						try {
							a = new JSONObject(response);
							parse(discussions,a);
							
							msg.obj = discussions;
							msg.what = 4;
							handler1.sendMessage(msg);
						} catch (Exception e) {
							e.printStackTrace();
							msg.obj = discussions;
							msg.what = 4;
							handler1.sendMessage(msg);
						}
						
						break;
					}
					case HttpConnection.DID_ERROR: {
						Exception e = (Exception) message.obj;
						e.printStackTrace();
						msg.obj = discussions;
						msg.what = 4;
						handler1.sendMessage(msg);
						break;
					}
				}
			}
		};
		
		
		new HttpConnection(handler).get(server_url);
		
//		//test
//		JSONObject a;
////		
//		try {
//			a = new JSONObject("{'qid':'1800','discussions':[{'id':'935','discussion_id':'251','user_id':'1425','subject':'test subject','message':'Why is answer C correct? ','date':'2012-01-25 09:13:19','replied_to':'0','pseudonym':'0','cid':'1425','profile':'1','profile_pic':'http://www.arcadiaprep.com/_layouts/images/img_user65.png','uname':'tyrone gibson'},{'id':'936','discussion_id':'251','user_id':'1610','subject':'','message':'Tyrone, the conclusion is based upon the assumption that How people feel depends on the time they have been treated, while answer C is claiming the exact opposite; How people feel will determine the length of treatment and it will make the conclusion proposed(even the whole survey) meaningless :)\r\n\r\nNiels','date':'2012-01-25 14:23:20','replied_to':'0','pseudonym':'0','cid':'1610','profile':'5','profile_pic':'http://www.arcadiaprep.com//_upload/pictures/132735469593830142.jpg','uname':'Niels Krook','member':'Contributor'},{'id':'935','discussion_id':'251','user_id':'1425','subject':'test subject','message':'Why is answer C correct? ','date':'2012-01-25 09:13:19','replied_to':'0','pseudonym':'0','cid':'1425','profile':'1','profile_pic':'http://www.arcadiaprep.com/_layouts/images/img_user65.png','uname':'tyrone gibson'},{'id':'936','discussion_id':'251','user_id':'1610','subject':'','message':'Tyrone, the conclusion is based upon the assumption that How people feel depends on the time they have been treated, while answer C is claiming the exact opposite; How people feel will determine the length of treatment and it will make the conclusion proposed(even the whole survey) meaningless :)\r\n\r\nNiels','date':'2012-01-25 14:23:20','replied_to':'0','pseudonym':'0','cid':'1610','profile':'5','profile_pic':'http://www.arcadiaprep.com//_upload/pictures/132735469593830142.jpg','uname':'Niels Krook','member':'Contributor'},{'id':'935','discussion_id':'251','user_id':'1425','subject':'test subject','message':'Why is answer C correct? ','date':'2012-01-25 09:13:19','replied_to':'0','pseudonym':'0','cid':'1425','profile':'1','profile_pic':'http://www.arcadiaprep.com/_layouts/images/img_user65.png','uname':'tyrone gibson'},{'id':'936','discussion_id':'251','user_id':'1610','subject':'','message':'Tyrone, the conclusion is based upon the assumption that How people feel depends on the time they have been treated, while answer C is claiming the exact opposite; How people feel will determine the length of treatment and it will make the conclusion proposed(even the whole survey) meaningless :)\r\n\r\nNiels','date':'2012-01-25 14:23:20','replied_to':'0','pseudonym':'0','cid':'1610','profile':'5','profile_pic':'http://www.arcadiaprep.com//_upload/pictures/132735469593830142.jpg','uname':'Niels Krook','member':'Contributor'},{'id':'935','discussion_id':'251','user_id':'1425','subject':'test subject','message':'Why is answer C correct? ','date':'2012-01-25 09:13:19','replied_to':'0','pseudonym':'0','cid':'1425','profile':'1','profile_pic':'http://www.arcadiaprep.com/_layouts/images/img_user65.png','uname':'tyrone gibson'},{'id':'936','discussion_id':'251','user_id':'1610','subject':'','message':'Tyrone, the conclusion is based upon the assumption that How people feel depends on the time they have been treated, while answer C is claiming the exact opposite; How people feel will determine the length of treatment and it will make the conclusion proposed(even the whole survey) meaningless :)\r\n\r\nNiels','date':'2012-01-25 14:23:20','replied_to':'0','pseudonym':'0','cid':'1610','profile':'5','profile_pic':'http://www.arcadiaprep.com//_upload/pictures/132735469593830142.jpg','uname':'Niels Krook','member':'Contributor'},{'id':'935','discussion_id':'251','user_id':'1425','subject':'test subject','message':'Why is answer C correct? ','date':'2012-01-25 09:13:19','replied_to':'0','pseudonym':'0','cid':'1425','profile':'1','profile_pic':'http://www.arcadiaprep.com/_layouts/images/img_user65.png','uname':'tyrone gibson'},{'id':'936','discussion_id':'251','user_id':'1610','subject':'','message':'Tyrone, the conclusion is based upon the assumption that How people feel depends on the time they have been treated, while answer C is claiming the exact opposite; How people feel will determine the length of treatment and it will make the conclusion proposed(even the whole survey) meaningless :)\r\n\r\nNiels','date':'2012-01-25 14:23:20','replied_to':'0','pseudonym':'0','cid':'1610','profile':'5','profile_pic':'http://www.arcadiaprep.com//_upload/pictures/132735469593830142.jpg','uname':'Niels Krook','member':'Contributor'},{'id':'935','discussion_id':'251','user_id':'1425','subject':'test subject','message':'Why is answer C correct? ','date':'2012-01-25 09:13:19','replied_to':'0','pseudonym':'0','cid':'1425','profile':'1','profile_pic':'http://www.arcadiaprep.com/_layouts/images/img_user65.png','uname':'tyrone gibson'},{'id':'936','discussion_id':'251','user_id':'1610','subject':'','message':'Tyrone, the conclusion is based upon the assumption that How people feel depends on the time they have been treated, while answer C is claiming the exact opposite; How people feel will determine the length of treatment and it will make the conclusion proposed(even the whole survey) meaningless :)\r\n\r\nNiels','date':'2012-01-25 14:23:20','replied_to':'0','pseudonym':'0','cid':'1610','profile':'5','profile_pic':'http://www.arcadiaprep.com//_upload/pictures/132735469593830142.jpg','uname':'Niels Krook','member':'Contributor'},{'id':'935','discussion_id':'251','user_id':'1425','subject':'test subject','message':'Why is answer C correct? ','date':'2012-01-25 09:13:19','replied_to':'0','pseudonym':'0','cid':'1425','profile':'1','profile_pic':'http://www.arcadiaprep.com/_layouts/images/img_user65.png','uname':'tyrone gibson'},{'id':'936','discussion_id':'251','user_id':'1610','subject':'','message':'Tyrone, the conclusion is based upon the assumption that How people feel depends on the time they have been treated, while answer C is claiming the exact opposite; How people feel will determine the length of treatment and it will make the conclusion proposed(even the whole survey) meaningless :)\r\n\r\nNiels','date':'2012-01-25 14:23:20','replied_to':'0','pseudonym':'0','cid':'1610','profile':'5','profile_pic':'http://www.arcadiaprep.com//_upload/pictures/132735469593830142.jpg','uname':'Niels Krook','member':'Contributor'},{'id':'935','discussion_id':'251','user_id':'1425','subject':'test subject','message':'Why is answer C correct? ','date':'2012-01-25 09:13:19','replied_to':'0','pseudonym':'0','cid':'1425','profile':'1','profile_pic':'http://www.arcadiaprep.com/_layouts/images/img_user65.png','uname':'tyrone gibson'},{'id':'936','discussion_id':'251','user_id':'1610','subject':'','message':'Tyrone, the conclusion is based upon the assumption that How people feel depends on the time they have been treated, while answer C is claiming the exact opposite; How people feel will determine the length of treatment and it will make the conclusion proposed(even the whole survey) meaningless :)\r\n\r\nNiels','date':'2012-01-25 14:23:20','replied_to':'0','pseudonym':'0','cid':'1610','profile':'5','profile_pic':'http://www.arcadiaprep.com//_upload/pictures/132735469593830142.jpg','uname':'Niels Krook','member':'Contributor'},{'id':'935','discussion_id':'251','user_id':'1425','subject':'test subject','message':'Why is answer C correct? ','date':'2012-01-25 09:13:19','replied_to':'0','pseudonym':'0','cid':'1425','profile':'1','profile_pic':'http://www.arcadiaprep.com/_layouts/images/img_user65.png','uname':'tyrone gibson'},{'id':'936','discussion_id':'251','user_id':'1610','subject':'','message':'Tyrone, the conclusion is based upon the assumption that How people feel depends on the time they have been treated, while answer C is claiming the exact opposite; How people feel will determine the length of treatment and it will make the conclusion proposed(even the whole survey) meaningless :)\r\n\r\nNiels','date':'2012-01-25 14:23:20','replied_to':'0','pseudonym':'0','cid':'1610','profile':'5','profile_pic':'http://www.arcadiaprep.com//_upload/pictures/132735469593830142.jpg','uname':'Niels Krook','member':'Contributor'},{'id':'935','discussion_id':'251','user_id':'1425','subject':'test subject','message':'Why is answer C correct? ','date':'2012-01-25 09:13:19','replied_to':'0','pseudonym':'0','cid':'1425','profile':'1','profile_pic':'http://www.arcadiaprep.com/_layouts/images/img_user65.png','uname':'tyrone gibson'},{'id':'936','discussion_id':'251','user_id':'1610','subject':'','message':'Tyrone, the conclusion is based upon the assumption that How people feel depends on the time they have been treated, while answer C is claiming the exact opposite; How people feel will determine the length of treatment and it will make the conclusion proposed(even the whole survey) meaningless :)\r\n\r\nNiels','date':'2012-01-25 14:23:20','replied_to':'0','pseudonym':'0','cid':'1610','profile':'5','profile_pic':'http://www.arcadiaprep.com//_upload/pictures/132735469593830142.jpg','uname':'Niels Krook','member':'Contributor'},{'id':'935','discussion_id':'251','user_id':'1425','subject':'test subject','message':'Why is answer C correct? ','date':'2012-01-25 09:13:19','replied_to':'0','pseudonym':'0','cid':'1425','profile':'1','profile_pic':'http://www.arcadiaprep.com/_layouts/images/img_user65.png','uname':'tyrone gibson'},{'id':'936','discussion_id':'251','user_id':'1610','subject':'','message':'Tyrone, the conclusion is based upon the assumption that How people feel depends on the time they have been treated, while answer C is claiming the exact opposite; How people feel will determine the length of treatment and it will make the conclusion proposed(even the whole survey) meaningless :)\r\n\r\nNiels','date':'2012-01-25 14:23:20','replied_to':'0','pseudonym':'0','cid':'1610','profile':'5','profile_pic':'http://www.arcadiaprep.com//_upload/pictures/132735469593830142.jpg','uname':'Niels Krook','member':'Contributor'},{'id':'935','discussion_id':'251','user_id':'1425','subject':'test subject','message':'Why is answer C correct? ','date':'2012-01-25 09:13:19','replied_to':'0','pseudonym':'0','cid':'1425','profile':'1','profile_pic':'http://www.arcadiaprep.com/_layouts/images/img_user65.png','uname':'tyrone gibson'},{'id':'936','discussion_id':'251','user_id':'1610','subject':'','message':'Tyrone, the conclusion is based upon the assumption that How people feel depends on the time they have been treated, while answer C is claiming the exact opposite; How people feel will determine the length of treatment and it will make the conclusion proposed(even the whole survey) meaningless :)\r\n\r\nNiels','date':'2012-01-25 14:23:20','replied_to':'0','pseudonym':'0','cid':'1610','profile':'5','profile_pic':'http://www.arcadiaprep.com//_upload/pictures/132735469593830142.jpg','uname':'Niels Krook','member':'Contributor'},{'id':'935','discussion_id':'251','user_id':'1425','subject':'test subject','message':'Why is answer C correct? ','date':'2012-01-25 09:13:19','replied_to':'0','pseudonym':'0','cid':'1425','profile':'1','profile_pic':'http://www.arcadiaprep.com/_layouts/images/img_user65.png','uname':'tyrone gibson'},{'id':'936','discussion_id':'251','user_id':'1610','subject':'','message':'Tyrone, the conclusion is based upon the assumption that How people feel depends on the time they have been treated, while answer C is claiming the exact opposite; How people feel will determine the length of treatment and it will make the conclusion proposed(even the whole survey) meaningless :)\r\n\r\nNiels','date':'2012-01-25 14:23:20','replied_to':'0','pseudonym':'0','cid':'1610','profile':'5','profile_pic':'http://www.arcadiaprep.com//_upload/pictures/132735469593830142.jpg','uname':'Niels Krook','member':'Contributor'},{'id':'935','discussion_id':'251','user_id':'1425','subject':'test subject','message':'Why is answer C correct? ','date':'2012-01-25 09:13:19','replied_to':'0','pseudonym':'0','cid':'1425','profile':'1','profile_pic':'http://www.arcadiaprep.com/_layouts/images/img_user65.png','uname':'tyrone gibson'},{'id':'936','discussion_id':'251','user_id':'1610','subject':'','message':'Tyrone, the conclusion is based upon the assumption that How people feel depends on the time they have been treated, while answer C is claiming the exact opposite; How people feel will determine the length of treatment and it will make the conclusion proposed(even the whole survey) meaningless :)\r\n\r\nNiels','date':'2012-01-25 14:23:20','replied_to':'0','pseudonym':'0','cid':'1610','profile':'5','profile_pic':'http://www.arcadiaprep.com//_upload/pictures/132735469593830142.jpg','uname':'Niels Krook','member':'Contributor'},{'id':'935','discussion_id':'251','user_id':'1425','subject':'test subject','message':'Why is answer C correct? ','date':'2012-01-25 09:13:19','replied_to':'0','pseudonym':'0','cid':'1425','profile':'1','profile_pic':'http://www.arcadiaprep.com/_layouts/images/img_user65.png','uname':'tyrone gibson'},{'id':'936','discussion_id':'251','user_id':'1610','subject':'','message':'Tyrone, the conclusion is based upon the assumption that How people feel depends on the time they have been treated, while answer C is claiming the exact opposite; How people feel will determine the length of treatment and it will make the conclusion proposed(even the whole survey) meaningless :)\r\n\r\nNiels','date':'2012-01-25 14:23:20','replied_to':'0','pseudonym':'0','cid':'1610','profile':'5','profile_pic':'http://www.arcadiaprep.com//_upload/pictures/132735469593830142.jpg','uname':'Niels Krook','member':'Contributor'},{'id':'935','discussion_id':'251','user_id':'1425','subject':'test subject','message':'Why is answer C correct? ','date':'2012-01-25 09:13:19','replied_to':'0','pseudonym':'0','cid':'1425','profile':'1','profile_pic':'http://www.arcadiaprep.com/_layouts/images/img_user65.png','uname':'tyrone gibson'},{'id':'936','discussion_id':'251','user_id':'1610','subject':'','message':'Tyrone, the conclusion is based upon the assumption that How people feel depends on the time they have been treated, while answer C is claiming the exact opposite; How people feel will determine the length of treatment and it will make the conclusion proposed(even the whole survey) meaningless :)\r\n\r\nNiels','date':'2012-01-25 14:23:20','replied_to':'0','pseudonym':'0','cid':'1610','profile':'5','profile_pic':'http://www.arcadiaprep.com//_upload/pictures/132735469593830142.jpg','uname':'Niels Krook','member':'Contributor'},{'id':'935','discussion_id':'251','user_id':'1425','subject':'test subject','message':'Why is answer C correct? ','date':'2012-01-25 09:13:19','replied_to':'0','pseudonym':'0','cid':'1425','profile':'1','profile_pic':'http://www.arcadiaprep.com/_layouts/images/img_user65.png','uname':'tyrone gibson'},{'id':'936','discussion_id':'251','user_id':'1610','subject':'','message':'Tyrone, the conclusion is based upon the assumption that How people feel depends on the time they have been treated, while answer C is claiming the exact opposite; How people feel will determine the length of treatment and it will make the conclusion proposed(even the whole survey) meaningless :)\r\n\r\nNiels','date':'2012-01-25 14:23:20','replied_to':'0','pseudonym':'0','cid':'1610','profile':'5','profile_pic':'http://www.arcadiaprep.com//_upload/pictures/132735469593830142.jpg','uname':'Niels Krook','member':'Contributor'},{'id':'935','discussion_id':'251','user_id':'1425','subject':'test subject','message':'Why is answer C correct? ','date':'2012-01-25 09:13:19','replied_to':'0','pseudonym':'0','cid':'1425','profile':'1','profile_pic':'http://www.arcadiaprep.com/_layouts/images/img_user65.png','uname':'tyrone gibson'},{'id':'936','discussion_id':'251','user_id':'1610','subject':'','message':'Tyrone, the conclusion is based upon the assumption that How people feel depends on the time they have been treated, while answer C is claiming the exact opposite; How people feel will determine the length of treatment and it will make the conclusion proposed(even the whole survey) meaningless :)\r\n\r\nNiels','date':'2012-01-25 14:23:20','replied_to':'0','pseudonym':'0','cid':'1610','profile':'5','profile_pic':'http://www.arcadiaprep.com//_upload/pictures/132735469593830142.jpg','uname':'Niels Krook','member':'Contributor'},{'id':'935','discussion_id':'251','user_id':'1425','subject':'test subject','message':'Why is answer C correct? ','date':'2012-01-25 09:13:19','replied_to':'0','pseudonym':'0','cid':'1425','profile':'1','profile_pic':'http://www.arcadiaprep.com/_layouts/images/img_user65.png','uname':'tyrone gibson'},{'id':'936','discussion_id':'251','user_id':'1610','subject':'','message':'Tyrone, the conclusion is based upon the assumption that How people feel depends on the time they have been treated, while answer C is claiming the exact opposite; How people feel will determine the length of treatment and it will make the conclusion proposed(even the whole survey) meaningless :)\r\n\r\nNiels','date':'2012-01-25 14:23:20','replied_to':'0','pseudonym':'0','cid':'1610','profile':'5','profile_pic':'http://www.arcadiaprep.com//_upload/pictures/132735469593830142.jpg','uname':'Niels Krook','member':'Contributor'},{'id':'935','discussion_id':'251','user_id':'1425','subject':'test subject','message':'Why is answer C correct? ','date':'2012-01-25 09:13:19','replied_to':'0','pseudonym':'0','cid':'1425','profile':'1','profile_pic':'http://www.arcadiaprep.com/_layouts/images/img_user65.png','uname':'tyrone gibson'},{'id':'936','discussion_id':'251','user_id':'1610','subject':'','message':'Tyrone, the conclusion is based upon the assumption that How people feel depends on the time they have been treated, while answer C is claiming the exact opposite; How people feel will determine the length of treatment and it will make the conclusion proposed(even the whole survey) meaningless :)\r\n\r\nNiels','date':'2012-01-25 14:23:20','replied_to':'0','pseudonym':'0','cid':'1610','profile':'5','profile_pic':'http://www.arcadiaprep.com//_upload/pictures/132735469593830142.jpg','uname':'Niels Krook','member':'Contributor'}]}");
//			parse(discussions,a);
//			
//			Message msg = new Message();
//			msg.obj = discussions;
//			msg.what = 4;
//			handler1.sendMessage(msg);
//			
//			
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//	
		
		//parse data to discussion
		//test begin
//		DiscussionVO d = new DiscussionVO();
//		discussions.add(d);
//		d.setComment("this is comment");
//		d.setDate("2012-12-12 12:12:12");
//		d.setPortrait("image");
//		d.setTitle("this is title");
		//test end

	}

	private void parse(List<DiscussionVO> discussions, JSONObject a) throws JSONException {
		JSONArray ds;
		
			ds = a.getJSONArray("discussions");

			for (int i = 0; i < ds.length(); i++) {
				DiscussionVO d = new DiscussionVO();
				JSONObject o = (JSONObject) ds.opt(i);
				d.setId(o.getInt("id"));
				d.setDiscusstionId(o.getInt("discussion_id"));
				d.setUserId(o.getInt("user_id"));
				d.setSubject(o.getString("subject"));
				d.setMessage(o.getString("message"));
				d.setDate(o.getString("date"));
				d.setRepliedTo(o.getInt("replied_to"));
				d.setPseudonym(o.getInt("pseudonym"));
				
				d.setCid(o.getString("cid"));
				
				d.setProfile(o.getInt("profile"));
				d.setProfilePic(o.getString("profile_pic"));
				d.setUname(o.getString("uname"));
				if(o.toString().contains("question_id"))
					d.setQuestionId(o.getInt("question_id"));
				if(o.toString().contains("member"))
					d.setMember(o.getString("member"));
				if(o.toString().contains("attach_image"))
					d.setAttachIamge(o.getString("attach_image"));
					
					
				discussions.add(d);
			}
		
	}

	/**
	 * 
	 * @param examType exam type: LSAT, GRE,GMAT,SAT
	 * @return
	 */
	public void findDiscussionsByExamType(final Handler handler1){
		final List<DiscussionVO> discussions = new ArrayList<DiscussionVO>();
		String examType = SystemService.parseModuelInfo(context).getExamType();
		String server_url = SystemService.parseModuelInfo(context).getServerURL()+"/"+ConstantSystem.GET_SERVER_URL_DISCUSSIONS_BY_EXAM(examType);
		Handler handler = new Handler() {
			public void handleMessage(Message message) {
				Message msg = new Message();
				switch (message.what) {
					case HttpConnection.DID_START: {
						Log.d("Image", "Starting Connection");
						break;
					}
					case HttpConnection.DID_SUCCEED: {
						String response = (String) message.obj;
						JSONObject a;
						try {
							a = new JSONObject(response);
							parse(discussions,a);
							
							msg.obj = discussions;
							msg.what = HttpConnection.DID_SUCCEED;
							handler1.sendMessage(msg);
						} catch (Exception e) {
							e.printStackTrace();
							msg.obj = discussions;
							msg.what = HttpConnection.DID_SUCCEED;
							handler1.sendMessage(msg);
						}
						
						break;
					}
					case HttpConnection.DID_ERROR: {
						Exception e = (Exception) message.obj;
						e.printStackTrace();
						msg.obj = discussions;
						msg.what = HttpConnection.DID_SUCCEED;
						handler1.sendMessage(msg);
						break;
					}
				}
			}
		};
		
		
		new HttpConnection(handler).get
		(server_url);
//		
//		JSONObject a;
//		try {
//			a = new JSONObject("{\"qid\": 0,\"discussions\": [{\"id\": \"2047\",\"discussion_id\": \"547\",\"user_id\": \"608\",\"subject\": \"\",\"message\": \"Diana, here's a great analogy I found:\r\n\r\nAntonio: You can make great pizza with any cheese.\r\nMarla: No, you must use mozzarella to have great pizza!\r\n\r\n(B) They disagree about what is required to make great pizza (Marla thinks that mozzarella is required, Antonio does not).\",\"date\": \"2012-08-23 14:38:15\",\"replied_to\": \"0\",\"pseudonym\": \"0\",\"cid\": \"608\",\"profile_pic\": \"http://www.arcadiaprep.com/_layouts/images/img_admin65.png\",\"uname\": \"Arcadia Admin\",\"question_id\": \"1792\",\"profile\": \"10\",\"member\": \"Admin\"}]}");
//			parse(discussions,a);
//			Message msg = new Message();
//			msg.obj = discussions;
//			msg.what = HttpConnection.DID_SUCCEED;
//			handler1.sendMessage(msg);
//			
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
	}
	
	
	public void postNew(final Handler handler1,User user,String message,File file){
		
		String server_url = SystemService.parseModuelInfo(context).getServerURL()+"/"+ConstantSystem.GET_SERVER_URL_POST_A_NEW_DISCUSSION();
		
		Handler handler = new Handler() {
			public void handleMessage(Message message) {
				Message msg = new Message();
				switch (message.what) {
					case HttpConnection.DID_START: {
						Log.d("Image", "Starting Connection");
						break;
					}
					case HttpConnection.DID_SUCCEED: {
						String response = (String) message.obj;
						try {
							msg.what = HttpConnection.DID_SUCCEED;
							handler1.sendMessage(msg);
						} catch (Exception e) {
							e.printStackTrace();
							msg.what = -1;
							handler1.sendMessage(msg);
						}
						
						break;
					}
					case HttpConnection.DID_ERROR: {
						Exception e = (Exception) message.obj;
						e.printStackTrace();
						msg.what = -1;
						handler1.sendMessage(msg);
						break;
					}
				}
			}
		};
		
		new HttpConnection(handler).post(server_url,wrapPostParams(user.getUserName(),user.getPassword(),message,file),"");
		

	}

	public void postForAGivenQuestion(final Handler handler1,User user,int questionId,String message,File file){
		String server_url = SystemService.parseModuelInfo(context).getServerURL()+"/"+ConstantSystem.GET_SERVER_URL_POST_FOR_A_GIVEN_QUESTION(questionId);
		
		Handler handler = new Handler() {
			public void handleMessage(Message message) {
				Message msg = new Message();
				switch (message.what) {
					case HttpConnection.DID_START: {
						Log.d("Image", "Starting Connection");
						break;
					}
					case HttpConnection.DID_SUCCEED: {
						String response = (String) message.obj;
						try {
							msg.what = HttpConnection.DID_SUCCEED;
							handler1.sendMessage(msg);
						} catch (Exception e) {
							e.printStackTrace();
							msg.what = -1;
							handler1.sendMessage(msg);
						}
						
						break;
					}
					case HttpConnection.DID_ERROR: {
						Exception e = (Exception) message.obj;
						e.printStackTrace();
						msg.what = -1;
						handler1.sendMessage(msg);
						break;
					}
				}
			}
		};
		
		new HttpConnection(handler).post(server_url,wrapPostParams(user.getUserName(),user.getPassword(),message,file),"");
		

	}
	
	
	public void postReply(final Handler handler1,User user,int discussionId,String message,File file){
		String server_url = SystemService.parseModuelInfo(context).getServerURL()+"/"+ConstantSystem.GET_SERVER_URL_POST_REPLY(discussionId);
		
		Handler handler = new Handler() {
			public void handleMessage(Message message) {
				Message msg = new Message();
				switch (message.what) {
					case HttpConnection.DID_START: {
						Log.d("Image", "Starting Connection");
						break;
					}
					case HttpConnection.DID_SUCCEED: {
						try {
							msg.what = HttpConnection.DID_SUCCEED;
							handler1.sendMessage(msg);
						} catch (Exception e) {
							e.printStackTrace();
							msg.what = -1;
							handler1.sendMessage(msg);
						}
						
						break;
					}
					case HttpConnection.DID_ERROR: {
						Exception e = (Exception) message.obj;
						e.printStackTrace();
						msg.what = -1;
						handler1.sendMessage(msg);
						break;
					}
				}
			}
		};
		
		new HttpConnection(handler).post(server_url,wrapPostParams(user.getUserName(),user.getPassword(),message,file),"");
		
	}
	
	public void getUserInfo(final Handler handler1,final DiscussionUserVO vo) {
		String server_url = SystemService.parseModuelInfo(context).getServerURL()+"/"+ConstantSystem.GET_SERVER_URL_USER_INFO(vo.getUserID());
//		{"user_id":"37","about":"","uname":"Jing Wang","n_post":"17"}
		Handler handler = new Handler() {
			public void handleMessage(Message message) {
				Message msg = new Message();
				switch (message.what) {
					case HttpConnection.DID_START: {
						break;
					}
					case HttpConnection.DID_SUCCEED: {
						
						String response = (String) message.obj;
						JSONObject a;
						try {
							a = new JSONObject(response);
							parseUser(vo,a);
//							msg.what = HttpConnection.DID_SUCCEED;
							msg.obj = vo;
							handler1.sendMessage(msg);
						} catch (Exception e) {
							e.printStackTrace();
							msg.obj = vo;
							handler1.sendMessage(msg);
						}
						
						break;
					}
					case HttpConnection.DID_ERROR: {
						Exception e = (Exception) message.obj;
						e.printStackTrace();
						msg.obj = vo;
						handler1.sendMessage(msg);
						break;
					}
				}
			}
		};
		new HttpConnection(handler).post(server_url,null,"");
		
//		DiscussionUserVO vo = new DiscussionUserVO();
//		vo.setAboutme("this is about me");
//		vo.setHead("this is head portrait");
//		vo.setLocation("this is location");
//		vo.setUname("this is uname");
//		vo.setExtra("+230 Posts");
	}

	protected void parseUser(DiscussionUserVO vo, JSONObject a) {
//		{"user_id":"37","about":"","uname":"Jing Wang","n_post":"17"}
		try {
			if(a.getString("about")!=null)
				vo.setAboutme(a.getString("about"));
			if(a.getString("uname")!=null)
				vo.setUname(a.getString("uname"));
			if(a.getString("n_post")!=null)
				vo.setPost(a.getString("n_post"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}

	public String getHttpDataTest() {
		try {
			return HTTPUtils.getUrl("www.baidu.com");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "error";
	}
	
	private HttpEntity wrapPostParams(String userName,String password,String message,File file){
		MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		try {
			entity.addPart("login_user", new StringBody(userName));
			entity.addPart("login_pass", new StringBody(password));
			entity.addPart("login", new StringBody(ConstantSystem.DISCUSSION_POST_LOGIN));
			entity.addPart("step", new StringBody(ConstantSystem.DISCUSSION_POST_STEP));
			entity.addPart("cod", new StringBody(new Random().nextInt()+""));
			entity.addPart("type", new StringBody(ConstantSystem.DISCUSSION_POST_TYPE));
			entity.addPart("play_time", new StringBody(ConstantSystem.DISCUSSION_POST_PLAY_TIME));
			entity.addPart("message", new StringBody(message));
			String subject = message;
			if(message!=null&&message.length()>32)subject = subject.substring(0,32);
			entity.addPart("subject", new StringBody(subject));
			
			if(file!=null){
				entity.addPart(EntityHelper.PICTURE,new FileBody(file,"image/jpeg"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
//		params.add(new BasicNameValuePair("login_user", userName));
//		params.add(new BasicNameValuePair("login_pass", password));
//		params.add(new BasicNameValuePair("login", ConstantSystem.DISCUSSION_POST_LOGIN));
//		params.add(new BasicNameValuePair("step", "2"));
//		params.add(new BasicNameValuePair("cod", ""+System.currentTimeMillis()));
//		params.add(new BasicNameValuePair("type", ConstantSystem.DISCUSSION_POST_TYPE));
//		params.add(new BasicNameValuePair("play_time", ConstantSystem.DISCUSSION_POST_PLAY_TIME));
//		params.add(new BasicNameValuePair("message", message));
//		String subject = message;
//		if(message!=null&&message.length()>32)
//			subject = subject.substring(0,32);
//		params.add(new BasicNameValuePair("subject", subject));
		return entity;
	}
}
