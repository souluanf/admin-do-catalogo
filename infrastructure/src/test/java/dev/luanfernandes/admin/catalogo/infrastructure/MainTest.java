package dev.luanfernandes.admin.catalogo.infrastructure;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class MainTest {

    @Test
    void testMain() {
        Main main = new Main();
        Main.main(new String[] {});
        assertNotNull(main);
    }
}
