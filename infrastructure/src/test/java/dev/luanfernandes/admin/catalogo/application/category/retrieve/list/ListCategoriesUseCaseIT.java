package dev.luanfernandes.admin.catalogo.application.category.retrieve.list;

import static java.util.stream.Stream.of;
import static org.junit.jupiter.api.Assertions.assertEquals;

import dev.luanfernandes.admin.catalogo.IntegrationTest;
import dev.luanfernandes.admin.catalogo.domain.category.Category;
import dev.luanfernandes.admin.catalogo.domain.category.CategorySearchQuery;
import dev.luanfernandes.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import dev.luanfernandes.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
class ListCategoriesUseCaseIT {

    @Autowired
    private ListCategoriesUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void mockUp() {
        final var categories = of(
                        Category.newCategory("Filmes", null, true),
                        Category.newCategory("Netflix Originals", "Títulos de autoria da Netflix", true),
                        Category.newCategory("Amazon Originals", "Títulos de autoria da Amazon", true),
                        Category.newCategory("Documentários", null, true),
                        Category.newCategory("Sports", null, true),
                        Category.newCategory("Kids", "categoria para crianças", true),
                        Category.newCategory("Series", null, true))
                .map(CategoryJpaEntity::from)
                .toList();
        categoryRepository.saveAllAndFlush(categories);
    }

    @Test
    void givenAValidTerm_whenTermDoesntMatchesPrePersisted_shouldReturnEmptyPage() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "invalid";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 0;
        final var expectedTotal = 0;

        final var aQuery =
                new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
        final var actualResult = useCase.execute(aQuery);

        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
    }

    @ParameterizedTest
    @CsvSource({
        "fil,0,10,1,1,Filmes",
        "net,0,10,1,1,Netflix Originals",
        "ZON,0,10,1,1,Amazon Originals",
        "KI,0,10,1,1,Kids",
        "crianças,0,10,1,1,Kids",
        "da Amazon,0,10,1,1,Amazon Originals",
    })
    void givenAValidTerm_whenCallsListCategories_shouldReturnCategoriesFiltered(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedCategoryName) {
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var aQuery =
                new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
        final var actualResult = useCase.execute(aQuery);

        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedCategoryName, actualResult.items().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
        "name,asc,0,10,7,7,Amazon Originals",
        "name,desc,0,10,7,7,Sports",
        "createdAt,asc,0,10,7,7,Filmes",
        "createdAt,desc,0,10,7,7,Series"
    })
    void givenAValidSortAndDirection_whenCallsListCategories_shouldReturnCategoriesOrdered(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedCategoryName) {
        final var expectedTerms = "";
        final var aQuery =
                new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
        final var actualResult = useCase.execute(aQuery);

        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedCategoryName, actualResult.items().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
        "0,2,2,7,Amazon Originals;Documentários",
        "1,2,2,7,Filmes;Kids",
        "2,2,2,7,Netflix Originals;Series",
        "3,2,1,7,Sports",
    })
    void givenAValidPage_whenCallsListCategories_shouldReturnCategoriesPaginated(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedCategoriesName) {
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTerms = "";

        final var aQuery =
                new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
        final var actualResult = useCase.execute(aQuery);

        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());

        int index = 0;
        for (final String expectedName : expectedCategoriesName.split(";")) {
            final var actualName = actualResult.items().get(index).name();
            assertEquals(expectedName, actualName);
            index++;
        }
    }
}
