package com.sesac.domain.item.entity;

import com.sesac.domain.common.BaseEntity;
import com.sesac.domain.user.entity.Like;
import com.sesac.domain.order.entity.Image;
import com.sesac.domain.order.entity.Review;
import com.sesac.domain.user.entity.User;
import lombok.*;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
@Entity
public class Store extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;
    private String name;
    private String phoneNum;
    private boolean isOpen;
    private int totalWaitingCount;
    private String notice;
    private Double avgRate;
    private String bNo; // 사업자등록번호(13)
    private String pNm; // 사업자명
    private String sDt; // 개업일

    @Embedded
    private Address address;

    @Embedded
    private Image storeImage;

    // User
    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // Review
    @OneToMany(mappedBy = "store")
    private List<Review> reviews = new ArrayList<>();

    // Category
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    // Like
    @OneToMany(mappedBy = "store")
    private List<Like> likes = new ArrayList<>();
}
