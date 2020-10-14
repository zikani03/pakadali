package me.zikani.labs.pakadali.api.placeholder.validator.impl;

import static java.lang.Integer.parseInt;

public class PlaceholderValidator {
    private static final int MAX_WIDTH_HEIGHT = 9999;

    // Parse and validate dimensions in given string in (AxB) format
    public static int[] parseImageWidthAndHeight(String spec) {
        if (spec == null) return null;

        if (!spec.toLowerCase().contains("x")) {
            return null;
        }
        String[] wh = spec.toLowerCase().split("x", 2);
        if (wh.length < 2) return null;
        // later can provide regex to check string contains numbers
        int[] dimensions = new int[]{ parseInt(wh[0]), parseInt(wh[1]) };
        int width = dimensions[0];
        int height = dimensions[1];
        if ((width < 0 || width > MAX_WIDTH_HEIGHT) || (height < 0 || height > MAX_WIDTH_HEIGHT)) {
            return null;
        }
        return dimensions;
    }
}
