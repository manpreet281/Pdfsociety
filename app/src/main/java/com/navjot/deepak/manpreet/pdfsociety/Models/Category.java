package com.navjot.deepak.manpreet.pdfsociety.Models;

/**
 * Created by manpreet on 30/11/17.
 */

public class Category {

    private int category_pic_id;
    private String category_name;

    public Category(){}

    public Category(String category_name,int category_pic_id) {
        this.category_name = category_name;
        this.category_pic_id = category_pic_id;
    }

    public int getCategory_pic_id() {
        return category_pic_id;
    }

    public void setCategory_pic_id(int category_pic_id) {
        this.category_pic_id = category_pic_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

}
