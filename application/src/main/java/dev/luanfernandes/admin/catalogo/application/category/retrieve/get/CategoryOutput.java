package dev.luanfernandes.admin.catalogo.application.category.retrieve.get;

import dev.luanfernandes.admin.catalogo.domain.category.Category;
import dev.luanfernandes.admin.catalogo.domain.category.CategoryID;
import java.time.Instant;

public record CategoryOutput(
        CategoryID id,
        String name,
        String description,
        boolean active,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt) {
    public static CategoryOutput from(Category aCategory) {
        return new CategoryOutput(
                aCategory.getId(),
                aCategory.getName(),
                aCategory.getDescription(),
                aCategory.isActive(),
                aCategory.getCreatedAt(),
                aCategory.getUpdatedAt(),
                aCategory.getDeletedAt());
    }
}
