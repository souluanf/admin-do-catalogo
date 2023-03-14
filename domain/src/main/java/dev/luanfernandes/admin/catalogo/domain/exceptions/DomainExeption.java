package dev.luanfernandes.admin.catalogo.domain.exceptions;


import dev.luanfernandes.admin.catalogo.domain.validation.Error;

import java.util.List;

public class DomainExeption extends NoStackTraceException {

    private final List<Error> errors;

    private DomainExeption(final String aMessage,final List<Error> anErrors) {
        super(aMessage);
        this.errors = anErrors;
    }

    public static DomainExeption with(final Error anError) {
        return new DomainExeption(anError.message(),List.of(anError));
    }

    public static DomainExeption with(final List<Error> anErrors) {
        return new DomainExeption("",anErrors);
    }

    public List<Error> getErrors() {
        return errors;
    }
}
