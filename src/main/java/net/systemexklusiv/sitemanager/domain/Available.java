package net.systemexklusiv.sitemanager.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Available.
 */
@Entity
@Table(name = "available")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Available implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "begin_of_availability", nullable = false)
    private LocalDate beginOfAvailability;

    @NotNull
    @Column(name = "end_of_availability", nullable = false)
    private LocalDate endOfAvailability;

    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getBeginOfAvailability() {
        return beginOfAvailability;
    }

    public void setBeginOfAvailability(LocalDate beginOfAvailability) {
        this.beginOfAvailability = beginOfAvailability;
    }

    public LocalDate getEndOfAvailability() {
        return endOfAvailability;
    }

    public void setEndOfAvailability(LocalDate endOfAvailability) {
        this.endOfAvailability = endOfAvailability;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Available available = (Available) o;
        if(available.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, available.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Available{" +
            "id=" + id +
            ", beginOfAvailability='" + beginOfAvailability + "'" +
            ", endOfAvailability='" + endOfAvailability + "'" +
            '}';
    }
}
