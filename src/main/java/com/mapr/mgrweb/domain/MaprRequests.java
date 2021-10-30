package com.mapr.mgrweb.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MaprRequests.
 */
@Entity
@Table(name = "mapr_requests")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MaprRequests implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 3)
    @Column(name = "type", nullable = false)
    private String type;

    @NotNull
    @Size(min = 3)
    @Column(name = "action", nullable = false)
    private String action;

    @NotNull
    @Size(min = 3)
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Size(min = 3)
    @Column(name = "path", nullable = false)
    private String path;

    @NotNull
    @Size(min = 3)
    @Column(name = "request_user", nullable = false)
    private String requestUser;

    @NotNull
    @Column(name = "request_date", nullable = false)
    private Instant requestDate;

    @NotNull
    @Size(min = 3)
    @Column(name = "status", nullable = false)
    private String status;

    @NotNull
    @Column(name = "status_date", nullable = false)
    private Instant statusDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MaprRequests id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public MaprRequests type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAction() {
        return this.action;
    }

    public MaprRequests action(String action) {
        this.setAction(action);
        return this;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getName() {
        return this.name;
    }

    public MaprRequests name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return this.path;
    }

    public MaprRequests path(String path) {
        this.setPath(path);
        return this;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRequestUser() {
        return this.requestUser;
    }

    public MaprRequests requestUser(String requestUser) {
        this.setRequestUser(requestUser);
        return this;
    }

    public void setRequestUser(String requestUser) {
        this.requestUser = requestUser;
    }

    public Instant getRequestDate() {
        return this.requestDate;
    }

    public MaprRequests requestDate(Instant requestDate) {
        this.setRequestDate(requestDate);
        return this;
    }

    public void setRequestDate(Instant requestDate) {
        this.requestDate = requestDate;
    }

    public String getStatus() {
        return this.status;
    }

    public MaprRequests status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getStatusDate() {
        return this.statusDate;
    }

    public MaprRequests statusDate(Instant statusDate) {
        this.setStatusDate(statusDate);
        return this;
    }

    public void setStatusDate(Instant statusDate) {
        this.statusDate = statusDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MaprRequests)) {
            return false;
        }
        return id != null && id.equals(((MaprRequests) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MaprRequests{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", action='" + getAction() + "'" +
            ", name='" + getName() + "'" +
            ", path='" + getPath() + "'" +
            ", requestUser='" + getRequestUser() + "'" +
            ", requestDate='" + getRequestDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", statusDate='" + getStatusDate() + "'" +
            "}";
    }
}
