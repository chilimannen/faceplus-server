package Mock;

import com.rduda.API.Image;

/**
 * Created by Robin on 2015-11-26.
 * <p>
 * Mocks an image.
 */
class ImageMock implements Image {
    private Long id;
    private String base64;

    public ImageMock(String data) {
        this.id = (long) data.hashCode();
        this.base64 = data;
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
