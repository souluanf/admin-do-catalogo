package dev.luanfernandes.admin.catalogo.application.category.retrieve.get;

import dev.luanfernandes.admin.catalogo.domain.category.CategoryGateway;
import dev.luanfernandes.admin.catalogo.domain.category.CategoryID;
import dev.luanfernandes.admin.catalogo.domain.exceptions.DomainException;
import dev.luanfernandes.admin.catalogo.domain.validation.Error;
import java.util.Objects;
import java.util.function.Supplier;

import static dev.luanfernandes.admin.catalogo.domain.category.CategoryID.from;
import static dev.luanfernandes.admin.catalogo.domain.exceptions.DomainException.with;
import static java.util.Objects.requireNonNull;

public class DefaultGetCategoryByIdUseCase extends GetCategoryByIdUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultGetCategoryByIdUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = requireNonNull(categoryGateway);
    }

    @Override
    public CategoryOutput execute(String anIn) {
        final var aCategoryId = from(anIn);
        return this.categoryGateway
                .findById(aCategoryId)
                .map(CategoryOutput::from)
                .orElseThrow(notFound(aCategoryId));
    }

    private static Supplier<DomainException> notFound(CategoryID anId) {
        return () -> with(new Error("Category with id %s not found".formatted(anId.getValue())));
    }
}
