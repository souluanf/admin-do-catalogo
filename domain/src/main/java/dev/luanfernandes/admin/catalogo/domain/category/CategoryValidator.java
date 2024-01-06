package dev.luanfernandes.admin.catalogo.domain.category;

import dev.luanfernandes.admin.catalogo.domain.validation.ValidationHandler;
import dev.luanfernandes.admin.catalogo.domain.validation.Validator;
import dev.luanfernandes.admin.catalogo.domain.validation.Error;

public class CategoryValidator extends Validator {
    private final Category category;

    public CategoryValidator(final Category aCategory,ValidationHandler aHandler) {
        super(aHandler);
        this.category = aCategory;
    }

    @Override
    public void validate() {
        if (category.getName() == null) {
            this.validationHandler().append(new Error("'name' cannot be null"));
        }
    }
}
