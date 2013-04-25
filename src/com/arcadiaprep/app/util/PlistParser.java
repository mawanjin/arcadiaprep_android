package com.arcadiaprep.app.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import android.content.res.Resources;

/**
 * For Samsung market.
 * 
 * @author Sharon
 * 
 */

// generic plist parser
// assumptions:
// 1: top level is one array
// 2: 2nd level is dict
// 3: values for lower level array are string type only
// 4: only 4 data types: string, boolean, array, dict
public class PlistParser
{
    static final String ARRAY = "array";
    static final String DICT = "dict";
    static final String KEY = "key";
    static final String STRING = "string";
    static final String FALSE = "false";
    static final String TRUE = "true";

    private Resources _resources;

    public PlistParser(Resources resources)
    {
        _resources = resources;
    }

    public InputStream openFile(String filename)
    {
        InputStream is = null;
        try
        {
            is = _resources.getAssets().open(filename);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

        return is;
    }

    public List<Dict> parse(String filename)
    {
        List<Dict> dictList = new ArrayList<Dict>();
        InputStream is = openFile(filename);
        if (is == null)
        {
            return dictList;
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try
        {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document dom = builder.parse(is);
            Element root = dom.getDocumentElement();
            // root.normalize();
            Element arr = null;
            NodeList nodes = root.getChildNodes();
            for (int i = 0; i < nodes.getLength(); i++)
            {
                Node node = nodes.item(i);
                if (node instanceof Element)
                {
                    Element element = (Element) node;
                    if (element.getTagName().equals(ARRAY))
                    {
                        arr = element;
                        break;
                    }
                }
            }
            if (arr == null)
                return dictList;

            nodes = arr.getChildNodes();
            for (int i = 0; i < nodes.getLength(); i++)
            {
                Node node = nodes.item(i);
                if (!(node instanceof Element))
                    continue;

                Element element = (Element) node;
                if (element.getTagName().equals(DICT))
                {
                    Dict dict = new Dict();
                    buildDict(dict, element);
                    dictList.add(dict);
                }
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            try
            {
                is.close();
            }
            catch (Throwable t)
            {
            }
        }

        return dictList;
    }

    // recursive method
    private void buildDict(Dict dict, Element dictElement)
    {
        if (!dictElement.hasChildNodes())
            return;

        String key = null;
        NodeList nodes = dictElement.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++)
        {
            Node node = nodes.item(i);
            if (!(node instanceof Element))
                continue;

            Element element = (Element) node;
            String tagName = element.getTagName();
            if (tagName.equals(KEY))
            {
                key = getElementTextValue(element);
                // System.out.println(" *** Arcadia *** key = " + key);
                continue;
            }

            // process values element, which follows key element
            if (key == null)
                throw new RuntimeException("Value not follow key: " + tagName
                        + " = " + getElementTextValue(element));

            if (tagName.equals(FALSE))
                dict.add(key, Boolean.valueOf(false));
            else if (tagName.equals(TRUE))
                dict.add(key, Boolean.valueOf(true));
            else if (tagName.equals(STRING))
            {
                String value = getElementTextValue(element);
                dict.add(key, value);
            }
            else if (tagName.equals(DICT))
            {
                Dict d = new Dict();
                buildDict(d, element);
                dict.add(key, d);
            }
            else if (tagName.equals(ARRAY))
            {
                ArrayList<String> valueList = new ArrayList<String>();

                NodeList children = element.getChildNodes();
                for (int j = 0; j < children.getLength(); j++)
                {
                    Node child = children.item(j);
                    if (!(child instanceof Element))
                        continue;

                    Element childElement = (Element) child;
                    String childTagName = childElement.getTagName();
                    if (childTagName.equals(STRING))
                        valueList.add(getElementTextValue(childElement));
                }

                dict.add(key, valueList);
            }

            key = null;
        }
    }

    public static String getElementTextValue(Element e)
    {
        if (!e.hasChildNodes())
            return null;

        StringBuffer buf = new StringBuffer();
        NodeList nodes = e.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++)
        {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.TEXT_NODE)
                buf.append(node.getNodeValue());
        }

        return buf.toString();
    }
}
