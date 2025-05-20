import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class DockerConnectionTest {

    @Test
    void shouldStartSimpleContainer() {
        try (GenericContainer<?> container = new GenericContainer<>("alpine:3.18")
                .withCommand("echo", "It works")) {
            container.start();
            String log = container.getLogs();
            assertTrue(log.contains("It works"));
        }
    }
}