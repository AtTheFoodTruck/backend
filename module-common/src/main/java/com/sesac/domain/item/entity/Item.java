package com.sesac.domain.item.entity;

import com.sesac.domain.common.BaseEntity;
import com.sesac.domain.order.entity.Image;
import lombok.*;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Item extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;
    private String name;
    private String description;
    private int price;

    @Embedded
    private Image itemImg;

    // CartItem
    @OneToMany(mappedBy = "item")
    private List<CartItem> cartItems = new ArrayList<>();
}
