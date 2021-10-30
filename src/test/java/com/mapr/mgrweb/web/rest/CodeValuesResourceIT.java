package com.mapr.mgrweb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mapr.mgrweb.IntegrationTest;
import com.mapr.mgrweb.domain.CodeValues;
import com.mapr.mgrweb.repository.CodeValuesRepository;
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
 * Integration tests for the {@link CodeValuesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CodeValuesResourceIT {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/code-values";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CodeValuesRepository codeValuesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCodeValuesMockMvc;

    private CodeValues codeValues;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CodeValues createEntity(EntityManager em) {
        CodeValues codeValues = new CodeValues().key(DEFAULT_KEY).value(DEFAULT_VALUE);
        return codeValues;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CodeValues createUpdatedEntity(EntityManager em) {
        CodeValues codeValues = new CodeValues().key(UPDATED_KEY).value(UPDATED_VALUE);
        return codeValues;
    }

    @BeforeEach
    public void initTest() {
        codeValues = createEntity(em);
    }

    @Test
    @Transactional
    void createCodeValues() throws Exception {
        int databaseSizeBeforeCreate = codeValuesRepository.findAll().size();
        // Create the CodeValues
        restCodeValuesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(codeValues)))
            .andExpect(status().isCreated());

        // Validate the CodeValues in the database
        List<CodeValues> codeValuesList = codeValuesRepository.findAll();
        assertThat(codeValuesList).hasSize(databaseSizeBeforeCreate + 1);
        CodeValues testCodeValues = codeValuesList.get(codeValuesList.size() - 1);
        assertThat(testCodeValues.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testCodeValues.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void createCodeValuesWithExistingId() throws Exception {
        // Create the CodeValues with an existing ID
        codeValues.setId(1L);

        int databaseSizeBeforeCreate = codeValuesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCodeValuesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(codeValues)))
            .andExpect(status().isBadRequest());

        // Validate the CodeValues in the database
        List<CodeValues> codeValuesList = codeValuesRepository.findAll();
        assertThat(codeValuesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = codeValuesRepository.findAll().size();
        // set the field null
        codeValues.setKey(null);

        // Create the CodeValues, which fails.

        restCodeValuesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(codeValues)))
            .andExpect(status().isBadRequest());

        List<CodeValues> codeValuesList = codeValuesRepository.findAll();
        assertThat(codeValuesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCodeValues() throws Exception {
        // Initialize the database
        codeValuesRepository.saveAndFlush(codeValues);

        // Get all the codeValuesList
        restCodeValuesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(codeValues.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getCodeValues() throws Exception {
        // Initialize the database
        codeValuesRepository.saveAndFlush(codeValues);

        // Get the codeValues
        restCodeValuesMockMvc
            .perform(get(ENTITY_API_URL_ID, codeValues.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(codeValues.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    void getNonExistingCodeValues() throws Exception {
        // Get the codeValues
        restCodeValuesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCodeValues() throws Exception {
        // Initialize the database
        codeValuesRepository.saveAndFlush(codeValues);

        int databaseSizeBeforeUpdate = codeValuesRepository.findAll().size();

        // Update the codeValues
        CodeValues updatedCodeValues = codeValuesRepository.findById(codeValues.getId()).get();
        // Disconnect from session so that the updates on updatedCodeValues are not directly saved in db
        em.detach(updatedCodeValues);
        updatedCodeValues.key(UPDATED_KEY).value(UPDATED_VALUE);

        restCodeValuesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCodeValues.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCodeValues))
            )
            .andExpect(status().isOk());

        // Validate the CodeValues in the database
        List<CodeValues> codeValuesList = codeValuesRepository.findAll();
        assertThat(codeValuesList).hasSize(databaseSizeBeforeUpdate);
        CodeValues testCodeValues = codeValuesList.get(codeValuesList.size() - 1);
        assertThat(testCodeValues.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testCodeValues.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void putNonExistingCodeValues() throws Exception {
        int databaseSizeBeforeUpdate = codeValuesRepository.findAll().size();
        codeValues.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCodeValuesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, codeValues.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(codeValues))
            )
            .andExpect(status().isBadRequest());

        // Validate the CodeValues in the database
        List<CodeValues> codeValuesList = codeValuesRepository.findAll();
        assertThat(codeValuesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCodeValues() throws Exception {
        int databaseSizeBeforeUpdate = codeValuesRepository.findAll().size();
        codeValues.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCodeValuesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(codeValues))
            )
            .andExpect(status().isBadRequest());

        // Validate the CodeValues in the database
        List<CodeValues> codeValuesList = codeValuesRepository.findAll();
        assertThat(codeValuesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCodeValues() throws Exception {
        int databaseSizeBeforeUpdate = codeValuesRepository.findAll().size();
        codeValues.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCodeValuesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(codeValues)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CodeValues in the database
        List<CodeValues> codeValuesList = codeValuesRepository.findAll();
        assertThat(codeValuesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCodeValuesWithPatch() throws Exception {
        // Initialize the database
        codeValuesRepository.saveAndFlush(codeValues);

        int databaseSizeBeforeUpdate = codeValuesRepository.findAll().size();

        // Update the codeValues using partial update
        CodeValues partialUpdatedCodeValues = new CodeValues();
        partialUpdatedCodeValues.setId(codeValues.getId());

        restCodeValuesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCodeValues.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCodeValues))
            )
            .andExpect(status().isOk());

        // Validate the CodeValues in the database
        List<CodeValues> codeValuesList = codeValuesRepository.findAll();
        assertThat(codeValuesList).hasSize(databaseSizeBeforeUpdate);
        CodeValues testCodeValues = codeValuesList.get(codeValuesList.size() - 1);
        assertThat(testCodeValues.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testCodeValues.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateCodeValuesWithPatch() throws Exception {
        // Initialize the database
        codeValuesRepository.saveAndFlush(codeValues);

        int databaseSizeBeforeUpdate = codeValuesRepository.findAll().size();

        // Update the codeValues using partial update
        CodeValues partialUpdatedCodeValues = new CodeValues();
        partialUpdatedCodeValues.setId(codeValues.getId());

        partialUpdatedCodeValues.key(UPDATED_KEY).value(UPDATED_VALUE);

        restCodeValuesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCodeValues.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCodeValues))
            )
            .andExpect(status().isOk());

        // Validate the CodeValues in the database
        List<CodeValues> codeValuesList = codeValuesRepository.findAll();
        assertThat(codeValuesList).hasSize(databaseSizeBeforeUpdate);
        CodeValues testCodeValues = codeValuesList.get(codeValuesList.size() - 1);
        assertThat(testCodeValues.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testCodeValues.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingCodeValues() throws Exception {
        int databaseSizeBeforeUpdate = codeValuesRepository.findAll().size();
        codeValues.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCodeValuesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, codeValues.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(codeValues))
            )
            .andExpect(status().isBadRequest());

        // Validate the CodeValues in the database
        List<CodeValues> codeValuesList = codeValuesRepository.findAll();
        assertThat(codeValuesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCodeValues() throws Exception {
        int databaseSizeBeforeUpdate = codeValuesRepository.findAll().size();
        codeValues.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCodeValuesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(codeValues))
            )
            .andExpect(status().isBadRequest());

        // Validate the CodeValues in the database
        List<CodeValues> codeValuesList = codeValuesRepository.findAll();
        assertThat(codeValuesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCodeValues() throws Exception {
        int databaseSizeBeforeUpdate = codeValuesRepository.findAll().size();
        codeValues.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCodeValuesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(codeValues))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CodeValues in the database
        List<CodeValues> codeValuesList = codeValuesRepository.findAll();
        assertThat(codeValuesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCodeValues() throws Exception {
        // Initialize the database
        codeValuesRepository.saveAndFlush(codeValues);

        int databaseSizeBeforeDelete = codeValuesRepository.findAll().size();

        // Delete the codeValues
        restCodeValuesMockMvc
            .perform(delete(ENTITY_API_URL_ID, codeValues.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CodeValues> codeValuesList = codeValuesRepository.findAll();
        assertThat(codeValuesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
