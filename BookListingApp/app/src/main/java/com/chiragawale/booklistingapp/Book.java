package com.chiragawale.booklistingapp;

/**
 * Created by chirag on 6/22/2017.
 */

public class Book {
    String name,authors,link,currencyCode;
    double rating,price;

    public Book() {
    }

    public Book(String name, String authors, String link, String currencyCode, double rating, double price) {
        this.name = name;
        this.authors = authors;
        this.link = link;
        this.currencyCode = currencyCode;
        this.rating = rating;
        this.price = price;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
