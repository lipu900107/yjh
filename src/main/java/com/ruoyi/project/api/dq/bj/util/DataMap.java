package com.ruoyi.project.api.dq.bj.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DataMap extends HashMap<String, Object> implements IData {

    private static final String CLASS_REP_STRING1 = "\"_^CCBW^_\":";
    private static final String CLASS_REP_STRING2 = "_^CCBW^_";
    private static final String CLASS_STRING1 = "\"class\":";
    private static final long serialVersionUID = 5728540280422795959L;

    public DataMap() {}

    public DataMap(int size) {
        super(size);
    }

    public DataMap(Map<String, Object> map) {
        super(map);
    }

    public DataMap(String jsonObject) {
        if (!(jsonObject == null || jsonObject.indexOf(CLASS_STRING1) == -1)) {
            jsonObject = jsonObject.replaceAll(CLASS_STRING1, CLASS_REP_STRING1);
        }
        JSONObject map = new JSONObject(parseJsonString(jsonObject));
        if (map != null) {
            putAll(fromJSONObject(map));
        }
    }

    public static DataMap fromJSONObject(JSONObject object) {
        if (object == null) {
            return null;
        }
        DataMap dataMap = new DataMap();
        Iterator<?> keys = object.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            Object value = object.get(key);
            if (key.indexOf(CLASS_REP_STRING2) != -1) {
                key = key.replaceAll(CLASS_REP_STRING2, "class");
            }
            if (value == null) {
                dataMap.put(key, value);
            } else if (value instanceof JSONObject) {
                dataMap.put(key, JSONObject.NULL.equals(value) ? null : fromJSONObject((JSONObject) value));
            } else if (value instanceof JSONArray) {
                dataMap.put(key, JSONObject.NULL.equals(value) ? null : DatasetList.fromJSONArray((JSONArray) value));
            } else if (value instanceof String) {
                dataMap.put(key, value);
            } else {
                dataMap.put(key, value);
            }
        }
        return dataMap;
    }

    public Object get(String key) {
        Object value = super.get(key);
        if (value == null) {
            return null;
        }
        if (JSONObject.NULL.equals(value)) {
            return null;
        }
        return value;
    }

    public String[] getNames() {
        String[] names = new String[size()];
        int index = 0;
        for (String str : keySet()) {
            names[index] = str;
            index++;
        }
        return names;
    }

    public boolean isNoN(String name) {
        return name == null || !containsKey(name);
    }

    public String getString(String name) {
        Object value = get(name);
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    public String getString(String name, String defaultValue) {
        String value = getString(name);
        return value == null ? defaultValue : value;
    }

    public boolean getBoolean(String name) {
        return getBoolean(name, false);
    }

    public boolean getBoolean(String name, boolean defaultValue) {
        Object value = get(name);
        return value == null ? defaultValue : "true".equalsIgnoreCase(value.toString());
    }

    public double getDouble(String name) {
        return getDouble(name, 0.0d);
    }

    public double getDouble(String name, double defaultValue) {
        Object value = get(name);
        return value == null ? defaultValue : Double.parseDouble(value.toString());
    }

    public int getInt(String name) {
        return getInt(name, 0);
    }

    public int getInt(String name, int defaultValue) {
        Object value = get(name);
        return value == null ? defaultValue : Integer.parseInt(value.toString());
    }

    public long getLong(String name) {
        return getLong(name, 0);
    }

    public long getLong(String name, long defaultValue) {
        Object value = get(name);
        return value == null ? defaultValue : Long.parseLong(value.toString());
    }

    public IData getData(String name) {
        Object value = get(name);
        if (value == null) {
            return null;
        }
        if (value instanceof IData) {
            return (IData) value;
        }
        return value instanceof String ? new DataMap((String) value) : null;
    }

    public IData getData(String name, IData def) {
        Object value = get(name);
        if (value == null) {
            return def;
        }
        if (value instanceof IData) {
            return (IData) value;
        }
        if (value instanceof String) {
            return new DataMap((String) value);
        }
        return def;
    }

    public IDataset getDataset(String name, IDataset def) {
        Object value = get(name);
        if (value == null) {
            return def;
        }
        if (value instanceof IDataset) {
            return (IDataset) value;
        }
        if (value instanceof JSONArray) {
            return DatasetList.fromJSONArray((JSONArray) value);
        }
        return def;
    }

    public IDataset getDataset(String name) {
        return getDataset(name, null);
    }

    public IData subData(String group) throws Exception {
        return subData(group, false);
    }

    public IData subData(String group, boolean istrim) throws Exception {
        IData element = new DataMap();
        String prefix = group + BjConstant.SEPARATOR_UNDERLINE;
        for (String name : getNames()) {
            if (name.startsWith(prefix)) {
                String substring = "";
                if (istrim) {
                    substring = name.substring(prefix.length());
                } else {
                }
                element.put(substring, get(name));
            }
        }
        return element;
    }

    public String put(String key, String value) {
        return (String) super.put(key, value);
    }

    public IData put(String key, IData value) {
        return (IData) super.put(key, value);
    }

    public IDataset put(String key, IDataset value) {
        return (IDataset) super.put(key, value);
    }

    private static String parseJsonString(String str) {
        if (str == null) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            switch (c) {
                case '\b':
                    sb.append("\\b");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }

    public String toString() {
        return new JSONObject((Map) this).toString();
    }

}
