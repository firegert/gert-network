package com.firegert.network.data.entity;

import lombok.*;
import org.springframework.data.relational.core.mapping.Column;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Network extends BaseEntity<Long> {

    @Column("name")
    private String name;
    @Column("description")
    private String description;

}
