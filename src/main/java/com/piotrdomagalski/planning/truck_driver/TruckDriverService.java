package com.piotrdomagalski.planning.truck_driver;

import com.piotrdomagalski.planning.carrier.CarrierActions;
import com.piotrdomagalski.planning.carrier.CarrierEntity;
import com.piotrdomagalski.planning.carrier.CarrierRepository;
import com.piotrdomagalski.planning.logs.LogsService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

import static com.piotrdomagalski.planning.app.ConfigurationLibrary.TRUCKDRIVER_RESULTS_PER_PAGE;

/**
 * Service class for driver entity.
 */

@Service
@Qualifier("truckDriverRest")
class TruckDriverService {

    private final TruckDriverRepository truckDriverRepository;
    private final CarrierRepository carrierRepository;
    private final TruckDriverTransformer transformer;
    private final CarrierActions carrierOperations;
    private final LogsService logsService;

    public TruckDriverService(TruckDriverRepository truckDriverRepository, CarrierRepository carrierRepository, TruckDriverTransformer transformer, CarrierActions carrierOperations, LogsService logsService) {
        this.truckDriverRepository = truckDriverRepository;
        this.carrierRepository = carrierRepository;
        this.transformer = transformer;
        this.carrierOperations = carrierOperations;
        this.logsService = logsService;
    }

    TruckDriverInfoDTO getTruckDriverById(Long id) {
        return transformer.entityToDriverInfoDto(truckDriverRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("No driver found with id: " + id)));
    }

    Page<TruckDriverInfoDTO> getAllTruckDrivers(Integer page, Integer size) {
        page = page == null || page < 0 ? 0 : page;
        size = size == null || size < 1 ? TRUCKDRIVER_RESULTS_PER_PAGE : size;
        Page<TruckDriverEntity> results = truckDriverRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "fullName")));
        return results == null ? Page.empty() : results.map(transformer::entityToDriverInfoDto);
    }

    TruckDriverNewUpdateDTO addNewDriver(String carrierSap, TruckDriverNewUpdateDTO driver) {
        TruckDriverEntity truckDriverEntity = transformer.newUpdatDriverDtoToEntity(driver);
        CarrierEntity carrierEntity = carrierRepository.findBySap(carrierSap).orElseThrow(
                () -> new NoSuchElementException("No carrier with sap: " + carrierSap));
        carrierOperations.addDriver(carrierEntity, truckDriverEntity);
        TruckDriverEntity save = truckDriverRepository.save(truckDriverEntity);
        logsService.createNewEntityLog(save.getFullName() + " " + save.getIdDocument());
        return transformer.entityToNewUpdateDriverDto(save);
    }

    TruckDriverEntity deleteTruckDriverById(Long id) {
        TruckDriverEntity truckDriverById = truckDriverRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("No driver found with id: " + id));
        new ClearTruckDriverAction(truckDriverById).execute();
        truckDriverRepository.delete(truckDriverById);
        logsService.createDeleteEntityLog(truckDriverById.getFullName() + " " + truckDriverById.getIdDocument());
        return truckDriverById;
    }

    TruckDriverNewUpdateDTO updateTruckDriver(Long id, TruckDriverNewUpdateDTO driverDto) {
        TruckDriverEntity truckDriverById = truckDriverRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("No driver found with id: " + id));
        TruckDriverEntity driver = transformer.newUpdatDriverDtoToEntity(driverDto);
        String originalName = truckDriverById.getFullName(), originalId = truckDriverById.getIdDocument();
        String nameChanged = null;
        if (driver.getFullName() != null && !driver.getFullName().equals(truckDriverById.getFullName())) {
            nameChanged = truckDriverById.getFullName();
            truckDriverById.setFullName(driver.getFullName());
        }
        if (driver.getTel() != null && !driver.getTel().equals(truckDriverById.getTel())) {
            truckDriverById.setTel(driver.getTel());
        }
        String idDocumentChanged = null;
        if (driver.getIdDocument() != null && !driver.getIdDocument().equals(truckDriverById.getIdDocument())) {
            idDocumentChanged = truckDriverById.getIdDocument();
            truckDriverById.setIdDocument(driver.getIdDocument());
        }

        String previousNameAndId = transformer.transformToPreviousNameAndId(nameChanged, idDocumentChanged, originalName, originalId);
        TruckDriverEntity saved = truckDriverRepository.save(truckDriverById);
        logsService.createEditEntityLog(truckDriverById.getFullName() + " " + truckDriverById.getIdDocument(), previousNameAndId);
        return transformer.entityToNewUpdateDriverDto(saved);
    }

}
