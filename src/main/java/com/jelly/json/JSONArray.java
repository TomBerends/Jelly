package com.jelly.json;

import java.util.ArrayList;

/**
 * Runtime-Representation of the array type in JSON.
 * <p>
 * This class is implemented as an ArrayList-Delegator with additional type-checking
 * and an enhanced {@link #toString()} method to generate a valid JSON-String to represent this JSONArray.
 * @author Tom Berends
 */
public final class JSONArray {
    /**
     * ArrayList holding all objects in this JSONArray.
     */
    private final ArrayList<Object> list;

    public JSONArray() {
        list = new ArrayList<>();
    }

    /**
     * General method to add an object to this JSONArray.
     * <p>
     * Note that additional type-checking is needed in order to only allow valid JSON-Types to be added.
     * <p>
     * For each valid JSON-Type there is a separate method that gets called by this method.
     *
     * @param value value to be added to this JSONArray.
     * @throws IllegalArgumentException if {@code value} is not a valid JSON-Type.
     * @implNote The necessary type-checking in this method can be expensive so that it should only be called when the class of {@code value} is unknown.
     *           Otherwise the respective separate add method should be called.
     * @see #add(boolean)
     * @see #add(int)
     * @see #add(float)
     * @see #add(String)
     * @see #add(JSONObject)
     * @see #add(JSONArray)
     */
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

    /**
     * Adds a boolean value to this JSONArray.
     * @param value boolean to be added
     */
    public void add(final boolean value) {
        list.add(value);
    }

    /**
     * Adds an integer value to this JSONArray.
     * @param value integer to be added
     */
    public void add(final int value) {
        list.add(value);
    }

    /**
     * Adds a float value to this JSONArray.
     * @param value float to be added
     */
    public void add(final float value) {
        list.add(value);
    }

    /**
     * Adds a String to this JSONArray.
     * @param value String to be added.
     */
    public void add(final String value) {
        list.add(value);
    }

    /**
     * Adds a JSONObject to this JSONArray.
     * @param value JSONObject to be added.
     * @see JSONObject
     */
    public void add(final JSONObject value) {
        list.add(value);
    }

    /**
     * Adds a JSONArray to this JSONArray.
     * @param value JSONArray to be added.
     * @see JSONArray
     */
    public void add(final JSONArray value) {
        list.add(value);
    }

    /**
     * Returns the Object at the specified position in this JSONArray.
     *
     * @param idx position of the Object to return.
     * @return the Object at the specified position in this JSONArray.
     */
    public Object get(final int idx) {
        return list.get(idx);
    }

    /**
     * Returns the Object at the specified position in this JSONArray casted to a boolean value.
     * <p>
     * If the type of the Object at the specified position is unknown the more general {@link #get(int)} should be called
     * as this method throws an Exception when the Object at the specified position cannot be cast to a boolean.      
     * 
     * @param idx position of the boolean to return.
     * @return the boolean value at the specified position.
     * @throws ClassCastException if the Object at the specified position cannot be cast to a boolean value.
     * @see #get(int)
     */
    public boolean getBoolean(final int idx) {
        return (boolean) get(idx);
    }

    /**
     * Returns the Object at the specified position in this JSONArray casted to an integer value.
     * <p>
     * If the type of the Object at the specified position is unknown the more general {@link #get(int)} should be called
     * as this method throws an Exception when the Object at the specified position cannot be cast to an integer.      
     *
     * @param idx position of the integer to return.
     * @return the integer value at the specified position.
     * @throws ClassCastException if the Object at the specified position cannot be cast to an integer value.
     * @see #get(int)
     */
    public int getInteger(final int idx) {
        return (int) get(idx);
    }

    /**
     * Returns the Object at the specified position in this JSONArray casted to a float value.
     * <p>
     * If the type of the Object at the specified position is unknown the more general {@link #get(int)} should be called
     * as this method throws an Exception when the Object at the specified position cannot be cast to a float.      
     *
     * @param idx position of the float to return.
     * @return the float value at the specified position.
     * @throws ClassCastException if the Object at the specified position cannot be cast to a float value.
     * @see #get(int)
     */
    public float getFloat(final int idx) {
        return (float) get(idx);
    }

    /**
     * Returns the Object at the specified position in this JSONArray casted to a String.
     * <p>
     * If the type of the Object at the specified position is unknown the more general {@link #get(int)} should be called
     * as this method throws an Exception when the Object at the specified position cannot be cast to a String.      
     *
     * @param idx position of the String to return.
     * @return the String at the specified position.
     * @throws ClassCastException if the Object at the specified position cannot be cast to a String.
     * @see #get(int)
     */
    public String getString(final int idx) {
        return (String) get(idx);
    }

    /**
     * Returns the Object at the specified position in this JSONArray casted to a JSONObject.
     * <p>
     * If the type of the Object at the specified position is unknown the more general {@link #get(int)} should be called
     * as this method throws an Exception when the Object at the specified position cannot be cast to a JSONObject.      
     *
     * @param idx position of the JSONObject to return.
     * @return the JSONObject at the specified position.
     * @throws ClassCastException if the Object at the specified position cannot be cast to a JSONObject.
     * @see #get(int)
     * @see JSONObject
     */
    public JSONObject getJSONObject(final int idx) {
        return (JSONObject) get(idx);
    }

    /**
     * Returns the Object at the specified position in this JSONArray casted to a JSONArray.
     * <p>
     * If the type of the Object at the specified position is unknown the more general {@link #get(int)} should be called
     * as this method throws an Exception when the Object at the specified position cannot be cast to a JSONArray.
     *
     * @param idx position of the JSONArray to return.
     * @return the JSONArray at the specified position.
     * @throws ClassCastException if the Object at the specified position cannot be cast to a JSONArray.
     * @see #get(int)
     * @see JSONArray
     */
    public JSONArray getJSONArray(final int idx) {
        return (JSONArray) get(idx);
    }

    /**
     * Produces a valid JSON-String describing this JSONArray.
     *
     * @return valid JSON-Code describing this JSONArray.
     */
    @Override
    public String toString() {
        return list.toString();
    }
}
