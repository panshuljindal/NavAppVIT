package edu.vit.vtop.navapp.RecyclerviewModels;

public class PlacesModel {
    String placeName;
    String categoriesName;

    public PlacesModel(String placeName, String categoriesName) {
        this.placeName = placeName;
        this.categoriesName = categoriesName;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getCategoriesName() {
        return categoriesName;
    }

    public void setCategoriesName(String categoriesName) {
        this.categoriesName = categoriesName;
    }


}
