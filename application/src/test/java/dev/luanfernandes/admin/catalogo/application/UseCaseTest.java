package dev.luanfernandes.admin.catalogo.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UseCaseTest {

    @Test
    void testUseCase() {
        UseCase<String, String> useCase = new UseCase<>() {
            @Override
            public String execute(String anIn) {
                return anIn;
            }
        };
        assertEquals("Hello world!", useCase.execute("Hello world!"));
    }

}