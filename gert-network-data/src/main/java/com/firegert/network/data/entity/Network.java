package com.firegert.network.data.entity;

import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Network extends BaseEntity<Long> {

    private String name;

}
