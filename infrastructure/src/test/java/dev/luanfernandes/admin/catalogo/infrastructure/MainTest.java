package dev.luanfernandes.admin.catalogo.infrastructure;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class MainTest {

    @Test
    void testMain() {
        Main main = new Main();
        Main.main(new String[]{});
        assertNotNull(main);
    }
}