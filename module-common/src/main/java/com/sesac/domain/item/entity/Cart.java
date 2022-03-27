package com.sesac.domain.item.entity;

import com.sesac.domain.common.BaseEntity;
import com.sesac.domain.user.entity.Member;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Getter @Setter
@Entity
public class Cart extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    // Member
    @OneToOne(mappedBy = "cart", fetch = LAZY)
    private Member member;

    // CartItem
    @OneToMany(mappedBy = "cart")
    private List<CartItem> cartItems = new ArrayList<>();
}
