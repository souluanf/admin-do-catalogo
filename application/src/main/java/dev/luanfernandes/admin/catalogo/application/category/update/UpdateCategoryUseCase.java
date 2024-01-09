package dev.luanfernandes.admin.catalogo.application.category.update;

import dev.luanfernandes.admin.catalogo.application.UseCase;
import dev.luanfernandes.admin.catalogo.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class UpdateCategoryUseCase
        extends UseCase<UpdateCategoryCommand, Either<Notification, UpdateCategoryOutput>> {}
