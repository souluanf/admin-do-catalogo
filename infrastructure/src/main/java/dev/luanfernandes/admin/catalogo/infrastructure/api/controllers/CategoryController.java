package dev.luanfernandes.admin.catalogo.infrastructure.api.controllers;

import static java.util.Objects.requireNonNull;
import static org.springframework.http.ResponseEntity.unprocessableEntity;

import dev.luanfernandes.admin.catalogo.application.category.create.CreateCategoryCommand;
import dev.luanfernandes.admin.catalogo.application.category.create.CreateCategoryOutput;
import dev.luanfernandes.admin.catalogo.application.category.create.CreateCategoryUseCase;
import dev.luanfernandes.admin.catalogo.application.category.delete.DeleteCategoryUseCase;
import dev.luanfernandes.admin.catalogo.application.category.retrieve.get.GetCategoryByIdUseCase;
import dev.luanfernandes.admin.catalogo.application.category.update.UpdateCategoryCommand;
import dev.luanfernandes.admin.catalogo.application.category.update.UpdateCategoryOutput;
import dev.luanfernandes.admin.catalogo.application.category.update.UpdateCategoryUseCase;
import dev.luanfernandes.admin.catalogo.domain.pagination.Pagination;
import dev.luanfernandes.admin.catalogo.domain.validation.handler.Notification;
import dev.luanfernandes.admin.catalogo.infrastructure.api.CategoryAPI;
import dev.luanfernandes.admin.catalogo.infrastructure.category.models.CategoryApiOutput;
import dev.luanfernandes.admin.catalogo.infrastructure.category.models.CreateCategoryApiInput;
import dev.luanfernandes.admin.catalogo.infrastructure.category.models.UpdateCategoryApiInput;
import dev.luanfernandes.admin.catalogo.infrastructure.category.presenters.CategoryApiPresenter;
import java.net.URI;
import java.util.function.Function;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryController implements CategoryAPI {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final GetCategoryByIdUseCase getCategoryByIdUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;

    public CategoryController(
            final CreateCategoryUseCase createCategoryUseCase,
            final GetCategoryByIdUseCase getCategoryByIdUseCase,
            final UpdateCategoryUseCase updateCategoryUseCase,
            final DeleteCategoryUseCase deleteCategoryUseCase) {
        this.createCategoryUseCase = requireNonNull(createCategoryUseCase);
        this.getCategoryByIdUseCase = requireNonNull(getCategoryByIdUseCase);
        this.updateCategoryUseCase = requireNonNull(updateCategoryUseCase);
        this.deleteCategoryUseCase = requireNonNull(deleteCategoryUseCase);
    }

    @Override
    public ResponseEntity<?> createCategory(final CreateCategoryApiInput input) {
        final var aCommand = new CreateCategoryCommand(
                input.name(), input.description(), input.active() != null ? input.active() : true);

        final Function<Notification, ResponseEntity<?>> onError = unprocessableEntity()::body;

        final Function<CreateCategoryOutput, ResponseEntity<?>> onSuccess = output ->
                ResponseEntity.created(URI.create("/categories/" + output.id())).body(output);

        return this.createCategoryUseCase.execute(aCommand).fold(onError, onSuccess);
    }

    @Override
    public Pagination<?> listCategories(String search, Integer page, Integer perPage, String sort, String direction) {
        return null;
    }

    @Override
    public CategoryApiOutput getById(final String id) {
        return CategoryApiPresenter.present(this.getCategoryByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateCategoryApiInput input) {
        final var aCommand = new UpdateCategoryCommand(
                id, input.name(), input.description(), input.active() != null ? input.active() : true);

        final Function<Notification, ResponseEntity<?>> onError = unprocessableEntity()::body;

        final Function<UpdateCategoryOutput, ResponseEntity<?>> onSuccess = ResponseEntity::ok;

        return this.updateCategoryUseCase.execute(aCommand).fold(onError, onSuccess);
    }

    @Override
    public void deleteById(final String anID) {
        this.deleteCategoryUseCase.execute(anID);
    }
}
