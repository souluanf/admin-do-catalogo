package dev.luanfernandes.admin.catalogo.application.category.create;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.luanfernandes.admin.catalogo.domain.category.CategoryGateway;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateCategoryUseCaseTest {

    @Test
    void givenAValidCommand_whenCallsCreateCategory_thenShouldReturnCategoryID() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedActive = true;

        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedActive);

        CategoryGateway categoryGateway = mock(CategoryGateway.class);

        when(categoryGateway.create(any())).thenAnswer(returnsFirstArg());

        final var useCase = new DefaultCreateCategoryUseCase(categoryGateway);

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
}
