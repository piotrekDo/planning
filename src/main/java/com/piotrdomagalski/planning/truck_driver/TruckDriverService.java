package com.piotrdomagalski.planning.truck_driver;

import com.piotrdomagalski.planning.carrier.CarrierEntity;
import com.piotrdomagalski.planning.carrier.CarrierActions;
import com.piotrdomagalski.planning.carrier.CarrierRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Qualifier("truckDriverRest")
class TruckDriverService {

    private final TruckDriverRepository truckDriverRepository;
    private final CarrierRepository carrierRepository;
    private final TruckDriverTransformer transformer;
    private final CarrierActions carrierOperations;

    TruckDriverService(TruckDriverRepository truckDriverRepository, CarrierRepository carrierRepository, TruckDriverTransformer transformer, CarrierActions carrierOperations) {
        this.truckDriverRepository = truckDriverRepository;
        this.carrierRepository = carrierRepository;
        this.transformer = transformer;
        this.carrierOperations = carrierOperations;
    }

    TruckDriverInfoDTO getTruckDriverById(Long id) {
        return transformer.entityToDriverInfoDto(truckDriverRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("No driver found with id: " + id)));
    }

    List<TruckDriverEntity> getAllTruckDrivers() {
        return truckDriverRepository.findAll(Sort.by(Sort.Direction.ASC, "fullName"));
    }

    TruckDriverNewUpdateDTO addNewDriver(String carrierSap, TruckDriverNewUpdateDTO driver) {
        TruckDriverEntity truckDriverEntity = transformer.newUpdatDriverDtoToEntity(driver);
        CarrierEntity carrierEntity = carrierRepository.findBySap(carrierSap).orElseThrow(
                () -> new NoSuchElementException("No carrier with sap: " + carrierSap));
        carrierOperations.addDriver(carrierEntity, truckDriverEntity);
        TruckDriverEntity save = truckDriverRepository.save(truckDriverEntity);
        return transformer.entityToNewUpdateDriverDto(save);
    }

    TruckDriverEntity deleteTruckDriverById(Long id) {
        TruckDriverEntity truckDriverById = truckDriverRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("No driver found with id: " + id));
        new ClearTruckDriverAction(truckDriverById).execute();
        truckDriverRepository.delete(truckDriverById);
        return truckDriverById;
    }

    TruckDriverNewUpdateDTO updateTruckDriver(Long id, TruckDriverNewUpdateDTO driverDto) {
        TruckDriverEntity truckDriverById = truckDriverRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("No driver found with id: " + id));
        TruckDriverEntity driver = transformer.newUpdatDriverDtoToEntity(driverDto);

        if (driver.getFullName() != null && !driver.getFullName().equals(truckDriverById.getFullName())) {
            truckDriverById.setFullName(driver.getFullName());
        }
        if (driver.getTel() != null && !driver.getTel().equals(truckDriverById.getTel())) {
            truckDriverById.setTel(driver.getTel());
        }
        if (driver.getIdDocument() != null && !driver.getIdDocument().equals(truckDriverById.getIdDocument())) {
            truckDriverById.setIdDocument(driver.getIdDocument());
        }

        TruckDriverEntity saved = truckDriverRepository.save(truckDriverById);
        return transformer.entityToNewUpdateDriverDto(saved);
    }

}
