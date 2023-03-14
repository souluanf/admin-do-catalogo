package dev.luanfernandes.admin.catalogo.domain.category;

import dev.luanfernandes.admin.catalogo.domain.validation.Error;
import dev.luanfernandes.admin.catalogo.domain.validation.ValidationHandler;
import dev.luanfernandes.admin.catalogo.domain.validation.Validator;

public class CategoryValidator extends Validator {
    private final Category category;
    private static final int NAME_MAX_LENGTH = 255;
    private static final int NAME_MIN_LENGTH = 3;


    public CategoryValidator(final Category aCategory, final ValidationHandler handler) {
        super(handler);
        this.category = aCategory;
    }

    @Override
    protected void validate() {
        checkNameConstraints();
    }

    private void checkNameConstraints() {
        final String name = category.getName();
        if (name == null) {
            validationHandler().append(new Error("'name' should not be null"));
            return;
        }
        if (name.isBlank()) {
            validationHandler().append(new Error("'name' should not be empty"));
            return;
        }
        final int length = name.trim().length();
        if (length > NAME_MAX_LENGTH || length < NAME_MIN_LENGTH) {
            validationHandler().append(new Error("'name' must be between 3 and 255 characters"));
        }
    }
}
