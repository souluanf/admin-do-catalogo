package dev.luanfernandes.admin.catalogo.application.category.create;

public record CreateCategoryCommand(String name, String description, boolean active) {
    public static CreateCategoryCommand with(final String aName, final String aDescription, boolean isActive) {
        return new CreateCategoryCommand(aName, aDescription, isActive);
    }
}
