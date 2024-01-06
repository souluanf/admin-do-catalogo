package dev.luanfernandes.admin.catalogo.application;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class UseCaseTest {

    @Test
    void testCreateUseCase() {
        UseCase useCase = new UseCase();
        assertNotNull(useCase);
        assertNotNull(useCase.execute());
    }
}