package com.arcadiaprep.app.arca;

import java.io.InputStream;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arcadiaprep.app.arca.constants.ConstantSystem;
import com.arcadiaprep.app.arca.event.ApplicationEvent;
import com.arcadiaprep.app.arca.event.EventDispatcher;
import com.arcadiaprep.app.arca.listener.ApplicationEventListener;
import com.arcadiaprep.app.arca.service.MainDataService;
import com.arcadiaprep.app.arca.service.MainLayoutService;
import com.arcadiaprep.app.arca.service.MainListenerService;
import com.arcadiaprep.app.arca.service.SystemService;
import com.arcadiaprep.app.arca.ui.list.ListViewQuestionAdapter;
import com.arcadiaprep.app.arca.ui.list.ListViewRecommendationsAdapter;
import com.arcadiaprep.app.arca.util.HTTPUtils;
import com.arcadiaprep.app.arca.vo.ExamSection;
import com.arcadiaprep.app.arca.vo.ListItemMyQuestionVO;
import com.arcadiaprep.app.arca.vo.ModuleInfoVO;
import com.arcadiaprep.app.login.common.Constants;
import com.arcadiaprep.app.login.common.Login;
import com.arcadiaprep.app.login.common.UserService;
import com.arcadiaprep.app.login.model.User;
import com.arcadiaprep.app.market.ItemViewActivity;
import com.arcadiaprep.app.market.MyApplication;
import com.arcadiaprep.app.util.Item;
import com.foound.amazinglistview.demo.Composer;
import com.foound.amazinglistview.demo.Data;
import com.foound.widget.AmazingAdapter;
import com.foound.widget.AmazingListView;

/**
 * The portal for App.
 * @author lala 
 * integrate with Samsung market by Sharon
 */
public class ArcadiaprepActivity extends Activity {
	
	String LOG_TAG = ArcadiaprepActivity.class+"";
	
	private AlertDialog errorDialog;
	private ListView _recommendationItemsListView, listViewQuestion;
	private TextView visitMarketplaceView;
	//My Question Sets Bar
	Button btnReason,btnGames,btnRead;
	//Function ICON
	ImageView btnIconInfo,btnIconPerfdata,btnIconShopcart,btnIconInteraction,btnIconBookmark,btnIconEmail,btnSearchNotes;
	//Login Logout
	Button btnlogin;
	//category bar layout
	LinearLayout categorybarLayout;
	public static ListViewRecommendationsAdapter _listViewRecommendationAdapter;
	public static String _currSection = null;
	public static String _currSectionName = null;
	AmazingListView lsComposer;
	
	User mUser = null;
	 List<ExamSection> sectionList;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        
//        SystemService.setOrientation(this);
        
        Log.d(LOG_TAG, "invoke function [onCreate]");
        setContentView(R.layout.main);
        
        init(); 
        
        //set ScrollView Height
        View rv = (View) findViewById(R.id.recommendations_visitmarketplace);
        View rc_ll = (View) findViewById(R.id.recommendations_ll);
        _recommendationItemsListView = (ListView)findViewById(R.id.list);
//        ScrollView scrollView_MyQuestionSet = (ScrollView) findViewById(R.id.scrollView_MyQuestionSet);
        
        int height_screen = (int)(SystemService.getScreenHeight(getWindowManager()));
        int width_screen = (int)(SystemService.getScreenWith(getWindowManager()));
//        test.setText("width="+width_screen+";height="+height_screen);
        int height_recommendation = 0;
        int height_margin = 0;
        if (height_screen >  width_screen) {          
            height_recommendation = (int)(width_screen*0.3);
            height_margin = width_screen - 2 * height_recommendation;
        }
        else 
        {
            height_recommendation = (int)(height_screen*0.3);
            height_margin = height_screen - 2 * height_recommendation;
        }
        
        
        int height_question;
        
