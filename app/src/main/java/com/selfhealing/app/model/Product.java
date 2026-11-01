package com.selfhealing.app.model;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double price;

    // Constructors
    public Product() {}

    public Product(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    // Getters & Setters
    public Long getId()             { return id; }
    public String getName()         { return name; }
    public void setName(String n)   { this.name = n; }
    public Double getPrice()        { return price; }
    public void setPrice(Double p)  { this.price = p; }
}
