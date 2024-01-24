package dev.luanfernandes.admin.catalogo;

import static org.springframework.test.context.junit.jupiter.SpringExtension.getApplicationContext;

import java.util.Collection;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.data.repository.CrudRepository;

public class CleanUpExtension implements BeforeEachCallback {
    @Override
    public void beforeEach(ExtensionContext context) {
        final var repositories = getApplicationContext(context)
                .getBeansOfType(CrudRepository.class)
                .values();
        cleanUp(repositories);
    }

    private void cleanUp(final Collection<CrudRepository> repositories) {
        repositories.forEach(CrudRepository::deleteAll);
    }
}
