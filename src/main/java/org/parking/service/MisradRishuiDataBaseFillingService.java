package org.parking.service;
import org.parking.entities.*;

public interface MisradRishuiDataBaseFillingService {
	void addCar(CarEntity car);
	void addOwner(OwnerEntity owner);
	void addArea(AreaEntity area);
	boolean carExists (CarEntity car);
	boolean ownerExists(OwnerEntity owner);
	boolean areaExists(AreaEntity area);
}
