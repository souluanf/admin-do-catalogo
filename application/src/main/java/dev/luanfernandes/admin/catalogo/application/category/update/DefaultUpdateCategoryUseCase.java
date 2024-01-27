package dev.luanfernandes.admin.catalogo.application.category.update;

import static dev.luanfernandes.admin.catalogo.domain.category.CategoryID.from;
import static io.vavr.API.Try;
import static io.vavr.control.Either.left;
import static java.util.Objects.requireNonNull;

import dev.luanfernandes.admin.catalogo.domain.category.Category;
import dev.luanfernandes.admin.catalogo.domain.category.CategoryGateway;
import dev.luanfernandes.admin.catalogo.domain.category.CategoryID;
import dev.luanfernandes.admin.catalogo.domain.exceptions.DomainException;
import dev.luanfernandes.admin.catalogo.domain.exceptions.NotFoundException;
import dev.luanfernandes.admin.catalogo.domain.validation.handler.Notification;
import io.vavr.control.Either;
import java.util.function.Supplier;

public class DefaultUpdateCategoryUseCase extends UpdateCategoryUseCase {
    private final CategoryGateway categoryGateway;

    public DefaultUpdateCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = requireNonNull(categoryGateway);
    }

    @Override
    public Either<Notification, UpdateCategoryOutput> execute(final UpdateCategoryCommand aCommand) {
        final var anId = from(aCommand.id());
        final var aName = aCommand.name();
        final var aDescription = aCommand.description();
        final var isActive = aCommand.isActive();
        Category aCategory = this.categoryGateway.findById(anId).orElseThrow(notFound(anId));
        final var notification = Notification.create();
        aCategory.update(aName, aDescription, isActive).validate(notification);
        return notification.hasErrors() ? left(notification) : update(aCategory);
    }

    private Either<Notification, UpdateCategoryOutput> update(Category aCategory) {
        return Try(() -> this.categoryGateway.update(aCategory))
                .toEither()
                .bimap(Notification::create, UpdateCategoryOutput::from);
    }

    private static Supplier<DomainException> notFound(CategoryID anId) {
        return () -> NotFoundException.with(Category.class, anId);
    }
}
