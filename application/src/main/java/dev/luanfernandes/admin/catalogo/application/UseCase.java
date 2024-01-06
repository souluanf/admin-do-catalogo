package dev.luanfernandes.admin.catalogo.application;


import dev.luanfernandes.admin.catalogo.domain.category.Category;

public class UseCase {
    public Category execute() {
        return Category.newCategory("Category 1", "Description 1", true);
    }
}