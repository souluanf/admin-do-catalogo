package dev.luanfernandes.admin.catalogo.application.category.create;

import dev.luanfernandes.admin.catalogo.domain.category.CategoryGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;

@ExtendWith(MockitoExtension.class)
public class CreateCategoryUseCaseTest {

    // 1. Teste do caminho feliz
    // 2. Teste passando propriedade inválida (name)
    // 3. Teste criando uma categoria inativa
    // 4. Teste simulando um erro genérico vindo do gateway


    void givenAValidCommand_whenCallCreateCategory_shouldReturnCategoryId() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A melhor categoria de filmes";
        final var expectedIsActive = true;
        final var aCommand = new CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        CategoryGateway categoryGateway = Mockito.mock(CategoryGateway.class);
        Mockito.when(categoryGateway.create(Mockito.any())).thenAnswer(returnsFirstArg());

        final var useCase = new CreateCategoryUseCase(categoryGateway);

        final var actualoutput = useCase.execute(aCommand);

        Assertions.assertNotNull(actualoutput);
        Assertions.assertNotNull(actualoutput.getId());

        Mockito.verify(categoryGateway, Mockito.times(1)).create(Mockito.argThat(aCategory ->
                Objects.equals(expectedName, aCategory.getName())
                        && Objects.equals(expectedDescription, aCategory.getDescription())
                        && Objects.equals(expectedIsActive, aCategory.isActive())
                        && Objects.nonNull(aCategory.getId())
                        && Objects.nonNull(aCategory.getCreatedAt())
                        && Objects.nonNull(aCategory.getUpdatedAt())
                        && Objects.nonNull(aCategory.getDeletedAt())));
    }
}

