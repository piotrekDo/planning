package com.piotrdomagalski.planning.truck_driver;

import com.piotrdomagalski.planning.carrier.CarrierEntity;
import com.piotrdomagalski.planning.carrier.CarrierModelService;
import com.piotrdomagalski.planning.carrier.CarrierRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TruckDriverRestService {

    private final TruckDriverRepository truckDriverRepository;
    private final CarrierRepository carrierRepository;
    private final TruckDriverTransformer transformer;

    public TruckDriverRestService(TruckDriverRepository truckDriverRepository, CarrierRepository carrierRepository, TruckDriverTransformer transformer) {
        this.truckDriverRepository = truckDriverRepository;
        this.carrierRepository = carrierRepository;
        this.transformer = transformer;
    }

    TruckDriverEntity getTruckDriverById(Long id) {
        return truckDriverRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("No driver found with id: " + id));
    }

    List<TruckDriverEntity> getAllTruckDrivers() {
        return truckDriverRepository.findAll(Sort.by(Sort.Direction.ASC, "fullName"));
    }

    TruckDriverNewUpdateDTO addNewDriver(Long carrierId, TruckDriverNewUpdateDTO driver) {
        TruckDriverEntity truckDriverEntity = transformer.newUpdatDriverDtoToEntity(driver);
        CarrierEntity carrierEntity = carrierRepository.findById(carrierId).orElseThrow(
                () -> new NoSuchElementException("No carrier with id: " + carrierId));
        new CarrierModelService(carrierEntity).addDriver(truckDriverEntity);
        TruckDriverEntity save = truckDriverRepository.save(truckDriverEntity);
        return transformer.entityToNewUpdateDriverDto(save);
    }

    TruckDriverEntity deleteTruckDriverById(Long id) {
        TruckDriverEntity truckDriverById = getTruckDriverById(id);
        new TruckDriverModelService(truckDriverById).clearDriver();
        truckDriverRepository.delete(truckDriverById);
        return truckDriverById;
    }

    TruckDriverNewUpdateDTO updateTruckDriver(Long id, TruckDriverNewUpdateDTO driverDto) {
        TruckDriverEntity truckDriverById = getTruckDriverById(id);
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