        //Recommendations
        List<Item> recommendations = ListViewRecommendationsAdapter.findRecommendations(this, _currSection);
        int recommendations_number = recommendations.size();
        if (recommendations_number == 0) { 
            height_question = height_screen - height_margin;

            // remove recommendations layout
            rv.setVisibility(View.GONE);
            
            if(!getString(R.string.device_screen).equals("xhdpi")&&!getString(R.string.device_screen).equals("hdpi"))
            	rc_ll.setVisibility(View.GONE);
        }
        else 
        {
        	if (recommendations_number < 3) 
        		height_recommendation = height_recommendation *recommendations_number/3; // re-calculate height_recommendation based on how many items in the recommendation list
            height_question = height_screen - height_recommendation - height_margin;

            rv.setVisibility(View.VISIBLE);
            rc_ll.setVisibility(View.VISIBLE);
        }

//        scrollView_MyQuestionSet.setLayoutParams(new android.widget.LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.FILL_PARENT, height_question));                     
             
        _listViewRecommendationAdapter = new ListViewRecommendationsAdapter(this, R.layout.row, recommendations);
        _recommendationItemsListView.setLayoutParams(new android.widget.LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.FILL_PARENT, height_recommendation));
        _recommendationItemsListView.setAdapter(_listViewRecommendationAdapter);
         
        //Visit Marketplace
        visitMarketplaceView = (TextView)findViewById(R.id.visitmarketplace);
       
        //My Question Sets
        listViewQuestion=(ListView)findViewById(R.id.list_question);        

        //category bar
        sectionList = MainDataService.findCategoryBars(this);
        if (_currSection == null)
            _currSection = sectionList.get(0).getSecNameShort();
        if (_currSectionName == null)
            _currSectionName = sectionList.get(0).getSecName();
        List<Button> categoryButtons = MainLayoutService.generateCategoryBar(this, sectionList, (LinearLayout) findViewById(R.id.linear_main_category_bar));
