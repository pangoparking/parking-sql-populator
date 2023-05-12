package org.parking;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import org.parking.entities.AreaEntity;
import org.parking.entities.CarEntity;
import org.parking.entities.OwnerEntity;
import org.parking.service.MisradRishuiDataBaseFillingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.javafaker.Faker;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@Transactional(readOnly = true)
public class MisradRishuiDataBasePopulator {

	Set<OwnerEntity> owners = new HashSet<>();

	@Value("${app.org.parking.minCarNumber:10000000}")
	Integer minCarNumber;
	@Value("${app.org.parking.maxCarNumber:50}")
	Integer maxCarNumber;
	@Value("${app.org.parking.nAreas:3}")
	Integer nAreas;

	@Value("${app.parkingPlaceID.min:1}")
	private int minParkingLotID;

	@Value("${app.parking.amount:101}")
	private int maxParkingLotID;

	@Value("${spring.jpa.hibernate.ddl-auto}")
	String ddlAuto;

	@Autowired
	MisradRishuiDataBaseFillingService service;

	public void fillDB() {
//		addOwners();
//		addCars();
		addAreas();
	}

	public void fillDBIfCreate() {
		if (ddlAuto.equals("create")) {
			addOwners();
			addCars();
			addAreas();
		}
	}

	@Transactional
	public void addOwners() {
		log.trace("MisradRishuiDataBasePopulator : addOwners");
		createListOfOwners();
		owners.stream().forEach(owner -> {
			log.trace("addOwners : adding owner {}", owner);
			service.addOwner(owner);
		});
	}

	@Transactional
	public void addCars() {
		int[] carCounter = { 0 };
		log.trace("MisradRishuiDataBasePopulator : addCars");
		var iterator = owners.iterator();
		IntStream.range(minCarNumber, maxCarNumber).asLongStream().forEach(carNum -> {
			service.addCar(new CarEntity(carNum, iterator.next()));
			carCounter[0]++;
		});
		log.trace("addCars : added {} cars", carCounter[0]);
	}

	@Transactional
	public void addAreas() {
		log.trace("MisradRishuiDataBasePopulator : addAreas");
		IntStream.range(1, nAreas).forEach(parkingLotID -> {
			addArea(parkingLotID);
		});
	}

	private void addArea(int parkingLotID) {
		var areaEntity = new AreaEntity(parkingLotID, parkingLotID * 100);
		service.addArea(areaEntity);
		log.trace("addAreas : added area {}", areaEntity);
	}

	private void createListOfOwners() {
		log.trace("MisradRishuiDataBasePopulator : createListOfOwners");
		Faker faker = new Faker();
		int ownersAmount = Double.valueOf(maxCarNumber * 0.7).intValue();
		IntStream.range(0, ownersAmount).asLongStream().map(x -> generateRandomOwnerId()).distinct()
				.forEach(ownerId -> {
					var firstName = faker.name().firstName();
					var lastName = faker.name().lastName();
					var birthDate = faker.date().birthday(18, 85);
					var newOwner = new OwnerEntity(ownerId, firstName, lastName, birthDate);
					owners.add(newOwner);
				});
		log.trace("createListOfOwners : created list of {} owners", owners.size());
	}

	private long generateRandomOwnerId() {
		return ThreadLocalRandom.current().nextLong(000000000, 999999999);
	}
}
