package dev.luanfernandes.admin.catalogo.application.category.retrieve.list;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import dev.luanfernandes.admin.catalogo.domain.category.Category;
import dev.luanfernandes.admin.catalogo.domain.category.CategoryGateway;
import dev.luanfernandes.admin.catalogo.domain.category.CategoryID;
import dev.luanfernandes.admin.catalogo.domain.category.CategorySearchQuery;
import dev.luanfernandes.admin.catalogo.domain.exceptions.DomainException;
import dev.luanfernandes.admin.catalogo.domain.pagination.Pagination;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ListCategoriesUseCaseTest {

    @InjectMocks
    private DefaultListCategoriesUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    public void cleanUp() {
        reset(categoryGateway);
    }

    @Test
    void givenAValidQuery_whenCallsListCategories_thenShouldReturnCategories() {
        final var categories =
                List.of(Category.newCategory("Filmes", null, true), Category.newCategory("Series", null, true));
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var terms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var query =
                new CategorySearchQuery(expectedPage, expectedPerPage, terms, expectedSort, expectedDirection);

        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, categories.size(), categories);

        final var expectedItemsCount = 2;
        final var expectedResult = expectedPagination.map(CategoryListOutput::from);

        when(categoryGateway.findAll(eq(query))).thenReturn(expectedPagination);

        final var actualResult = useCase.execute(expectedId.getValue());

        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedResult, actualResult);
        assertEquals(expectedPage, actualResult.page());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(categories.size(), actualResult.total());
    }

    @Test
    void givenAValidQuery_whenHasNoResults_thenShouldReturnEmptyCategories() {
        final var expectedErrorMessage = "Category with id 123 not found";
        final var expectedId = CategoryID.from("123");

        when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.empty());

        final var actualException = assertThrows(DomainException.class, () -> useCase.execute(expectedId.getValue()));
        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    void givenAValidQuery_whenGatewayThrowsException_thenShouldReturnException() {
        final var expectedErrorMessage = "Gateway error";
        final var expectedId = CategoryID.from("123");

        when(categoryGateway.findById(eq(expectedId))).thenThrow(new IllegalStateException(expectedErrorMessage));

        final var actualException =
                assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));
        assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
