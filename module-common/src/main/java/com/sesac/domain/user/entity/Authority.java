package com.sesac.domain.user.entity;

import lombok.*;

import javax.annotation.PostConstruct;
import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "authority")
@Entity
public class Authority {
    @Id
    @Column(name = "authority_name", length = 50)
    private String authorityName;
}
