package com.mymonas.ngobrol.io.model;

import com.mymonas.ngobrol.model.CategoryItem;

import java.util.ArrayList;

/**
 * Created by Huteri on 10/21/2014.
 */
public class CategoryCallback extends BaseCallback{
    private ArrayList<CategoryItem> data;

    public ArrayList<CategoryItem> getData() {
        return data;
    }

    public void setData(ArrayList<CategoryItem> data) {
        this.data = data;
    }
}
