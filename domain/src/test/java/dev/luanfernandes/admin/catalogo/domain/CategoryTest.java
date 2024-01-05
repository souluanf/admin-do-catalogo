package dev.luanfernandes.admin.catalogo.domain;

import dev.luanfernandes.admin.catalogo.domain.category.Category;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    void testNewCategory() {
        Category category = new Category();
        assertNotNull(category);
    }

}