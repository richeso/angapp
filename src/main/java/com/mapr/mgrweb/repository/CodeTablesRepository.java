package com.mapr.mgrweb.repository;

import com.mapr.mgrweb.domain.CodeTables;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CodeTables entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CodeTablesRepository extends JpaRepository<CodeTables, Long> {}
