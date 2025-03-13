package vn.dathocjava.dathocjava_sample;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication()
public class DathocjavaSampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(DathocjavaSampleApplication.class, args);
	}

}
