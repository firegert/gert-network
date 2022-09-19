package com.firegert.network.data.repo;

import com.firegert.network.data.entity.Network;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface NetworkRepository extends BaseRepository<Network, Long> {

    Optional<Network> findByName(String name);

}
