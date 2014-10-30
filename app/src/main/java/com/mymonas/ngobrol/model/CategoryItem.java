package com.mymonas.ngobrol.model;

import java.io.Serializable;

/**
 * Created by Huteri on 10/21/2014.
 */
public class CategoryItem implements Serializable{
    private int id;
    private String name, color, icon;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
