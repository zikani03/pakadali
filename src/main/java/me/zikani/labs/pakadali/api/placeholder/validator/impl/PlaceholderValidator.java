package me.zikani.labs.pakadali.api.placeholder.validator.impl;

import static java.lang.Integer.parseInt;

public class PlaceholderValidator {
    private static final int MAX_WIDTH_HEIGHT = 9999;

    // Validate dimensions in given string in (AxB) format
    public static boolean isWidthAndHeightValid(String spec) {
        if (spec == null) return false;

        if (!spec.toLowerCase().contains("x")) {
            return false;
        }
        String[] wh = spec.toLowerCase().split("x", 2);
        if (wh.length < 2) return false;
        if (!wh[0].matches("[0-9]+") && wh[0].length() > 1) {
            return false;
        }
        if (!wh[1].matches("[0-9]+") && wh[1].length() > 1) {
            return false;
        }
        int[] dimensions = new int[]{ parseInt(wh[0]), parseInt(wh[1]) };
        int width = dimensions[0];
        int height = dimensions[1];
        return (width >= 0 && width <= MAX_WIDTH_HEIGHT) && (height >= 0 && height <= MAX_WIDTH_HEIGHT);
    }
}
