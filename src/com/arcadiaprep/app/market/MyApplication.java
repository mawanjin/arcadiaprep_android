package com.arcadiaprep.app.market;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.arcadiaprep.app.util.EncryptionUtil;
import com.arcadiaprep.app.util.Item;
import com.arcadiaprep.app.util.ItemParser;
import com.samsungapps.plasma.ItemInformation;

import android.app.Application;
import android.content.Context;
import android.provider.Settings.Secure;

/**
 * For Samsung market.
 * 
 * @author Sharon
 * 
 */

public class MyApplication extends Application
{
    public static String _ITEM_GROUP_ID = null;
    public static boolean _developer;
    public static int _transactionId = 0;
    public static HashMap<String, Item> _itemMap = new HashMap<String, Item>();
    public static List<Item> _itemList = null;
    public static ArrayList<ItemInformation> _itemInformationList = new ArrayList<ItemInformation>();
    public static HashSet<String> _purchasedPkgNames;
    public static HashSet<String> _purchasedProblemSetIds;
    private static MyApplication _app;
    private static final String FILENAME_PURCHASED_PKG_NAME = "packagedata";
    private static final String FILENAME_PURCHASED_PKG_PROBLEM_NAME = "problemdata";
    private static String ANDROID_ID;
    // The key is education
    private static final String KEY = "education";

    public void onCreate()
    {
        super.onCreate();
        _app = this;
        ANDROID_ID = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
        readItemsList();
        parsePlist();
    }

    public static MyApplication getApp()
    {
        return _app;
    }

    private void readItemsList()
    {
        _purchasedPkgNames = buildHashSet(FILENAME_PURCHASED_PKG_NAME);
        _purchasedProblemSetIds = buildHashSet(FILENAME_PURCHASED_PKG_PROBLEM_NAME);
    }

    private HashSet<String> buildHashSet(String filename)
    {
        FileInputStream fin = null;
        InputStreamReader isr = null;
        BufferedReader reader = null;
        HashSet<String> readsetname = new HashSet<String>();
        try
        {
            fin = openFileInput(filename);
            isr = new InputStreamReader(fin, "UTF-8");
            reader = new BufferedReader(isr);

            int row = 0;
            String line;
            while ((line = reader.readLine()) != null)
            {
                if (row == 0)
                {
                    // skip UTF-8 BOM
                    if (line.charAt(0) == 0xFEFF || line.charAt(0) == 0xFFFE)
                        line = line.substring(1);
                }

                String decryptedString = EncryptionUtil.dencrypt(line, false, KEY);
                // first line is android device id
                if (row == 0)
                {
                    if (!ANDROID_ID.equals(decryptedString))
                        break;
                }
                else
                    readsetname.add(decryptedString);
                row++;
            }
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (reader != null)
                    reader.close();
                if (isr != null)
                    isr.close();
                if (fin != null)
                    fin.close();
            }
            catch (Throwable err)
            {
            }
        }
        return readsetname;
    }

    public String saveItemsList()
    {
        String errMsg = null;
        errMsg = saveItemsListToFile(FILENAME_PURCHASED_PKG_PROBLEM_NAME,
                _purchasedProblemSetIds);
        if (errMsg == null)
            errMsg = saveItemsListToFile(FILENAME_PURCHASED_PKG_NAME,
                    _purchasedPkgNames);
        return errMsg;
    }

    public String saveItemsListToFile(String filename, HashSet<String> saveset)
    {
        String errMsg = null;
        FileOutputStream fout = null;
        OutputStreamWriter osw = null;
        try
        {
            fout = openFileOutput(filename, Context.MODE_PRIVATE);
            osw = new OutputStreamWriter(fout);
            // write android id as first line
            String encryptedString = EncryptionUtil.dencrypt(ANDROID_ID, true, KEY);
            osw.write(encryptedString);
            // osw.write("\n");
            for (String eachWord : saveset)
            {
                encryptedString = EncryptionUtil.dencrypt(eachWord, true, KEY);
                osw.write(encryptedString);
                // osw.write("\n");
            }
        }
        catch (Throwable e)
        {
            errMsg = e.getMessage();
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (osw != null)
                    osw.close();
                if (fout != null)
                    fout.close();
            }
            catch (Throwable err)
            {
            }
        }
        return errMsg;
    }

    private void parsePlist()
    {
        ItemParser itemParser = new ItemParser(getResources());
        _itemList = itemParser.getItems("plist/problempackages.plist");
        for (Item item : _itemList)
        {
            String key = item.getPackageSetName();
            MyApplication._itemMap.put(key, item);
        }
    }
}
