package com.rduda.Model;

import com.rduda.API.Image;

/**
 * Created by Robin on 2015-11-26.
 * <p>
 * ImageBean.
 */
class ImageBean implements Image {
    private String base64;
    private Long id;

    public ImageBean(Image image) {
        this.base64 = image.getBase64();
        this.id = image.getId();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getBase64() {
        return base64;
    }
}
