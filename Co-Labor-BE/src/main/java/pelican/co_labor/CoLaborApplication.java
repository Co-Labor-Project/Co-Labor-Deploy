package pelican.co_labor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CoLaborApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoLaborApplication.class, args);
    }

}
