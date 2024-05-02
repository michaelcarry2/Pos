package com.example.pos;

import java.util.Date;

public class productData {

    private Integer id;
    private String productID;
    private String productName;
    private String detail;
    private Integer stock;
    private Double price;
    private String category;
    private String location;
    private String status;
    private String image;
    private  Integer quantity;
    private Date date;

    public productData(Integer id, String productID, String productName, String detail, Integer stock, Double price, String category, String location, String status, String image, Date date){
        this.id = id;
        this.productID = productID;
        this.productName = productName;
        this.detail = detail;
        this.stock = stock;
        this.price = price;
        this.category = category;
        this.location = location;
        this.status = status;
        this.image = image;
        this.date = date;
    }

    public productData(Integer id, String productID, String productName,Integer quantity, Double price, String category, String image, Date date){
        this.id = id;
        this.productID = productID;
        this.productName = productName;
        this.price = price;
        this.category = category;
        this.image = image;
        this.quantity = quantity;
        this.date = date;

    }

    public Integer getId(){

        return  id;
    }
    public String getProductID(){
        return productID;
    }
    public String getProductName(){

        return productName;
    }
    public String getDetail(){

        return detail;
    }
    public Integer getStock(){

        return stock;
    }
    public Double getPrice(){

        return price;
    }

    public String getCategory(){

        return category;
    }
    public String getLocation(){

        return location;
    }

    public String getStatus(){
        return status;
    }

    public String getImage(){

        return image;
    }
    public Integer getQuantity(){
        return quantity;
    }
    public Date getDate(){
        return date;
    }
}
