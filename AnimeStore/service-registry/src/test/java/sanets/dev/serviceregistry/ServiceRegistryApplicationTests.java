package sanets.dev.serviceregistry;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "eureka.client.enabled=false",
        "spring.cloud.config.enabled=false",
        "spring.autoconfigure.exclude=org.springframework.cloud.netflix.eureka.server.EurekaServerAutoConfiguration"
})
class ServiceRegistryApplicationTests {
    @Test
    void contextLoads() { }
}
