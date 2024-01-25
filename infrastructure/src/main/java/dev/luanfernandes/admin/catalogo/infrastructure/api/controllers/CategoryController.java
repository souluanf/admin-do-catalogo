package dev.luanfernandes.admin.catalogo.infrastructure.api.controllers;

import dev.luanfernandes.admin.catalogo.domain.pagination.Pagination;
import dev.luanfernandes.admin.catalogo.infrastructure.api.CategoryAPI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryController implements CategoryAPI {

    @Override
    public ResponseEntity<?> createCategory() {
        return null;
    }

    @Override
    public Pagination<?> listCategories(String search, Integer page, Integer perPage, String sort, String direction) {
        return null;
    }
}
