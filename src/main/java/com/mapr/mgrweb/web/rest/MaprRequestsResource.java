package com.mapr.mgrweb.web.rest;

import com.mapr.mgrweb.domain.MaprRequests;
import com.mapr.mgrweb.repository.MaprRequestsRepository;
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
 * REST controller for managing {@link com.mapr.mgrweb.domain.MaprRequests}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MaprRequestsResource {

    private final Logger log = LoggerFactory.getLogger(MaprRequestsResource.class);

    private static final String ENTITY_NAME = "maprRequests";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MaprRequestsRepository maprRequestsRepository;

    public MaprRequestsResource(MaprRequestsRepository maprRequestsRepository) {
        this.maprRequestsRepository = maprRequestsRepository;
    }

    /**
     * {@code POST  /mapr-requests} : Create a new maprRequests.
     *
     * @param maprRequests the maprRequests to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new maprRequests, or with status {@code 400 (Bad Request)} if the maprRequests has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/mapr-requests")
    public ResponseEntity<MaprRequests> createMaprRequests(@Valid @RequestBody MaprRequests maprRequests) throws URISyntaxException {
        log.debug("REST request to save MaprRequests : {}", maprRequests);
        if (maprRequests.getId() != null) {
            throw new BadRequestAlertException("A new maprRequests cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MaprRequests result = maprRequestsRepository.save(maprRequests);
        return ResponseEntity
            .created(new URI("/api/mapr-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /mapr-requests/:id} : Updates an existing maprRequests.
     *
     * @param id the id of the maprRequests to save.
     * @param maprRequests the maprRequests to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated maprRequests,
     * or with status {@code 400 (Bad Request)} if the maprRequests is not valid,
     * or with status {@code 500 (Internal Server Error)} if the maprRequests couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/mapr-requests/{id}")
    public ResponseEntity<MaprRequests> updateMaprRequests(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MaprRequests maprRequests
    ) throws URISyntaxException {
        log.debug("REST request to update MaprRequests : {}, {}", id, maprRequests);
        if (maprRequests.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, maprRequests.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!maprRequestsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MaprRequests result = maprRequestsRepository.save(maprRequests);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, maprRequests.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /mapr-requests/:id} : Partial updates given fields of an existing maprRequests, field will ignore if it is null
     *
     * @param id the id of the maprRequests to save.
     * @param maprRequests the maprRequests to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated maprRequests,
     * or with status {@code 400 (Bad Request)} if the maprRequests is not valid,
     * or with status {@code 404 (Not Found)} if the maprRequests is not found,
     * or with status {@code 500 (Internal Server Error)} if the maprRequests couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/mapr-requests/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MaprRequests> partialUpdateMaprRequests(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MaprRequests maprRequests
    ) throws URISyntaxException {
        log.debug("REST request to partial update MaprRequests partially : {}, {}", id, maprRequests);
        if (maprRequests.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, maprRequests.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!maprRequestsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MaprRequests> result = maprRequestsRepository
            .findById(maprRequests.getId())
            .map(existingMaprRequests -> {
                if (maprRequests.getType() != null) {
                    existingMaprRequests.setType(maprRequests.getType());
                }
                if (maprRequests.getAction() != null) {
                    existingMaprRequests.setAction(maprRequests.getAction());
                }
                if (maprRequests.getName() != null) {
                    existingMaprRequests.setName(maprRequests.getName());
                }
                if (maprRequests.getPath() != null) {
                    existingMaprRequests.setPath(maprRequests.getPath());
                }
                if (maprRequests.getRequestUser() != null) {
                    existingMaprRequests.setRequestUser(maprRequests.getRequestUser());
                }
                if (maprRequests.getRequestDate() != null) {
                    existingMaprRequests.setRequestDate(maprRequests.getRequestDate());
                }
                if (maprRequests.getStatus() != null) {
                    existingMaprRequests.setStatus(maprRequests.getStatus());
                }
                if (maprRequests.getStatusDate() != null) {
                    existingMaprRequests.setStatusDate(maprRequests.getStatusDate());
                }

                return existingMaprRequests;
            })
            .map(maprRequestsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, maprRequests.getId().toString())
        );
    }

    /**
     * {@code GET  /mapr-requests} : get all the maprRequests.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of maprRequests in body.
     */
    @GetMapping("/mapr-requests")
    public List<MaprRequests> getAllMaprRequests() {
        log.debug("REST request to get all MaprRequests");
        return maprRequestsRepository.findAll();
    }

    /**
     * {@code GET  /mapr-requests/:id} : get the "id" maprRequests.
     *
     * @param id the id of the maprRequests to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the maprRequests, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/mapr-requests/{id}")
    public ResponseEntity<MaprRequests> getMaprRequests(@PathVariable Long id) {
        log.debug("REST request to get MaprRequests : {}", id);
        Optional<MaprRequests> maprRequests = maprRequestsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(maprRequests);
    }

    /**
     * {@code DELETE  /mapr-requests/:id} : delete the "id" maprRequests.
     *
     * @param id the id of the maprRequests to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/mapr-requests/{id}")
    public ResponseEntity<Void> deleteMaprRequests(@PathVariable Long id) {
        log.debug("REST request to delete MaprRequests : {}", id);
        maprRequestsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
