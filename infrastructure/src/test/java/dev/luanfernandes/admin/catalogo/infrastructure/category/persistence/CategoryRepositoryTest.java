package dev.luanfernandes.admin.catalogo.infrastructure.category.persistence;

import static dev.luanfernandes.admin.catalogo.domain.category.Category.newCategory;
import static dev.luanfernandes.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity.from;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dev.luanfernandes.admin.catalogo.MySQLGatewayTest;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

@MySQLGatewayTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void givenAnInvalidNullName_whenCallsSave_shouldReturnError() {
        final var expectedProperty = "name";
        final var expectedMessage =
                "not-null property references a null or transient value : dev.luanfernandes.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity.name";
        final var aCategory = newCategory("Filmes", "A categoria mais assistida", true);
        final var anEntity = from(aCategory);

        anEntity.setName(null);

        final var actualException =
                assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(anEntity));
        final var actualCause = assertInstanceOf(PropertyValueException.class, actualException.getCause());

        assertEquals(expectedProperty, actualCause.getPropertyName());
        assertEquals(expectedMessage, actualCause.getMessage());
    }

    @Test
    void givenAnInvalidCreatedAt_whenCallsSave_shouldReturnError() {
        final var expectedProperty = "createdAt";
        final var expectedMessage =
                "not-null property references a null or transient value : dev.luanfernandes.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity.createdAt";
        final var aCategory = newCategory("Filmes", "A categoria mais assistida", true);
        final var anEntity = from(aCategory);

        anEntity.setCreatedAt(null);

        final var actualException =
                assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(anEntity));
        final var actualCause = assertInstanceOf(PropertyValueException.class, actualException.getCause());

        assertEquals(expectedProperty, actualCause.getPropertyName());
        assertEquals(expectedMessage, actualCause.getMessage());
    }

    @Test
    void givenAnInvalidUpdatedAt_whenCallsSave_shouldReturnError() {
        final var expectedProperty = "updatedAt";
        final var expectedMessage =
                "not-null property references a null or transient value : dev.luanfernandes.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity.updatedAt";
        final var aCategory = newCategory("Filmes", "A categoria mais assistida", true);
        final var anEntity = from(aCategory);

        anEntity.setUpdatedAt(null);

        final var actualException =
                assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(anEntity));
        final var actualCause = assertInstanceOf(PropertyValueException.class, actualException.getCause());

        assertEquals(expectedProperty, actualCause.getPropertyName());
        assertEquals(expectedMessage, actualCause.getMessage());
    }
}
