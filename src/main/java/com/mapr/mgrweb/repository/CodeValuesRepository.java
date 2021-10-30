package com.mapr.mgrweb.repository;

import com.mapr.mgrweb.domain.CodeValues;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CodeValues entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CodeValuesRepository extends JpaRepository<CodeValues, Long> {}
