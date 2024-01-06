package dev.luanfernandes.admin.catalogo.domain.validation;

public abstract class Validator {
    protected final ValidationHandler handler;

    protected Validator(final ValidationHandler aHandler) {
        this.handler = aHandler;
    }
    public abstract void validate();

    protected ValidationHandler validationHandler() {
        return this.handler;
    }
}
