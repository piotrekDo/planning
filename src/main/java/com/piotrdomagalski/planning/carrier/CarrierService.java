package com.piotrdomagalski.planning.carrier;

import com.piotrdomagalski.planning.error.IllegalOperationException;
import com.piotrdomagalski.planning.tautliner.TautlinerRepository;
import com.piotrdomagalski.planning.truck.TruckRepository;
import com.piotrdomagalski.planning.truck_driver.TruckDriverRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
class CarrierService {

    private final CarrierRepository carrierRepository;
    private final CarrierTransformer transformer;
    private final CarrierOperations carrierOperations;
    private final TruckRepository truckRepository;
    private final TruckDriverRepository driverRepository;
    private final TautlinerRepository tautlinerRepository;

    public CarrierService(CarrierRepository carrierRepository, CarrierTransformer transformer,
                          CarrierOperations carrierOperations, TruckRepository truckRepository,
                          TruckDriverRepository driverRepository, TautlinerRepository tautlinerRepository) {
        this.carrierRepository = carrierRepository;
        this.transformer = transformer;
        this.carrierOperations = carrierOperations;
        this.truckRepository = truckRepository;
        this.driverRepository = driverRepository;
        this.tautlinerRepository = tautlinerRepository;
    }

    List<CarrierFullIDto> getAllCarriers() {
        return carrierRepository.findAll(Sort.by(Sort.Direction.ASC, "name")).stream()
                .map(transformer::toCarrierFullIDto)
                .collect(Collectors.toList());
    }

    List<CarrierShortInfoDTO> getCarriersShortInfo() {
        return carrierRepository.findAll(Sort.by(Sort.Direction.ASC, "name")).stream()
                .map(transformer::entityToShortInfoDto)
                .collect(Collectors.toList());
    }

    CarrierFullIDto getCarrierById(Long id) {
        return transformer.toCarrierFullIDto(carrierRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("No carrier found with id: " + id)));
    }

    CarrierFullIDto getCarrierBySap(String sap) {
        return transformer.toCarrierFullIDto(carrierRepository.findBySap(sap).orElseThrow(
                () -> new NoSuchElementException("No carrier found with id: " + sap)));
    }

    CarrierNewUpdateDTO addNewCarrier(CarrierNewUpdateDTO carrier) {
        carrierRepository.findBySap(carrier.getSap()).ifPresent(c -> {
            throw new IllegalOperationException(String.format("Carrier with SAP number: %s already exists!", carrier.getSap()));
        });
        CarrierEntity carrierEntity = transformer.newUpdateToEntity(carrier);
        CarrierEntity savedEntity = carrierRepository.save(carrierEntity);
        return transformer.entityToNewUpdateDto(savedEntity);
    }

    CarrierShortInfoDTO deleteCarrierBySap(String sap) {
        CarrierEntity carrierBySap = carrierRepository.findBySap(sap).orElseThrow(
                () -> new NoSuchElementException("No carrier found with id: " + sap));
        boolean cleanCarrier = carrierOperations.clear(carrierBySap);
        if (cleanCarrier) {
            driverRepository.deleteAll(carrierBySap.getDrivers());
            truckRepository.deleteAll(carrierBySap.getTrucks());
            tautlinerRepository.deleteAll(carrierBySap.getTautliners().stream()
                    .filter(taut -> !taut.getXpo())
                    .collect(Collectors.toList()));
        } else {
            throw new RuntimeException("Could not clean carrier!!");
        }
        carrierRepository.delete(carrierBySap);
        return transformer.entityToShortInfoDto(carrierBySap);
    }

    CarrierNewUpdateDTO updateCarrier(String sap, CarrierNewUpdateDTO dto) {
        CarrierEntity carrierBySap = carrierRepository.findBySap(sap).orElseThrow(
                () -> new NoSuchElementException("No carrier found with id: " + sap));
        CarrierEntity carrier = transformer.newUpdateToEntity(dto);

        if (carrier.getSap() != null && !carrier.getSap().equals(carrierBySap.getSap())) {
            carrierRepository.findBySap(carrier.getSap()).ifPresent(c -> {
                throw new IllegalOperationException("Carrier with provided SAP already exists, SAP has to be unique");
            });
            carrierBySap.setSap(carrier.getSap());
        }

        if (carrier.getName() != null && !carrier.getName().equals(carrierBySap.getName())) {
            carrierBySap.setName(carrier.getName());
        }

        if (carrier.getOrigin() != null && !carrier.getOrigin().equals(carrierBySap.getOrigin())) {
            carrierBySap.setOrigin(carrier.getOrigin());
        }

        if (carrier.getRate() != null && !carrier.getRate().equals(carrierBySap.getRate())) {
            carrierBySap.setRate(carrier.getRate());
        }

        CarrierEntity savedCarrier = carrierRepository.save(carrierBySap);
        return transformer.entityToNewUpdateDto(savedCarrier);
    }
}