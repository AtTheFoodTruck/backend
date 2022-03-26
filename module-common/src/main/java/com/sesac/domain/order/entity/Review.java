package com.sesac.domain.order.entity;

import com.sesac.domain.common.BaseEntity;
import com.sesac.domain.item.entity.Store;
import com.sesac.domain.member.entity.Member;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Getter @Setter
@Entity
public class Review extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;
    private String content;
    private int rating;

    // Order
    @OneToOne(mappedBy = "review", fetch = LAZY)
    private Order order;

    // Member
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // Store
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

}
