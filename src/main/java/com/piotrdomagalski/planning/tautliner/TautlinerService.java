package com.piotrdomagalski.planning.tautliner;

import com.piotrdomagalski.planning.error.IllegalOperationException;
import com.piotrdomagalski.planning.carrier.CarrierEntity;
import com.piotrdomagalski.planning.carrier.CarrierActions;
import com.piotrdomagalski.planning.carrier.CarrierRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
class TautlinerService {

    private final TautlinerRepository tautlinerRepository;
    private final CarrierRepository carrierRepository;
    private final TautlinerTransformer transformer;
    private final CarrierActions carrierOperations;

    TautlinerService(TautlinerRepository tautlinerRepository, CarrierRepository carrierRepository,
                     TautlinerTransformer transformer, CarrierActions carrierOperations) {
        this.tautlinerRepository = tautlinerRepository;
        this.carrierRepository = carrierRepository;
        this.transformer = transformer;
        this.carrierOperations = carrierOperations;
    }

    List<TautlinerInfoDTO> getAllTautliners(Boolean isXpo) {
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues();
        Example<TautlinerEntity> example = Example.of(
                new TautlinerEntity(isXpo, null, null, null, null), matcher);

        return tautlinerRepository.findAll(example, Sort.by(Sort.Direction.ASC, "tautlinerPlates")).stream()
                .map(transformer::entityToInfoDTO)
                .collect(Collectors.toList());
    }

    TautlinerInfoDTO getTautlinerByPlates(String plates) {
        return transformer.entityToInfoDTO(tautlinerRepository.findByTautlinerPlatesIgnoreCase(plates).orElseThrow(() ->
                new NoSuchElementException("No tautliner found with plates: " + plates)));
    }

    TautlinerEntity getTautlinerById(Long id) {
        return tautlinerRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("No tautliner found with id:" + id));
    }

    TautlinerInfoDTO addNewTautliner(String carrierSap, TautlinerNewUpdateDTO tautliner) {
        tautlinerRepository.findByTautlinerPlatesIgnoreCase(tautliner.getTautlinerPlates()).ifPresent(t -> {
            throw new IllegalOperationException(String.format("Tautliner with plates %s already exists!", t.getTautlinerPlates()));
        });

        TautlinerEntity tautlinerEntity;
        try {
            if ((carrierSap == null && !tautliner.getXpo()) || (carrierSap != null && Long.parseLong(carrierSap) <= 0 && !tautliner.getXpo())) {
                throw new IllegalOperationException("Non xpo tautliner must have any carrier!");
            }

            tautlinerEntity = transformer.newUpdateDTOtoEntity(tautliner);

            if (carrierSap != null && Long.parseLong(carrierSap) > 0) {
                CarrierEntity carrier = carrierRepository.findBySap(carrierSap).orElseThrow(
                        () -> new NoSuchElementException("No carrier found with sap: " + carrierSap));
                carrierOperations.addTautliner(carrier, tautlinerEntity);
            }
        } catch (NumberFormatException e) {
            throw new IllegalOperationException(carrierSap + " is not a valid SAP number");
        }

        TautlinerEntity savedEntity = tautlinerRepository.save(tautlinerEntity);
        return transformer.entityToInfoDTO(savedEntity);
    }

    TautlinerEntity deleteTautlinerByPlates(String plates) {
        TautlinerEntity tautlinerByPlates = tautlinerRepository.findByTautlinerPlatesIgnoreCase(plates)
                .orElseThrow(() -> new NoSuchElementException("No tautliner found with plates: " + plates));
        new ClearTautlinerAction(tautlinerByPlates).execute();
        tautlinerRepository.delete(tautlinerByPlates);
        return tautlinerByPlates;
    }

    TautlinerNewUpdateDTO updateTautlinerByPlates(String plates, TautlinerNewUpdateDTO tautlinerDto) {
        TautlinerEntity tautlinerByPlates = tautlinerRepository.findByTautlinerPlatesIgnoreCase(plates)
                .orElseThrow(() -> new NoSuchElementException("No tautliner found with plates: " + plates));
        TautlinerEntity tautliner = transformer.newUpdateDTOtoEntity(tautlinerDto);
        if (tautliner.getXpo() != null && !tautliner.getXpo().equals(tautlinerByPlates.getXpo())) {
            tautlinerByPlates.setXpo(tautliner.getXpo());
        }

        if (tautliner.getTautlinerPlates() != null && !tautliner.getTautlinerPlates().equals(tautlinerByPlates.getTautlinerPlates())) {
            tautlinerRepository.findByTautlinerPlatesIgnoreCase(tautliner.getTautlinerPlates()).ifPresent(t -> {
                throw new IllegalOperationException(String.format("Tautliner with plates: %s already exists", tautliner.getTautlinerPlates()));
            });
            tautlinerByPlates.setTautlinerPlates(tautliner.getTautlinerPlates());
        }

        if (tautliner.getTechInspection() != null && !tautliner.getTechInspection().equals(tautlinerByPlates.getTechInspection())) {
            tautlinerByPlates.setTechInspection(tautliner.getTechInspection());
        }

        TautlinerEntity saved = tautlinerRepository.save(tautlinerByPlates);
        return transformer.entityToNewUpdateDTO(saved);
    }
}


