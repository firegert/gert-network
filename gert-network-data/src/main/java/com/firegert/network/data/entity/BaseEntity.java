package com.firegert.network.data.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "identifier", unique = true, updatable = false, insertable = false)
    private UUID identifier;

    @Version
    @Column(name = "record_version")
    private int recordVersion;

}
