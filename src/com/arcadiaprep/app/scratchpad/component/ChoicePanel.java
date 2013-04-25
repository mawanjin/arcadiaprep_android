package com.arcadiaprep.app.scratchpad.component;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.arcadiaprep.app.arca.QuestionViewActivity;
import com.arcadiaprep.app.arca.R;
import com.arcadiaprep.app.arca.mo.Questions;
import com.arcadiaprep.app.arca.ui.dialog.QuestionDialogFragment;
import com.arcadiaprep.app.arca.vo.ExamResultInfo;

@SuppressLint("NewApi")
public class ChoicePanel {
	
	private static View view;
	public static int currentPageNO;
	private static ExamResultInfo examResultInfo;
	private static List<Questions> questions;
	private static MainWebView mWebView;
	private static int hintCount=0;
	private static int currentHintIndex=1;
	private static Context context ;
	private static Activity activity;
	private static Dialog dialog;
	private static ImageButton btnNxt;
	static Button btnFinish;
	static Button btnHint=null;
	
	
	public static boolean enable;

	
	public static Button bA,bB,bC,bD,bE,bF;
	private static List<Button> choiceBtns = new ArrayList<Button>();
	
	/**
	 * Adjust the number of choice button along with the count of question's choice options.
	 * @param count
	 */
	public static void adjustButtons(int count){
		
		if (count == 0) {
			count = 5;
		}
		for(int i=0;i<choiceBtns.size();i++){
				if(i+1<=count)choiceBtns.get(i).setVisibility(View.VISIBLE);
				else {
					choiceBtns.get(i).setVisibility(View.VISIBLE);
					choiceBtns.get(i).setVisibility(View.GONE);
				}
		}
		
		if(btnFinish!=null){
			btnFinish.setText("Confirm");
			for(Button b : choiceBtns){
				b.setEnabled(true);
			}
		}
			
	}
	
	public static View getInstance(Activity _activity,Context context,ImageButton btnNxt,ExamResultInfo examResultInfo,List<Questions> questions,MainWebView mWebView,int currentPageNO){
			activity = _activity;
			ChoicePanel.context = context;
			ChoicePanel.btnNxt = btnNxt;
			ChoicePanel.examResultInfo = examResultInfo;
			ChoicePanel.questions = questions;
			ChoicePanel.mWebView = mWebView;
			ChoicePanel.currentPageNO = currentPageNO;
			view = LayoutInflater.from(context.getApplicationContext()).inflate(
					R.layout.choice_panel, null);
			init();
			if(dialog==null)
				initDialog();
		
		return view;
	}
	
	private static Button whichIsChoice=bA;

	private static void changeBGForPress(Button which){
		
		if(whichIsChoice!=null){
			if(examResultInfo.getQuestions().get(currentPageNO-1).getCorrectChoice().length()>1){
				
			}else
				whichIsChoice.setBackgroundDrawable(view.getContext().getResources().getDrawable(R.drawable.answerbtn));
		}
		whichIsChoice = which;
		which.setBackgroundDrawable(view.getContext().getResources().getDrawable(R.drawable.answerbtnselect));

	}
	
	public static void resetBGForPress(Button which){
	    if (view == null)
	        return; // this can happen if we come here from initial note search
		Drawable d = view.getContext().getResources().getDrawable(R.drawable.answerbtn);
		
		if(which!=null)
			which.setBackgroundDrawable(d);
		else {
			bA.setBackgroundDrawable(d);
			bB.setBackgroundDrawable(d);
			bC.setBackgroundDrawable(d);
			bD.setBackgroundDrawable(d);
			bE.setBackgroundDrawable(d);
			bF.setBackgroundDrawable(d);

		}
		currentHintIndex=1;
	}
	
	public static void setPreviousSelect(String choice){
		Button which = null;
		String _choice = "";
		if(!choice.equals("-1")&&!choice.equals("")){         
			char[] qs1 = choice.toCharArray();
			for(int i=0;i<qs1.length;i++){
				_choice = ""+qs1[i];
				if (choiceBtns.size() != 0)  //for initial note search
				    whichIsChoice = choiceBtns.get(Integer.parseInt(_choice));
				else 
				    whichIsChoice = null;
				if(whichIsChoice!=null) which=whichIsChoice;
				if(which!=null)
					which.setBackgroundDrawable(view.getContext().getResources().getDrawable(R.drawable.answerbtnselect));
			}
			currentHintIndex=1;
		}
		
	}
	
