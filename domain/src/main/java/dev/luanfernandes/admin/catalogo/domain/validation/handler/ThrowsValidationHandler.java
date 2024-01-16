package dev.luanfernandes.admin.catalogo.domain.validation.handler;

import static dev.luanfernandes.admin.catalogo.domain.exceptions.DomainException.with;
import static java.util.List.of;

import dev.luanfernandes.admin.catalogo.domain.validation.Error;
import dev.luanfernandes.admin.catalogo.domain.validation.ValidationHandler;
import java.util.List;

public class ThrowsValidationHandler implements ValidationHandler {

    @Override
    public ValidationHandler append(final Error anError) {
        throw with(anError);
    }

    @Override
    public ValidationHandler append(final ValidationHandler anHandler) {
        throw with(anHandler.getErrors());
    }

    @Override
    public ValidationHandler validate(final Validation aValidation) {
        try {
            aValidation.validate();
        } catch (final Exception e) {
            throw with(of(new Error(e.getMessage())));
        }
        return this;
    }

    @Override
    public List<Error> getErrors() {
        return of();
    }
}
