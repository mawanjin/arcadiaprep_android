package com.arcadiaprep.app.market;

import java.io.InputStream;
import java.util.ArrayList;

import com.arcadiaprep.app.arca.ArcadiaprepActivity;
import com.arcadiaprep.app.arca.R;
import com.arcadiaprep.app.util.Item;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.samsungapps.plasma.Plasma;
import com.samsungapps.plasma.ItemInformation;
import com.samsungapps.plasma.PlasmaListener;
import com.samsungapps.plasma.PurchaseTicket;
import com.samsungapps.plasma.PurchasedItemInformation;

/**
 * For Samsung market.
 * 
 * @author Sharon
 * 
 */

public class ItemsListViewActivity extends Activity implements PlasmaListener,
        OnClickListener
{
    private ItemInformationListAdapter __itemInformationListAdapter = null;
    private Plasma __plasma = null;

    private class ItemInformationListAdapter extends
            ArrayAdapter<ItemInformation>
    {
        private ArrayList<ItemInformation> __itemInformationList = null;
        private Context __context;
        
        public ItemInformationListAdapter(Context context,
                int textViewResourceId, ArrayList<ItemInformation> objects)
        {
            super(context, textViewResourceId, objects);
            __context = context;
            __itemInformationList = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View view = convertView;
            // if (view == null)
            // {
            LayoutInflater inflater = ItemsListViewActivity.this
                    .getLayoutInflater();
            view = inflater.inflate(R.layout.row, null);
            // }

            final ItemInformation item = __itemInformationList.get(position);
            if (item != null)
            {

                String key = item.getReserved1().trim();
                Item myItem = MyApplication._itemMap.get(key);
                String img_name = "Pkg_LG.png";
                Bitmap image = null;
                
                if (myItem != null && myItem.getIcon() != null)
                {
                    img_name = myItem.getIcon();
                }
                
                if (img_name!=null) {
                    InputStream is = null;
                    try {
                      is = __context.getResources().getAssets().open(img_name);
                      image = BitmapFactory.decodeStream(is);

                    } catch (Exception e) {
                        System.out.println("Failure to get image (" + img_name +") from assets. " + e);
                    }
                    
                }
                ImageView txvItemName_image = (ImageView) view
                        .findViewById(R.id.txvItemName_image);
                
                txvItemName_image.setImageBitmap(image);
                
                TextView txvItemName = (TextView) view
                        .findViewById(R.id.txvItemName);
                txvItemName.setText(item.getItemName());
                TextView txvItemPrice = (TextView) view
                        .findViewById(R.id.txvItemPrice);

                txvItemPrice.setText(getPriceStringWithCurrencyUnit(item));
                // Check if it is purchased
                if (MyApplication._purchasedPkgNames.contains(key))
                    txvItemPrice.setText("Purchased");

                view.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(ItemsListViewActivity.this,
                                ItemViewActivity.class);
                        intent.putExtra(ItemViewActivity.EXTRA_ITEMID,
                                item.getItemId());
                        intent.putExtra(ItemViewActivity.EXTRA_PACKAGESETNAME,
                                item.getReserved1().trim());
                        startActivityForResult(intent,
                                ItemViewActivity.REQUEST_CODE);
                    }
                });
            }

            return view;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.itemslist);
        setTitle("Market Place");

        Button btnBack = (Button) findViewById(getBtnBackId());
        Button btnRestore = (Button) findViewById(getBtnRestoreId());
        btnBack.setOnClickListener(this);
        btnRestore.setOnClickListener(this);

        __plasma = new Plasma(MyApplication._ITEM_GROUP_ID, this);
        __plasma.setPlasmaListener(this);
        if (MyApplication._developer == true)
        	__plasma.setDeveloperFlag(1);

        __itemInformationListAdapter = new ItemInformationListAdapter(this,
                R.layout.row, MyApplication._itemInformationList);

        ListView lsvItemList = (ListView) findViewById(R.id.lsvItemList);
        lsvItemList.setAdapter(__itemInformationListAdapter);

        int count = MyApplication._itemInformationList.size();
        System.out.println(" *** Arcadia *** : count = " + count);
        if (count > 0)
            return;

        __itemInformationListAdapter.clear();
        __plasma.requestItemInformationList(MyApplication._transactionId++, 1,
                MyApplication._itemList.size());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // If the request went well (OK) and the request was
        // PURCHASE
        if (resultCode == Activity.RESULT_OK
                && requestCode == ItemViewActivity.REQUEST_CODE)
        {
            Log.d("ItemsListView", " *** Arcadia *** refesh list");
            __itemInformationListAdapter.notifyDataSetChanged();
            ArcadiaprepActivity._listViewRecommendationAdapter
                    .removePurchasedItems();
            ArcadiaprepActivity._listViewRecommendationAdapter
                    .notifyDataSetChanged();
        }
    }

    @Override
    public void onItemInformationListReceived(int transactionId,
            int statusCode, ArrayList<ItemInformation> itemInformationList)
    {
        switch (statusCode)
        {
            case Plasma.STATUS_CODE_SUCCESS:
                __itemInformationListAdapter.clear();
                for (int i = 0; i < itemInformationList.size(); i++)
                {
                    ItemInformation itemInformation = itemInformationList
                            .get(i);

                    Item item = MyApplication._itemMap.get(itemInformation
                            .getReserved1().trim());
                    if (item == null)
                    {
                        Log.d("ItemsListView", "item "
                                + itemInformation.getReserved1().trim()
                                + " not found in assets");
                        continue;
                    }
                    // set id to market item id
                    item.setId(itemInformation.getItemId());
                    // if no text from plist, set test to market item
                    // description
                    if (item.getText() == null)
                        item.setText(itemInformation.getItemDescription());

                    __itemInformationListAdapter.add(itemInformation);
                }
                __itemInformationListAdapter.notifyDataSetChanged();
                break;
            default:
                String errorMessage = "Failed to retrieve the item list from Samsung Market";
                showErrorDialog(this, statusCode, errorMessage);
                break;
        }
    }

    @Override
    public void onPurchasedItemInformationListReceived(int transactionId,
            int statusCode,
            ArrayList<PurchasedItemInformation> purchasedItemInformationList)
    {
        switch (statusCode)
        {
            case Plasma.STATUS_CODE_SUCCESS:
                MyApplication._purchasedPkgNames.clear();
                MyApplication._purchasedProblemSetIds.clear();

                for (int i = 0; i < purchasedItemInformationList.size(); i++)
                {
                    PurchasedItemInformation purchasedItemInformation = purchasedItemInformationList
                            .get(i);
                    String reserved1 = purchasedItemInformation.getReserved1()
                            .trim();
                    MyApplication._purchasedPkgNames.add(reserved1);
                    Item item = MyApplication._itemMap.get(reserved1);
                    if (item != null)
                        MyApplication._purchasedProblemSetIds.addAll(item
                                .getProblemSets());
                }
                MyApplication.getApp().saveItemsList();
                __itemInformationListAdapter.notifyDataSetChanged();
                ArcadiaprepActivity._listViewRecommendationAdapter
                        .removePurchasedItems();
                ArcadiaprepActivity._listViewRecommendationAdapter
                        .notifyDataSetChanged();
                break;
            default:
                String errorMessage = "Failed to retrieve the purchase list from Samsung Market";
                showErrorDialog(this, statusCode, errorMessage);
                break;
        }
    }

    @Override
    public void onPurchaseItemInitialized(int transactionId, int statusCode,
            PurchaseTicket purchaseTicket)
    {
        // After onPurchaseItemFinished is successfully called, you can use
        // PurchaseTicket, onPurchaseItemInitialized method's argument to
        // verify
        // a purchase on the SamsungApps server. It's simple process. You can
        // send a query to PurchaseTicket's VerifyURL address the query can be
        // made with combination of PurchaseTicket's PurchaseID, Param1,
        // Param2
        // and Param3.
        // If your application uses a server-client model, server-to-server
        // purchase verification explained above is recommended.
    }

    @Override
    public void onPurchaseItemFinished(int transactionId, int statusCode,
            PurchasedItemInformation purchasedItemInformation)
    {
    }

    public static void showErrorDialog(Context context, int errorCode,
            String errorMessage)
    {
        String errorMessageWithCode = String.format("%s (%d)", errorMessage,
                errorCode);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        alertDialogBuilder.setTitle("Error");
        alertDialogBuilder.setMessage(errorMessageWithCode);
        alertDialogBuilder.show();
    }

    public static String getPriceStringWithCurrencyUnit(
            ItemInformation itemInformation)
    {
        String priceStringFormatString = null;
        if (itemInformation.getCurrencyUnitHasPenny())
        {
            priceStringFormatString = "%.2f";
        }
        else
        {
            priceStringFormatString = "%.0f";
        }

        String priceString = String.format(priceStringFormatString,
                itemInformation.getItemPrice());

        StringBuffer priceStringWithCurrencyUnitBuffer = new StringBuffer();
        if (itemInformation.getCurrencyUnitPrecedes())
        {
            priceStringWithCurrencyUnitBuffer.append(itemInformation
                    .getCurrencyUnit());
            priceStringWithCurrencyUnitBuffer.append(priceString);
        }
        else
        {
            priceStringWithCurrencyUnitBuffer.append(priceString);
            priceStringWithCurrencyUnitBuffer.append(itemInformation
                    .getCurrencyUnit());
        }

        return priceStringWithCurrencyUnitBuffer.toString();
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == getBtnBackId())
        {
            finish();
        }
        else if (v.getId() == getBtnRestoreId())
        {
            // request purchased list first
            __plasma.requestPurchasedItemInformationList(
                    MyApplication._transactionId++, 1,
                    MyApplication._itemList.size());
        }

    }

    private int getBtnRestoreId()
    {
        return R.id.btnRestore;
    }

    private int getBtnBackId()
    {
        return R.id.btnBack;
    }
}
