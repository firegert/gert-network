package com.firegert.network.data.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public abstract class BaseEntity<I extends Serializable> {

    private I recordId;

    private UUID identifier;

    private int recordVersion;

}
