package dev.luanfernandes.admin.catalogo.domain.category;

public record CategorySearchQuery(
        int page,
        int perPage,
        String tems,
        String sort,
        String direction
) {
}
