package me.zikani.labs.pakadali.api.placeholder.service;


import com.github.benmanes.caffeine.cache.Cache;

/**
 *  Handles access to PNGImagePlaceHolders methods
 **/
public interface PNGImagePlaceHolderService {
    byte[] getImagePlaceHolder(int width, int height, Cache<String, byte[]> cache) throws Exception;
}
