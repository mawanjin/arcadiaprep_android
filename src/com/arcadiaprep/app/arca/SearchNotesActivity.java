package com.arcadiaprep.app.arca;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.arcadiaprep.app.arca.constants.ConstantQuestion;
import com.arcadiaprep.app.arca.service.QuestionViewService;
import com.arcadiaprep.app.arca.ui.list.ListViewSearchNotesAdapter;
import com.arcadiaprep.app.arca.vo.ListItemWorkspaceNotesVO;

public class SearchNotesActivity extends Activity implements OnClickListener,
        OnItemClickListener
{

    EditText editfield;
    Button btnSearch;
    ListView searchList;
    ListViewSearchNotesAdapter _searchNotesAdapter;
    Button btnBack;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_notes);

        editfield = (EditText) findViewById(R.id.searchKey);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);

		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SearchNotesActivity.this,ArcadiaprepActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				finish();
			}
		});
        
        searchList = (ListView) findViewById(R.id.searchList);
        String searchKey = editfield.getText().toString();
        List<ListItemWorkspaceNotesVO> listItems = QuestionViewService.searchWorkspaceNotesByKey(this,
                searchKey);
        _searchNotesAdapter = new ListViewSearchNotesAdapter(this, R.layout.list_item_searchnotes, listItems);
        searchList.setAdapter(_searchNotesAdapter);
        searchList.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        // refresh search listview
        String searchKey = editfield.getText().toString();
        List<ListItemWorkspaceNotesVO> listItems = QuestionViewService
                .searchWorkspaceNotesByKey(this, searchKey);
        _searchNotesAdapter.setListItems(listItems);
        _searchNotesAdapter.notifyDataSetChanged();
        
        TextView tv = (TextView) findViewById(R.id.resultMessage);
        if (listItems.size() > 1)
        	tv.setText("Found " + listItems.size() + " matched notes in your workspace!");
        else
        	tv.setText("Found " + listItems.size() + " matched note in your workspace!");
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
            long arg3)
    {
        ListItemWorkspaceNotesVO item = (ListItemWorkspaceNotesVO) searchList
                .getAdapter().getItem(position);

        Intent intent = new Intent(SearchNotesActivity.this,
                QuestionViewActivity.class);
        // intent.putExtra("show_discussion", true);
        intent.putExtra("VIEW_TYPE",
                ConstantQuestion.QUESTION_VIEW_TYPE_SEARCHNOTES);
        intent.putExtra("question_id", item.getQuestionID());

        startActivity(intent);
    }
}