	private static void init(){
		if(examResultInfo.getQuestions().size()==0)return;
		bA = (Button) view.findViewById(R.id.btnA);
		bA.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				String choice = examResultInfo.getQuestions().get(currentPageNO-1).getChoice();
				
				if(choice.contains("0")){
					mWebView.loadUrl("javascript:cancelchoice(0)");
					if(choice.equals("0"))
						examResultInfo.getQuestions().get(currentPageNO-1).setChoice("-1");
					else
						examResultInfo.getQuestions().get(currentPageNO-1).setChoice(choice.replace("0", ""));
					
					resetBGForPress(bA);
				}else{
					
					if(choice.equals("-1")||examResultInfo.getQuestions().get(currentPageNO-1).getCorrectChoice().length()==1){
						mWebView.loadUrl("javascript:cancelchoice("+examResultInfo.getQuestions().get(currentPageNO-1).getChoice()+")");
						mWebView.clearChoice();
						examResultInfo.getQuestions().get(currentPageNO-1).setChoice("0");
						
					}
					else
						examResultInfo.getQuestions().get(currentPageNO-1).setChoice(choice+"0");
					
					mWebView.choice('0');
					changeBGForPress(bA);
				}
				
			}
		});
		
		 bB = (Button) view.findViewById(R.id.btnB);
		bB.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				String choice = examResultInfo.getQuestions().get(currentPageNO-1).getChoice();
				if(!choice.contains("-1")&&choice.contains("1")){
					mWebView.loadUrl("javascript:cancelchoice(1)");
					
					if(choice.equals("1"))
						examResultInfo.getQuestions().get(currentPageNO-1).setChoice("-1");
					else
						examResultInfo.getQuestions().get(currentPageNO-1).setChoice(choice.replace("1", ""));
					
					resetBGForPress(bB);
				}else{
					
					if(choice.equals("-1")||examResultInfo.getQuestions().get(currentPageNO-1).getCorrectChoice().length()==1){
						mWebView.loadUrl("javascript:cancelchoice("+examResultInfo.getQuestions().get(currentPageNO-1).getChoice()+")");
						mWebView.clearChoice();
						examResultInfo.getQuestions().get(currentPageNO-1).setChoice("1");
						
					}
					else
						examResultInfo.getQuestions().get(currentPageNO-1).setChoice(choice+"1");
					
					mWebView.choice('1');
					changeBGForPress(bB);
				}
				
					
				
			}
		});
		
		bC = (Button) view.findViewById(R.id.btnC);
		bC.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				String choice = examResultInfo.getQuestions().get(currentPageNO-1).getChoice();
				if(choice.contains("2")){
					mWebView.loadUrl("javascript:cancelchoice(2)");
					if(choice.equals("2"))
						examResultInfo.getQuestions().get(currentPageNO-1).setChoice("-1");
					else
						examResultInfo.getQuestions().get(currentPageNO-1).setChoice(choice.replace("2", ""));
					resetBGForPress(bC);
				}else{
					
					if(choice.equals("-1")||examResultInfo.getQuestions().get(currentPageNO-1).getCorrectChoice().length()==1){
						mWebView.loadUrl("javascript:cancelchoice("+examResultInfo.getQuestions().get(currentPageNO-1).getChoice()+")");
						mWebView.clearChoice();
						examResultInfo.getQuestions().get(currentPageNO-1).setChoice("2");
					}
						
					else
						examResultInfo.getQuestions().get(currentPageNO-1).setChoice(choice+"2");
					
					mWebView.choice('2');
					changeBGForPress(bC);
				}
				
			}
		});
		
		bD = (Button) view.findViewById(R.id.btnD);
		bD.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				String choice = examResultInfo.getQuestions().get(currentPageNO-1).getChoice();
				if(choice.contains("3")){
					mWebView.loadUrl("javascript:cancelchoice(3)");
					if(choice.equals("3"))
						examResultInfo.getQuestions().get(currentPageNO-1).setChoice("-1");
					else
						examResultInfo.getQuestions().get(currentPageNO-1).setChoice(choice.replace("3", ""));
					resetBGForPress(bD);
				}else{
					
					if(choice.equals("-1")||examResultInfo.getQuestions().get(currentPageNO-1).getCorrectChoice().length()==1){
						mWebView.loadUrl("javascript:cancelchoice("+examResultInfo.getQuestions().get(currentPageNO-1).getChoice()+")");
						mWebView.clearChoice();
						examResultInfo.getQuestions().get(currentPageNO-1).setChoice("3");
						
					}
						
					else
						examResultInfo.getQuestions().get(currentPageNO-1).setChoice(choice+"3");
					
					mWebView.choice('3');
					changeBGForPress(bD);
				}
				
			}
		});
		
		bE = (Button) view.findViewById(R.id.btnE);
		bE.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				String choice = examResultInfo.getQuestions().get(currentPageNO-1).getChoice();
				if(choice.contains("4")){
					mWebView.loadUrl("javascript:cancelchoice(4)");
					if(choice.equals("4"))
						examResultInfo.getQuestions().get(currentPageNO-1).setChoice("-1");
					else
						examResultInfo.getQuestions().get(currentPageNO-1).setChoice(choice.replace("4", ""));
					resetBGForPress(bE);
				}else{
					
					if(choice.equals("-1")||examResultInfo.getQuestions().get(currentPageNO-1).getCorrectChoice().length()==1){
						mWebView.loadUrl("javascript:cancelchoice("+examResultInfo.getQuestions().get(currentPageNO-1).getChoice()+")");
						mWebView.clearChoice();
						examResultInfo.getQuestions().get(currentPageNO-1).setChoice("4");
						
					}
						
					else
						examResultInfo.getQuestions().get(currentPageNO-1).setChoice(choice+"4");
					
					mWebView.choice('4');
					changeBGForPress(bE);
				}
				
			}
			
		});
		
		bF = (Button) view.findViewById(R.id.btnF);
		bF.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				String choice = examResultInfo.getQuestions().get(currentPageNO-1).getChoice();
				if(choice.contains("5")){
					mWebView.loadUrl("javascript:cancelchoice(5)");
					if(choice.equals("5"))
						examResultInfo.getQuestions().get(currentPageNO-1).setChoice("-1");
					else
						examResultInfo.getQuestions().get(currentPageNO-1).setChoice(choice.replace("5", ""));
					resetBGForPress(bF);
					
				}else{
					
					if(choice.equals("-1")||examResultInfo.getQuestions().get(currentPageNO-1).getCorrectChoice().length()==1){
						mWebView.loadUrl("javascript:cancelchoice("+examResultInfo.getQuestions().get(currentPageNO-1).getChoice()+")");
						examResultInfo.getQuestions().get(currentPageNO-1).setChoice("5");
						mWebView.clearChoice();
					}else
						examResultInfo.getQuestions().get(currentPageNO-1).setChoice(choice+"5");
					
					mWebView.choice('5');
					changeBGForPress(bF);
				}
				
			}
			
		});
		
		
		btnFinish = (Button) view.findViewById(R.id.btnFinish);
		btnFinish.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(btnFinish.getText().equals("Next")){
					btnFinish.setText("Confirm");
					btnNxt.performClick();
					for(Button b : choiceBtns){
						b.setEnabled(true);
					}
					return ;
				}
				boolean last = false;
				if(currentPageNO==questions.size()){
					last = true;
				}
				
				FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
		        Fragment prev = activity.getFragmentManager().findFragmentByTag("q_dialog");
		        if (prev != null) {
		            ft.remove(prev);
		        }
		        ft.addToBackStack(null);
		        
		        if (QuestionViewActivity.timerOn == true) {
					if(last)
						((QuestionViewActivity)(context)).saveExamStatus(true);
					else
						btnNxt.performClick();
					return;
		        }
		        
		        // Create and show the dialog.
		        QuestionDialogFragment newFragment = QuestionDialogFragment.newInstance(last,new QuestionDialogFragment.ButtonClickCallBack() {
					
					@Override
					public void onShowSolutionButtonClick(boolean last) {
						mWebView.showSolution();
						mWebView.refreshScreenWidthHeight(false);

						if(!last)
							btnFinish.setText("Next");

						//btnFinish.setText("Next");

						for(Button b : choiceBtns){
							b.setEnabled(false);
						}
					}
					
					

					@Override
					public void onNextQuestionButtonClick(boolean last) {
						if(last)
							((QuestionViewActivity)(context)).saveExamStatus(true);
						else
							btnNxt.performClick();
					}
				});
		        newFragment.show(ft, "q_dialog");
		        
			}
		});
		
		btnHint = (Button) view.findViewById(R.id.btnHint);
		hintCount = questions.get(currentPageNO-1).getHintCount(); 
		if(hintCount>0){
			btnHint.setVisibility(View.VISIBLE);
			if(currentHintIndex==1)
				btnHint.setText("Hint");
			else
				btnHint.setText("Hint"+currentHintIndex);
			
		}else
			btnHint.setVisibility(View.GONE);
		
		btnHint.setOnClickListener(new View.OnClickListener() {
			Questions question = questions.get(currentPageNO-1); 
			@Override
			public void onClick(View v) {
				if(currentHintIndex<hintCount){
					if(currentHintIndex==1){
						mWebView.loadUrl("javascript:freshPassageHtml('"+question.getTextBlock1B().replaceAll("\'", "\\\\'")+"')");	
					}else if(currentHintIndex==2){
						mWebView.loadUrl("javascript:freshPassageHtml('"+question.getTextBlock1C().replaceAll("\'", "\\\\'")+"')");
					}
					currentHintIndex++;
					if(currentHintIndex==1)
						btnHint.setText("Hint");
					else
						btnHint.setText("Hint"+currentHintIndex);
					//update html content
				}else if(currentHintIndex==hintCount){
					if(currentHintIndex==1){
						mWebView.loadUrl("javascript:freshPassageHtml('"+question.getTextBlock1B().replaceAll("\'", "\\\\'")+"')");	
					}else if(currentHintIndex==2){
						mWebView.loadUrl("javascript:freshPassageHtml('"+question.getTextBlock1C().replaceAll("\'", "\\\\'")+"')");
					}else if(currentHintIndex==3)
						mWebView.loadUrl("javascript:freshPassageHtml('"+question.getTextBlock1D().replaceAll("\'", "\\\\'")+"')");
					
					currentHintIndex++;
					btnHint.setText("Passage");
					//mWebView.loadUrl("javascript:freshPassageHtml('"+question.getTextBlock1A().replaceAll("\'", "\\\\'")+"')");
				}else if(currentHintIndex>hintCount){
					currentHintIndex=1;
					btnHint.setText("Hint");
					mWebView.loadUrl("javascript:freshPassageHtml('"+question.getTextBlock1A().replaceAll("\'", "\\\\'")+"')");
				}
				
				mWebView.refreshScreenWidthHeight(true);
				
			}
		});
		
			choiceBtns.clear();
			choiceBtns.add(bA);
			choiceBtns.add(bB);
			choiceBtns.add(bC);
			choiceBtns.add(bD);
			choiceBtns.add(bE);
			choiceBtns.add(bF);
		
	}
	
	private static void initDialog(){
		
//		FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
//        Fragment prev = activity.getFragmentManager().findFragmentByTag("q_dialog");
//        if (prev != null) {
//            ft.remove(prev);
//        }
//        ft.addToBackStack(null);
//        // Create and show the dialog.
//        QuestionDialogFragment newFragment = QuestionDialogFragment.newInstance(new QuestionDialogFragment.ButtonClickCallBack() {
//			
//			@Override
//			public void onShowSolutionButtonClick() {
//				mWebView.showSolution();
//				mWebView.refreshScreenWidthHeight();
//			}
//			
//			@Override
//			public void onNextQuestionButtonClick() {
//				btnNxt.performClick();
//			}
//		});
//        newFragment.show(ft, "q_dialog");
//		LayoutInflater factory = LayoutInflater.from(context);
//		final View v = factory.inflate(R.layout.alert_dialog, null);
//		final Button btnShowCorrect = (Button) v.findViewById(R.id.btnShowCorrect);
//		final Button btnNxtQuestion = (Button) v.findViewById(R.id.btnNxtQuestion);
//		
//		btnNxtQuestion.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				btnNxt.performClick();
//				dialog.dismiss();
//			}
//		});
//		
//		btnShowCorrect.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				mWebView.showSolution();
//				mWebView.refreshScreenWidthHeight();
//				dialog.dismiss();
//			}
//		});
//		
//		AlertDialog.Builder builder = new Builder(context,R.style.dialog);
//		builder.setView(v);
////		builder.setInverseBackgroundForced(true);
//		dialog = builder.create();
	}

	public static void refreshHint() {
		if(btnHint==null)return;
		currentHintIndex=1;
		if(questions==null)hintCount=0;
		else
			hintCount = questions.get(currentPageNO-1).getHintCount(); 
		if(hintCount>0){
			btnHint.setVisibility(View.VISIBLE);
			if(currentHintIndex==1)
				btnHint.setText("Hint");
			else
				btnHint.setText("Hint"+currentHintIndex);
			
		}else
			btnHint.setVisibility(View.GONE);
		
	}
	
	
}
