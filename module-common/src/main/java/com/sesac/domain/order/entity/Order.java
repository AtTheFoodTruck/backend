package com.sesac.domain.order.entity;

import com.sesac.domain.common.BaseEntity;
import com.sesac.domain.user.entity.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "orders")
@Entity
public class Order extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orders_id")
    private Long id;
    private int waitingNum; // 대기번호
    private LocalDateTime orderDate;
    private boolean hasReview; // 리뷰 작성 여부

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    // User
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // OrderItem
    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    // Review
    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "review_id")
    private Review review;
}
