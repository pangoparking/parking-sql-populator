package org.parking.service;

import org.parking.entities.*;
import org.parking.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class MisradRishuiDataBaseFillingServiceImpl implements MisradRishuiDataBaseFillingService {
	@Autowired
	AreasRepository areasRepository;
	@Autowired
	CarsRepository carsRepository;
	@Autowired
	OwnersRepository ownersRepository;

	@Override
	public void addCar(CarEntity car) {
		if (!carExists(car)) {
			carsRepository.save(car);
		} else {
			log.trace("car {} exists in carsRepository", car);
		}
	}

	@Override
	public void addOwner(OwnerEntity owner) {
		if (!ownerExists(owner)) {
			ownersRepository.save(owner);
		} else {
			log.trace("owner {} exists in ownersRepository", owner);
		}
	}

	@Override
	public void addArea(AreaEntity area) {
		if (!areaExists(area)) {
			areasRepository.save(area);
			log.trace("area {} does not exists in areasRepository", area);
		} else {
			log.trace("area {} exists in areasRepository", area);
		}
	}

	@Override
	public boolean carExists(CarEntity car) {
		return carsRepository.existsById(car.carID);
	}

	@Override
	public boolean ownerExists(OwnerEntity owner) {
		return ownersRepository.existsById(owner.ownerID);
	}

	@Override
	public boolean areaExists(AreaEntity area) {
		return areasRepository.existsById(area.areaID);
	}

}
