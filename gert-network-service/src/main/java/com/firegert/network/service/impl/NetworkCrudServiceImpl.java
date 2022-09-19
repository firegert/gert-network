package com.firegert.network.service.impl;

import com.firegert.network.data.entity.Network;
import com.firegert.network.data.repo.NetworkRepository;
import com.firegert.network.service.NetworkCrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NetworkCrudServiceImpl implements NetworkCrudService {

    private final NetworkRepository networkRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Network createNetwork(String name) {
        Network network = new Network();
        network.setName(name);
        return networkRepository.save(network);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Network findByName(String name) {
        return networkRepository.findByName(name).orElse(null);
    }
}