//        MainListenerService.getInstance().registerCategoryBar(this, categoryButtons,listViewQuestion);
        ModuleInfoVO module = SystemService.parseModuelInfo(this);
        MyApplication._ITEM_GROUP_ID = module.getMarketGroupId();
        MyApplication._developer = module.getDeveloperFlag();
        //find my question
        if(getString(R.string.device_screen).equals("xhdpi")||getString(R.string.device_screen).equals("hdpi")){
        	lsComposer = (AmazingListView) findViewById(R.id.lsComposer);
    		lsComposer.setPinnedHeaderView(LayoutInflater.from(this).inflate(
    				R.layout.item_composer_header, lsComposer, false));
    		lsComposer.setAdapter(new SectionComposerAdapter());
    		MainListenerService.getInstance().registerCategoryBar(this, categoryButtons,lsComposer);
        }else{
        	MainListenerService.getInstance().registerCategoryBar(this, categoryButtons,listViewQuestion);
        	listViewQuestion.setAdapter(new ListViewQuestionAdapter(this, MainDataService.findMyQuestionSets(this,_currSectionName)));
        }
        	
        
        int btnIndex = 0;
        for (btnIndex = 0; btnIndex < categoryButtons.size(); btnIndex++){
        	
        	for(ExamSection e : sectionList){
        		if(_currSectionName.equals(e.getSecName()))break;
        		else if(_currSectionName.equals(e.getSecNameShort())){
        			_currSectionName = e.getSecName();
        		}
        	}
        	
        	if(getString(R.string.device_screen).equals("xhdpi")||getString(R.string.device_screen).equals("hdpi")){
    			if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
    				if (categoryButtons.get(btnIndex).getText().toString().equals(_currSection))
    	                break;
    			}else{
    				if (categoryButtons.get(btnIndex).getText().toString().equals(_currSectionName))
                        break;
    			}
    		}else{
    			if (categoryButtons.get(btnIndex).getText().toString().equals(_currSectionName))
                    break;
    		}
        	
        }
            
        if(categoryButtons!=null&&categoryButtons.size()>0)categoryButtons.get(btnIndex).performClick();
        
        //Register Listener
        registerListener();        
        
		btnlogin = (Button) findViewById(R.id.main_btn_login);
        btnlogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(!HTTPUtils.isConnectInternet(ArcadiaprepActivity.this)) {
					ArcadiaprepActivity.this.showNoNetErrorDialog();
					return;
				}
				
				if ((mUser = UserService.getUser(v.getContext())) == null) {
					Login.login(ArcadiaprepActivity.this);
				}
				else {
					Login.logout(ArcadiaprepActivity.this);
					update();
				}
			}
			
		});
		
    }

	private void init() {
		ConstantSystem.SYSTEM_ROOT_PATH =getPackageName();
		initErrorDialog();
	}

	private void registerListener() {
		//Recommendations
		MainListenerService.getInstance().registerRecommendationsListener(this, _recommendationItemsListView);
		//Visit Marketplace
        MainListenerService.getInstance().registerVisitMarketplaceListener(this,visitMarketplaceView);
		//my question set
        
        if(getString(R.string.device_screen).equals("xhdpi")||getString(R.string.device_screen).equals("hdpi"))
        	MainListenerService.getInstance().registerMyQuestionSetsListener(this,lsComposer);
        else
        	MainListenerService.getInstance().registerMyQuestionSetsListener(this,listViewQuestion);
		//function icon
		MainListenerService.getInstance().registerFunctionIconListener(this,(ImageView)findViewById(R.id.main_img_btn_icon_info),(ImageView)findViewById(R.id.main_img_btn_icon_perfdata),(ImageView)findViewById(R.id.main_img_btn_icon_shopcart),(ImageView)findViewById(R.id.main_img_btn_icon_interaction),(ImageView)findViewById(R.id.main_img_btn_icon_bookmark),(ImageView)findViewById(R.id.main_img_btn_icon_email),(ImageView)findViewById(R.id.main_img_btn_icon_searchnotes));
		//login/logout
		//MainListenerService.getInstance().registerLoginoutListener(this, (Button) findViewById(R.id.main_btn_login));
		
	} 
	
	public void showNoNetErrorDialog() {
		errorDialog.show();
	}
	
	private void initErrorDialog() {
		errorDialog = new AlertDialog.Builder(this)
				.setTitle("Network Alert")
				.setMessage("You need to have an Internet connect to use this feature!")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						errorDialog.dismiss();
					}
				}).create();
	}
	

	@Override
	protected void onDestroy() {
		Log.d(LOG_TAG, "invoke function [onDestroy]");
		super.onDestroy();
	}
	
	//ApplicationEvent listener for login/logout/registration events
	private ApplicationEventListener applicationEventListener = new ApplicationEventListener() {

		@SuppressWarnings("deprecation")
		@Override
		public void onApplicationEvent(ApplicationEvent event) {
			if (Constants.TEST_SHOW_TOASTS)
				Toast.makeText(ArcadiaprepActivity.this, "Type: " + event.type + ", data: " + event.data,
						Toast.LENGTH_SHORT).show();

			if (null != event) {
				String t = event.type;
				String d = event.data;

				if (t.equals(ApplicationEvent.TYPE_NO_CONNECTIVITY)) {

				} else if (t.equals(ApplicationEvent.TYPE_LOGIN_SUCCESS)) {
					update();
					// refresh the QuestionList, if user.profile > 5, all the question will be unlocked.
			        if(getString(R.string.device_screen).equals("xhdpi")||getString(R.string.device_screen).equals("hdpi")){
			        	
			        }else{
						listViewQuestion.setAdapter(new ListViewQuestionAdapter(ArcadiaprepActivity.this, MainDataService.findMyQuestionSets(ArcadiaprepActivity.this,_currSectionName)));
			        }	

				} else if (t.equals(ApplicationEvent.TYPE_LOGIN_FAIL)) {

				} else if (t.equals(ApplicationEvent.TYPE_REGISTER_SUCCESS)) {
					update();
				} else if (t.equals(ApplicationEvent.TYPE_REGISTER_FAIL)) {

				} else if (t.equals(ApplicationEvent.TYPE_REGISTRATION_SHOW)) {

				}
			}
		}

	};

    public void onStart() {
    	super.onStart();
		EventDispatcher.getInstance().addEventListener(applicationEventListener);
		update();
		
//		listViewQuestion.setAdapter(new ListViewQuestionAdapter(this, MainDataService.findMyQuestionSets(this,_currSectionName)));
//        ListHeightUtils.setListViewHeightBasedOnChildren(listViewQuestion);
    }
    
    protected void onStop() {
        super.onStop();
		EventDispatcher.getInstance().removeEventListener(applicationEventListener);
    }

    private void update() {
		String loginBtnLabel = (UserService.getUser(this) == null) ? getString(R.string.main_login) : getString(R.string.main_logout);
		btnlogin.setText(loginBtnLabel);
    }
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			final AlertDialog isExit = new AlertDialog.Builder(this).create();
			isExit.setTitle("Information");
			isExit.setMessage("Do you want to exit?");
			
			DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case AlertDialog.BUTTON1:
						
						 NotificationManager notificationManager = (NotificationManager) ArcadiaprepActivity.this 
		                    .getSystemService(NOTIFICATION_SERVICE); 
						 	notificationManager.cancel(0); 
						 	String packagename = getPackageName();
							ActivityManager manager = (ActivityManager)getSystemService(ACTIVITY_SERVICE); 
							finish();
							manager.killBackgroundProcesses(packagename); 

						break;
					case AlertDialog.BUTTON2:
						isExit.cancel();
						break;
					default:
						break;
					}
				}
			};
			isExit.setButton("exit", listener);
			isExit.setButton2("cancel", listener);
			isExit.show();
			return false;
		}
		return false;
	}
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // If the request went well (OK) and the request was
        // PURCHASE
        if (resultCode == Activity.RESULT_OK
                && requestCode == ItemViewActivity.REQUEST_CODE)
        {
            Log.d("ArcadiaprepActivity", " *** Arcadia *** refesh list");
	        if(getString(R.string.device_screen).equals("xhdpi")||getString(R.string.device_screen).equals("hdpi")){
	        	
	        } else {
	        	((ListViewQuestionAdapter)listViewQuestion.getAdapter()).setListItems(MainDataService.findMyQuestionSets(this, _currSectionName));
	        	((ListViewQuestionAdapter)listViewQuestion.getAdapter()).notifyDataSetChanged();
	        }
        }
    }
    
    @Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
