package com.mapr.mgrweb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CodeTables.
 */
@Entity
@Table(name = "code_tables")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CodeTables implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 3)
    @Column(name = "table_name", nullable = false)
    private String tableName;

    @OneToMany(mappedBy = "codeTables")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "codeTables" }, allowSetters = true)
    private Set<CodeValues> codeValues = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CodeTables id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTableName() {
        return this.tableName;
    }

    public CodeTables tableName(String tableName) {
        this.setTableName(tableName);
        return this;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Set<CodeValues> getCodeValues() {
        return this.codeValues;
    }

    public void setCodeValues(Set<CodeValues> codeValues) {
        if (this.codeValues != null) {
            this.codeValues.forEach(i -> i.setCodeTables(null));
        }
        if (codeValues != null) {
            codeValues.forEach(i -> i.setCodeTables(this));
        }
        this.codeValues = codeValues;
    }

    public CodeTables codeValues(Set<CodeValues> codeValues) {
        this.setCodeValues(codeValues);
        return this;
    }

    public CodeTables addCodeValues(CodeValues codeValues) {
        this.codeValues.add(codeValues);
        codeValues.setCodeTables(this);
        return this;
    }

    public CodeTables removeCodeValues(CodeValues codeValues) {
        this.codeValues.remove(codeValues);
        codeValues.setCodeTables(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CodeTables)) {
            return false;
        }
        return id != null && id.equals(((CodeTables) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CodeTables{" +
            "id=" + getId() +
            ", tableName='" + getTableName() + "'" +
            "}";
    }
}
