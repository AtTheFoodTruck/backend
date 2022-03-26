package com.sesac.domain.order.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

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
