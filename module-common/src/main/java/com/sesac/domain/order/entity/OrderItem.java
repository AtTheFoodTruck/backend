package com.sesac.domain.order.entity;

import com.sesac.domain.common.BaseEntity;
import com.sesac.domain.item.entity.Item;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class OrderItem extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderItem_id")
    private Long id;
    private int count; //주문 수량
    private int orderPrice; //주문 가격

    // Order
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
}
