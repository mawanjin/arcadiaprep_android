package com.arcadiaprep.app.util;

import java.util.ArrayList;
import java.util.List;

/**
 * For Samsung market.
 * 
 * @author Sharon
 * 
 */

public class Dict
{
    public static final int BOOLEAN_TYPE = 0;
    public static final int STRING_TYPE = 1;
    public static final int DICT_TYPE = 2;
    public static final int ARRAY_TYPE = 3;

    private ArrayList<KVPair> _kvPairs;

    public Dict()
    {
        _kvPairs = new ArrayList<KVPair>();
    }

    public void add(String key, Boolean value)
    {
        KVPair pair = new KVPair(key, value);
        _kvPairs.add(pair);
    }

    public void add(String key, String value)
    {
        KVPair pair = new KVPair(key, value);
        _kvPairs.add(pair);
    }

    public void add(String key, Dict value)
    {
        KVPair pair = new KVPair(key, value);
        _kvPairs.add(pair);
    }

    public void add(String key, List<String> values)
    {
        KVPair pair = new KVPair(key, values);
        _kvPairs.add(pair);
    }

    public Object get(String key)
    {
        for (KVPair pair : _kvPairs)
        {
            if (pair.getKey().equals(key))
                return pair.getValue();
        }
        return null;
    }
}

class KVPair
{
    private String _key;
    private Object _value;
    private int _valueType;

    public KVPair(String key, Boolean value)
    {
        _key = key;
        _value = value;
        _valueType = Dict.BOOLEAN_TYPE;
    }

    public KVPair(String key, String value)
    {
        _key = key;
        _value = value;
        _valueType = Dict.STRING_TYPE;
    }

    public KVPair(String key, Dict value)
    {
        _key = key;
        _value = value;
        _valueType = Dict.DICT_TYPE;
    }

    public KVPair(String key, List<String> value)
    {
        _key = key;
        _value = value;
        _valueType = Dict.ARRAY_TYPE;
    }

    public String getKey()
    {
        return _key;
    }

    public Object getValue()
    {
        return _value;
    }

    public int getValueType()
    {
        return _valueType;
    }
}
