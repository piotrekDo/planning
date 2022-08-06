package com.piotrdomagalski.planning.truck;

import com.piotrdomagalski.planning.app.IllegalOperationException;
import com.piotrdomagalski.planning.carrier.CarrierEntity;
import com.piotrdomagalski.planning.carrier.CarrierOperations;
import com.piotrdomagalski.planning.carrier.CarrierRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class TruckRestService {

    private final TruckRepository truckRepository;
    private final CarrierRepository carrierRepository;
    private final TruckTransformer transformer;
    private final CarrierOperations carrierOperations;

    public TruckRestService(TruckRepository truckRepository, CarrierRepository carrierRepository, TruckTransformer transformer, CarrierOperations carrierOperations) {
        this.truckRepository = truckRepository;
        this.carrierRepository = carrierRepository;
        this.transformer = transformer;
        this.carrierOperations = carrierOperations;
    }

    List<TruckInfoDTO> getAllTrucks() {
        return truckRepository.findAll(Sort.by(Sort.Direction.ASC, "truckPlates")).stream()
                .map(transformer::toinfoDto)
                .collect(Collectors.toList());
    }

    TruckInfoDTO getTruckById(Long id) {
        return transformer.toinfoDto(truckRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("No truck found with id: " + id)));
    }

    TruckInfoDTO getTruckByPlates(String plates) {
        return transformer.toinfoDto(truckRepository.findByTruckPlatesIgnoreCase(plates).orElseThrow(() ->
                new NoSuchElementException("No truck found with plates: " + plates)));
    }

    TruckInfoDTO addNewTruck(Long carrierId, TruckNewUpdateDTO truck) {
        truckRepository.findByTruckPlatesIgnoreCase(truck.getTruckPlates()).ifPresent(t -> {
            throw new IllegalOperationException(String.format("Truck with plates: %s already exists!", truck.getTruckPlates()));
        });
        TruckEntity truckEntity = transformer.newUpdateToEntity(truck);
        CarrierEntity carrierEntity = carrierRepository.findById(carrierId).orElseThrow(
                () -> new NoSuchElementException("No carrier with id: " + carrierId));
        carrierOperations.addTruck(carrierEntity, truckEntity);
        return transformer.toinfoDto(truckRepository.save(truckEntity));
    }

    TruckEntity deleteTruckByPlates(String plates) {
        TruckEntity truckByPlates = truckRepository.findByTruckPlatesIgnoreCase(plates).orElseThrow(() ->
                new NoSuchElementException("No truck found with plates: " + plates));
        new ClearTruck(truckByPlates).execute();
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
