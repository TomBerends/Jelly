package com.jelly.json;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class JSONObject {
    final Map<String, Object> fields;

    public JSONObject() {
        fields = new HashMap<>();
    }

    public void set(final String fieldName, final Object value) {
        if(value == null)
            fields.put(fieldName, null);
        else if(value instanceof Boolean)
            set(fieldName, (boolean) value);
        else if(value instanceof Integer)
            set(fieldName, (int) value);
        else if(value instanceof Float)
            set(fieldName, (float) value);
        else if(value instanceof final String stringValue)
            set(fieldName, stringValue);
        else if(value instanceof final JSONObject jsonObject)
            set(fieldName, jsonObject);
        else if(value instanceof final JSONArray jsonArray)
            set(fieldName, jsonArray);
        else
            throw new IllegalArgumentException("invalid type " + value.getClass().getSimpleName() + " for JSON value");
    }

    public void set(final String fieldName, final boolean value) {
        fields.put(Objects.requireNonNull(fieldName), value);
    }

    public void set(final String fieldName, final int value) {
        fields.put(Objects.requireNonNull(fieldName), value);
    }

    public void set(final String fieldName, final float value) {
        fields.put(Objects.requireNonNull(fieldName), value);
    }

    public void set(final String fieldName, final String value) {
        fields.put(Objects.requireNonNull(fieldName), value);
    }

    public void set(final String fieldName, final JSONObject value) {
        fields.put(Objects.requireNonNull(fieldName), value);
    }

    public void set(final String fieldName, final JSONArray value) {
        fields.put(Objects.requireNonNull(fieldName), value);
    }

    public Object get(final String fieldName) {
        return fields.get(fieldName);
    }

    public boolean getBoolean(final String fieldName) {
        return (boolean) get(fieldName);
    }

    public int getInteger(final String fieldName) {
        return (int) get(fieldName);
    }

    public float getFloat(final String fieldName) {
        return (float) get(fieldName);
    }

    public String getString(final String fieldName) {
        return (String) get(fieldName);
    }

    public JSONObject getJSONObject(final String fieldName) {
        return (JSONObject) get(fieldName);
    }

    public JSONArray getJSONArray(final String fieldName) {
        return (JSONArray) get(fieldName);
    }

    @Override
    public String toString() {
        return fields.toString();
    }
}
