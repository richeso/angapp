package com.mapr.mgrweb.repository;

import com.mapr.mgrweb.domain.MaprRequests;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MaprRequests entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MaprRequestsRepository extends JpaRepository<MaprRequests, Long> {}
