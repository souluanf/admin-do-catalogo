package dev.luanfernandes.admin.catalogo.infrastructure.category;

import static dev.luanfernandes.admin.catalogo.infrastructure.utils.SpecificationUtils.like;
import static java.util.Optional.ofNullable;
import static org.springframework.data.jpa.domain.Specification.where;

import dev.luanfernandes.admin.catalogo.domain.category.Category;
import dev.luanfernandes.admin.catalogo.domain.category.CategoryGateway;
import dev.luanfernandes.admin.catalogo.domain.category.CategoryID;
import dev.luanfernandes.admin.catalogo.domain.category.CategorySearchQuery;
import dev.luanfernandes.admin.catalogo.domain.pagination.Pagination;
import dev.luanfernandes.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import dev.luanfernandes.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import dev.luanfernandes.admin.catalogo.infrastructure.utils.SpecificationUtils;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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
    public void deleteById(final CategoryID anId) {
        final String anIdValue = anId.getValue();
        if (categoryRepository.existsById(anIdValue)) {
            categoryRepository.deleteById(anIdValue);
        }
    }

    @Override
    public Optional<Category> findById(final CategoryID anId) {
        return this.categoryRepository.findById(anId.getValue()).map(CategoryJpaEntity::toAggregate);
    }

    @Override
    public Category update(final Category aCategory) {
        return save(aCategory);
    }

    @Override
    public Pagination<Category> findAll(final CategorySearchQuery aQuery) {
        final var page = PageRequest.of(
                aQuery.page(), aQuery.perPage(), Sort.by(Direction.fromString(aQuery.direction()), aQuery.sort()));

        final var specifications = ofNullable(aQuery.terms())
                .filter(str -> !str.isBlank())
                .map(str ->
                        SpecificationUtils.<CategoryJpaEntity>like("name", str).or(like("description", str)))
                .orElse(null);

        final var pageResult = this.categoryRepository.findAll(where(specifications), page);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(CategoryJpaEntity::toAggregate).toList());
    }

    private Category save(final Category aCategory) {
        return this.categoryRepository.save(CategoryJpaEntity.from(aCategory)).toAggregate();
    }
}
