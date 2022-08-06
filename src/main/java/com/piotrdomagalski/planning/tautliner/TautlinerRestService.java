package com.piotrdomagalski.planning.tautliner;

import com.piotrdomagalski.planning.app.IllegalOperationException;
import com.piotrdomagalski.planning.carrier.CarrierEntity;
import com.piotrdomagalski.planning.carrier.CarrierOperations;
import com.piotrdomagalski.planning.carrier.CarrierRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class TautlinerRestService {

    private final TautlinerRepository tautlinerRepository;
    private final CarrierRepository carrierRepository;
    private final TautlinerTransformer transformer;
    private final CarrierOperations carrierOperations;

    public TautlinerRestService(TautlinerRepository tautlinerRepository, CarrierRepository carrierRepository,
                                TautlinerTransformer transformer, CarrierOperations carrierOperations) {
        this.tautlinerRepository = tautlinerRepository;
        this.carrierRepository = carrierRepository;
        this.transformer = transformer;
        this.carrierOperations = carrierOperations;
    }

    List<TautlinerInfoDTO> getAllTautliners(Boolean isXpo) {
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues();
        Example<TautlinerEntity> example = Example.of(
                new TautlinerEntity(isXpo, null, null, null,null), matcher);

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

    TautlinerInfoDTO addNewTautliner(Long carrierId, TautlinerNewUpdateDTO tautliner) {
        tautlinerRepository.findByTautlinerPlatesIgnoreCase(tautliner.getTautlinerPlates()).ifPresent(t -> {
            throw new IllegalOperationException(String.format("Tautliner with plates %s already exists!", t.getTautlinerPlates()));
        });

        if ((carrierId == null && !tautliner.getXpo()) || (carrierId!= null && carrierId <= 0 && !tautliner.getXpo())) {
            throw new IllegalOperationException("Non xpo tautliner must have any carrier!");
        }

        TautlinerEntity tautlinerEntity = transformer.newUpdateDTOtoEntity(tautliner);

        if (carrierId != null && carrierId > 0) {
            CarrierEntity carrier = carrierRepository.findById(carrierId).orElseThrow(
                    () -> new NoSuchElementException("No carrier found with id: " + carrierId));
            carrierOperations.addTautliner(carrier, tautlinerEntity);
        }

        TautlinerEntity savedEntity = tautlinerRepository.save(tautlinerEntity);
        return transformer.entityToInfoDTO(savedEntity);
    }

    TautlinerEntity deleteTautlinerByPlates(String plates) {
        TautlinerEntity tautlinerByPlates = tautlinerRepository.findByTautlinerPlatesIgnoreCase(plates)
                .orElseThrow(() -> new NoSuchElementException("No tautliner found with plates: " + plates));
        new ClearTautliner(tautlinerByPlates).execute();
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


