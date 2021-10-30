package com.mapr.mgrweb.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mapr.mgrweb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CodeTablesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CodeTables.class);
        CodeTables codeTables1 = new CodeTables();
        codeTables1.setId(1L);
        CodeTables codeTables2 = new CodeTables();
        codeTables2.setId(codeTables1.getId());
        assertThat(codeTables1).isEqualTo(codeTables2);
        codeTables2.setId(2L);
        assertThat(codeTables1).isNotEqualTo(codeTables2);
        codeTables1.setId(null);
        assertThat(codeTables1).isNotEqualTo(codeTables2);
    }
}
