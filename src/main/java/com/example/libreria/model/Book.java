package com.example.libreria.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    private String title;

    @NotNull
    private String author;
    @NotNull
    private int yearPubblication;
    @NotNull
    @Min(0)
    private double price;

    public Book() {
    }

    public Book(@NotNull String title, @NotNull String author, @NotNull int yearPubblication, @NotNull double price) {
        this.title = title;
        this.author = author;
        this.yearPubblication = yearPubblication;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYearPubblication() {
        return yearPubblication;
    }

    public void setYearPubblication(int yearPubblication) {
        this.yearPubblication = yearPubblication;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", yearPubblication=" + yearPubblication +
                ", price=" + price +
                '}';
    }
}
