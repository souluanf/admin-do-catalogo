package dev.luanfernandes.admin.catalogo.application;


import dev.luanfernandes.admin.catalogo.domain.category.Category;

public class UseCase {
    public Category execute() {
        return new Category();
    }
}