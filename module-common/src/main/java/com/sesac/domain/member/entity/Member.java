package com.sesac.domain.member.entity;

import com.sesac.domain.common.BaseEntity;
import com.sesac.domain.item.entity.Cart;
import com.sesac.domain.item.entity.Store;
import com.sesac.domain.order.entity.Order;
import com.sesac.domain.order.entity.Review;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
public class Member extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    private String email;
    private String name;
    private String password;
    private String phoneNum;
    private boolean active;

    @Enumerated(EnumType.STRING)
    private Role role;

    // Order
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    // Store
    @OneToOne(fetch = LAZY, mappedBy = "member")
    private Store store;

    // Review
    @OneToMany(mappedBy = "member")
    private List<Review> reviews = new ArrayList<>();

    // Like
    @OneToMany(mappedBy = "member")
    private List<Like> likes = new ArrayList<>();

    // Cart
    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL) // member 삭제 시 cart 삭제?
    @JoinColumn(name = "cart_id")
    private Cart cart;
}
