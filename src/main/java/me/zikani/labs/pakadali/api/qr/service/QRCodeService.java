package me.zikani.labs.pakadali.api.qr.service;

import com.github.benmanes.caffeine.cache.Cache;

public interface QRCodeService {
    byte[] getQRCode(int size,String content, Cache<String, byte[]> cache) throws Exception;
}