//		SystemService.setOrientation(this);
	}
    
    public class SectionComposerAdapter extends AmazingAdapter {
    	
    	public final class ListItemView {
    		public ImageView logoimg;
    		public TextView title;
    		public TextView info;
    		public ImageView arrowIcon;
    		public TextView progressTxt;
    		public TextView txtPrice;
    		public ProgressBar progress;
    	}
    	
		public List<Pair<String, List<ListItemMyQuestionVO>>> all = Data.getAllData();

		@Override
		public int getCount() {
			int res = 0;
			for (int i = 0; i < all.size(); i++) {
				res += all.get(i).second.size();
			}
			return res;
		}

		@Override
		public ListItemMyQuestionVO getItem(int position) {
			int c = 0;
			for (int i = 0; i < all.size(); i++) {
				if (position >= c && position < c + all.get(i).second.size()) {
					return all.get(i).second.get(position - c);
				}
				c += all.get(i).second.size();
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		protected void onNextPageRequested(int page) {
		}

		@Override
		protected void bindSectionHeader(View view, int position,
				boolean displaySectionHeader) {
			if (displaySectionHeader) {
				view.findViewById(R.id.header).setVisibility(View.VISIBLE);
				TextView lSectionTitle = (TextView) view
						.findViewById(R.id.header);
				lSectionTitle
						.setText(getSections()[getSectionForPosition(position)]);
			} else {
				view.findViewById(R.id.header).setVisibility(View.GONE);
			}
		}

		@Override
		public View getAmazingView(int position, View convertView,
				ViewGroup parent) {
			
			View res = convertView;

			res = getLayoutInflater().inflate(R.layout.item_composer, null);
			//begin
				ListItemView _listItemView = new ListItemView();   
				
				_listItemView.logoimg = (ImageView) res.findViewById(R.id.imageItem); 
				_listItemView.title = (TextView)res.findViewById(R.id.titleItem);   
				_listItemView.info = (TextView)res.findViewById(R.id.infoItem);
				_listItemView.progressTxt = (TextView) res.findViewById(R.id.progress_txt);
				_listItemView.progress = (ProgressBar) res.findViewById(R.id.progressBar);
				_listItemView.txtPrice = (TextView) res.findViewById(R.id.txtPrice);
				
			
			ListItemMyQuestionVO listItemMyQuestionVO = getItem(position);
			if(listItemMyQuestionVO==null)return res;
			String img_name = listItemMyQuestionVO.getLogoImg();
			
			if (img_name!=null) {
	            Bitmap image = null;
	            InputStream is = null;
	            try {
	              is = getResources().getAssets().open(img_name);
	              image = BitmapFactory.decodeStream(is);

	            } catch (Exception e) {
	                System.out.println("Failure to get image (" + img_name +") from assets. " + e);
	            }
	            _listItemView.logoimg.setImageBitmap(image);   

	        }
			
			_listItemView.title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
			_listItemView.title.getPaint().setFakeBoldText(true);
	        _listItemView.title.setText(Html.fromHtml( (String) listItemMyQuestionVO  
	                .getTitle()));   
	        if(!listItemMyQuestionVO.isRecommends())
	        _listItemView.info.setText(Html.fromHtml((String) listItemMyQuestionVO.getInfo()));
	        if(listItemMyQuestionVO.isRecommends()){
	        	_listItemView.txtPrice.setText(listItemMyQuestionVO.getProgress());
	        	_listItemView.progressTxt.setVisibility(View.INVISIBLE);
	        	_listItemView.progress.setVisibility(View.INVISIBLE);
	        }else{
	        	_listItemView.txtPrice.setVisibility(View.INVISIBLE);
	        	_listItemView.progressTxt.setText(listItemMyQuestionVO.getProgress());
	        }
	        	
	        
	        
	       if(listItemMyQuestionVO.isFinish()){
	    	   _listItemView.progress.setVisibility(View.GONE); 
	       }else{
	    	   if(_listItemView.progressTxt.getText().equals(""))
	    		   _listItemView.progress.setVisibility(View.GONE); 
	    	   else{
	    		   _listItemView.progress.setMax(listItemMyQuestionVO.getProgressMax());
	    		   _listItemView.progress.setProgress(listItemMyQuestionVO.getProgressCount());
	    	   }
	       }
			//end

			return res;
		}

		@Override
		public void configurePinnedHeader(View header, int position, int alpha) {
			TextView lSectionHeader = (TextView) header;
			lSectionHeader
					.setText(getSections()[getSectionForPosition(position)]);
			lSectionHeader.setBackgroundColor(alpha << 24 | (0xbbffbb));
			lSectionHeader.setTextColor(alpha << 24 | (0x000000));
		}

		@Override
		public int getPositionForSection(int section) {
			if (section < 0)
				section = 0;
			if (section >= all.size())
				section = all.size() - 1;
			int c = 0;
			for (int i = 0; i < all.size(); i++) {
				if (section == i) {
					return c;
				}
				c += all.get(i).second.size();
			}
			return 0;
		}

		@Override
		public int getSectionForPosition(int position) {
			int c = 0;
			for (int i = 0; i < all.size(); i++) {
				if (position >= c && position < c + all.get(i).second.size()) {
					return i;
				}
				c += all.get(i).second.size();
			}
			return -1;
		}

		@Override
		public String[] getSections() {
			String[] res = new String[all.size()];
			for (int i = 0; i < all.size(); i++) {
				res[i] = all.get(i).first;
			}
			return res;
		}

	}
}