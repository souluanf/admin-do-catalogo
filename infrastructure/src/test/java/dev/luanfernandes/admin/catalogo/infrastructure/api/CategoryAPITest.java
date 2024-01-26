package dev.luanfernandes.admin.catalogo.infrastructure.api;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.luanfernandes.admin.catalogo.ControllerTest;
import dev.luanfernandes.admin.catalogo.application.category.create.CreateCategoryOutput;
import dev.luanfernandes.admin.catalogo.application.category.create.CreateCategoryUseCase;
import dev.luanfernandes.admin.catalogo.application.category.retrieve.get.CategoryOutput;
import dev.luanfernandes.admin.catalogo.application.category.retrieve.get.GetCategoryByIdUseCase;
import dev.luanfernandes.admin.catalogo.domain.category.Category;
import dev.luanfernandes.admin.catalogo.domain.category.CategoryID;
import dev.luanfernandes.admin.catalogo.domain.exceptions.DomainException;
import dev.luanfernandes.admin.catalogo.domain.validation.Error;
import dev.luanfernandes.admin.catalogo.domain.validation.handler.Notification;
import dev.luanfernandes.admin.catalogo.infrastructure.category.models.CreateCategoryApiInput;
import io.vavr.API;
import java.util.Objects;
import org.hamcrest.Matchers;
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

    @MockBean
    private GetCategoryByIdUseCase getCategoryByIdUseCase;

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

    @Test
    void givenAInvalidName_whenCallsCreateCategory_theShouldReturnNotification() throws Exception {
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedActive = true;
        final var expectedMessage = "name' should not be null";

        final var anInput = new CreateCategoryApiInput(expectedName, expectedDescription, expectedActive);

        when(createCategoryUseCase.execute(any()))
                .thenReturn(API.Left(Notification.create(new Error(expectedMessage))));

        final var request = post("/categories")
                .contentType(APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(anInput));

        this.mvc
                .perform(request)
                .andDo(print())
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        header().string("Location", Matchers.nullValue()),
                        header().string("Content-Type", APPLICATION_JSON.toString()),
                        jsonPath("$.errors", hasSize(1)),
                        jsonPath("$.errors[0].message", equalTo(expectedMessage)));

        verify(createCategoryUseCase, times(1)).execute(argThat(command -> {
            Objects.equals(expectedName, command.name());
            Objects.equals(expectedDescription, command.description());
            Objects.equals(expectedActive, command.active());
            return true;
        }));
    }

    @Test
    void givenAInvalidCommand_whenCallsCreateCategory_theShouldReturnDomainException() throws Exception {
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedActive = true;
        final var expectedMessage = "name' should not be null";

        final var anInput = new CreateCategoryApiInput(expectedName, expectedDescription, expectedActive);

        when(createCategoryUseCase.execute(any())).thenThrow(DomainException.with(new Error(expectedMessage)));

        final var request = post("/categories")
                .contentType(APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(anInput));

        this.mvc
                .perform(request)
                .andDo(print())
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        header().string("Location", Matchers.nullValue()),
                        header().string("Content-Type", APPLICATION_JSON.toString()),
                        jsonPath("$.errors", hasSize(1)),
                        jsonPath("$.message", equalTo(expectedMessage)),
                        jsonPath("$.errors[0].message", equalTo(expectedMessage)));

        verify(createCategoryUseCase, times(1)).execute(argThat(command -> {
            Objects.equals(expectedName, command.name());
            Objects.equals(expectedDescription, command.description());
            Objects.equals(expectedActive, command.active());
            return true;
        }));
    }

    @Test
    void givenAValidId_whenCallsGetCategory_thenShouldReturnCategory() throws Exception {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedActive);

        final var expectedId = aCategory.getId().getValue();

        when(getCategoryByIdUseCase.execute(any())).thenReturn(CategoryOutput.from(aCategory));

        final var request = get("/categories/{id}", expectedId);

        final var response = this.mvc.perform(request).andDo(print());

        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON.toString()))
                .andExpect(jsonPath("$.id", equalTo(expectedId)))
                .andExpect(jsonPath("$.name", equalTo(expectedName)))
                .andExpect(jsonPath("$.description", equalTo(expectedDescription)))
                .andExpect(jsonPath("$.is_active", equalTo(expectedActive)))
                .andExpect(jsonPath(
                        "$.created_at", equalTo(aCategory.getCreatedAt().toString())))
                .andExpect(jsonPath(
                        "$.updated_at", equalTo(aCategory.getUpdatedAt().toString())))
                .andExpect(jsonPath("$.deleted_at", equalTo(aCategory.getDeletedAt())));

        verify(getCategoryByIdUseCase, times(1)).execute(eq(expectedId));
    }

    @Test
    void givenAnInvalidId_whenCallsGetCategory_thenShouldReturnNotFound() throws Exception {
        final var expectedErrorMessage = "Category with id 123 not found";
        final var expectedId = CategoryID.from("123").getValue();

        when(getCategoryByIdUseCase.execute(any()))
                .thenThrow(DomainException.with(new Error("Category with id %S was not found".formatted(expectedId))));

        final var request = get("/categories/{id}", expectedId);

        final var response = this.mvc.perform(request).andDo(print());

        response.andExpect(status().isNotFound()).andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));
    }
}
