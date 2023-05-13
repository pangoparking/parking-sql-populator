package org.parking.service;
import org.parking.entities.*;

public interface MisradRishuiDataBaseFillingService {
	void addCar(CarEntity car);
	void addOwner(OwnerEntity owner);
	void addArea(AreaEntity area);
	void addParkingLot(ParkingLotEntity lot);
	boolean LotExists(ParkingLotEntity lot);
	boolean carExists (CarEntity car);
	boolean ownerExists(OwnerEntity owner);
	boolean areaExists(AreaEntity area);
}
