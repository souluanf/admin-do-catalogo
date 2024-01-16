package dev.luanfernandes.admin.catalogo.application.category.create;

import static dev.luanfernandes.admin.catalogo.domain.category.Category.newCategory;
import static io.vavr.API.Try;
import static io.vavr.control.Either.left;

import dev.luanfernandes.admin.catalogo.domain.category.Category;
import dev.luanfernandes.admin.catalogo.domain.category.CategoryGateway;
import dev.luanfernandes.admin.catalogo.domain.validation.handler.Notification;
import io.vavr.control.Either;
import java.util.Objects;

public class DefaultCreateCategoryUseCase extends CreateCategoryUseCase {
    private final CategoryGateway categoryGateway;

    public DefaultCreateCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Either<Notification, CreateCategoryOutput> execute(final CreateCategoryCommand aCommand) {
        final var aName = aCommand.name();
        final var aDescription = aCommand.description();
        final var isActive = aCommand.active();

        final var notification = Notification.create();
        final var aCategory = newCategory(aName, aDescription, isActive);
        aCategory.validate(notification);

        return notification.hasErrors() ? left(notification) : create(aCategory);
    }

    private Either<Notification, CreateCategoryOutput> create(final Category aCategory) {
        return Try(() -> this.categoryGateway.create(aCategory))
                .toEither()
                .bimap(Notification::create, CreateCategoryOutput::from);
    }
}
