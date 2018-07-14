package com.caesar.ken.coralfits;

/**
 * Created by e on 6/13/2018.
 */

public class CoralModelClass {

    private String title;
    private String imageId;

    public CoralModelClass(String title, String imageId) {
        this.title = title;
        this.imageId = imageId;
    }

    public CoralModelClass() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
}
