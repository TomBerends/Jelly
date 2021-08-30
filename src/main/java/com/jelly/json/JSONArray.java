package com.jelly.json;

import java.util.ArrayList;

public class JSONArray {
    private final ArrayList<Object> list;

    public JSONArray() {
        list = new ArrayList<>();
    }

    public void add(final Object value) {
        if (value == null)
            list.add(null);
        else if (value instanceof Boolean)
            add((boolean) value);
        else if (value instanceof Integer)
            add((int) value);
        else if (value instanceof Float)
            add((float) value);
        else if (value instanceof final String stringValue)
            add(stringValue);
        else if (value instanceof final JSONObject jsonObject)
            add(jsonObject);
        else if (value instanceof final JSONArray jsonArray)
            add(jsonArray);
        else
            throw new IllegalArgumentException("invalid type " + value.getClass().getSimpleName() + " for JSON value");
    }

    public void add(final boolean value) {
        list.add(value);
    }

    public void add(final int value) {
        list.add(value);
    }

    public void add(final float value) {
        list.add(value);
    }

    public void add(final String value) {
        list.add(value);
    }

    public void add(final JSONObject value) {
        list.add(value);
    }

    public void add(final JSONArray value) {
        list.add(value);
    }

    public boolean getBoolean(final int idx) {
        return (boolean) get(idx);
    }

    public Object get(final int idx) {
        return list.get(idx);
    }

    public int getInteger(final int idx) {
        return (int) get(idx);
    }

    public float getFloat(final int idx) {
        return (float) get(idx);
    }

    public String getString(final int idx) {
        return (String) get(idx);
    }

    public JSONObject getJSONObject(final int idx) {
        return (JSONObject) get(idx);
    }

    public JSONArray getJSONArray(final int idx) {
        return (JSONArray) get(idx);
    }

    @Override
    public String toString() {
        return list.toString();
    }

}
