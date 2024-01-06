package dev.luanfernandes.admin.catalogo.application;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class UseCaseTest {

    @Test
    void testCreateUseCase() {
        UseCase useCase = new UseCase();
        assertNotNull(useCase);
        assertNotNull(useCase.execute());
    }
}
