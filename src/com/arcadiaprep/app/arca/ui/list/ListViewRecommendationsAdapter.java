package com.arcadiaprep.app.arca.ui.list;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.arcadiaprep.app.arca.ArcadiaprepActivity;
import com.arcadiaprep.app.arca.R;
import com.arcadiaprep.app.arca.util.HTTPUtils;
import com.arcadiaprep.app.market.ItemViewActivity;
import com.arcadiaprep.app.market.MyApplication;
import com.arcadiaprep.app.util.Item;

public class ListViewRecommendationsAdapter extends ArrayAdapter<Item>
{
    private List<Item> _listItems;
    private Context _context;

    public ListViewRecommendationsAdapter(Context context, int resourceId,
            List<Item> listItems)
    {
        super(context, resourceId, listItems);
        _context = context;
        _listItems = listItems;
    }

    @Override
    public int getCount()
    {
        return _listItems.size();
    }

    @Override
    public Item getItem(int arg0)
    {
        if (_listItems != null && _listItems.size() > arg0)
            return _listItems.get(arg0);
        else
            return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = convertView;
        // if (view == null)
        // {
        LayoutInflater inflater = ((Activity)_context)
                .getLayoutInflater();
        view = inflater.inflate(R.layout.row, null);
        // }

        final Item myItem = _listItems.get(position);
        
        String img_name = "Pkg_RC.png";
        Bitmap image = null;
        
        if (myItem != null && myItem.getIcon() != null)
        {
            img_name = myItem.getIcon();
        }
        
        if (img_name!=null) {
            InputStream is = null;
            try {
              is = _context.getResources().getAssets().open(img_name);
              image = BitmapFactory.decodeStream(is);

            } catch (Exception e) {
                System.out.println("Failure to get image (" + img_name +") from assets. " + e);
            }
            
        }

        ImageView txvItemName_image = (ImageView) view
                .findViewById(R.id.txvItemName_image);
        
        txvItemName_image.setImageBitmap(image);
        
        TextView txvItemName = (TextView) view.findViewById(R.id.txvItemName);
        String info = myItem.getTitle();
        if (info != null)
            txvItemName.setText(info);
        else
            txvItemName.setText("Missing Information");
        TextView txvItemPrice = (TextView) view.findViewById(R.id.txvItemPrice);

        txvItemPrice.setText(myItem.getPrice());
        view.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            	
				if(!HTTPUtils.isConnectInternet(_context)) {
			        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
			                _context);
			        alertDialogBuilder.setTitle("Alert");
			        alertDialogBuilder.setMessage("You need to have an Internet connection to use this feature!");
			        alertDialogBuilder.show();
					
					return;
				}
                Intent intent = new Intent(_context,
                        ItemViewActivity.class);
                intent.putExtra(ItemViewActivity.EXTRA_ITEMID,
                        myItem.getId());
                intent.putExtra(ItemViewActivity.EXTRA_PACKAGESETNAME,
                        myItem.getPackageSetName());
               ((Activity) _context).startActivityForResult(intent,
                        ItemViewActivity.REQUEST_CODE);
            }
        });

        return view;
    }

    public void removePurchasedItems()
    {
        for (int i = _listItems.size() - 1; i >= 0; i--)
        {
            if (MyApplication._purchasedPkgNames.contains(_listItems.get(i)
                    .getPackageSetName()))
                _listItems.remove(i);
        }
    }

    public static List<Item> findRecommendations(Context context,
            String currSection)
    {
        List<Item> recommendations1 = new ArrayList<Item>();  
        List<Item> recommendations2 = new ArrayList<Item>(); 
        List<Item> recommendations3 = new ArrayList<Item>();
        for (Item item : MyApplication._itemList)
        {
            // check if not purchased
            if (!MyApplication._purchasedPkgNames.contains(item
                    .getPackageSetName()))
            {
                if (item.getSectionName().equalsIgnoreCase(currSection))
                    recommendations1.add(item);
                else if (item.getSectionName().equalsIgnoreCase("ALL"))
                    recommendations2.add(item);
                else
                    recommendations3.add(item);
            }
        }

        recommendations1.addAll(recommendations2);
        recommendations1.addAll(recommendations3);
        return recommendations1;
    }

    public void setList(List<Item> listItems)
    {
        _listItems = listItems;
    }
}
