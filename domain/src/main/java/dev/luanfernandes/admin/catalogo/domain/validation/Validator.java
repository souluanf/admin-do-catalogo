package dev.luanfernandes.admin.catalogo.domain.validation;

public abstract class Validator {
    private final ValidationHandler handler;

    protected Validator(ValidationHandler handler) {
        this.handler = handler;
    }

    protected abstract void validate();

    protected ValidationHandler validationHandler() {
        return this.handler;
    }
}
