package dev.luanfernandes.admin.catalogo.application.category.create;

import dev.luanfernandes.admin.catalogo.domain.category.CategoryGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;

@ExtendWith(MockitoExtension.class)
class CreateCategoryUseCaseTest {
    // 1. Teste do caminho feliz
    // 2. Teste passando uma propriedade inválida (name)
    // 3. Teste criando uma categoria inativa
    // 4. Teste simulando um erro genérico vindo do gateway



    @Test
    void givenAValidCommand_whenCallsCreateCategory_thenShouldReturnCategoryID() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedActive = true;

        final var aCommand = new CreateCategoryCommand.with(expectedName, expectedDescription, expectedActive);

        CategoryGateway categoryGateway = Mockito.mock(CategoryGateway.class);
        Mockito.when(categoryGateway.create(Mockito.any())).thenAnswer(returnsFirstArg());


        final var useCase = new CreateCategoryUseCase(categoryGateway);

        final var actualOutput = useCase.execute(aCommand);

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.getId());

        Mockito.verify(categoryGateway, Mockito.times(1)).create(Mockito.argThat(category ->
                Objects.equals(expectedName,category.getName()) &&
                Objects.equals(expectedDescription,category.getDescription()) &&
                Objects.equals(expectedActive,category.isActive()) &&
                Objects.nonNull(category.getId()) &&
                Objects.nonNull(category.getCreatedAt()) &&
                Objects.nonNull(category.getUpdatedAt()) &&
                Objects.isNull(category.getDeletedAt())

        ));
    }

}
