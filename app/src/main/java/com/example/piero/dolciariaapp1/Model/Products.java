package com.example.piero.dolciariaapp1.Model;

public class Products {

    private String pname, price, description, image, pid, time, date, category;

    public Products() {

    }

    public Products(String pname, String price, String description, String image, String pid, String time, String date, String category) {
        this.pname = pname;
        this.price = price;
        this.description = description;
        this.image = image;
        this.pid = pid;
        this.time = time;
        this.date = date;
        this.category = category;
    }


    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
