package com.mapr.mgrweb.web.rest;

import com.mapr.mgrweb.domain.CodeValues;
import com.mapr.mgrweb.repository.CodeValuesRepository;
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
 * REST controller for managing {@link com.mapr.mgrweb.domain.CodeValues}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CodeValuesResource {

    private final Logger log = LoggerFactory.getLogger(CodeValuesResource.class);

    private static final String ENTITY_NAME = "codeValues";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CodeValuesRepository codeValuesRepository;

    public CodeValuesResource(CodeValuesRepository codeValuesRepository) {
        this.codeValuesRepository = codeValuesRepository;
    }

    /**
     * {@code POST  /code-values} : Create a new codeValues.
     *
     * @param codeValues the codeValues to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new codeValues, or with status {@code 400 (Bad Request)} if the codeValues has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/code-values")
    public ResponseEntity<CodeValues> createCodeValues(@Valid @RequestBody CodeValues codeValues) throws URISyntaxException {
        log.debug("REST request to save CodeValues : {}", codeValues);
        if (codeValues.getId() != null) {
            throw new BadRequestAlertException("A new codeValues cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CodeValues result = codeValuesRepository.save(codeValues);
        return ResponseEntity
            .created(new URI("/api/code-values/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /code-values/:id} : Updates an existing codeValues.
     *
     * @param id the id of the codeValues to save.
     * @param codeValues the codeValues to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated codeValues,
     * or with status {@code 400 (Bad Request)} if the codeValues is not valid,
     * or with status {@code 500 (Internal Server Error)} if the codeValues couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/code-values/{id}")
    public ResponseEntity<CodeValues> updateCodeValues(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CodeValues codeValues
    ) throws URISyntaxException {
        log.debug("REST request to update CodeValues : {}, {}", id, codeValues);
        if (codeValues.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, codeValues.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!codeValuesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CodeValues result = codeValuesRepository.save(codeValues);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, codeValues.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /code-values/:id} : Partial updates given fields of an existing codeValues, field will ignore if it is null
     *
     * @param id the id of the codeValues to save.
     * @param codeValues the codeValues to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated codeValues,
     * or with status {@code 400 (Bad Request)} if the codeValues is not valid,
     * or with status {@code 404 (Not Found)} if the codeValues is not found,
     * or with status {@code 500 (Internal Server Error)} if the codeValues couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/code-values/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CodeValues> partialUpdateCodeValues(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CodeValues codeValues
    ) throws URISyntaxException {
        log.debug("REST request to partial update CodeValues partially : {}, {}", id, codeValues);
        if (codeValues.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, codeValues.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!codeValuesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CodeValues> result = codeValuesRepository
            .findById(codeValues.getId())
            .map(existingCodeValues -> {
                if (codeValues.getKey() != null) {
                    existingCodeValues.setKey(codeValues.getKey());
                }
                if (codeValues.getValue() != null) {
                    existingCodeValues.setValue(codeValues.getValue());
                }

                return existingCodeValues;
            })
            .map(codeValuesRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, codeValues.getId().toString())
        );
    }

    /**
     * {@code GET  /code-values} : get all the codeValues.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of codeValues in body.
     */
    @GetMapping("/code-values")
    public List<CodeValues> getAllCodeValues() {
        log.debug("REST request to get all CodeValues");
        return codeValuesRepository.findAll();
    }

    /**
     * {@code GET  /code-values/:id} : get the "id" codeValues.
     *
     * @param id the id of the codeValues to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the codeValues, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/code-values/{id}")
    public ResponseEntity<CodeValues> getCodeValues(@PathVariable Long id) {
        log.debug("REST request to get CodeValues : {}", id);
        Optional<CodeValues> codeValues = codeValuesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(codeValues);
    }

    /**
     * {@code DELETE  /code-values/:id} : delete the "id" codeValues.
     *
     * @param id the id of the codeValues to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/code-values/{id}")
    public ResponseEntity<Void> deleteCodeValues(@PathVariable Long id) {
        log.debug("REST request to delete CodeValues : {}", id);
        codeValuesRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
