package dev.luanfernandes.admin.catalogo.domain;

import java.util.Objects;

public abstract class Entity<ID extends Identifier> {
    protected final ID id;
    protected Entity(final ID id) {
        Objects.requireNonNull(id, "id cannot be null");
        this.id = id;
    }

    public ID getId() {
        return id;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Entity<?> entity = (Entity<?>) o;
        return getId().equals(entity.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
