package com.caesar.ken.coralfits;

/**
 * Created by e on 6/13/2018.
 */

public class CoralModelClass {

    private String Title;
    private String imageId;

    public CoralModelClass(String title, String imageId) {
        this.Title = title;
        this.imageId = imageId;
    }

    public CoralModelClass() {
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
}
