package uk.org.brooklyn.ranger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author ImBrooklyn
 * @since 07/01/2024
 */
@SpringBootApplication
public class ZkShellApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ZkShellApplication.class);
        application.run(args);
    }
}
