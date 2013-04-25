package com.arcadiaprep.app.arca.service;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.arcadiaprep.app.arca.ArcadiaprepActivity;
import com.arcadiaprep.app.arca.BookMarkActivity;
import com.arcadiaprep.app.arca.DiscussionActivity;
import com.arcadiaprep.app.arca.ExamStatActivity;
import com.arcadiaprep.app.arca.InformationActivity;
import com.arcadiaprep.app.arca.ProblemSetIntroductionActivity;
import com.arcadiaprep.app.arca.R;
import com.arcadiaprep.app.arca.SearchNotesActivity;
import com.arcadiaprep.app.arca.constants.ConstantSystem;
import com.arcadiaprep.app.arca.ui.list.ListExamStatAdapter;
import com.arcadiaprep.app.arca.ui.list.ListViewQuestionAdapter;
import com.arcadiaprep.app.arca.ui.list.ListViewRecommendationsAdapter;
import com.arcadiaprep.app.arca.util.HTTPUtils;
import com.arcadiaprep.app.arca.util.ListHeightUtils;
import com.arcadiaprep.app.arca.vo.ExamSection;
import com.arcadiaprep.app.arca.vo.ListItemMyQuestionVO;
import com.arcadiaprep.app.arca.vo.ModuleInfoVO;
import com.arcadiaprep.app.market.ItemViewActivity;
import com.arcadiaprep.app.market.ItemsListViewActivity;
import com.arcadiaprep.app.util.Item;
import com.foound.amazinglistview.demo.Data;

/**
 * Register listener for all widgets which from main.xml
 * 
 * @author lala
 * integrate with Samsung market by Sharon
 */
public class MainListenerService {
	
	private static MainListenerService service;
	private static List<Button> categoryBarButtons;
	
	public static MainListenerService getInstance(){
		if(service==null)service=new MainListenerService();
		return service;
	}
	
