package com.quest94.demo.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternTest {

    private static final Pattern NAME_SEPARATOR = Pattern.compile("\\$\\{abc}");

    public static void main(String[] args) {
        String string = replace("${abc}", "xx");
        System.out.println(string);
    }

    private static String replace(CharSequence input, CharSequence replaceString) {
        StringBuilder stringBuilder = new StringBuilder();
        Matcher m = NAME_SEPARATOR.matcher(input);
        int index = 0;
        while (m.find()) {
            if (index == 0 && index == m.start() && m.start() == m.end()) {
                // no empty leading substring included for zero-width match at the beginning of the input char sequence.
                continue;
            }
            stringBuilder.append(input.subSequence(index, m.start()));
            stringBuilder.append(replaceString);
            index = m.end();
        }
        // If no match was found, return this
        if (index == 0)
            return input.toString();

        // Add remaining segment
        stringBuilder.append(input.subSequence(index, input.length()).toString());

        return stringBuilder.toString();
    }

}
