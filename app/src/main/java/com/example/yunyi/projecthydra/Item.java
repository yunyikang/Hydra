package com.example.yunyi.projecthydra;

/**
 * Created by 1000593 on 7/12/16.
 */

public class Item {

    String animalName;
    int animalImage;

    public Item(String animalName, int animalImage)
    {
        this.animalImage=animalImage;
        this.animalName=animalName;
    }
    public String getAnimalName()
    {
        return animalName;
    }
    public int getAnimalImage()
    {
        return animalImage;
    }
}
