package bsep.hospital;

import bsep.hospital.repository.LogRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

//@EnableMongoRepositories(basePackageClasses = LogRepository.class)
@SpringBootApplication
@EnableScheduling
public class HospitalApplication {

	public static void main(String[] args) {
		SpringApplication.run(HospitalApplication.class, args);
	}

}
