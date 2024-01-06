package dev.luanfernandes.admin.catalogo.domain;

import dev.luanfernandes.admin.catalogo.domain.category.Category;
import dev.luanfernandes.admin.catalogo.domain.exceptions.DomainException;
import dev.luanfernandes.admin.catalogo.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CategoryTest {

    @Test
    void givenAValidParams_whenCallNewCategory_thenInstantiateACategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        assertNotNull(actualCategory);
        assertNotNull(actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertNotNull(actualCategory.getCreatedAt());
        assertNotNull(actualCategory.getUpdatedAt());
        assertNull(actualCategory.getDeletedAt());
    }

    @Test
    void givenAnInvalidNullName_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        Executable createAndValidateCategory = () -> {
            final var actualCategory = Category.newCategory(null, expectedDescription, expectedIsActive);
            actualCategory.validate(new ThrowsValidationHandler());
        };
        final var actualException = assertThrows(DomainException.class, createAndValidateCategory);
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    void givenAnInvalidEmptyName_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final var expectedName = " ";
        final var expectedErrorMessage = "'name' should not be blank";
        final var expectedErrorCount = 1;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        Executable createAndValidateCategory = () -> {
            final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
            actualCategory.validate(new ThrowsValidationHandler());
        };
        final var actualException = assertThrows(DomainException.class, createAndValidateCategory);
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    void givenAnInvalidInvalidNameLengthLessThan3_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final var expectedName = "Fi ";
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";
        final var expectedErrorCount = 1;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        Executable createAndValidateCategory = () -> {
            final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
            actualCategory.validate(new ThrowsValidationHandler());
        };
        final var actualException = assertThrows(DomainException.class, createAndValidateCategory);
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    void givenAnInvalidInvalidNameLengthMoreThan255_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final var expectedName = """
                Assim mesmo, a estrutura atual da organização auxilia a preparação
                e a composição das posturas dos órgãos dirigentes com relação
                às suas atribuições. Do mesmo modo, a percepção das dificuldades exige
                a precisão e a definição das formas de ação e a necessidade de renovação.
                """;
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";
        final var expectedErrorCount = 1;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        Executable createAndValidateCategory = () -> {
            final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
            actualCategory.validate(new ThrowsValidationHandler());
        };
        final var actualException = assertThrows(DomainException.class, createAndValidateCategory);
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    void givenAValidEmptyDescription_whenCallNewCategoryAndValidate_thenReceiveOK() {
        final var expectedName = "Filmes";
        final var expectedDescription = " ";
        final var expectedIsActive = true;
        assertDoesNotThrow(() -> {
            final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
            actualCategory.validate(new ThrowsValidationHandler());
            assertNotNull(actualCategory);
            assertNotNull(actualCategory.getId());
            assertEquals(expectedName, actualCategory.getName());
            assertEquals(expectedDescription, actualCategory.getDescription());
            assertEquals(expectedIsActive, actualCategory.isActive());
            assertNotNull(actualCategory.getCreatedAt());
            assertNotNull(actualCategory.getUpdatedAt());
            assertNull(actualCategory.getDeletedAt());
        });
    }

    @Test
    void givenAValidFalseActive_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;
        assertDoesNotThrow(() -> {
            final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
            actualCategory.validate(new ThrowsValidationHandler());
            assertNotNull(actualCategory);
            assertNotNull(actualCategory.getId());
            assertEquals(expectedName, actualCategory.getName());
            assertEquals(expectedDescription, actualCategory.getDescription());
            assertEquals(expectedIsActive, actualCategory.isActive());
            assertNotNull(actualCategory.getCreatedAt());
            assertNotNull(actualCategory.getUpdatedAt());
            assertNotNull(actualCategory.getDeletedAt());
        });
    }


}