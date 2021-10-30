package com.mapr.mgrweb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mapr.mgrweb.IntegrationTest;
import com.mapr.mgrweb.domain.MaprRequests;
import com.mapr.mgrweb.repository.MaprRequestsRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MaprRequestsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MaprRequestsResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_ACTION = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_REQUEST_USER = "AAAAAAAAAA";
    private static final String UPDATED_REQUEST_USER = "BBBBBBBBBB";

    private static final Instant DEFAULT_REQUEST_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REQUEST_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final Instant DEFAULT_STATUS_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_STATUS_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/mapr-requests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MaprRequestsRepository maprRequestsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMaprRequestsMockMvc;

    private MaprRequests maprRequests;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MaprRequests createEntity(EntityManager em) {
        MaprRequests maprRequests = new MaprRequests()
            .type(DEFAULT_TYPE)
            .action(DEFAULT_ACTION)
            .name(DEFAULT_NAME)
            .path(DEFAULT_PATH)
            .requestUser(DEFAULT_REQUEST_USER)
            .requestDate(DEFAULT_REQUEST_DATE)
            .status(DEFAULT_STATUS)
            .statusDate(DEFAULT_STATUS_DATE);
        return maprRequests;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MaprRequests createUpdatedEntity(EntityManager em) {
        MaprRequests maprRequests = new MaprRequests()
            .type(UPDATED_TYPE)
            .action(UPDATED_ACTION)
            .name(UPDATED_NAME)
            .path(UPDATED_PATH)
            .requestUser(UPDATED_REQUEST_USER)
            .requestDate(UPDATED_REQUEST_DATE)
            .status(UPDATED_STATUS)
            .statusDate(UPDATED_STATUS_DATE);
        return maprRequests;
    }

    @BeforeEach
    public void initTest() {
        maprRequests = createEntity(em);
    }

    @Test
    @Transactional
    void createMaprRequests() throws Exception {
        int databaseSizeBeforeCreate = maprRequestsRepository.findAll().size();
        // Create the MaprRequests
        restMaprRequestsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(maprRequests)))
            .andExpect(status().isCreated());

        // Validate the MaprRequests in the database
        List<MaprRequests> maprRequestsList = maprRequestsRepository.findAll();
        assertThat(maprRequestsList).hasSize(databaseSizeBeforeCreate + 1);
        MaprRequests testMaprRequests = maprRequestsList.get(maprRequestsList.size() - 1);
        assertThat(testMaprRequests.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testMaprRequests.getAction()).isEqualTo(DEFAULT_ACTION);
        assertThat(testMaprRequests.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMaprRequests.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(testMaprRequests.getRequestUser()).isEqualTo(DEFAULT_REQUEST_USER);
        assertThat(testMaprRequests.getRequestDate()).isEqualTo(DEFAULT_REQUEST_DATE);
        assertThat(testMaprRequests.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testMaprRequests.getStatusDate()).isEqualTo(DEFAULT_STATUS_DATE);
    }

    @Test
    @Transactional
    void createMaprRequestsWithExistingId() throws Exception {
        // Create the MaprRequests with an existing ID
        maprRequests.setId(1L);

        int databaseSizeBeforeCreate = maprRequestsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMaprRequestsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(maprRequests)))
            .andExpect(status().isBadRequest());

        // Validate the MaprRequests in the database
        List<MaprRequests> maprRequestsList = maprRequestsRepository.findAll();
        assertThat(maprRequestsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = maprRequestsRepository.findAll().size();
        // set the field null
        maprRequests.setType(null);

        // Create the MaprRequests, which fails.

        restMaprRequestsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(maprRequests)))
            .andExpect(status().isBadRequest());

        List<MaprRequests> maprRequestsList = maprRequestsRepository.findAll();
        assertThat(maprRequestsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActionIsRequired() throws Exception {
        int databaseSizeBeforeTest = maprRequestsRepository.findAll().size();
        // set the field null
        maprRequests.setAction(null);

        // Create the MaprRequests, which fails.

        restMaprRequestsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(maprRequests)))
            .andExpect(status().isBadRequest());

        List<MaprRequests> maprRequestsList = maprRequestsRepository.findAll();
        assertThat(maprRequestsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = maprRequestsRepository.findAll().size();
        // set the field null
        maprRequests.setName(null);

        // Create the MaprRequests, which fails.

        restMaprRequestsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(maprRequests)))
            .andExpect(status().isBadRequest());

        List<MaprRequests> maprRequestsList = maprRequestsRepository.findAll();
        assertThat(maprRequestsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPathIsRequired() throws Exception {
        int databaseSizeBeforeTest = maprRequestsRepository.findAll().size();
        // set the field null
        maprRequests.setPath(null);

        // Create the MaprRequests, which fails.

        restMaprRequestsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(maprRequests)))
            .andExpect(status().isBadRequest());

        List<MaprRequests> maprRequestsList = maprRequestsRepository.findAll();
        assertThat(maprRequestsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRequestUserIsRequired() throws Exception {
        int databaseSizeBeforeTest = maprRequestsRepository.findAll().size();
        // set the field null
        maprRequests.setRequestUser(null);

        // Create the MaprRequests, which fails.

        restMaprRequestsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(maprRequests)))
            .andExpect(status().isBadRequest());

        List<MaprRequests> maprRequestsList = maprRequestsRepository.findAll();
        assertThat(maprRequestsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRequestDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = maprRequestsRepository.findAll().size();
        // set the field null
        maprRequests.setRequestDate(null);

        // Create the MaprRequests, which fails.

        restMaprRequestsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(maprRequests)))
            .andExpect(status().isBadRequest());

        List<MaprRequests> maprRequestsList = maprRequestsRepository.findAll();
        assertThat(maprRequestsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = maprRequestsRepository.findAll().size();
        // set the field null
        maprRequests.setStatus(null);

        // Create the MaprRequests, which fails.

        restMaprRequestsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(maprRequests)))
            .andExpect(status().isBadRequest());

        List<MaprRequests> maprRequestsList = maprRequestsRepository.findAll();
        assertThat(maprRequestsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = maprRequestsRepository.findAll().size();
        // set the field null
        maprRequests.setStatusDate(null);

        // Create the MaprRequests, which fails.

        restMaprRequestsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(maprRequests)))
            .andExpect(status().isBadRequest());

        List<MaprRequests> maprRequestsList = maprRequestsRepository.findAll();
        assertThat(maprRequestsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMaprRequests() throws Exception {
        // Initialize the database
        maprRequestsRepository.saveAndFlush(maprRequests);

        // Get all the maprRequestsList
        restMaprRequestsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(maprRequests.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)))
            .andExpect(jsonPath("$.[*].requestUser").value(hasItem(DEFAULT_REQUEST_USER)))
            .andExpect(jsonPath("$.[*].requestDate").value(hasItem(DEFAULT_REQUEST_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].statusDate").value(hasItem(DEFAULT_STATUS_DATE.toString())));
    }

    @Test
    @Transactional
    void getMaprRequests() throws Exception {
        // Initialize the database
        maprRequestsRepository.saveAndFlush(maprRequests);

        // Get the maprRequests
        restMaprRequestsMockMvc
            .perform(get(ENTITY_API_URL_ID, maprRequests.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(maprRequests.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH))
            .andExpect(jsonPath("$.requestUser").value(DEFAULT_REQUEST_USER))
            .andExpect(jsonPath("$.requestDate").value(DEFAULT_REQUEST_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.statusDate").value(DEFAULT_STATUS_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingMaprRequests() throws Exception {
        // Get the maprRequests
        restMaprRequestsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMaprRequests() throws Exception {
        // Initialize the database
        maprRequestsRepository.saveAndFlush(maprRequests);

        int databaseSizeBeforeUpdate = maprRequestsRepository.findAll().size();

        // Update the maprRequests
        MaprRequests updatedMaprRequests = maprRequestsRepository.findById(maprRequests.getId()).get();
        // Disconnect from session so that the updates on updatedMaprRequests are not directly saved in db
        em.detach(updatedMaprRequests);
        updatedMaprRequests
            .type(UPDATED_TYPE)
            .action(UPDATED_ACTION)
            .name(UPDATED_NAME)
            .path(UPDATED_PATH)
            .requestUser(UPDATED_REQUEST_USER)
            .requestDate(UPDATED_REQUEST_DATE)
            .status(UPDATED_STATUS)
            .statusDate(UPDATED_STATUS_DATE);

        restMaprRequestsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMaprRequests.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMaprRequests))
            )
            .andExpect(status().isOk());

        // Validate the MaprRequests in the database
        List<MaprRequests> maprRequestsList = maprRequestsRepository.findAll();
        assertThat(maprRequestsList).hasSize(databaseSizeBeforeUpdate);
        MaprRequests testMaprRequests = maprRequestsList.get(maprRequestsList.size() - 1);
        assertThat(testMaprRequests.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testMaprRequests.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testMaprRequests.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMaprRequests.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testMaprRequests.getRequestUser()).isEqualTo(UPDATED_REQUEST_USER);
        assertThat(testMaprRequests.getRequestDate()).isEqualTo(UPDATED_REQUEST_DATE);
        assertThat(testMaprRequests.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testMaprRequests.getStatusDate()).isEqualTo(UPDATED_STATUS_DATE);
    }

    @Test
    @Transactional
    void putNonExistingMaprRequests() throws Exception {
        int databaseSizeBeforeUpdate = maprRequestsRepository.findAll().size();
        maprRequests.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaprRequestsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, maprRequests.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(maprRequests))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaprRequests in the database
        List<MaprRequests> maprRequestsList = maprRequestsRepository.findAll();
        assertThat(maprRequestsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMaprRequests() throws Exception {
        int databaseSizeBeforeUpdate = maprRequestsRepository.findAll().size();
        maprRequests.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaprRequestsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(maprRequests))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaprRequests in the database
        List<MaprRequests> maprRequestsList = maprRequestsRepository.findAll();
        assertThat(maprRequestsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMaprRequests() throws Exception {
        int databaseSizeBeforeUpdate = maprRequestsRepository.findAll().size();
        maprRequests.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaprRequestsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(maprRequests)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MaprRequests in the database
        List<MaprRequests> maprRequestsList = maprRequestsRepository.findAll();
        assertThat(maprRequestsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMaprRequestsWithPatch() throws Exception {
        // Initialize the database
        maprRequestsRepository.saveAndFlush(maprRequests);

        int databaseSizeBeforeUpdate = maprRequestsRepository.findAll().size();

        // Update the maprRequests using partial update
        MaprRequests partialUpdatedMaprRequests = new MaprRequests();
        partialUpdatedMaprRequests.setId(maprRequests.getId());

        partialUpdatedMaprRequests.statusDate(UPDATED_STATUS_DATE);

        restMaprRequestsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMaprRequests.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMaprRequests))
            )
            .andExpect(status().isOk());

        // Validate the MaprRequests in the database
        List<MaprRequests> maprRequestsList = maprRequestsRepository.findAll();
        assertThat(maprRequestsList).hasSize(databaseSizeBeforeUpdate);
        MaprRequests testMaprRequests = maprRequestsList.get(maprRequestsList.size() - 1);
        assertThat(testMaprRequests.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testMaprRequests.getAction()).isEqualTo(DEFAULT_ACTION);
        assertThat(testMaprRequests.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMaprRequests.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(testMaprRequests.getRequestUser()).isEqualTo(DEFAULT_REQUEST_USER);
        assertThat(testMaprRequests.getRequestDate()).isEqualTo(DEFAULT_REQUEST_DATE);
        assertThat(testMaprRequests.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testMaprRequests.getStatusDate()).isEqualTo(UPDATED_STATUS_DATE);
    }

    @Test
    @Transactional
    void fullUpdateMaprRequestsWithPatch() throws Exception {
        // Initialize the database
        maprRequestsRepository.saveAndFlush(maprRequests);

        int databaseSizeBeforeUpdate = maprRequestsRepository.findAll().size();

        // Update the maprRequests using partial update
        MaprRequests partialUpdatedMaprRequests = new MaprRequests();
        partialUpdatedMaprRequests.setId(maprRequests.getId());

        partialUpdatedMaprRequests
            .type(UPDATED_TYPE)
            .action(UPDATED_ACTION)
            .name(UPDATED_NAME)
            .path(UPDATED_PATH)
            .requestUser(UPDATED_REQUEST_USER)
            .requestDate(UPDATED_REQUEST_DATE)
            .status(UPDATED_STATUS)
            .statusDate(UPDATED_STATUS_DATE);

        restMaprRequestsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMaprRequests.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMaprRequests))
            )
            .andExpect(status().isOk());

        // Validate the MaprRequests in the database
        List<MaprRequests> maprRequestsList = maprRequestsRepository.findAll();
        assertThat(maprRequestsList).hasSize(databaseSizeBeforeUpdate);
        MaprRequests testMaprRequests = maprRequestsList.get(maprRequestsList.size() - 1);
        assertThat(testMaprRequests.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testMaprRequests.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testMaprRequests.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMaprRequests.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testMaprRequests.getRequestUser()).isEqualTo(UPDATED_REQUEST_USER);
        assertThat(testMaprRequests.getRequestDate()).isEqualTo(UPDATED_REQUEST_DATE);
        assertThat(testMaprRequests.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testMaprRequests.getStatusDate()).isEqualTo(UPDATED_STATUS_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingMaprRequests() throws Exception {
        int databaseSizeBeforeUpdate = maprRequestsRepository.findAll().size();
        maprRequests.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaprRequestsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, maprRequests.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(maprRequests))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaprRequests in the database
        List<MaprRequests> maprRequestsList = maprRequestsRepository.findAll();
        assertThat(maprRequestsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMaprRequests() throws Exception {
        int databaseSizeBeforeUpdate = maprRequestsRepository.findAll().size();
        maprRequests.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaprRequestsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(maprRequests))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaprRequests in the database
        List<MaprRequests> maprRequestsList = maprRequestsRepository.findAll();
        assertThat(maprRequestsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMaprRequests() throws Exception {
        int databaseSizeBeforeUpdate = maprRequestsRepository.findAll().size();
        maprRequests.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaprRequestsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(maprRequests))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MaprRequests in the database
        List<MaprRequests> maprRequestsList = maprRequestsRepository.findAll();
        assertThat(maprRequestsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMaprRequests() throws Exception {
        // Initialize the database
        maprRequestsRepository.saveAndFlush(maprRequests);

        int databaseSizeBeforeDelete = maprRequestsRepository.findAll().size();

        // Delete the maprRequests
        restMaprRequestsMockMvc
            .perform(delete(ENTITY_API_URL_ID, maprRequests.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MaprRequests> maprRequestsList = maprRequestsRepository.findAll();
        assertThat(maprRequestsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
