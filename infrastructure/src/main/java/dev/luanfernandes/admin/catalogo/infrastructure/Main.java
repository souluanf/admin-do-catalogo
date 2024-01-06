package dev.luanfernandes.admin.catalogo.infrastructure;

import dev.luanfernandes.admin.catalogo.application.UseCase;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        System.out.println(new UseCase().execute());
    }
}
