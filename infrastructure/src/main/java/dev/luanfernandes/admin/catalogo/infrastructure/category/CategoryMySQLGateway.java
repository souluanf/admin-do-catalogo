package dev.luanfernandes.admin.catalogo.infrastructure.category;

import static java.util.Optional.empty;

import dev.luanfernandes.admin.catalogo.domain.category.Category;
import dev.luanfernandes.admin.catalogo.domain.category.CategoryGateway;
import dev.luanfernandes.admin.catalogo.domain.category.CategoryID;
import dev.luanfernandes.admin.catalogo.domain.category.CategorySearchQuery;
import dev.luanfernandes.admin.catalogo.domain.pagination.Pagination;
import dev.luanfernandes.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import dev.luanfernandes.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class CategoryMySQLGateway implements CategoryGateway {

    private final CategoryRepository categoryRepository;

    public CategoryMySQLGateway(final CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category create(final Category aCategory) {
        return save(aCategory);
    }

    @Override
    public void deleteById(CategoryID anId) {
        final String anIdValue = anId.getValue();
        if (categoryRepository.existsById(anIdValue)) {
            categoryRepository.deleteById(anIdValue);
        }
    }

    @Override
    public Optional<Category> findById(CategoryID anId) {
        return empty();
    }

    @Override
    public Category update(final Category aCategory) {
        return save(aCategory);
    }

    @Override
    public Pagination<Category> findAll(CategorySearchQuery aQuery) {
        return null;
    }

    private Category save(final Category aCategory) {
        return this.categoryRepository.save(CategoryJpaEntity.from(aCategory)).toAggregate();
    }
}
