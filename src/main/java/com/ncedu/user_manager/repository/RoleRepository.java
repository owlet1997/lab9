package com.ncedu.user_manager.repository;

import com.ncedu.user_manager.model.entity.RoleEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends CrudRepository<RoleEntity, UUID> {

    Optional<RoleEntity> findByCode(String code);
    boolean existsByCode(String code);

}
