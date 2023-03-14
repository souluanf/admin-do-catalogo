package dev.luanfernandes.admin.catalogo.domain.validation.handler;

import dev.luanfernandes.admin.catalogo.domain.exceptions.DomainExeption;
import dev.luanfernandes.admin.catalogo.domain.validation.Error;
import dev.luanfernandes.admin.catalogo.domain.validation.ValidationHandler;

import java.util.List;

public class ThrowsValidationHandler implements ValidationHandler {

    @Override
    public ValidationHandler append(final Error anError) {
        throw DomainExeption.with(anError);
    }

    @Override
    public ValidationHandler append(final ValidationHandler anHandler) {
        throw DomainExeption.with(anHandler.getErrors());
    }

    @Override
    public ValidationHandler validate(Validation anValidation) {
        try {
            anValidation.validate();
        } catch (final Exception e) {
            throw DomainExeption.with(new Error(e.getMessage()));
        }
        return this;
    }

    @Override
    public List<Error> getErrors() {
        return List.of();
    }
}
