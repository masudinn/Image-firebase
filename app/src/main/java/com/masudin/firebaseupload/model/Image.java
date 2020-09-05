package com.masudin.firebaseupload.model;

public class Image {
    private String imageDes;
    private String imageUrl;

    public Image(String imageDes, String imageUrl) {
        this.imageDes = imageDes;
        this.imageUrl = imageUrl;
    }

    public Image() {
    }

    public String getImageDes() {
        return imageDes;
    }

    public void setImageDes(String imageDes) {
        this.imageDes = imageDes;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
