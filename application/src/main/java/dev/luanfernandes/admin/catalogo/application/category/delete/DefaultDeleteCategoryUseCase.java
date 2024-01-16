package dev.luanfernandes.admin.catalogo.application.category.delete;

import static dev.luanfernandes.admin.catalogo.domain.category.CategoryID.from;
import static java.util.Objects.requireNonNull;

import dev.luanfernandes.admin.catalogo.domain.category.CategoryGateway;
import dev.luanfernandes.admin.catalogo.domain.category.CategoryID;

public class DefaultDeleteCategoryUseCase extends DeleteCategoryUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultDeleteCategoryUseCase(final CategoryGateway gateway) {
        this.categoryGateway = requireNonNull(gateway);
    }

    @Override
    public void execute(final String anIn) {
        this.categoryGateway.deleteById(from(anIn));
    }
}
