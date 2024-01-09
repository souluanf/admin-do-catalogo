package dev.luanfernandes.admin.catalogo.application.category.update;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.luanfernandes.admin.catalogo.domain.category.Category;
import dev.luanfernandes.admin.catalogo.domain.category.CategoryGateway;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateCategoryUseCaseTest {

    @InjectMocks
    private DefaultUpdateCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Test
    void givenAValidCommand_whenCallsUpdateCategory_thenShouldReturnCategoryID() {
        final var aCategory = Category.newCategory("Film", null, true);

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedActive = true;
        final var expectedId = aCategory.getId();

        final var aCommand =
                UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedActive);

        when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(aCategory));
        when(categoryGateway.update(any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand).get();

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(categoryGateway, times(1)).findById(eq(expectedId));
        verify(categoryGateway, times(1))
                .update(argThat(anUpdatedCategory -> Objects.equals(expectedName, anUpdatedCategory.getName())
                        && Objects.equals(expectedDescription, anUpdatedCategory.getDescription())
                        && Objects.equals(expectedActive, anUpdatedCategory.isActive())
                        && Objects.equals(expectedId, anUpdatedCategory.getId())
                        && Objects.equals(aCategory.getCreatedAt(), anUpdatedCategory.getCreatedAt())
                        && aCategory.getUpdatedAt().isBefore(anUpdatedCategory.getUpdatedAt())
                        && Objects.isNull(anUpdatedCategory.getDeletedAt())));
    }
}
