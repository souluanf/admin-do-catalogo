package dev.luanfernandes.admin.catalogo.application.category.retrieve.get;

import static java.util.Arrays.stream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;

import dev.luanfernandes.admin.catalogo.IntegrationTest;
import dev.luanfernandes.admin.catalogo.domain.category.Category;
import dev.luanfernandes.admin.catalogo.domain.category.CategoryGateway;
import dev.luanfernandes.admin.catalogo.domain.category.CategoryID;
import dev.luanfernandes.admin.catalogo.domain.exceptions.DomainException;
import dev.luanfernandes.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import dev.luanfernandes.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

@IntegrationTest
class GetCategoryUseCaseIT {

    @Autowired
    private GetCategoryByIdUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    void givenAValidId_whenCallsGetCategory_thenShouldReturnCategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedActive);

        save(aCategory);

        final var expectedId = aCategory.getId();

        final var actualCategory = useCase.execute(expectedId.getValue());

        assertEquals(expectedId, actualCategory.id());
        assertEquals(expectedName, actualCategory.name());
        assertEquals(expectedDescription, actualCategory.description());
        assertEquals(expectedActive, actualCategory.active());
        assertEquals(aCategory.getCreatedAt(), actualCategory.createdAt());
        assertEquals(aCategory.getUpdatedAt(), actualCategory.updatedAt());
        assertEquals(aCategory.getDeletedAt(), actualCategory.deletedAt());
    }

    private void save(final Category... aCategory) {
        categoryRepository.saveAllAndFlush(
                stream(aCategory).map(CategoryJpaEntity::from).toList());
    }

    @Test
    void givenAnInvalidId_whenCallsGetCategory_thenShouldReturnNotFound() {
        final var expectedErrorMessage = "Category with id 123 not found";
        final var expectedId = CategoryID.from("123");

        final var actualException = assertThrows(DomainException.class, () -> useCase.execute(expectedId.getValue()));
        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    void givenAValidId_whenGatewayThrowsException_thenShouldReturnException() {
        final var expectedErrorMessage = "Gateway error";
        final var expectedId = CategoryID.from("123");

        doThrow(new IllegalStateException(expectedErrorMessage))
                .when(categoryGateway)
                .findById(eq(expectedId));

        final var actualException =
                Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

        assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
