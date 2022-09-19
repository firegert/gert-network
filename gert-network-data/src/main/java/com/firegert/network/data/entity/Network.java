package com.firegert.network.data.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "networks")
public class Network extends BaseEntity {

    @Column(name = "name", nullable = false, unique = true)
    private String name;

}
