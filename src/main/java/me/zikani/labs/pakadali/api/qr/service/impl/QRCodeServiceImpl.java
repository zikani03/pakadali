package me.zikani.labs.pakadali.api.qr.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import me.zikani.labs.pakadali.api.qr.service.interfaces.QRCodeService;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;

import java.io.ByteArrayOutputStream;

import static me.zikani.labs.pakadali.api.utils.HashUtil.sha256;

public class QRCodeServiceImpl implements QRCodeService {
    @Override
    public  byte[] getQRCode(int widthAndHeight,String content, Cache<String, byte[]> cache) throws Exception {
        final String hashed = sha256(widthAndHeight + "/" + content);
        final String qrCacheID = String.format("qr__%s", hashed);
        byte[] image = cache.getIfPresent(qrCacheID);

        if (image == null) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            QRCode.from(content)
                    .withSize(widthAndHeight, widthAndHeight)
                    .to(ImageType.PNG)
                    .writeTo(bos);
            image = bos.toByteArray();
            cache.put(qrCacheID + "", image);
        }

        return image;
    }
}
