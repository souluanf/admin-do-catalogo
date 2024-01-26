package dev.luanfernandes.admin.catalogo.infrastructure.category.presenters;

import dev.luanfernandes.admin.catalogo.application.category.retrieve.get.CategoryOutput;
import dev.luanfernandes.admin.catalogo.infrastructure.category.models.CategoryApiOutput;
import java.util.function.Function;

public interface CategoryApiPresenter {

    Function<CategoryOutput, CategoryApiOutput> present = output -> new CategoryApiOutput(
            output.id().getValue(),
            output.name(),
            output.description(),
            output.active(),
            output.createdAt(),
            output.updatedAt(),
            output.deletedAt());

    static CategoryApiOutput present(final CategoryOutput output) {
        return new CategoryApiOutput(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.active(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt());
    }
}
