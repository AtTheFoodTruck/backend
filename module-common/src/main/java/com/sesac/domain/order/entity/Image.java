package com.sesac.domain.order.entity;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Builder
@Getter
@Table(name = "images")
@Embeddable
public class Image {

    private String imgName;
    private String imgUrl;

    public Image() {
    }

    public Image(String imgName, String imgUrl) {
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }
}
