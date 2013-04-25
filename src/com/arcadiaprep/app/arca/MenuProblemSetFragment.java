package com.arcadiaprep.app.arca;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.arcadiaprep.app.arca.service.MainDataService;
import com.arcadiaprep.app.arca.service.MainListenerService;
import com.arcadiaprep.app.arca.ui.list.ListViewMenuProblemSetAdapter;
import com.arcadiaprep.app.arca.util.ListHeightUtils;

/**
 * This class is used as a menu in the ProblemSet Introduction Page,which
 * contains a list of problem. It is consist of two parts:icon and name of
 * problem set. This class should be invoked as a fragment.
 * 
 * @author lala
 * 
 */
public class MenuProblemSetFragment extends DialogFragment {

	private Button btnBack;
	private ListView listView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.menu_problemset, container, true);

		btnBack = (Button) v.findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), ArcadiaprepActivity.class);
				startActivity(i);
			}
		});

		listView = (ListView) v.findViewById(R.id.list);
		listView.setAdapter(new ListViewMenuProblemSetAdapter(getActivity(),
				MainDataService.questionSets));
		
		ListHeightUtils.setListViewHeightBasedOnChildren(listView);
		
		MainListenerService.getInstance().registerMyQuestionSetsListener(
				getActivity(), listView);

		return v;
	}

}
