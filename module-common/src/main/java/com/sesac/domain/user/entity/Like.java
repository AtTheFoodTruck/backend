package com.sesac.domain.user.entity;

import com.sesac.domain.common.BaseEntity;
import com.sesac.domain.item.entity.Store;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Table(name = "likes")
@Getter @Setter
@Entity
public class Like extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    // User
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User User;

    // Store
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "store_id")
    private Store store;
}
