package com.sesac.domain.member.entity;

import com.sesac.domain.common.BaseEntity;
import com.sesac.domain.item.entity.Cart;
import com.sesac.domain.item.entity.Store;
import com.sesac.domain.order.entity.Order;
import com.sesac.domain.order.entity.Review;
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
public class Member extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    private String email;
    private String username; //nickname
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

//    @Builder
//    public Member(String email, String username, String password, String phoneNum, boolean active, Role role, List<Order> orders, Store store, List<Review> reviews, List<Like> likes, Cart cart) {
//        this.email = email;
//        this.username = username;
//        this.password = password;
//        this.phoneNum = phoneNum;
//        this.active = active;
//        this.role = role;
//        this.orders = orders;
//        this.store = store;
//        this.reviews = reviews;
//        this.likes = likes;
//        this.cart = cart;
//    }
}
