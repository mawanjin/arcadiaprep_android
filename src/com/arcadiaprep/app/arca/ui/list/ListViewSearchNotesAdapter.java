package com.arcadiaprep.app.arca.ui.list;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.arcadiaprep.app.arca.R;
import com.arcadiaprep.app.arca.service.SystemService;
import com.arcadiaprep.app.arca.vo.ListItemWorkspaceNotesVO;

public class ListViewSearchNotesAdapter extends
        ArrayAdapter<ListItemWorkspaceNotesVO>
{

    LayoutInflater _inflater;
    List<ListItemWorkspaceNotesVO> _listItems;
    Context _context;

    public void setListItems(List<ListItemWorkspaceNotesVO> listItems)
    {
        _listItems = listItems;
        clear();
        addAll(listItems);
    }

    public ListViewSearchNotesAdapter(Context context, int resId,
            List<ListItemWorkspaceNotesVO> list)
    {
        super(context, resId, list);
        _context = context;
        _inflater = LayoutInflater.from(context);
        _listItems = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2)
    {
        if (convertView == null)
        {
            convertView = _inflater.inflate(R.layout.list_item_searchnotes,
                    null);
        }

        ListItemWorkspaceNotesVO item = _listItems.get(position);
        TextView questionId = (TextView) convertView.findViewById(R.id.txtQuestionID);
        TextView searchNote = (TextView) convertView.findViewById(R.id.txtNotes);
        String name = SystemService.findQuestionsDesciptionByID(_context, item.getQuestionID());
        questionId.setText(name);
        searchNote.setText(item.getNote());
        return convertView;
    }
}
