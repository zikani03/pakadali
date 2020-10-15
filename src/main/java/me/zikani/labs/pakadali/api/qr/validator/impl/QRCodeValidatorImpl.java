package me.zikani.labs.pakadali.api.qr.validator.impl;

import me.zikani.labs.pakadali.api.qr.validator.QRCodeValidator;

public class QRCodeValidatorImpl implements QRCodeValidator {
    private static final int QR_MAX_SIZE = 9999;
    public boolean isQRCodeSizeValid(String size) {
        if (size == null) return false;
        size = size.trim();
        if (!size.matches("[0-9]+") && size.length() > 1) {
           return false;
        }
        int widthAndHeight = Integer.parseInt(size);
        return widthAndHeight >= 0 && widthAndHeight <= QR_MAX_SIZE;
    }
}
