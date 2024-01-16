package dev.luanfernandes.admin.catalogo.infrastructure.category;

import static dev.luanfernandes.admin.catalogo.domain.category.Category.newCategory;
import static dev.luanfernandes.admin.catalogo.domain.category.CategoryID.from;
import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.luanfernandes.admin.catalogo.domain.category.CategoryID;
import dev.luanfernandes.admin.catalogo.domain.category.CategorySearchQuery;
import dev.luanfernandes.admin.catalogo.infrastructure.MySQLGatewayTest;
import dev.luanfernandes.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import dev.luanfernandes.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@MySQLGatewayTest
class CategoryMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway categoryGateway;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void givenAValidCategory_whenCallsCreate_shouldReturnANewCategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedActive = true;
        final var aCategory = newCategory(expectedName, expectedDescription, expectedActive);

        assertEquals(0, categoryRepository.count());

        final var actualCategory = categoryGateway.create(aCategory);

        assertEquals(1, categoryRepository.count());

        assertEquals(aCategory.getId(), actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedActive, actualCategory.isActive());
        assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
        assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
        assertNull(actualCategory.getDeletedAt());

        final var actualEntity =
                categoryRepository.findById(aCategory.getId().getValue()).get();
        assertEquals(aCategory.getId().getValue(), actualEntity.getId());
        assertEquals(expectedName, actualEntity.getName());
        assertEquals(expectedDescription, actualEntity.getDescription());
        assertEquals(expectedActive, actualEntity.isActive());
        assertEquals(aCategory.getCreatedAt(), actualEntity.getCreatedAt());
        assertEquals(aCategory.getUpdatedAt(), actualEntity.getUpdatedAt());
        assertEquals(aCategory.getDeletedAt(), actualEntity.getDeletedAt());
        assertNull(actualEntity.getDeletedAt());
    }

    @Test
    void givenAValidCategory_whenCallsUpdate_shouldReturnCategoryUpdated() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedActive = true;
        final var aCategory = newCategory("Film", null, expectedActive);

        assertEquals(0, categoryRepository.count());

        categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));

        final var actualInvalidEntity =
                categoryRepository.findById(aCategory.getId().getValue()).get();

        assertEquals("Film", actualInvalidEntity.getName());
        assertNull(actualInvalidEntity.getDescription());
        assertEquals(expectedActive, actualInvalidEntity.isActive());

        assertEquals(1, categoryRepository.count());

        final var anUpdatedCategory = aCategory.clone().update(expectedName, expectedDescription, expectedActive);
        final var actualCategory = categoryGateway.update(anUpdatedCategory);

        assertEquals(1, categoryRepository.count());

        assertEquals(aCategory.getId(), actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedActive, actualCategory.isActive());
        assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
        assertNull(actualCategory.getDeletedAt());

        final var actualEntity =
                categoryRepository.findById(aCategory.getId().getValue()).get();
        assertEquals(aCategory.getId().getValue(), actualEntity.getId());
        assertEquals(expectedName, actualEntity.getName());
        assertEquals(expectedDescription, actualEntity.getDescription());
        assertEquals(expectedActive, actualEntity.isActive());
        assertEquals(aCategory.getCreatedAt(), actualEntity.getCreatedAt());
        assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        assertEquals(aCategory.getDeletedAt(), actualEntity.getDeletedAt());
        assertNull(actualEntity.getDeletedAt());
    }

    @Test
    void givenAPrePersistedCategoryAndValidCategoryID_whenTryToDeleteIt_shouldDeleteCategory() {
        final var aCategory = newCategory("Filmes", "A categoria mais assistida", true);
        assertEquals(0, categoryRepository.count());

        final var actualCategory = categoryGateway.create(aCategory);

        assertEquals(1, categoryRepository.count());

        categoryGateway.deleteById(actualCategory.getId());
        assertEquals(0, categoryRepository.count());
    }

    @Test
    void givenAnValidCategoryID_whenTryToDeleteIt_shouldDeleteCategory() {
        assertEquals(0, categoryRepository.count());
        categoryGateway.deleteById(from("invalid"));
        assertEquals(0, categoryRepository.count());
    }

    @Test
    void givenAPrePersistedCategoryAndValidCategoryID_whenCallsFindById_shouldReturnCategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedActive = true;
        final var aCategory = newCategory(expectedName, expectedDescription, expectedActive);

        assertEquals(0, categoryRepository.count());

        categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));

        assertEquals(1, categoryRepository.count());

        final var actualCategory = categoryGateway.findById(aCategory.getId()).get();

        assertEquals(1, categoryRepository.count());

        assertEquals(aCategory.getId(), actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedActive, actualCategory.isActive());
        assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
        assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
        assertNull(actualCategory.getDeletedAt());
    }

    @Test
    void givenAValidCategoryIDNotStored_whenCallsFindById_shouldReturnEmpty() {
        assertEquals(0, categoryRepository.count());

        final var actualCategory = categoryGateway.findById(CategoryID.from(""));

        assertTrue(actualCategory.isEmpty());
    }

    @Test
    void givenPrePersistedCategory_whenCallsFindAll_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var films = newCategory("Filmes", null, true);
        final var series = newCategory("Séries", null, true);
        final var documentaries = newCategory("Documentários", null, true);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(of(
                CategoryJpaEntity.from(films), CategoryJpaEntity.from(series), CategoryJpaEntity.from(documentaries)));

        Assertions.assertEquals(3, categoryRepository.count());

        final var query = new CategorySearchQuery(0, 1, "", "name", "asc");
        final var actualResult = categoryGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(
                documentaries.getId(), actualResult.items().get(0).getId());
    }

    @Test
    void givenEmptyCategoriesTable_whenCallsFindAll_shouldReturnEmptyPage() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 0;

        final var query = new CategorySearchQuery(0, 1, "", "name", "asc");
        final var actualResult = categoryGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(0, actualResult.items().size());
    }

    @Test
    void givenFollowPagination_whenCallsFindAllWithPage1_shouldReturnPaginated() {
        var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var films = newCategory("Filmes", null, true);
        final var series = newCategory("Séries", null, true);
        final var documentaries = newCategory("Documentários", null, true);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAllAndFlush(of(
                CategoryJpaEntity.from(films), CategoryJpaEntity.from(series), CategoryJpaEntity.from(documentaries)));

        Assertions.assertEquals(3, categoryRepository.count());

        var query = new CategorySearchQuery(0, 1, "", "name", "asc");
        var actualResult = categoryGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(
                documentaries.getId(), actualResult.items().get(0).getId());

        expectedPage = 1;
        query = new CategorySearchQuery(1, 1, "", "name", "asc");
        actualResult = categoryGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(films.getId(), actualResult.items().get(0).getId());

        expectedPage = 2;
        query = new CategorySearchQuery(2, 1, "", "name", "asc");
        actualResult = categoryGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(series.getId(), actualResult.items().get(0).getId());
    }

    @Test
    void givenPrePersistedCategoryAndDocAsTerms_whenCallsFindAllAndTermsMatchesCategoryName_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var films = newCategory("Filmes", null, true);
        final var series = newCategory("Séries", null, true);
        final var documentaries = newCategory("Documentários", null, true);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(of(
                CategoryJpaEntity.from(films), CategoryJpaEntity.from(series), CategoryJpaEntity.from(documentaries)));

        Assertions.assertEquals(3, categoryRepository.count());

        final var query = new CategorySearchQuery(0, 1, "doc", "name", "asc");
        final var actualResult = categoryGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(
                documentaries.getId(), actualResult.items().get(0).getId());
    }

    @Test
    void
            givenPrePersistedCategoryAndMaisAssistidaAsTerms_whenCallsFindAllAndTermsMatchesCategoryDescription_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var films = newCategory("Filmes", "A categoria mais assistida", true);
        final var series = newCategory("Séries", "Uma categoria assistida ", true);
        final var documentaries = newCategory("Documentários", "A categoria menos assistida", true);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(of(
                CategoryJpaEntity.from(films), CategoryJpaEntity.from(series), CategoryJpaEntity.from(documentaries)));

        Assertions.assertEquals(3, categoryRepository.count());

        final var query = new CategorySearchQuery(0, 1, "MAIS ASSISTIDA", "name", "asc");
        final var actualResult = categoryGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(films.getId(), actualResult.items().get(0).getId());
    }
}
