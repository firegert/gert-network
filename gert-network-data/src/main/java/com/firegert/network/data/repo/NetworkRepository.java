package com.firegert.network.data.repo;

import com.firegert.network.data.entity.Network;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface NetworkRepository extends JpaRepository<Network, UUID> {

    Optional<Network> findByName(String name);
}
