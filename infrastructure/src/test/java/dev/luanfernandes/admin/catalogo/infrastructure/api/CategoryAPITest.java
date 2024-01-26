package dev.luanfernandes.admin.catalogo.infrastructure.api;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.luanfernandes.admin.catalogo.ControllerTest;
import dev.luanfernandes.admin.catalogo.application.category.create.CreateCategoryOutput;
import dev.luanfernandes.admin.catalogo.application.category.create.CreateCategoryUseCase;
import dev.luanfernandes.admin.catalogo.infrastructure.category.models.CreateCategoryApiInput;
import io.vavr.API;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@ControllerTest(controllers = CategoryAPI.class)
class CategoryAPITest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CreateCategoryUseCase createCategoryUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenAValidCommand_whenCallsCreateCategory_thenShouldReturnCategoryID() throws Exception {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedActive = true;

        final var anInput = new CreateCategoryApiInput(expectedName, expectedDescription, expectedActive);

        when(createCategoryUseCase.execute(any())).thenReturn(API.Right(new CreateCategoryOutput("123")));

        final var request = post("/categories")
                .contentType(APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(anInput));

        this.mvc
                .perform(request)
                .andDo(print())
                .andExpectAll(
                        status().isCreated(),
                        header().string("Location", "/categories/123"),
                        header().string("Content-Type", APPLICATION_JSON.toString()),
                        jsonPath("$.id", equalTo("123")));

        verify(createCategoryUseCase, times(1)).execute(argThat(command -> {
            Objects.equals(expectedName, command.name());
            Objects.equals(expectedDescription, command.description());
            Objects.equals(expectedActive, command.active());
            return true;
        }));
    }
}
