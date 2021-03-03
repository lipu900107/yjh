package com.ruoyi.project.api.dq.bj.util;

import com.alibaba.fastjson.JSONException;

import java.util.*;

public class DatasetList extends ArrayList<Object> implements IDataset {
	
    private static final long serialVersionUID = 8302984775243577040L;

    public DatasetList() {
        super(20);
    }

    public DatasetList(int size) {
        super(size);
    }

    public DatasetList(IData data) {
        super(20);
        add(data);
    }

    public DatasetList(IData[] datas) {
        super(20);
        for (IData data : datas) {
            add(data);
        }
    }

    public DatasetList(IDataset list) {
        super(20);
        addAll(list);
    }

    public DatasetList(String jsonArray) {
        super(20);
        try {
            if (!(jsonArray == null || jsonArray.indexOf("\"class\":") == -1)) {
                jsonArray = jsonArray.replaceAll("\"class\":", "\"__classChangedByFrameWork__\":");
            }
            addAll(fromJSONArray(new JSONArray(parseJsonString(jsonArray))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DatasetList(JSONArray array) {
        super(20);
        addAll(fromJSONArray(array));
    }

    public static DatasetList fromJSONArray(JSONArray array) {
        if (array == null) {
            return null;
        }
        DatasetList datasetList = new DatasetList();
        try {
            int cnt = array.length();
            for (int i = 0; i < cnt; i++) {
                Object value = array.get(i);
                if (value == null) {
                    datasetList.add(null);
                } else if (value instanceof JSONObject) {
                    datasetList.add(JSONObject.NULL.equals(value) ? null : DataMap.fromJSONObject((JSONObject) value));
                } else if (value instanceof DataMap) {
                    datasetList.add((IData) value);
                } else if (value instanceof String) {
                    if (!(value == null || ((String) value).indexOf("__classChangedByFrameWork__") == -1)) {
                        value = ((String) value).replaceAll("__classChangedByFrameWork__", BjConstant.ATTR_CLASS);
                    }
                    if (((String) value).startsWith("{")) {
                        datasetList.add(new DataMap((String) value));
                    } else if (((String) value).startsWith("[")) {
                        datasetList.add(new DatasetList((String) value));
                    } else {
                        datasetList.add(value);
                    }
                } else {
                    datasetList.add(value);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return datasetList;
    }

    public String[] getNames() {
        return size() > 0 ? ((IData) get(0)).getNames() : null;
    }

    public Object get(int index) {
        return super.get(index);
    }

    public Object get(int index, String name) {
        Object data = get(index);
        if (data == null || !(data instanceof Map)) {
            return null;
        }
        IData map = new DataMap();
        map.putAll((HashMap) data);
        return map.get(name);
    }

    public Object get(int index, String name, Object def) {
        Object value = get(index, name);
        return value == null ? def : value;
    }

    public IData getData(int index) {
        Object value = get(index);
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            return new DataMap((String) value);
        }
        if (value instanceof JSONObject) {
            return DataMap.fromJSONObject((JSONObject) value);
        }
        return (IData) value;
    }

    public IDataset getDataset(int index) {
        Object value = get(index);
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            return new DatasetList((String) value);
        }
        if (value instanceof JSONArray) {
            return fromJSONArray((JSONArray) value);
        }
        return (IDataset) value;
    }

    public IData first() {
        return size() > 0 ? (IData) get(0) : null;
    }

    public IData toData() {
        IData data = new DataMap();
        Iterator<Object> it = iterator();
        while (it.hasNext()) {
            IData element = (IData) it.next();
            for (String key : element.keySet()) {
                if (data.containsKey(key)) {
                    ((IDataset) data.get(key)).add(element.get(key));
                } else {
                    IDataset list = new DatasetList();
                    list.add(element.get(key));
                    data.put(key, list);
                }
            }
        }
        return data;
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
        return new JSONArray((Collection) this).toString();
    }
}
