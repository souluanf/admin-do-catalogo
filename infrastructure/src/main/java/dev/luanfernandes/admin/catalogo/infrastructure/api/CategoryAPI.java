package dev.luanfernandes.admin.catalogo.infrastructure.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import dev.luanfernandes.admin.catalogo.domain.pagination.Pagination;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("categories")
public interface CategoryAPI {

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    ResponseEntity<?> createCategory();

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    Pagination<?> listCategories(
            @RequestParam(name = "search", required = false, defaultValue = "") final String search,
            @RequestParam(name = "page", required = false, defaultValue = "0") final Integer page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") final Integer perPage,
            @RequestParam(name = "sort", required = false, defaultValue = "name") final String sort,
            @RequestParam(name = "direction", required = false, defaultValue = "ASC") final String direction);
}