	private MainListenerService(){}
	
	
	public  void registerCategoryBar(final Context context, final List<Button> buttons,final ListView listView) {
		categoryBarButtons = buttons;
		if(buttons==null||buttons.size()==0)return;
		for(int i=0;i<buttons.size();i++){
			//register onClick event,for color change part
			final Button b = buttons.get(i);
			b.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					//reset color
					for (int j = 0; j < categoryBarButtons.size(); j++) {
						Button button = categoryBarButtons.get(j);
						
						if (buttons.size() == 1) {
							if(button==b){
								button.setBackgroundDrawable(context.getResources()
										.getDrawable(R.drawable.main_category_bar_select));
							}else
								button.setBackgroundDrawable(context.getResources()
									.getDrawable(R.drawable.main_category_bar));
						} else if (buttons.size() == 2) {
							if (j == 0) {
								if(button==b)
									button.setBackgroundDrawable(context.getResources()
											.getDrawable(R.drawable.main_category_bar_left_select));
								else
									button.setBackgroundDrawable(context.getResources()
										.getDrawable(R.drawable.main_category_bar_left));
							} else {
								if(button==b)
									button.setBackgroundDrawable(context.getResources()
											.getDrawable(R.drawable.main_category_bar_right_select));
								else
									button.setBackgroundDrawable(context.getResources()
										.getDrawable(R.drawable.main_category_bar_right));
							}
						} else {
							if (j == 0) {
								if(button==b)
									button.setBackgroundDrawable(context.getResources()
											.getDrawable(R.drawable.main_category_bar_left_select));
								else
									button.setBackgroundDrawable(context.getResources()
										.getDrawable(R.drawable.main_category_bar_left));
							} else if (j == buttons.size()-1) {
								if(button==b)
									button.setBackgroundDrawable(context.getResources()
											.getDrawable(R.drawable.main_category_bar_right_select));
								else
									button.setBackgroundDrawable(context.getResources()
										.getDrawable(R.drawable.main_category_bar_right));
							} else {
								if(button==b)
									button.setBackgroundDrawable(context.getResources()
											.getDrawable(R.drawable.main_category_bar_middle_select));
								else
									button.setBackgroundDrawable(context.getResources()
										.getDrawable(R.drawable.main_category_bar_middle));
							} 
						}
					}
					//refresh data for list
					ArcadiaprepActivity._currSectionName = b.getText().toString();
					List<ExamSection> sectionList = MainDataService.findCategoryBars(context);
					for (ExamSection section : sectionList)
					{
					    if (section.getSecName().equals(ArcadiaprepActivity._currSectionName))
					        ArcadiaprepActivity._currSection = section.getSecNameShort();
					    
					    if(ArcadiaprepActivity._currSectionName.equals(section.getSecName()))break;
		        		else if(ArcadiaprepActivity._currSectionName.equals(section.getSecNameShort())){
		        			ArcadiaprepActivity._currSectionName = section.getSecName();
		        			break;
		        		}
					}
                    List<Item> recommendations = ListViewRecommendationsAdapter
                            .findRecommendations(context,
                                    ArcadiaprepActivity._currSection);
                    ArcadiaprepActivity._listViewRecommendationAdapter
                            .setList(recommendations);
                    ArcadiaprepActivity._listViewRecommendationAdapter
                            .notifyDataSetChanged();
                    
					
					int i ;
					if(context.getString(R.string.device_screen).equals("xhdpi")||context.getString(R.string.device_screen).equals("hdpi")){//composite myquestionset with recommendations together
						
						ListItemMyQuestionVO[][] voss = new ListItemMyQuestionVO[2][100];
						
						List<ListItemMyQuestionVO> questions = MainDataService.findMyQuestionSets(context, ArcadiaprepActivity._currSectionName);
						List<ListItemMyQuestionVO> questions2 = MainDataService.convertRecommendationsToMyQuestionSets(recommendations);
						
						for(int j=0;j<questions.size();j++){
							if(questions.get(j)!=null)
							voss[0][j] = questions.get(j);
						}
							
						
						for(int j=0;j<questions2.size();j++){
							if(questions2.get(j)!=null)
							voss[1][j] = questions2.get(j);
						}
							
						
						
						Data.ListItemMyQuestionVOss = voss;
						
						((ArcadiaprepActivity.SectionComposerAdapter)listView.getAdapter()).all = Data.getAllData();
						((ArcadiaprepActivity.SectionComposerAdapter)listView.getAdapter()).notifyDataSetChanged();
						
					}else{
						((ListViewQuestionAdapter)listView.getAdapter()).setListItems(MainDataService.findMyQuestionSets(context, ArcadiaprepActivity._currSectionName));
						((ListViewQuestionAdapter)listView.getAdapter()).notifyDataSetChanged();
					}
					
					
				}
			});
		}
		
	}

	public void registerLoginoutListener(final Context context,Button btnLoginout){
		btnLoginout.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(context, "login/out", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	public void registerFunctionIconListener(final Context context,ImageView... imgs){
		for(ImageView img:imgs){
			if(img.getId()==R.id.main_img_btn_icon_info){//information
				img.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						Intent i = new Intent(context,InformationActivity.class);
						context.startActivity(i);
					}
				});
			}else if(img.getId()==R.id.main_img_btn_icon_perfdata){//perf
				img.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						Intent i = new Intent(context,ExamStatActivity.class);
						context.startActivity(i);
					}
				});
			}else if(img.getId()==R.id.main_img_btn_icon_shopcart){//shopcart
		        ModuleInfoVO module = SystemService.parseModuelInfo(context);
		        if (module.getMarketGroupId() == null || module.getMarketGroupId().equalsIgnoreCase("0")) {
		        	img.setEnabled(false);
		        	img.setAlpha(50);
		        }
		        else 
	             img.setOnClickListener(new View.OnClickListener() {
	                    public void onClick(View v) {
	                    	
							if(!HTTPUtils.isConnectInternet(context)) {
								((ArcadiaprepActivity)context).showNoNetErrorDialog();
								return;
							}
	                    	
	                        Intent i = new Intent(context,ItemsListViewActivity.class);
	                        context.startActivity(i);
	                    }
	                });
				
			}else if(img.getId()==R.id.main_img_btn_icon_interaction){//interaction
				
				img.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						
						if(!HTTPUtils.isConnectInternet(context))((ArcadiaprepActivity)context).showNoNetErrorDialog();
						else{
							Intent i = new Intent(context,DiscussionActivity.class);
							context.startActivity(i);
						}
						
					}
				});
				
			}else if(img.getId()==R.id.main_img_btn_icon_bookmark){//bookmark
				img.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						Intent i = new Intent(context,BookMarkActivity.class);
						context.startActivity(i);
					}
				});
				
				
				
			}else if(img.getId()==R.id.main_img_btn_icon_email){//email
				
				img.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						if(!HTTPUtils.isConnectInternet(context)) {
							((ArcadiaprepActivity)context).showNoNetErrorDialog();
							return;
						}
						SystemService.sendMail(context, ConstantSystem.application_mail_address, ConstantSystem.application_mail_subject, ConstantSystem.application_mail_content);
					}
				});
				
			}else if(img.getId()==R.id.main_img_btn_icon_searchnotes){//searchnotes
                
			    img.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent i = new Intent(context,SearchNotesActivity.class);
                        context.startActivity(i);
                    }
                });
                
            }
		}
		
	}

	public void registerMyQuestionSetsListener(
			final Context context, ListView listViewQuestion) {
		
			listViewQuestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				
				ListItemMyQuestionVO vo = (ListItemMyQuestionVO)arg0.getAdapter().getItem(position);
				if(vo.isRecommends()){
					Intent intent = new Intent(context, ItemViewActivity.class);
					intent.putExtra(ItemViewActivity.EXTRA_ITEMID, vo.getRecommendId());
					intent.putExtra(ItemViewActivity.EXTRA_PACKAGESETNAME, vo.getPackageSetName());
					((Activity)context).startActivityForResult(intent, ItemViewActivity.REQUEST_CODE);
				}else{
					Intent intent = new Intent(context,ProblemSetIntroductionActivity.class);
					intent.putExtra("problemset", vo);
					context.startActivity(intent);
					((Activity)context).finish();
				}
			}
		});
	}

	public void registerRecommendationsListener(final Context context,
			ListView listView) {
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				
				Item myItem = (Item) arg0.getAdapter().getItem(position);
				Intent intent = new Intent(context, ItemViewActivity.class);
				intent.putExtra(ItemViewActivity.EXTRA_ITEMID, myItem.getId());
				intent.putExtra(ItemViewActivity.EXTRA_PACKAGESETNAME, myItem.getPackageSetName());
				((Activity)context).startActivityForResult(intent, ItemViewActivity.REQUEST_CODE);
			}
		});
	}
	
	public void registerVisitMarketplaceListener(final Context context,  View view)
    {
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
				if(!HTTPUtils.isConnectInternet(context)) {
					((ArcadiaprepActivity)context).showNoNetErrorDialog();
					return;
				}
                Intent i = new Intent(context, ItemsListViewActivity.class);
                context.startActivity(i);
            }
        });
        
    }
	
	public String category="";

	public void registerCategoryBarForExamStat(
			final Context context, final List<Button> buttons,final List<Button> typebuttons,
			final ListView listView) {
		
		if(buttons==null||buttons.size()==0)return;
		for(int i=0;i<buttons.size();i++){
			//register onClick event,for color change part
			final Button b = buttons.get(i);
			b.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					//reset color
					for (int j = 0; j < buttons.size(); j++) {
						Button button = buttons.get(j);
						
						if (buttons.size() == 1) {
							if(button==b){
								button.setBackgroundDrawable(context.getResources()
										.getDrawable(R.drawable.main_category_bar_select));
							}else
								button.setBackgroundDrawable(context.getResources()
									.getDrawable(R.drawable.main_category_bar));
						} else if (buttons.size() == 2) {
							if (j == 0) {
								if(button==b)
									button.setBackgroundDrawable(context.getResources()
											.getDrawable(R.drawable.main_category_bar_left_select));
								else
									button.setBackgroundDrawable(context.getResources()
										.getDrawable(R.drawable.main_category_bar_left));
							} else {
								if(button==b)
									button.setBackgroundDrawable(context.getResources()
											.getDrawable(R.drawable.main_category_bar_right_select));
								else
									button.setBackgroundDrawable(context.getResources()
										.getDrawable(R.drawable.main_category_bar_right));
							}
						} else {
							if (j == 0) {
								if(button==b)
									button.setBackgroundDrawable(context.getResources()
											.getDrawable(R.drawable.main_category_bar_left_select));
								else
									button.setBackgroundDrawable(context.getResources()
										.getDrawable(R.drawable.main_category_bar_left));
							} else if (j == buttons.size()-1) {
								if(button==b)
									button.setBackgroundDrawable(context.getResources()
											.getDrawable(R.drawable.main_category_bar_right_select));
								else
									button.setBackgroundDrawable(context.getResources()
										.getDrawable(R.drawable.main_category_bar_right));
							} else {
								if(button==b)
									button.setBackgroundDrawable(context.getResources()
											.getDrawable(R.drawable.main_category_bar_middle_select));
								else
									button.setBackgroundDrawable(context.getResources()
										.getDrawable(R.drawable.main_category_bar_middle));
							} 
						}
					}
					category = b.getText().toString();
					//refresh data for list
					((ListExamStatAdapter)listView.getAdapter()).setListItems(QuestionViewService.getExamStatsBySectionAndType(context, b.getText().toString(), 1));
					((ListExamStatAdapter)listView.getAdapter()).notifyDataSetChanged();
//					ListHeightUtils.setListViewHeightBasedOnChildren(listView);
					typebuttons.get(0).performClick();
				}
			});
		}
		
		for(int i=0;i<typebuttons.size();i++){
			//register onClick event,for color change part
			final Button b = typebuttons.get(i);
			b.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					//reset color
					for (int j = 0; j < typebuttons.size(); j++) {
						Button button = typebuttons.get(j);
						
						if (typebuttons.size() == 1) {
							if(button==b){
								button.setBackgroundDrawable(context.getResources()
										.getDrawable(R.drawable.main_category_bar_select));
							}else
								button.setBackgroundDrawable(context.getResources()
									.getDrawable(R.drawable.main_category_bar));
						} else if (typebuttons.size() == 2) {
							if (j == 0) {
								if(button==b)
									button.setBackgroundDrawable(context.getResources()
											.getDrawable(R.drawable.main_category_bar_left_select));
								else
									button.setBackgroundDrawable(context.getResources()
										.getDrawable(R.drawable.main_category_bar_left));
							} else {
								if(button==b)
									button.setBackgroundDrawable(context.getResources()
											.getDrawable(R.drawable.main_category_bar_right_select));
								else
									button.setBackgroundDrawable(context.getResources()
										.getDrawable(R.drawable.main_category_bar_right));
							}
						} else {
							if (j == 0) {
								if(button==b)
									button.setBackgroundDrawable(context.getResources()
											.getDrawable(R.drawable.main_category_bar_left_select));
								else
									button.setBackgroundDrawable(context.getResources()
										.getDrawable(R.drawable.main_category_bar_left));
							} else if (j == typebuttons.size()-1) {
								if(button==b)
									button.setBackgroundDrawable(context.getResources()
											.getDrawable(R.drawable.main_category_bar_right_select));
								else
									button.setBackgroundDrawable(context.getResources()
										.getDrawable(R.drawable.main_category_bar_right));
							} else {
								if(button==b)
									button.setBackgroundDrawable(context.getResources()
											.getDrawable(R.drawable.main_category_bar_middle_select));
								else
									button.setBackgroundDrawable(context.getResources()
										.getDrawable(R.drawable.main_category_bar_middle));
							} 
						}
					}
					//refresh data for list
					int type=1;
					if(b.getText().toString().contains("Last"))
						type=1;
					else if(b.getText().toString().contains("Average"))
						type=2;
					else if(b.getText().toString().contains("Best"))
						type=3;
					
					((ListExamStatAdapter)listView.getAdapter()).setListItems(QuestionViewService.getExamStatsBySectionAndType(context, category, type));
					((ListExamStatAdapter)listView.getAdapter()).notifyDataSetChanged();
					ListHeightUtils.setListViewHeightBasedOnChildren(listView);
				}
			});
		}
		
	}
}
