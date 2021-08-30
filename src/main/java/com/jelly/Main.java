package com.jelly;

import com.jelly.json.JSONObject;
import org.jetbrains.annotations.Contract;

import java.io.File;

import static com.jelly.parser.Parser.parseJSONObject;

public class Main {
    @Contract(pure = true)
    public static void main(final String[] args) throws Exception {
        final JSONObject jsonObject = parseJSONObject(new File("src/main/resources/source.json"));
        System.out.println(jsonObject.getString("firstName"));
        System.out.println(jsonObject);
    }
}
