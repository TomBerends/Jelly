package com.jelly.json;

import java.util.HashMap;

/**
 * Runtime-Representation of the object type in JSON.
 * <p>
 * This class is implemented as an HashMap-Delegator with additional type-checking
 * and an enhanced {@link #toString()} method to generate a valid JSON-String to represent this JSONObject.
 * @author Tom Berends
 */
public final class JSONObject {
    /**
     * HashMap holding all fields in this JSONObject.
     */
    private final HashMap<String, Object> fields;

    public JSONObject() {
        fields = new HashMap<>();
    }

    /**
     * General method to set a field in JSONObject.
     * <p>
     * Note that additional type-checking is needed in order to only allow valid JSON-Types to be set.
     * <p>
     * For each valid JSON-Type there is a separate method that gets called by this method.
     *
     * @param fieldName name of the field to be set.
     * @param value value the field is to be set to.
     * @throws IllegalArgumentException if {@code value} is not a valid JSON-Type.
     * @implNote The necessary type-checking in this method can be expensive so that it should only be called when the class of {@code value} is unknown.
     *           Otherwise the respective separate add method should be called.
     * @see #set(String, boolean)
     * @see #set(String, int)
     * @see #set(String, float)
     * @see #set(String, String)
     * @see #set(String, JSONObject)
     * @see #set(String, JSONArray)
     */
    public void set(final String fieldName, final Object value) {
        if (value == null)
            fields.put(fieldName, null);
        else if (value instanceof Boolean)
            set(fieldName, (boolean) value);
        else if (value instanceof Integer)
            set(fieldName, (int) value);
        else if (value instanceof Float)
            set(fieldName, (float) value);
        else if (value instanceof final String stringValue)
            set(fieldName, stringValue);
        else if (value instanceof final JSONObject jsonObject)
            set(fieldName, jsonObject);
        else if (value instanceof final JSONArray jsonArray)
            set(fieldName, jsonArray);
        else
            throw new IllegalArgumentException("invalid type " + value.getClass().getSimpleName() + " for JSON value");
    }

    /**
     * Sets the Field in this JSONObject with the specified name to a boolean value.
     * @param fieldName name of the field to set.
     * @param value value the field is to be set to.
     */
    public void set(final String fieldName, final boolean value) {
        fields.put(fieldName, value);
    }

    /**
     * Sets the Field in this JSONObject with the specified name to an integer value.
     * @param fieldName name of the field to set.
     * @param value value the field is to be set to.
     */
    public void set(final String fieldName, final int value) {
        fields.put(fieldName, value);
    }

    /**
     * Sets the Field in this JSONObject with the specified name to a float value.
     * @param fieldName name of the field to set.
     * @param value value the field is to be set to.
     */
    public void set(final String fieldName, final float value) {
        fields.put(fieldName, value);
    }

    /**
     * Sets the Field in this JSONObject with the specified name to a String.
     * @param fieldName name of the field to set.
     * @param value value the field is to be set to..
     */
    public void set(final String fieldName, final String value) {
        fields.put(fieldName, value);
    }

    /**
     * Sets the Field in this JSONObject with the specified name to a JSONObject.
     * @param fieldName name of the field to set.
     * @param value value the field is to be set to..
     * @see JSONObject
     */
    public void set(final String fieldName, final JSONObject value) {
        fields.put(fieldName, value);
    }

    /**
     * Sets the Field in this JSONObject with the specified name to a JSONArray.
     * @param fieldName name of the field to set.
     * @param value value the field is to be set to..
     * @see JSONArray
     */
    public void set(final String fieldName, final JSONArray value) {
        fields.put(fieldName, value);
    }

    /**
     * Returns the value of the field with the specified name.
     *
     * @param fieldName name of the field.
     * @return the value of the field with the specified name.
     */
    public Object get(final String fieldName) {
        return fields.get(fieldName);
    }

    /**
     * Returns the value of the field with the specified name cast to a boolean value.
     * <p>
     * If the type of the value of the specified field is unknown the more general {@link #get(String)} method should be called
     * as this method throws an Exception when the Object at the specified position cannot be cast to a boolean value.
     *
     * @param fieldName the name of the field.
     * @return the value of the field with the specified name.
     * @throws ClassCastException if the Object at the specified position cannot be cast to a boolean value.
     * @see #get(String)
     */
    public boolean getBoolean(final String fieldName) {
        return (boolean) get(fieldName);
    }

    /**
     * Returns the value of the field with the specified name cast to an integer value.
     * <p>
     * If the type of the value of the specified field is unknown the more general {@link #get(String)} method should be called
     * as this method throws an Exception when the Object at the specified position cannot be cast to an integer value.
     *
     * @param fieldName the name of the field.
     * @return the value of the field with the specified name.
     * @throws ClassCastException if the Object at the specified position cannot be cast to an integer value.
     * @see #get(String)
     */
    public int getInteger(final String fieldName) {
        return (int) get(fieldName);
    }

    /**
     * Returns the value of the field with the specified name cast to a float value.
     * <p>
     * If the type of the value of the specified field is unknown the more general {@link #get(String)} method should be called
     * as this method throws an Exception when the Object at the specified position cannot be cast to a float value.
     *
     * @param fieldName the name of the field.
     * @return the value of the field with the specified name.
     * @throws ClassCastException if the Object at the specified position cannot be cast to a float value.
     * @see #get(String)
     */
    public float getFloat(final String fieldName) {
        return (float) get(fieldName);
    }

    /**
     * Returns the value of the field with the specified name cast to a String.
     * <p>
     * If the type of the value of the specified field is unknown the more general {@link #get(String)} method should be called
     * as this method throws an Exception when the Object at the specified position cannot be cast to a String.
     *
     * @param fieldName the name of the field.
     * @return the value of the field with the specified name.
     * @throws ClassCastException if the Object at the specified position cannot be cast to a String.
     * @see #get(String)
     */
    public String getString(final String fieldName) {
        return (String) get(fieldName);
    }

    /**
     * Returns the value of the field with the specified name cast to a JSONObject.
     * <p>
     * If the type of the value of the specified field is unknown the more general {@link #get(String)} method should be called
     * as this method throws an Exception when the Object at the specified position cannot be cast to a JSONObject.
     *
     * @param fieldName the name of the field.
     * @return the value of the field with the specified name.
     * @throws ClassCastException if the Object at the specified position cannot be cast to a JSONObject.
     * @see #get(String)
     * @see JSONObject
     */
    public JSONObject getJSONObject(final String fieldName) {
        return (JSONObject) get(fieldName);
    }

    /**
     * Returns the value of the field with the specified name cast to a JSONArray.
     * <p>
     * If the type of the value of the specified field is unknown the more general {@link #get(String)} method should be called
     * as this method throws an Exception when the Object at the specified position cannot be cast to a JSONArray.
     *
     * @param fieldName the name of the field.
     * @return the value of the field with the specified name.
     * @throws ClassCastException if the Object at the specified position cannot be cast to a JSONArray.
     * @see #get(String)
     * @see JSONArray
     */
    public JSONArray getJSONArray(final String fieldName) {
        return (JSONArray) get(fieldName);
    }

    /**
     * Produces a valid JSON-String describing this JSONObject.
     *
     * @return valid JSON-Code describing this JSONObject.
     */
    @Override
    public String toString() {
        return fields.toString();
    }
}
