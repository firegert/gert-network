package com.firegert.network.data.repo;

import com.firegert.network.data.entity.BaseEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity<I>, I  extends Serializable> extends CrudRepository<T, I> {

}
