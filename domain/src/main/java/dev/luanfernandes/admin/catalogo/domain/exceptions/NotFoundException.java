package dev.luanfernandes.admin.catalogo.domain.exceptions;

import dev.luanfernandes.admin.catalogo.domain.AggregateRoot;
import dev.luanfernandes.admin.catalogo.domain.Identifier;
import dev.luanfernandes.admin.catalogo.domain.validation.Error;
import java.util.Collections;
import java.util.List;

public class NotFoundException extends DomainException {

    protected NotFoundException(final String aMessage, final List<Error> anErrors) {
        super(aMessage, anErrors);
    }

    public static NotFoundException with(final Class<? extends AggregateRoot<?>> anAggregate, final Identifier id) {
        final var andErrors = "%s with id %s not found".formatted(anAggregate.getSimpleName(), id.getValue());
        return new NotFoundException(andErrors, Collections.emptyList());
    }
}
