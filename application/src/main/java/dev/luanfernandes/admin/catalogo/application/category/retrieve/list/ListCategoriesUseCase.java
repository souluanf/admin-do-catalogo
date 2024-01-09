package dev.luanfernandes.admin.catalogo.application.category.retrieve.list;

import dev.luanfernandes.admin.catalogo.application.UseCase;
import dev.luanfernandes.admin.catalogo.domain.category.CategorySearchQuery;
import dev.luanfernandes.admin.catalogo.domain.pagination.Pagination;

public abstract class ListCategoriesUseCase extends UseCase<CategorySearchQuery, Pagination<CategoryListOutput>> {}
