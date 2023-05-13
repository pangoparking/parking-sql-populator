package org.parking;

import org.parking.entities.AreaEntity;
import org.parking.repo.AreasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootApplication
public class RandomSqlPopulatorApplication {

	@Autowired
	AreasRepository areasRepository;

	@Autowired
	MisradRishuiDataBasePopulator populator;

	public static void main(String[] args) {
		SpringApplication.run(RandomSqlPopulatorApplication.class, args);
	}

	@PostConstruct
	void fillDB() {
		log.trace("RandomSqlPopulatorApplication : fillDB");
		populator.fillDB();
	}
}
