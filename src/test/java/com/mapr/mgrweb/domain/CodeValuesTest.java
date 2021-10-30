package com.mapr.mgrweb.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mapr.mgrweb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CodeValuesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CodeValues.class);
        CodeValues codeValues1 = new CodeValues();
        codeValues1.setId(1L);
        CodeValues codeValues2 = new CodeValues();
        codeValues2.setId(codeValues1.getId());
        assertThat(codeValues1).isEqualTo(codeValues2);
        codeValues2.setId(2L);
        assertThat(codeValues1).isNotEqualTo(codeValues2);
        codeValues1.setId(null);
        assertThat(codeValues1).isNotEqualTo(codeValues2);
    }
}
