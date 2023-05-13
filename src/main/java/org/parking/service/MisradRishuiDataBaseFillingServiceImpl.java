package org.parking.service;

import org.parking.entities.*;
import org.parking.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@Transactional(readOnly = true)
public class MisradRishuiDataBaseFillingServiceImpl implements MisradRishuiDataBaseFillingService {
	@Autowired
	AreasRepository areasRepository;
	@Autowired
	CarsRepository carsRepository;
	@Autowired
	OwnersRepository ownersRepository;
	@Autowired
	ParkingLotsRepository parkingLotsRepository;

	@Transactional
	@Override
	public void addCar(CarEntity car) {
		if (!carExists(car)) {
			carsRepository.save(car);
		} else {
			log.trace("car {} exists in carsRepository", car);
		}
	}

	@Transactional
	@Override
	public void addOwner(OwnerEntity owner) {
		if (!ownerExists(owner)) {
			ownersRepository.save(owner);
		} else {
			log.debug("owner {} exists in ownersRepository", owner);
		}
	}

	@Transactional
	@Override
	public void addArea(AreaEntity area) {
		if (!areaExists(area)) {
			areasRepository.save(area);
		} else {
			log.debug("area {} exists in areasRepository", area);
		}
	}
	
	@Transactional
	@Override
	public void addParkingLot(ParkingLotEntity lot) {
		if (!LotExists(lot)) {
			parkingLotsRepository.save(lot);
		} else {
			log.debug("lot {} exists in parkingLotsRepository", lot);
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

	@Override
	public boolean LotExists(ParkingLotEntity lot) {
		return parkingLotsRepository.existsById(lot.parkingLotId);
	}
}
