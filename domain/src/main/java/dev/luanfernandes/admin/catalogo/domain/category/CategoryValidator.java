package dev.luanfernandes.admin.catalogo.domain.category;

import dev.luanfernandes.admin.catalogo.domain.validation.Error;
import dev.luanfernandes.admin.catalogo.domain.validation.ValidationHandler;
import dev.luanfernandes.admin.catalogo.domain.validation.Validator;

public class CategoryValidator extends Validator {
    private final Category category;

    public CategoryValidator(final Category aCategory, ValidationHandler aHandler) {
        super(aHandler);
        this.category = aCategory;
    }

    @Override
    public void validate() {
        checkNameConstraints();
    }

    private void checkNameConstraints() {
        final var name = category.getName();
        if (name == null) {
            this.validationHandler().append(new Error("'name' should not be null"));
            return;
        }
        if (name.isBlank()) {
            this.validationHandler().append(new Error("'name' should not be blank"));
            return;
        }
        final int length = name.trim().length();
        System.out.println(length);
        if (length > 255 || length < 3) {
            this.validationHandler().append(new Error("'name' must be between 3 and 255 characters"));
        }
    }
}
