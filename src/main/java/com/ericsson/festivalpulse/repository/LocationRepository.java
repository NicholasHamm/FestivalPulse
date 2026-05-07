package com.ericsson.festivalpulse.repository;

import com.ericsson.festivalpulse.models.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {}
