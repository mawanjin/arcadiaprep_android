package com.arcadiaprep.app.arca;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.arcadiaprep.app.arca.constants.ConstantQuestion;
import com.arcadiaprep.app.arca.service.MainDataService;
import com.arcadiaprep.app.arca.service.QuestionViewService;
import com.arcadiaprep.app.arca.service.SystemService;
import com.arcadiaprep.app.arca.ui.list.ListExamReviewAdapter;
import com.arcadiaprep.app.arca.util.TimeUtil;
import com.arcadiaprep.app.arca.vo.ExamResultInfo;
import com.arcadiaprep.app.market.ItemsListViewActivity;
import com.arcadiaprep.app.market.MyApplication;
import com.arcadiaprep.app.util.Item;
import com.samsungapps.plasma.ItemInformation;
import com.samsungapps.plasma.Plasma;
import com.samsungapps.plasma.PlasmaListener;
import com.samsungapps.plasma.PurchaseTicket;
import com.samsungapps.plasma.PurchasedItemInformation;

public class ExamReviewActivity extends Activity
implements PlasmaListener {
	
	@Override
	public void onBackPressed() {
		Intent i =  new Intent(this,ArcadiaprepActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		startActivity(i);
		finish();
	}

	Button btnMenu;
	ListView list;
	TextView txtTitle,score,totalTime,averageTime;
	ExamResultInfo examRsInfo;
	WebView webview;
    private Plasma _plasma;
    private static final String __CR_LF = "\n";
	private String LOG_TAG = "ExamReviewActivity";
	
	RelativeLayout webviewContainer;
	private Button btnContinue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		SystemService.setOrientation(this);
		setContentView(R.layout.exam_review);
		
        _plasma = new Plasma(MyApplication._ITEM_GROUP_ID, this);
        _plasma.setPlasmaListener(this);
        if (MyApplication._developer == true)
        	_plasma.setDeveloperFlag(1);
		
		btnMenu = (Button) findViewById(R.id.btnMenu);
		txtTitle = (TextView) findViewById(R.id.txtTitle);
		 
		examRsInfo = QuestionViewService.getExamResultInfoByExAppID(this, getIntent().getExtras().getInt("ex_app_id"));
		
		txtTitle.setText(examRsInfo.getExAppName());
		
		list = (ListView) findViewById(R.id.list_exam_review);
		
		list.setAdapter(new ListExamReviewAdapter(this, examRsInfo.getQuestions()));
		if(!getString(R.string.device_screen).equals("xhdpi")&&!getString(R.string.device_screen).equals("hdpi"))
		list.setLayoutParams(new LayoutParams(SystemService.getScreenWith(getWindowManager())/2, LayoutParams.WRAP_CONTENT));
		
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Intent i =new Intent(ExamReviewActivity.this,QuestionViewActivity.class);
				i.putExtra("ex_app_id", getIntent().getExtras().getInt("ex_app_id"));
				i.putExtra("VIEW_TYPE", ConstantQuestion.QUESTION_VIEW_TYPE_REVIEW);
				i.putExtra("pageno", position+1);
				i.putExtra("ex_app_name", examRsInfo.getExAppName());
				
				
				startActivity(i);
			}
		});
		 
		score = (TextView) findViewById(R.id.txt_score);
		totalTime = (TextView) findViewById(R.id.txt_totalTime);
		averageTime = (TextView) findViewById(R.id.txt_averageTime);
		
		score.setText(examRsInfo.getScore()+"/"+examRsInfo.getQuestions().size());
		
		totalTime.setText(TimeUtil.getTime(examRsInfo.getEndTime()));
		
		averageTime.setText(TimeUtil.getTime(examRsInfo.getEndTime()/examRsInfo.getQuestions().size()));
		btnContinue = (Button) findViewById(R.id.btnContinue);
		webviewContainer = (RelativeLayout) findViewById(R.id.webview_container);
		final View mainContainer = findViewById(R.id.main_container);

		if(getString(R.string.device_screen).equals("xhdpi")||getString(R.string.device_screen).equals("hdpi")){
			
			ExamReviewWebViewLayout v = ExamReviewWebViewLayout.getInstance(this, new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(ExamReviewActivity.this,ArcadiaprepActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
					finish();
				}
			});
			webviewContainer.addView(v);
			webviewContainer.setVisibility(View.GONE);
			btnContinue.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					webviewContainer.setVisibility(View.VISIBLE);
					mainContainer.setVisibility(View.GONE);
					webview.loadDataWithBaseURL("file:///android_asset/",MainDataService.findProblemSetIntroductionForExamResultByExAppId(ExamReviewActivity.this, getIntent().getExtras().getInt("ex_app_id")), "text/html", "utf-8",null);
				}
			});
			webview = v.webviewWidget;
			
		}else{
			if(webviewContainer!=null)
			webviewContainer.setVisibility(View.GONE);
			//find postSession
			webview = (WebView) findViewById(R.id.webview);
			
		}
		
		webview.getSettings().setJavaScriptEnabled(true);
		
		webview.loadDataWithBaseURL("file:///android_asset/",MainDataService.findProblemSetIntroductionForExamResultByExAppId(this, getIntent().getExtras().getInt("ex_app_id")), "text/html", "utf-8",null);
		
		
		webview.setWebViewClient(new WebViewClient()
        {
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                System.out.println("*** Arcadia url *** " + url);
                boolean gotoMarket = false;
                int questionIndex = url.lastIndexOf("?");
                if (questionIndex == -1)
                {
                    if (url.indexOf("marketplace") != -1)
                        gotoMarket = true;
                }
                else
                {
                    String packageName = url.substring(questionIndex + 1)
                            .trim();
                    int slashIndex = packageName.indexOf('\\');
                    if (slashIndex != -1)
                        packageName = packageName.substring(0, slashIndex);
                    if (MyApplication._purchasedPkgNames.contains(packageName))
                    {
                        String purchasedMsg = "This item is already purchased.";
                        Toast toast = Toast.makeText(ExamReviewActivity.this,
                                purchasedMsg, Toast.LENGTH_LONG);
                        toast.show();
                        return true;
                    }

                    Item clickItem = MyApplication._itemMap.get(packageName);
                    if (clickItem != null)
                    {
                        String itemId = clickItem.getId();
                        if (itemId != null)
                        {
                            _plasma.requestPurchaseItem(
                                    MyApplication._transactionId++, itemId);
                            return true;
                        }
                        else
                        {
                            // market id is null: either it is not on the market
                            // or we have not yet visited market
                            gotoMarket = true;
                        }
                    }
                    else
                        Log.d("ItemView", "Package: " + packageName
                                + " not in the assets");
                }
                
                if (gotoMarket)
                {
                    Intent i = new Intent(ExamReviewActivity.this, ItemsListViewActivity.class);
                    ExamReviewActivity.this.startActivity(i);
                    return true;
                }

                return false;
            }
        });
		
		registerListener();
				
	}

	private void registerListener() {
		btnMenu.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ExamReviewActivity.this,ArcadiaprepActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				finish();
			}
		});
	}

    @Override
    public void onItemInformationListReceived(int arg0, int arg1,
            ArrayList<ItemInformation> arg2)
    {
    }

    @Override
    public void onPurchaseItemFinished(int transactionId, int statusCode,
            PurchasedItemInformation purchasedItemInformation)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        switch (statusCode)
        {
            case Plasma.STATUS_CODE_SUCCESS:
                MyApplication._purchasedPkgNames.add(purchasedItemInformation
                        .getReserved1().trim());
                Item item = MyApplication._itemMap.get(purchasedItemInformation
                        .getReserved1().trim());
                MyApplication._purchasedProblemSetIds.addAll(item
                        .getProblemSets());
                MyApplication.getApp().saveItemsList();
                Log.d("ItemView", "****Purchased***** "
                        + purchasedItemInformation.getItemId());
                ArcadiaprepActivity._listViewRecommendationAdapter
                        .removePurchasedItems();
                ArcadiaprepActivity._listViewRecommendationAdapter
                        .notifyDataSetChanged();

                StringBuilder purchasedItemInformationStringBuilder = new StringBuilder();
                purchasedItemInformationStringBuilder.append("Item name: ");
                purchasedItemInformationStringBuilder
                        .append(purchasedItemInformation.getItemName());
                purchasedItemInformationStringBuilder.append(__CR_LF);
                purchasedItemInformationStringBuilder.append("Item ID: ");
                purchasedItemInformationStringBuilder
                        .append(purchasedItemInformation.getItemId());
                purchasedItemInformationStringBuilder.append(__CR_LF);
                purchasedItemInformationStringBuilder.append("Payment ID: ");
                purchasedItemInformationStringBuilder
                        .append(purchasedItemInformation.getPaymentId());
                purchasedItemInformationStringBuilder.append(__CR_LF);
                purchasedItemInformationStringBuilder.append("Purchase date: ");
                purchasedItemInformationStringBuilder
                        .append(purchasedItemInformation.getPurchaseDate());

                purchasedItemInformationStringBuilder.append(__CR_LF);
                purchasedItemInformationStringBuilder.append("Price: ");
                purchasedItemInformationStringBuilder
                        .append(ItemsListViewActivity
                                .getPriceStringWithCurrencyUnit(purchasedItemInformation));

                alertDialogBuilder.setTitle("Purchase information");
                alertDialogBuilder
                        .setMessage(purchasedItemInformationStringBuilder
                                .toString());
                alertDialogBuilder.show();
                break;
            case Plasma.STATUS_CODE_CANCEL:
                break;
            default:
                String errorMessage = "Failed to purchase the item";
                ItemsListViewActivity.showErrorDialog(this, statusCode,
                        errorMessage);
                break;
        }
    }

    @Override
    public void onPurchaseItemInitialized(int arg0, int arg1,
            PurchaseTicket arg2)
    {
    }

    @Override
    public void onPurchasedItemInformationListReceived(int arg0, int arg1,
            ArrayList<PurchasedItemInformation> arg2)
    {
    }
    
    @Override
   	public void onConfigurationChanged(Configuration newConfig) {
   		
   		super.onConfigurationChanged(newConfig);
//   		SystemService.setOrientation(this);
   	}

}
