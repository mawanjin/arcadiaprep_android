package com.arcadiaprep.app.market;

import java.util.ArrayList;

import com.arcadiaprep.app.arca.ArcadiaprepActivity;
import com.arcadiaprep.app.arca.R;
import com.arcadiaprep.app.util.Item;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class ItemViewActivity extends Activity implements OnClickListener,
        PlasmaListener
{
    public static final String EXTRA_ITEMID = "EXTRA_ITEMID";
    public static final String EXTRA_PACKAGESETNAME = "EXTRA_PACKAGESETNAME";
    public static final int REQUEST_CODE = 2;
    private int _resultCode = RESULT_CANCELED;
    private ItemInformation _itemBuy_Now;
    private Plasma _plasma;
    private Item _clickItem;
    private static final String __CR_LF = "\n";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.package_introduction);

        Bundle extras = getIntent().getExtras();
        String itemPackagename = null;
        if (extras != null)
        {
            _itemBuy_Now = new ItemInformation();
            String itemid = extras.getString(EXTRA_ITEMID);
            _itemBuy_Now.setItemId(itemid);
            itemPackagename = extras.getString(EXTRA_PACKAGESETNAME);
            _itemBuy_Now.setReserved1(itemPackagename);
            _clickItem = MyApplication._itemMap.get(itemPackagename);
            TextView txtSetName = (TextView) findViewById(R.id.packageName);
            txtSetName.setText(_clickItem.getTitle());
        }
        _resultCode = RESULT_CANCELED;

        _plasma = new Plasma(MyApplication._ITEM_GROUP_ID, this);
        _plasma.setPlasmaListener(this);
        if (MyApplication._developer == true)
        	_plasma.setDeveloperFlag(1);

        WebView webview = (WebView) findViewById(R.id.webviewPage1);
        webview.setWebViewClient(new WebViewClient()
        {
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                System.out.println("*** Arcadia url *** " + url);
                int questionIndex = url.lastIndexOf("?");
                String packageName = url.substring(questionIndex + 1).trim();
                if (MyApplication._purchasedPkgNames.contains(packageName))
                {
                    String purchasedMsg = "This item is already purchased.";
                    Toast toast = Toast.makeText(ItemViewActivity.this,
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
                        Log.d("ItemView", "Package: " + packageName
                                + " not in the market");
                }
                else
                    Log.d("ItemView", "Package: " + packageName
                            + " not in the assets");

                return false;
            }
        });

        webview.loadDataWithBaseURL("file:///android_asset/", _clickItem.getText(), "text/html", "utf-8", null);

        Button btnDone = (Button) findViewById(getBtnDoneId());
        Button btnBuyNow = (Button) findViewById(getBtnBuyNowId());
        RelativeLayout btnBuyNowRL = (RelativeLayout) findViewById(R.id.RelativeLayout02);
        btnDone.setOnClickListener(this);
        btnBuyNow.setOnClickListener(this);
        // Check if it is purchased
        if (MyApplication._purchasedPkgNames.contains(itemPackagename))
            btnBuyNowRL.setVisibility(View.GONE);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!MyApplication._purchasedPkgNames.contains(_clickItem
                .getPackageSetName()) && _clickItem.getId() == null)
        {
            _plasma.requestItemInformationList(MyApplication._transactionId++,
                    1, MyApplication._itemList.size());
        }
    }

    int getBtnDoneId()
    {
        return R.id.btnDone;
    }

    int getBtnBuyNowId()
    {
        return R.id.btnBuy_Now;
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == getBtnDoneId())
        {
            setResult(_resultCode);
            finish();
        }
        else if (v.getId() == getBtnBuyNowId())
        {
            if (_clickItem.getId() != null)
            {
                Log.d("ItemsListView", " *** Arcadia *** refesh list");
                _plasma.requestPurchaseItem(MyApplication._transactionId++,
                        _clickItem.getId());
            }
            else
            {
                String purchasedMsg = "Not yet connected to market, please try again later.";
                Toast toast = Toast.makeText(ItemViewActivity.this,
                        purchasedMsg, Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    @Override
    public void onBackPressed()
    {
        setResult(_resultCode);
        finish();
    }

    @Override
    public void onItemInformationListReceived(int transactionId,
            int statusCode, ArrayList<ItemInformation> itemInformationList)
    {
        switch (statusCode)
        {
            case Plasma.STATUS_CODE_SUCCESS:
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
                }
                break;
            default:
                String errorMessage = "Failed to retrieve the item list from Samsung Market";
                ItemsListViewActivity.showErrorDialog(this, statusCode,
                        errorMessage);
                break;
        }
    }

    @Override
    public void onPurchasedItemInformationListReceived(int transactionId,
            int statusCode,
            ArrayList<PurchasedItemInformation> purchasedItemInformationList)
    {
    }

    @Override
    public void onPurchaseItemInitialized(int transactionId, int statusCode,
            PurchaseTicket purchaseTicket)
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
                Button btnBuyNow = (Button) findViewById(getBtnBuyNowId());
                btnBuyNow.setVisibility(View.INVISIBLE);
                RelativeLayout btnBuyNowRL = (RelativeLayout) findViewById(R.id.RelativeLayout02);
                btnBuyNowRL.setVisibility(View.GONE);
                _resultCode = RESULT_OK;
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
}
