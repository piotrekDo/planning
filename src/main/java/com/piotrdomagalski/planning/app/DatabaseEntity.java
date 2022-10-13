package com.piotrdomagalski.planning.app;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Objects;

/**
 * Just a super class to any entity within the program, so I don't have to duplicate ID annotation
 */

@MappedSuperclass
public class DatabaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public DatabaseEntity() {
    }

    public DatabaseEntity(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "id=" + id + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DatabaseEntity)) return false;
        DatabaseEntity that = (DatabaseEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
