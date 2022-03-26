package com.sesac.domain.item.entity;

import lombok.Getter;

import javax.persistence.Embeddable;

@Getter
@Embeddable
public class Address {

    private String city;
    private String street;
    private String zipCode;
    private Double latitude;
    private Double longitude;

    public Address() {
    }

    public Address(String city, String street, String zipCode, Double latitude, Double longitude) {
        this.city = city;
        this.street = street;
        this.zipCode = zipCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}