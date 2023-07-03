package com.marketplace.profile.repository;

import com.marketplace.profile.entity.Profile;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProfileRepo extends ReactiveCrudRepository<Profile, Long> {

    Mono<Profile> findProfileByUsername(String firstName);

    Mono<Profile> findById(String userId);

    @Query(value = "select * from profiles as p where p.blocked=$1 order by $2 offset $3 limit $4")
    Flux<Profile> getAllBlockedOrUnblockedProfiles(boolean blocked, String property, int page, int size);

}
