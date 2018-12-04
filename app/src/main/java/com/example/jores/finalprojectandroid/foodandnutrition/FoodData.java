package com.example.jores.finalprojectandroid.foodandnutrition;

import android.util.Log;

import org.json.JSONObject;

class FoodData {
    private String foodID;
    private String label;
    private String brand;
    private String category;
    private String contents;
    private double kCal;
    private double procnt;
    private double fat;
    private double chocdf;
    private double fibtg;

    public FoodData() {
        this.foodID = null;
        this.label = null;
        this.brand = null;
        this.category = null;
        this.contents = null;
        this.kCal = 0;
        this.procnt = 0;
        this.fat = 0;
        this.chocdf = 0;
        this.fibtg = 0;
    }

    public FoodData(String foodID, String label, String brand, String category, String contents, Double kCal, Double procnt, Double fat, Double chocdf, Double fibtg) {
        this.foodID = foodID;
        this.label = label;
        this.brand = brand;
        this.category = category;
        this.contents = contents;
        this.kCal = kCal;
        this.procnt = procnt;
        this.fat = fat;
        this.chocdf = chocdf;
        this.fibtg = fibtg;
    }

    public FoodData(JSONObject foodJSON){
        try {
            if (foodJSON.has("foodId"))
                setFoodID(foodJSON.getString("foodId"));
            if (foodJSON.has("label"))
                setLabel(foodJSON.getString("label"));
            if (foodJSON.has("nutrients")) {
                JSONObject nut = foodJSON.getJSONObject("nutrients");
                if (nut.has("ENERC_KCAL"))
                    setkCal(nut.getDouble("ENERC_KCAL"));
                if (nut.has("PROCNT"))
                    setProcnt(nut.getDouble("PROCNT"));
                if (nut.has("FAT"))
                    setFat(nut.getDouble("FAT"));
                if (nut.has("CHOCDF"))
                    setChocdf(nut.getDouble("CHOCDF"));
                if (nut.has("FIBTG"))
                    setFibtg(nut.getDouble("FIBTG"));
            }
            if (foodJSON.has("brand"))
                setBrand(foodJSON.getString("brand"));
            if (foodJSON.has("category"))
                setCategory(foodJSON.getString("category"));
            if (foodJSON.has("foodContentsLabel"))
                setContents(foodJSON.getString("foodContentsLabel"));
        } catch (Exception e){
            Log.e(FoodData.class.getSimpleName(), e.toString());
        }
    }

    public String getFoodID() {
        return foodID;
    }

    public void setFoodID(String foodID) {
        this.foodID = foodID;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Double getkCal() {
        return kCal;
    }

    public void setkCal(Double kCal) {
        this.kCal = kCal;
    }

    public Double getProcnt() {
        return procnt;
    }

    public void setProcnt(Double procnt) {
        this.procnt = procnt;
    }

    public Double getFat() {
        return fat;
    }

    public void setFat(Double fat) {
        this.fat = fat;
    }

    public Double getChocdf() {
        return chocdf;
    }

    public void setChocdf(Double chocdf) {
        this.chocdf = chocdf;
    }

    public Double getFibtg() {
        return fibtg;
    }

    public void setFibtg(Double fibtg) {
        this.fibtg = fibtg;
    }
}
