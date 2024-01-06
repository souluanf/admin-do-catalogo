package dev.luanfernandes.admin.catalogo.domain.category;

import dev.luanfernandes.admin.catalogo.domain.pagination.Pagination;
import java.util.Optional;

public interface CategoryGateway {
    Category create(Category aCategory);

    Category delete(CategoryID anId);

    Optional<Category> findByID(CategoryID anId);

    Category update(Category aCategory);

    Pagination<Category> findAll(CategorySearchQuery aQuery);
}
