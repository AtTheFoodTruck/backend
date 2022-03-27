package com.sesac.domain.user.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Table(name = "authority")
@Getter
@Entity
public class Authority {
    @Id
    @Column(name = "authority_name", length = 50)
    private String authorityName;
}
