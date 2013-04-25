package com.arcadiaprep.app.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.res.Resources;
import android.util.Log;

/**
 * For Samsung market.
 * 
 * @author Sharon
 * 
 */

public class ItemParser
{
    static final String ATTRIBUTES = "Attributes";
    static final String ABOUTPAGE = "AboutPage";
    static final String TITLE = "Title";
    static final String TEXT = "Text";
    static final String PRICE = "Price";
    static final String ICON = "Icon";
    static final String DATA = "Data";
    static final String NAME = "Name";
    static final String PACKAGESETNAME = "PackageSetName";
    static final String PROBLEMSETS = "ProblemSets";
    static final String EXAMSECTIONNAME = "ExamSectionName";

    private Resources _resources;
    private static Map<String, List<Item>> _cache = new HashMap<String, List<Item>>();

    public ItemParser(Resources resources)
    {
        _resources = resources;
    }

    public List<Item> getItems(String plistFile)
    {
        synchronized (_cache)
        {
            List<Item> items = _cache.get(plistFile);
            if (items != null)
                return items;

            PlistParser parser = new PlistParser(_resources);
            List<Dict> dictList = parser.parse(plistFile);
            items = new ArrayList<Item>();

            for (Dict dict : dictList)
            {
                Dict aboutPage = null;
                String price = null;
                String icon = null;
                String title = null;
                String text = null;

                Dict attr = (Dict) dict.get(ATTRIBUTES);
                if (attr == null)
                {
                    Log.d("ItemParser", "*** Arcadia *** No " + ATTRIBUTES);
                }
                else
                {
                    aboutPage = (Dict) attr.get(ABOUTPAGE);
                    if (aboutPage == null)
                        Log.d("ItemParser", "*** Arcadia *** No " + ABOUTPAGE);

                    price = (String) attr.get(PRICE);
                    if (price == null)
                        Log.d("ItemParser", "*** Arcadia *** No " + PRICE);

                    icon = (String) attr.get(ICON);
                    if (icon == null)
                        Log.d("ItemParser", "*** Arcadia *** No " + ICON);
                }

                if (aboutPage != null)
                {
                    title = (String) aboutPage.get(TITLE);
                    text = (String) aboutPage.get(TEXT);
                }

                Dict data = (Dict) dict.get(DATA);
                if (data == null)
                {
                    Log.d("ItemParser", "*** Arcadia *** No " + DATA);
                    continue;
                }

                String packageSetName = (String) data.get(PACKAGESETNAME);
                List<String> problemSets = (List<String>) data.get(PROBLEMSETS);
                Dict name = (Dict) dict.get(NAME);
                String sectionName = (String) name.get(EXAMSECTIONNAME);

                Item item = new Item();
                item.setTitle(title);
                item.setText(text);
                item.setPackageSetName(packageSetName);
                item.setProlemSets(problemSets);
                item.setSectionName(sectionName);
                item.setPrice(price);
                item.setIcon(icon);
                items.add(item);
            }

            _cache.put(plistFile, items);
            return items;
        }
    }
}
