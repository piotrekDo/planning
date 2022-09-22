package com.piotrdomagalski.planning.truck;

import com.piotrdomagalski.planning.carrier.CarrierActions;
import com.piotrdomagalski.planning.carrier.CarrierEntity;
import com.piotrdomagalski.planning.carrier.CarrierRepository;
import com.piotrdomagalski.planning.error.IllegalOperationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

import static com.piotrdomagalski.planning.app.ConfigurationLibrary.TRUCK_RESULT_PER_PAGE;

@Service
class TruckService {

    private final TruckRepository truckRepository;
    private final CarrierRepository carrierRepository;
    private final TruckTransformer transformer;
    private final CarrierActions carrierOperations;

    TruckService(TruckRepository truckRepository, CarrierRepository carrierRepository, TruckTransformer transformer, CarrierActions carrierOperations) {
        this.truckRepository = truckRepository;
        this.carrierRepository = carrierRepository;
        this.transformer = transformer;
        this.carrierOperations = carrierOperations;
    }

    Page<TruckInfoDTO> getAllTrucks(Integer page, Integer size) {
        page = page == null || page < 0 ? 0 : page;
        size = size == null || size < 1 ? TRUCK_RESULT_PER_PAGE : size;
        Page<TruckEntity> results = truckRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "truckPlates")));

        return results == null ? Page.empty() : results.map(transformer::toinfoDto);
    }

    TruckInfoDTO getTruckById(Long id) {
        return transformer.toinfoDto(truckRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("No truck found with id: " + id)));
    }

    TruckInfoDTO getTruckByPlates(String plates) {
        return transformer.toinfoDto(truckRepository.findByTruckPlatesIgnoreCase(plates).orElseThrow(() ->
                new NoSuchElementException("No truck found with plates: " + plates)));
    }

    TruckInfoDTO addNewTruck(String carrierSap, TruckNewUpdateDTO truck) {
        truckRepository.findByTruckPlatesIgnoreCase(truck.getTruckPlates()).ifPresent(t -> {
            throw new IllegalOperationException(String.format("Truck with plates: %s already exists!", truck.getTruckPlates()));
        });
        TruckEntity truckEntity = transformer.newUpdateToEntity(truck);
        CarrierEntity carrierEntity = carrierRepository.findBySap(carrierSap).orElseThrow(
                () -> new NoSuchElementException("No carrier with sap: " + carrierSap));
        carrierOperations.addTruck(carrierEntity, truckEntity);
        return transformer.toinfoDto(truckRepository.save(truckEntity));
    }

    TruckEntity deleteTruckByPlates(String plates) {
        TruckEntity truckByPlates = truckRepository.findByTruckPlatesIgnoreCase(plates).orElseThrow(() ->
                new NoSuchElementException("No truck found with plates: " + plates));
        new ClearTruckAction(truckByPlates).execute();
        truckRepository.delete(truckByPlates);
        return truckByPlates;
    }

    TruckNewUpdateDTO updateTruck(String plates, TruckNewUpdateDTO dto) {
        TruckEntity truckByPlates = truckRepository.findByTruckPlatesIgnoreCase(plates).orElseThrow(() ->
                new NoSuchElementException("No truck found with plates: " + plates));
        TruckEntity truck = transformer.newUpdateToEntity(dto);

        if (truck.getTruckPlates() != null && !truck.getTruckPlates().equals(truckByPlates.getTruckPlates())) {
            truckRepository.findByTruckPlatesIgnoreCase(truck.getTruckPlates()).ifPresent(t -> {
                throw new IllegalOperationException("Truck with provided plates already exists!, plates has to be unique");
            });
            truckByPlates.setTruckPlates(truck.getTruckPlates());
        }

        if (truck.getMega() != null && !truck.getMega().equals(truckByPlates.getMega())) {
            truckByPlates.setMega(truck.getMega());
        }

        TruckEntity savedTruck = truckRepository.save(truckByPlates);
        return transformer.entityToNewUpdateDto(savedTruck);
    }

}
