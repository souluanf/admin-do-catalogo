package dev.luanfernandes.admin.catalogo.domain.category;

import static dev.luanfernandes.admin.catalogo.domain.category.CategoryID.unique;
import static java.time.Instant.now;
import static java.util.Objects.requireNonNull;

import dev.luanfernandes.admin.catalogo.domain.AggregateRoot;
import dev.luanfernandes.admin.catalogo.domain.validation.ValidationHandler;
import java.time.Instant;

public class Category extends AggregateRoot<CategoryID> implements Cloneable {
    private String name;
    private String description;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Category(
            final CategoryID anId,
            final String aName,
            final String aDescription,
            final boolean isActive,
            final Instant aCreationDate,
            final Instant anUpdateDate,
            final Instant aDeleteDate) {
        super(anId);
        this.name = aName;
        this.description = aDescription;
        this.active = isActive;
        this.createdAt = requireNonNull(aCreationDate, "'createdAt' should not be null");
        this.updatedAt = requireNonNull(anUpdateDate, "'updatedAt' should not be null");
        this.deletedAt = aDeleteDate;
    }

    public static Category newCategory(final String name, final String description, final boolean active) {
        final var id = unique();
        final var now = now();
        final var deletedAt = active ? null : now;
        return new Category(id, name, description, active, now, now, deletedAt);
    }

    public static Category with(
            final CategoryID anId,
            final String name,
            final String description,
            final boolean active,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt) {
        return new Category(anId, name, description, active, createdAt, updatedAt, deletedAt);
    }

    public static Category with(final Category aCategory) {
        return with(
                aCategory.getId(),
                aCategory.getName(),
                aCategory.getDescription(),
                aCategory.isActive(),
                aCategory.getCreatedAt(),
                aCategory.getUpdatedAt(),
                aCategory.getDeletedAt());
    }

    @Override
    public void validate(ValidationHandler handler) {
        new CategoryValidator(this, handler).validate();
    }

    public Category activate() {
        this.deletedAt = null;
        this.active = true;
        this.updatedAt = now();
        return this;
    }

    public Category deactivate() {
        if (getDeletedAt() == null) {
            this.deletedAt = now();
        }
        this.active = false;
        this.updatedAt = now();
        return this;
    }

    public Category update(final String aName, final String aDescription, final boolean isActive) {
        if (isActive) {
            activate();
        } else {
            deactivate();
        }
        this.name = aName;
        this.description = aDescription;
        this.updatedAt = now();
        return this;
    }

    public CategoryID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    @Override
    public Category clone() {
        try {
            return (Category) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
