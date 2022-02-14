package com.example.myapplication;
import java.util.Date;


        ////////////////////////////////
///     //     KLASA PRODUKTU         //    ///
        ////////////////////////////////

public class Item
{
    String name;
    String category;
    String subCategory;
    Integer amount;
    Date date;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getSubCategory()
    {
        return subCategory;
    }

    public Integer getAmount()
    {
        return amount;
    }

    public Date getDate()
    {
        return date;
    }

    public Item(String name, String subCategory, String category, Integer amount, Date date)
    {
        this.name = name;
        this.subCategory = subCategory;
        this.category = category;
        this.amount = amount;
        this.date = date;
    }

}
