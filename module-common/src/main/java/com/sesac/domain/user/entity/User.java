package com.sesac.domain.user.entity;

import com.sesac.domain.common.BaseEntity;
import com.sesac.domain.item.entity.Cart;
import com.sesac.domain.item.entity.Store;
import com.sesac.domain.order.entity.Order;
import com.sesac.domain.order.entity.Review;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class User extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String email;
    private String username; //nickname
    private String password;
    private String phoneNum;
    private boolean activated;

//    @Enumerated(EnumType.STRING)
//    private Role role;

    // Order
    @OneToMany(mappedBy = "User")
    private List<Order> orders = new ArrayList<>();

    // Store
    @OneToOne(fetch = LAZY, mappedBy = "User")
    private Store store;

    // Review
    @OneToMany(mappedBy = "User")
    private List<Review> reviews = new ArrayList<>();

    // Like
    @OneToMany(mappedBy = "User")
    private List<Like> likes = new ArrayList<>();

    // Cart
    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL) // User 삭제 시 cart 삭제?
    @JoinColumn(name = "cart_id")
    private Cart cart;

    //Authority
    @ManyToMany
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private List<Authority> authorities = new ArrayList<>();
}
