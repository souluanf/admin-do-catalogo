package dev.luanfernandes.admin.catalogo.application.category.create;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.luanfernandes.admin.catalogo.domain.category.CategoryGateway;
import dev.luanfernandes.admin.catalogo.domain.exceptions.DomainException;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateCategoryUseCaseTest {

    @Mock
    private CategoryGateway categoryGateway;

    @InjectMocks
    DefaultCreateCategoryUseCase useCase;

    @Test
    void givenAValidCommand_whenCallsCreateCategory_thenShouldReturnCategoryID() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedActive = true;
        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedActive);
        when(categoryGateway.create(any())).thenAnswer(returnsFirstArg());
        final var actualOutput = useCase.execute(aCommand);
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());
        verify(categoryGateway, times(1))
                .create(argThat(category -> Objects.equals(expectedName, category.getName())
                        && Objects.equals(expectedDescription, category.getDescription())
                        && Objects.equals(expectedActive, category.isActive())
                        && Objects.nonNull(category.getId())
                        && Objects.nonNull(category.getCreatedAt())
                        && Objects.nonNull(category.getUpdatedAt())
                        && Objects.isNull(category.getDeletedAt())));
    }

    @Test
    void givenAInvalidName_whenCallsCreateCategory_theShouldReturnDomainException() {
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedActive);
        final var actualException = assertThrows(DomainException.class, () -> useCase.execute(aCommand));
        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(categoryGateway, times(0)).create(any());
    }

    @Test
    void givenAValidCommandWithInactiveCategory_whenCallsCreateCategory_thenShouldReturnInactiveCategoryId() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedActive = false;
        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedActive);
        when(categoryGateway.create(any())).thenAnswer(returnsFirstArg());
        final var actualOutput = useCase.execute(aCommand);
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());
        verify(categoryGateway, times(1))
                .create(argThat(category -> Objects.equals(expectedName, category.getName())
                        && Objects.equals(expectedDescription, category.getDescription())
                        && Objects.equals(expectedActive, category.isActive())
                        && Objects.nonNull(category.getId())
                        && Objects.nonNull(category.getCreatedAt())
                        && Objects.nonNull(category.getUpdatedAt())
                        && Objects.nonNull(category.getDeletedAt())));
    }

    @Test
    void givenAValidCommand_whenGatewayThrowsRandomException_thenReturnAnException() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedActive = true;
        final var expectedErrorMessage = "Gateway error";
        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedActive);
        when(categoryGateway.create(any())).thenThrow(new IllegalStateException(expectedErrorMessage));
        final var actualException = assertThrows(IllegalStateException.class, () -> useCase.execute(aCommand));
        assertEquals(expectedErrorMessage, actualException.getMessage());
        verify(categoryGateway, times(1)).create(any());
    }
}
