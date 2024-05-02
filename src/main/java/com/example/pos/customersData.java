package com.example.pos;

import java.util.Date;

public class customersData {
    private Integer id;
    private Integer customerID;
    private Double total;
    private Date date;
    private String user;

    public customersData(Integer id, Integer customerID, Double total, Date date, String user){
        this.id = id;
        this.customerID = customerID;
        this.total = total;
        this.date = date;
        this.user = user;
    }
    public Integer getId(){
        return id;
    }
    public Integer getCustomerID(){
        return customerID;
    }
    public Double getTotal(){
        return total;
    }
    public Date getDate(){
        return date;
    }
    public String getUser(){
        return user;
    }

}
