package be.ehb.dig_x.ricardo.werkstuk_android.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "shoes")
public class ShoeEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String modelNr;
    private String brand;
    private String modelName;
    private String colorway;
    private String retailPrice;
    private String imageUrl;

    public ShoeEntry(int id, String modelNr, String brand, String modelName, String colorway, String retailPrice, String imageUrl) {
        this.id = id;
        this.modelNr = modelNr;
        this.brand = brand;
        this.modelName = modelName;
        this.colorway = colorway;
        this.retailPrice = retailPrice;
        this.imageUrl = imageUrl;
    }
    @Ignore
    public ShoeEntry(String modelNr, String brand, String modelName, String colorway, String retailPrice, String imageUrl) {
        this.modelNr = modelNr;
        this.brand = brand;
        this.modelName = modelName;
        this.colorway = colorway;
        this.retailPrice = retailPrice;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModelNr() {
        return modelNr;
    }

    public void setModelNr(String modelNr) {
        this.modelNr = modelNr;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getColorway() {
        return colorway;
    }

    public void setColorway(String colorway) {
        this.colorway = colorway;
    }

    public String getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(String retailPrice) {
        this.retailPrice = retailPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
