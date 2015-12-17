package com.rduda.API;

/**
 * Created by Robin on 2015-11-25.
 * <p>
 * Supplies image data and an image identification number
 * for caching and referencing.
 */
public interface Image {
    /**
     * Get the unique identifier of the image.
     *
     * @return A Long id.
     */
    public Long getId();

    /**
     * Get the image data.
     *
     * @return image data encoded as base64.
     */
    public String getBase64();
}
