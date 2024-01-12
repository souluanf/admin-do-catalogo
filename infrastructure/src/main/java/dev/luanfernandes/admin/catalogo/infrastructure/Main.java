package dev.luanfernandes.admin.catalogo.infrastructure;

import static java.lang.System.setProperty;
import static org.springframework.core.env.AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME;

import dev.luanfernandes.admin.catalogo.domain.category.Category;
import dev.luanfernandes.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import dev.luanfernandes.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import dev.luanfernandes.admin.catalogo.infrastructure.configuration.WebServerConfig;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        setProperty(DEFAULT_PROFILES_PROPERTY_NAME, "development");
        SpringApplication.run(WebServerConfig.class, args);
    }

    @Bean
    public ApplicationRunner runner(CategoryRepository repository) {
        return args -> {
            Category filmes = Category.newCategory("Filmes", null, true);
            repository.saveAndFlush(CategoryJpaEntity.from(filmes));
            repository.deleteAll();
        };
    }
}
