package com.piotrdomagalski.planning.truck_driver;

import com.piotrdomagalski.planning.carrier.CarrierEntity;
import com.piotrdomagalski.planning.carrier.CarrierOperations;
import com.piotrdomagalski.planning.carrier.CarrierRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Qualifier("truckDriverRest")
class TruckDriverRestService {

    private final TruckDriverRepository truckDriverRepository;
    private final CarrierRepository carrierRepository;
    private final TruckDriverTransformer transformer;
    private final CarrierOperations carrierOperations;

    TruckDriverRestService(TruckDriverRepository truckDriverRepository, CarrierRepository carrierRepository, TruckDriverTransformer transformer, CarrierOperations carrierOperations) {
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

    TruckDriverNewUpdateDTO addNewDriver(Long carrierId, TruckDriverNewUpdateDTO driver) {
        TruckDriverEntity truckDriverEntity = transformer.newUpdatDriverDtoToEntity(driver);
        CarrierEntity carrierEntity = carrierRepository.findById(carrierId).orElseThrow(
                () -> new NoSuchElementException("No carrier with id: " + carrierId));
        carrierOperations.addDriver(carrierEntity, truckDriverEntity);
        TruckDriverEntity save = truckDriverRepository.save(truckDriverEntity);
        return transformer.entityToNewUpdateDriverDto(save);
    }

    TruckDriverEntity deleteTruckDriverById(Long id) {
        TruckDriverEntity truckDriverById = truckDriverRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("No driver found with id: " + id));
        new ClearTruckDriver(truckDriverById).execute();
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
