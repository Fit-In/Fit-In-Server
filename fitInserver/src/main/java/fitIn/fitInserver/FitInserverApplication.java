package fitIn.fitInserver;

import fitIn.fitInserver.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class FitInserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(FitInserverApplication.class, args);
	}

}
