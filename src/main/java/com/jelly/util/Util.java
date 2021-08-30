package com.jelly.util;

import java.util.HashMap;
import java.util.Map;

public class Util {
    public static final char[] LINE_SEPARATOR = System.lineSeparator().toCharArray();

    private static final Map<Character, Character> escapeMap = new HashMap<>();

    static {
        escapeMap.put('\b', 'b');
        escapeMap.put('\t', 't');
        escapeMap.put('\r', 'r');
        escapeMap.put('\f', 'f');
        escapeMap.put('\n', 'n');
        escapeMap.put('\\', '\\');
        escapeMap.put('"', '"');
        escapeMap.put('\'', '\'');
    }

    public static String escape(final String str) {
        final StringBuilder escapeBuilder = new StringBuilder(str.length());
        System.out.println(str.translateEscapes());

        for(final char c : str.toCharArray()) {
            if(escapeMap.containsKey(c))
                escapeBuilder.append('\\').append(escapeMap.get(c));
            else
                escapeBuilder.append(c);
        }

        return escapeBuilder.toString();
    }

    public static String escape(final char c) {
        if(escapeMap.containsKey(c))
            return "\\" + escapeMap.get(c);
        else
            return String.valueOf(c);
    }
}
