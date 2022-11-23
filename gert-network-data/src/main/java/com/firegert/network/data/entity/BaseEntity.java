package com.firegert.network.data.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public abstract class BaseEntity<I extends Serializable> {

    @Id
    @Column("record_id")
    private I recordId;

    @Column("identifier")
    private UUID identifier;

    @Column("record_version")
    private int recordVersion;

}
