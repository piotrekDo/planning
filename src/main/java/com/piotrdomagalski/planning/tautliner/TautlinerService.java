package com.piotrdomagalski.planning.tautliner;

import com.piotrdomagalski.planning.carrier.CarrierActions;
import com.piotrdomagalski.planning.carrier.CarrierEntity;
import com.piotrdomagalski.planning.carrier.CarrierRepository;
import com.piotrdomagalski.planning.error.IllegalOperationException;
import com.piotrdomagalski.planning.logs.LogsService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

import static com.piotrdomagalski.planning.app.ConfigurationLibrary.TAUTLINER_RESULT_PER_PAGE;

/**
 * Service class for tautliner entity.
 */

@Service
class TautlinerService {

    private final TautlinerRepository tautlinerRepository;
    private final CarrierRepository carrierRepository;
    private final TautlinerTransformer transformer;
    private final CarrierActions carrierOperations;
    private final LogsService logsService;

    public TautlinerService(TautlinerRepository tautlinerRepository, CarrierRepository carrierRepository, TautlinerTransformer transformer, CarrierActions carrierOperations, LogsService logsService) {
        this.tautlinerRepository = tautlinerRepository;
        this.carrierRepository = carrierRepository;
        this.transformer = transformer;
        this.carrierOperations = carrierOperations;
        this.logsService = logsService;
    }

    Page<TautlinerInfoDTO> getAllTautliners(Boolean isXpo, Integer page, Integer size) {
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues();
        Example<TautlinerEntity> example = Example.of(
                new TautlinerEntity(isXpo, null, null, null, null), matcher);

        if (page != null && size != null && page == -1 && size == -1) {
            Pageable wholePage = Pageable.unpaged();
            return tautlinerRepository.findAll(example, wholePage).map(transformer::entityToInfoDTO);
        }

        page = page == null || page < 0 ? 0 : page;
        size = size == null || size < 1 ? TAUTLINER_RESULT_PER_PAGE : size;

        Page<TautlinerEntity> results = tautlinerRepository.findAll(example, PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "tautlinerPlates")));

        return results == null ? Page.empty() : results.map(transformer::entityToInfoDTO);

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
        logsService.createNewEntityLog(tautlinerEntity.getTautlinerPlates());
        return transformer.entityToInfoDTO(savedEntity);
    }

    TautlinerEntity deleteTautlinerByPlates(String plates) {
        TautlinerEntity tautlinerByPlates = tautlinerRepository.findByTautlinerPlatesIgnoreCase(plates)
                .orElseThrow(() -> new NoSuchElementException("No tautliner found with plates: " + plates));
        new ClearTautlinerAction(tautlinerByPlates).execute();
        tautlinerRepository.delete(tautlinerByPlates);
        logsService.createDeleteEntityLog(tautlinerByPlates.getTautlinerPlates());
        return tautlinerByPlates;
    }

    /**
     * platesChanged variable represents state of corresponding logs.
     * If not updated in logs table, then will not correspond anymore.
     * When updating tautliner's plates, logs must be updated as well.
     */

    TautlinerNewUpdateDTO updateTautlinerByPlates(String plates, TautlinerNewUpdateDTO tautlinerDto) {
        TautlinerEntity tautlinerByPlates = tautlinerRepository.findByTautlinerPlatesIgnoreCase(plates)
                .orElseThrow(() -> new NoSuchElementException("No tautliner found with plates: " + plates));
        TautlinerEntity tautliner = transformer.newUpdateDTOtoEntity(tautlinerDto);
        if (tautliner.getXpo() != null && !tautliner.getXpo().equals(tautlinerByPlates.getXpo())) {
            tautlinerByPlates.setXpo(tautliner.getXpo());
        }
        String platesChanged = null;
        if (tautliner.getTautlinerPlates() != null && !tautliner.getTautlinerPlates().equals(tautlinerByPlates.getTautlinerPlates())) {
            tautlinerRepository.findByTautlinerPlatesIgnoreCase(tautliner.getTautlinerPlates()).ifPresent(t -> {
                throw new IllegalOperationException(String.format("Tautliner with plates: %s already exists", tautliner.getTautlinerPlates()));
            });
            platesChanged = tautlinerByPlates.getTautlinerPlates();
            tautlinerByPlates.setTautlinerPlates(tautliner.getTautlinerPlates());
        }

        if (tautliner.getTechInspection() != null && !tautliner.getTechInspection().equals(tautlinerByPlates.getTechInspection())) {
            tautlinerByPlates.setTechInspection(tautliner.getTechInspection());
        }

        TautlinerEntity saved = tautlinerRepository.save(tautlinerByPlates);
        logsService.createEditEntityLog(saved.getTautlinerPlates(), platesChanged);
        return transformer.entityToNewUpdateDTO(saved);
    }
}


