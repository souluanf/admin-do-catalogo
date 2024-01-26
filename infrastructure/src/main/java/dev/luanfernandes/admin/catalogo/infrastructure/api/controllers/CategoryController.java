package dev.luanfernandes.admin.catalogo.infrastructure.api.controllers;

import static org.springframework.http.ResponseEntity.unprocessableEntity;

import dev.luanfernandes.admin.catalogo.application.category.create.CreateCategoryCommand;
import dev.luanfernandes.admin.catalogo.application.category.create.CreateCategoryOutput;
import dev.luanfernandes.admin.catalogo.application.category.create.CreateCategoryUseCase;
import dev.luanfernandes.admin.catalogo.domain.pagination.Pagination;
import dev.luanfernandes.admin.catalogo.domain.validation.handler.Notification;
import dev.luanfernandes.admin.catalogo.infrastructure.api.CategoryAPI;
import dev.luanfernandes.admin.catalogo.infrastructure.category.models.CreateCategoryApiInput;
import java.net.URI;
import java.util.Objects;
import java.util.function.Function;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryController implements CategoryAPI {

    private final CreateCategoryUseCase createCategoryUseCase;

    public CategoryController(CreateCategoryUseCase createCategoryUseCase) {
        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
    }

    @Override
    public ResponseEntity<?> createCategory(CreateCategoryApiInput input) {
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
}
