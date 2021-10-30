package com.mapr.mgrweb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CodeValues.
 */
@Entity
@Table(name = "code_values")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CodeValues implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 3)
    @Column(name = "jhi_key", nullable = false)
    private String key;

    @Column(name = "value")
    private String value;

    @ManyToOne
    @JsonIgnoreProperties(value = { "codeValues" }, allowSetters = true)
    private CodeTables codeTables;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CodeValues id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return this.key;
    }

    public CodeValues key(String key) {
        this.setKey(key);
        return this;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return this.value;
    }

    public CodeValues value(String value) {
        this.setValue(value);
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public CodeTables getCodeTables() {
        return this.codeTables;
    }

    public void setCodeTables(CodeTables codeTables) {
        this.codeTables = codeTables;
    }

    public CodeValues codeTables(CodeTables codeTables) {
        this.setCodeTables(codeTables);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CodeValues)) {
            return false;
        }
        return id != null && id.equals(((CodeValues) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CodeValues{" +
            "id=" + getId() +
            ", key='" + getKey() + "'" +
            ", value='" + getValue() + "'" +
            "}";
    }
}
