package com.sesac.domain.item.entity;

import com.sesac.domain.common.BaseEntity;
import com.sesac.domain.user.entity.User;
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
public class Cart extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    // User
    @OneToOne(mappedBy = "cart", fetch = LAZY)
    private User user;

    // CartItem
    @OneToMany(mappedBy = "cart")
    private List<CartItem> cartItems = new ArrayList<>();
}
