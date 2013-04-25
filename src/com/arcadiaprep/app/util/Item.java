package com.arcadiaprep.app.util;

import java.util.List;

/**
 * For Samsung market.
 * 
 * @author Sharon
 * 
 */

public class Item
{
    private String _id;
    private String _title;
    private String _text;
    private String _packageSetName;
    private List<String> _problemSets;
    private String _sectionName;
    private String _price;
    private String _icon;

    public Item()
    {
        _id = null;
        _title = null;
        _text = null;
        _packageSetName = null;
        _problemSets = null;
        _sectionName = null;
        _price = null;
        _icon = null;
    }

    public void setId(String id)
    {
        _id = id;
    }

    public String getId()
    {
        return _id;
    }

    public void setTitle(String nodeValue)
    {
        _title = nodeValue;
    }

    public String getTitle()
    {
        return _title;
    }

    public void setText(String nodeValue)
    {
        _text = nodeValue;
    }

    public String getText()
    {
        return _text;
    }

    public void setPackageSetName(String nodeValue)
    {
        _packageSetName = nodeValue;
    }

    public String getPackageSetName()
    {
        return _packageSetName;
    }

    public void setProlemSets(List<String> nodeValues)
    {
        _problemSets = nodeValues;
    }

    public List<String> getProblemSets()
    {
        return _problemSets;
    }

    public void setSectionName(String sectionName)
    {
        _sectionName = sectionName;
    }

    public String getSectionName()
    {
        return _sectionName;
    }

    public void setPrice(String price)
    {
        _price = price;
    }

    public String getPrice()
    {
        return _price;
    }

    public void setIcon(String icon)
    {
        _icon = icon;
    }

    public String getIcon()
    {
        return _icon;
    }
}
