package com.sesac.domain.user.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "authority")
@Getter
public class Authority {
    @Id
    @Column(name = "authority_name", length = 50)
    private String authorityName;
}
