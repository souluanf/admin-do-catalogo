package dev.luanfernandes.admin.catalogo;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import dev.luanfernandes.admin.catalogo.infrastructure.configuration.WebServerConfig;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Target(TYPE)
@Retention(RUNTIME)
@Inherited
@ActiveProfiles("test")
@SpringBootTest(classes = {WebServerConfig.class})
@ExtendWith(CleanUpExtension.class)
public @interface IntegrationTest {}
