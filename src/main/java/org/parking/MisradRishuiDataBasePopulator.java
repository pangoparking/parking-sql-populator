package org.parking;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import org.parking.entities.AreaEntity;
import org.parking.entities.CarEntity;
import org.parking.entities.OwnerEntity;
import org.parking.entities.ParkingLotEntity;
import org.parking.service.MisradRishuiDataBaseFillingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class MisradRishuiDataBasePopulator {

	Map<Long, OwnerEntity> ownersMap = new HashMap<>();
	Map<Long, AreaEntity> AreasMap = new HashMap<>();

	@Value("${app.org.parking.minCarNumber:10000000}")
	Integer minCarNumber;
	@Value("${app.org.parking.maxCarNumber:10000100}")
	Integer maxCarNumber;
	@Value("${app.org.parking.nAreas:3}")
	Integer nAreas;

	@Value("${app.org.parking.nLots:100}")
	Integer nLots;

	@Value("${app.parkingPlaceID.min:1}")
	private int minParkingLotID;

	@Value("${app.parking.amount:101}")
	private int maxParkingLotID;

	@Value("${app.parking.fineCoefficient:100}")
	private long fineCoefficient;
	
	@Value("${app.parking.nLotsInArea:100}")
	private long nLotsInArea;;

	@Value("${spring.jpa.hibernate.ddl-auto}")
	String ddlAuto;

	@Autowired
	MisradRishuiDataBaseFillingService service;
	
	public void fillDB() {
		addOwners();
		addCars();
		addAreas();
		addLots();
	}

	private void addLots() {
		log.trace("MisradRishuiDataBasePopulator : addLots");
		int commonAmountOfParkinLots = nAreas*nLots-1;
		IntStream.rangeClosed(0, commonAmountOfParkinLots).forEach(lotID-> {
			long areaID = lotID/nLotsInArea + 1;
			var parkingLot = new ParkingLotEntity(lotID, AreasMap.get(areaID));
			service.addParkingLot(parkingLot);
		});
	}

	public void addOwners() {
		log.trace("MisradRishuiDataBasePopulator : addOwners");
		fillOwnersMapWithRandomOwners();
		ownersMap.entrySet().stream().map(entry -> entry.getValue()).forEach(owner -> {
			service.addOwner(owner);
		});
	}

	private void fillOwnersMapWithRandomOwners() {
		log.trace("MisradRishuiDataBasePopulator : createListOfOwners");
		Faker faker = new Faker();
		int ownersAmount = Double.valueOf((maxCarNumber - minCarNumber) * 0.7).intValue();
		IntStream.range(0, ownersAmount).asLongStream().forEach(ownerNum -> {
			var newOwner = generateRandomOwner(faker, ownerNum);
			ownersMap.put(ownerNum, newOwner);
		});
		log.trace("createListOfOwners : created list of {} owners", ownersMap.size());
	}

	private OwnerEntity generateRandomOwner(Faker faker, long ownerId) {
		var firstName = faker.name().firstName();
		var lastName = faker.name().lastName();
		LocalDate birthDate = faker.date().birthday(18, 85).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		var email = String.format("%s%d@gmail.com", firstName, birthDate.getYear());
		var newOwner = new OwnerEntity(generateRandomOwnerId(), firstName, lastName, email, birthDate);
		return newOwner;
	}

	private long generateRandomOwnerId() {
		return ThreadLocalRandom.current().nextLong(100000000, 999999999);
	}

	public void addCars() {
		int[] carCounter = { 0 };
		log.trace("MisradRishuiDataBasePopulator : addCars");
		IntStream.range(0, maxCarNumber - minCarNumber + 1).asLongStream().forEach(carNum -> {
			CarEntity car = generateCarEntityWithExistingOwner(carNum);
			service.addCar(car);
			carCounter[0]++;
		});
		log.trace("addCars : added {} cars", carCounter[0]);
	}

	private CarEntity generateCarEntityWithExistingOwner(long carNum) {
		OwnerEntity owner = getOwner(carNum);
		var car = new CarEntity(carNum + minCarNumber, owner);
		return car;
	}

	private OwnerEntity getOwner(long carNum) {
		if (carNum < ownersMap.size()) {
			return ownersMap.get(carNum);
		} else {
			return getRandomOwner();
		}
	}

	private OwnerEntity getRandomOwner() {
		long randomNum = ThreadLocalRandom.current().nextLong(1, ownersMap.size());
		return ownersMap.get(randomNum);
	}

	public void addAreas() {
		log.trace("MisradRishuiDataBasePopulator : addAreas");
		generateMapOfAreas();
		AreasMap.entrySet().stream().map(entry -> entry.getValue()).forEach(area -> {
			service.addArea(area);
			log.trace("addAreas : added area {}", area);
		});
	}

	private void generateMapOfAreas() {
		IntStream.rangeClosed(1, nAreas).asLongStream().forEach(areaID -> {
			AreasMap.put(areaID, new AreaEntity(areaID, areaID * fineCoefficient));
		});
	}
}
