package com.example.springbatchdemo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name="Coffee")
public class Coffee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coffee_id")
    private Integer id;

    @Column(name = "brand")
    private String brand;
    private String origin;
    private String characteristics;

    public Coffee(String brand, String origin, String characteristics) {
        this.brand = brand;
        this.origin = origin;
        this.characteristics = characteristics;
    }


    public Coffee(Integer id, String brand, String origin, String characteristics) {
        this.id = id;
        this.brand = brand;
        this.origin = origin;
        this.characteristics = characteristics;
    }

    public Coffee() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    // getters and setters
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(String characteristics) {
        this.characteristics = characteristics;
    }
    @Override
    public String toString() {
        return "Coffee [brand=" + getBrand() + ", origin=" + getOrigin() + ", characteristics=" + getCharacteristics() + "]";
    }
}
