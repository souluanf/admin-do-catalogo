package dev.luanfernandes.admin.catalogo.domain.category;

import dev.luanfernandes.admin.catalogo.domain.pagination.Pagination;
import java.util.Optional;

public interface CategoryGateway {
    Category create(Category aCategory);

    Category deleteById(CategoryID anId);

    Optional<Category> findById(CategoryID anId);

    Category update(Category aCategory);

    Pagination<Category> findAll(CategorySearchQuery aQuery);
}
