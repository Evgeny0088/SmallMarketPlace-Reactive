package com.marketplace.profile.repository;

import com.marketplace.profile.entity.Role;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface RoleRepo extends ReactiveCrudRepository<Role, Long> {

    Mono<Role> findByName(String roleName);

    Mono<Boolean> existsByName(String roleName);
}
