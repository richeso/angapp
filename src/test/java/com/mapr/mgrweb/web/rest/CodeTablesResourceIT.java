package com.mapr.mgrweb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mapr.mgrweb.IntegrationTest;
import com.mapr.mgrweb.domain.CodeTables;
import com.mapr.mgrweb.repository.CodeTablesRepository;
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
 * Integration tests for the {@link CodeTablesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CodeTablesResourceIT {

    private static final String DEFAULT_TABLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TABLE_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/code-tables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CodeTablesRepository codeTablesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCodeTablesMockMvc;

    private CodeTables codeTables;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CodeTables createEntity(EntityManager em) {
        CodeTables codeTables = new CodeTables().tableName(DEFAULT_TABLE_NAME);
        return codeTables;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CodeTables createUpdatedEntity(EntityManager em) {
        CodeTables codeTables = new CodeTables().tableName(UPDATED_TABLE_NAME);
        return codeTables;
    }

    @BeforeEach
    public void initTest() {
        codeTables = createEntity(em);
    }

    @Test
    @Transactional
    void createCodeTables() throws Exception {
        int databaseSizeBeforeCreate = codeTablesRepository.findAll().size();
        // Create the CodeTables
        restCodeTablesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(codeTables)))
            .andExpect(status().isCreated());

        // Validate the CodeTables in the database
        List<CodeTables> codeTablesList = codeTablesRepository.findAll();
        assertThat(codeTablesList).hasSize(databaseSizeBeforeCreate + 1);
        CodeTables testCodeTables = codeTablesList.get(codeTablesList.size() - 1);
        assertThat(testCodeTables.getTableName()).isEqualTo(DEFAULT_TABLE_NAME);
    }

    @Test
    @Transactional
    void createCodeTablesWithExistingId() throws Exception {
        // Create the CodeTables with an existing ID
        codeTables.setId(1L);

        int databaseSizeBeforeCreate = codeTablesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCodeTablesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(codeTables)))
            .andExpect(status().isBadRequest());

        // Validate the CodeTables in the database
        List<CodeTables> codeTablesList = codeTablesRepository.findAll();
        assertThat(codeTablesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTableNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = codeTablesRepository.findAll().size();
        // set the field null
        codeTables.setTableName(null);

        // Create the CodeTables, which fails.

        restCodeTablesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(codeTables)))
            .andExpect(status().isBadRequest());

        List<CodeTables> codeTablesList = codeTablesRepository.findAll();
        assertThat(codeTablesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCodeTables() throws Exception {
        // Initialize the database
        codeTablesRepository.saveAndFlush(codeTables);

        // Get all the codeTablesList
        restCodeTablesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(codeTables.getId().intValue())))
            .andExpect(jsonPath("$.[*].tableName").value(hasItem(DEFAULT_TABLE_NAME)));
    }

    @Test
    @Transactional
    void getCodeTables() throws Exception {
        // Initialize the database
        codeTablesRepository.saveAndFlush(codeTables);

        // Get the codeTables
        restCodeTablesMockMvc
            .perform(get(ENTITY_API_URL_ID, codeTables.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(codeTables.getId().intValue()))
            .andExpect(jsonPath("$.tableName").value(DEFAULT_TABLE_NAME));
    }

    @Test
    @Transactional
    void getNonExistingCodeTables() throws Exception {
        // Get the codeTables
        restCodeTablesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCodeTables() throws Exception {
        // Initialize the database
        codeTablesRepository.saveAndFlush(codeTables);

        int databaseSizeBeforeUpdate = codeTablesRepository.findAll().size();

        // Update the codeTables
        CodeTables updatedCodeTables = codeTablesRepository.findById(codeTables.getId()).get();
        // Disconnect from session so that the updates on updatedCodeTables are not directly saved in db
        em.detach(updatedCodeTables);
        updatedCodeTables.tableName(UPDATED_TABLE_NAME);

        restCodeTablesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCodeTables.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCodeTables))
            )
            .andExpect(status().isOk());

        // Validate the CodeTables in the database
        List<CodeTables> codeTablesList = codeTablesRepository.findAll();
        assertThat(codeTablesList).hasSize(databaseSizeBeforeUpdate);
        CodeTables testCodeTables = codeTablesList.get(codeTablesList.size() - 1);
        assertThat(testCodeTables.getTableName()).isEqualTo(UPDATED_TABLE_NAME);
    }

    @Test
    @Transactional
    void putNonExistingCodeTables() throws Exception {
        int databaseSizeBeforeUpdate = codeTablesRepository.findAll().size();
        codeTables.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCodeTablesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, codeTables.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(codeTables))
            )
            .andExpect(status().isBadRequest());

        // Validate the CodeTables in the database
        List<CodeTables> codeTablesList = codeTablesRepository.findAll();
        assertThat(codeTablesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCodeTables() throws Exception {
        int databaseSizeBeforeUpdate = codeTablesRepository.findAll().size();
        codeTables.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCodeTablesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(codeTables))
            )
            .andExpect(status().isBadRequest());

        // Validate the CodeTables in the database
        List<CodeTables> codeTablesList = codeTablesRepository.findAll();
        assertThat(codeTablesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCodeTables() throws Exception {
        int databaseSizeBeforeUpdate = codeTablesRepository.findAll().size();
        codeTables.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCodeTablesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(codeTables)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CodeTables in the database
        List<CodeTables> codeTablesList = codeTablesRepository.findAll();
        assertThat(codeTablesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCodeTablesWithPatch() throws Exception {
        // Initialize the database
        codeTablesRepository.saveAndFlush(codeTables);

        int databaseSizeBeforeUpdate = codeTablesRepository.findAll().size();

        // Update the codeTables using partial update
        CodeTables partialUpdatedCodeTables = new CodeTables();
        partialUpdatedCodeTables.setId(codeTables.getId());

        partialUpdatedCodeTables.tableName(UPDATED_TABLE_NAME);

        restCodeTablesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCodeTables.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCodeTables))
            )
            .andExpect(status().isOk());

        // Validate the CodeTables in the database
        List<CodeTables> codeTablesList = codeTablesRepository.findAll();
        assertThat(codeTablesList).hasSize(databaseSizeBeforeUpdate);
        CodeTables testCodeTables = codeTablesList.get(codeTablesList.size() - 1);
        assertThat(testCodeTables.getTableName()).isEqualTo(UPDATED_TABLE_NAME);
    }

    @Test
    @Transactional
    void fullUpdateCodeTablesWithPatch() throws Exception {
        // Initialize the database
        codeTablesRepository.saveAndFlush(codeTables);

        int databaseSizeBeforeUpdate = codeTablesRepository.findAll().size();

        // Update the codeTables using partial update
        CodeTables partialUpdatedCodeTables = new CodeTables();
        partialUpdatedCodeTables.setId(codeTables.getId());

        partialUpdatedCodeTables.tableName(UPDATED_TABLE_NAME);

        restCodeTablesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCodeTables.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCodeTables))
            )
            .andExpect(status().isOk());

        // Validate the CodeTables in the database
        List<CodeTables> codeTablesList = codeTablesRepository.findAll();
        assertThat(codeTablesList).hasSize(databaseSizeBeforeUpdate);
        CodeTables testCodeTables = codeTablesList.get(codeTablesList.size() - 1);
        assertThat(testCodeTables.getTableName()).isEqualTo(UPDATED_TABLE_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingCodeTables() throws Exception {
        int databaseSizeBeforeUpdate = codeTablesRepository.findAll().size();
        codeTables.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCodeTablesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, codeTables.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(codeTables))
            )
            .andExpect(status().isBadRequest());

        // Validate the CodeTables in the database
        List<CodeTables> codeTablesList = codeTablesRepository.findAll();
        assertThat(codeTablesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCodeTables() throws Exception {
        int databaseSizeBeforeUpdate = codeTablesRepository.findAll().size();
        codeTables.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCodeTablesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(codeTables))
            )
            .andExpect(status().isBadRequest());

        // Validate the CodeTables in the database
        List<CodeTables> codeTablesList = codeTablesRepository.findAll();
        assertThat(codeTablesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCodeTables() throws Exception {
        int databaseSizeBeforeUpdate = codeTablesRepository.findAll().size();
        codeTables.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCodeTablesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(codeTables))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CodeTables in the database
        List<CodeTables> codeTablesList = codeTablesRepository.findAll();
        assertThat(codeTablesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCodeTables() throws Exception {
        // Initialize the database
        codeTablesRepository.saveAndFlush(codeTables);

        int databaseSizeBeforeDelete = codeTablesRepository.findAll().size();

        // Delete the codeTables
        restCodeTablesMockMvc
            .perform(delete(ENTITY_API_URL_ID, codeTables.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CodeTables> codeTablesList = codeTablesRepository.findAll();
        assertThat(codeTablesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
