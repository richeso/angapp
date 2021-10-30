package com.mapr.mgrweb.web.rest;

import com.mapr.mgrweb.domain.CodeTables;
import com.mapr.mgrweb.repository.CodeTablesRepository;
import com.mapr.mgrweb.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mapr.mgrweb.domain.CodeTables}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CodeTablesResource {

    private final Logger log = LoggerFactory.getLogger(CodeTablesResource.class);

    private static final String ENTITY_NAME = "codeTables";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CodeTablesRepository codeTablesRepository;

    public CodeTablesResource(CodeTablesRepository codeTablesRepository) {
        this.codeTablesRepository = codeTablesRepository;
    }

    /**
     * {@code POST  /code-tables} : Create a new codeTables.
     *
     * @param codeTables the codeTables to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new codeTables, or with status {@code 400 (Bad Request)} if the codeTables has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/code-tables")
    public ResponseEntity<CodeTables> createCodeTables(@Valid @RequestBody CodeTables codeTables) throws URISyntaxException {
        log.debug("REST request to save CodeTables : {}", codeTables);
        if (codeTables.getId() != null) {
            throw new BadRequestAlertException("A new codeTables cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CodeTables result = codeTablesRepository.save(codeTables);
        return ResponseEntity
            .created(new URI("/api/code-tables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /code-tables/:id} : Updates an existing codeTables.
     *
     * @param id the id of the codeTables to save.
     * @param codeTables the codeTables to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated codeTables,
     * or with status {@code 400 (Bad Request)} if the codeTables is not valid,
     * or with status {@code 500 (Internal Server Error)} if the codeTables couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/code-tables/{id}")
    public ResponseEntity<CodeTables> updateCodeTables(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CodeTables codeTables
    ) throws URISyntaxException {
        log.debug("REST request to update CodeTables : {}, {}", id, codeTables);
        if (codeTables.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, codeTables.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!codeTablesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CodeTables result = codeTablesRepository.save(codeTables);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, codeTables.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /code-tables/:id} : Partial updates given fields of an existing codeTables, field will ignore if it is null
     *
     * @param id the id of the codeTables to save.
     * @param codeTables the codeTables to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated codeTables,
     * or with status {@code 400 (Bad Request)} if the codeTables is not valid,
     * or with status {@code 404 (Not Found)} if the codeTables is not found,
     * or with status {@code 500 (Internal Server Error)} if the codeTables couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/code-tables/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CodeTables> partialUpdateCodeTables(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CodeTables codeTables
    ) throws URISyntaxException {
        log.debug("REST request to partial update CodeTables partially : {}, {}", id, codeTables);
        if (codeTables.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, codeTables.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!codeTablesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CodeTables> result = codeTablesRepository
            .findById(codeTables.getId())
            .map(existingCodeTables -> {
                if (codeTables.getTableName() != null) {
                    existingCodeTables.setTableName(codeTables.getTableName());
                }

                return existingCodeTables;
            })
            .map(codeTablesRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, codeTables.getId().toString())
        );
    }

    /**
     * {@code GET  /code-tables} : get all the codeTables.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of codeTables in body.
     */
    @GetMapping("/code-tables")
    public List<CodeTables> getAllCodeTables() {
        log.debug("REST request to get all CodeTables");
        return codeTablesRepository.findAll();
    }

    /**
     * {@code GET  /code-tables/:id} : get the "id" codeTables.
     *
     * @param id the id of the codeTables to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the codeTables, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/code-tables/{id}")
    public ResponseEntity<CodeTables> getCodeTables(@PathVariable Long id) {
        log.debug("REST request to get CodeTables : {}", id);
        Optional<CodeTables> codeTables = codeTablesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(codeTables);
    }

    /**
     * {@code DELETE  /code-tables/:id} : delete the "id" codeTables.
     *
     * @param id the id of the codeTables to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/code-tables/{id}")
    public ResponseEntity<Void> deleteCodeTables(@PathVariable Long id) {
        log.debug("REST request to delete CodeTables : {}", id);
        codeTablesRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
