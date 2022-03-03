package edu.vit.vtop.navapp.RecyclerviewModels;


public class SettingsModel {
    int img;
    String text;

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public SettingsModel(int img, String text) {
        this.img = img;
        this.text = text;
    }
}
