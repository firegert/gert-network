package com.firegert.network.service;

import com.firegert.network.data.entity.Network;

public interface NetworkCrudService {

    Network createNetwork(String name);

    Network findByName(String name);

}
