package com.piotrdomagalski.planning.tautliner;

import com.piotrdomagalski.planning.app.IllegalOperationException;
import com.piotrdomagalski.planning.carrier.CarrierEntity;
import com.piotrdomagalski.planning.carrier.CarrierModelService;
import com.piotrdomagalski.planning.carrier.CarrierRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TautlinerRestService {

    private final TautlinerRepository tautlinerRepository;
    private final CarrierRepository carrierRepository;
    private final TautlinerTransformer transformer;

    public TautlinerRestService(TautlinerRepository tautlinerRepository, CarrierRepository carrierRepository, TautlinerTransformer transformer) {
        this.tautlinerRepository = tautlinerRepository;
        this.carrierRepository = carrierRepository;
        this.transformer = transformer;
    }

    List<TautlinerEntity> getAllTautliners() {
        return tautlinerRepository.findAll(Sort.by(Sort.Direction.ASC, "tautlinerPlates"));
    }

    TautlinerEntity getTautlinerByPlates(String plates) {
        return tautlinerRepository.findByTautlinerPlates(plates).orElseThrow(() ->
                new NoSuchElementException("No tautliner found with plates: " + plates));
    }

    TautlinerEntity getTautlinerById(Long id) {
        return tautlinerRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("No tautliner found with id:" + id));
    }

    TautlinerEntity addNewTautliner(Long carrierId, TautlinerNewUpdateDTO tautliner) {
        tautlinerRepository.findByTautlinerPlates(tautliner.getTautlinerPlates()).ifPresent(tautliner1 -> {
            throw new IllegalOperationException(String.format("Tautliner with plates %s already exists!", tautliner1.getTautlinerPlates()));
        });

        TautlinerEntity tautlinerEntity = transformer.newUpdateDTOtoEntity(tautliner);

        if (carrierId != null) {
            CarrierEntity carrier = carrierRepository.findById(carrierId).orElseThrow(
                    () -> new NoSuchElementException("No carrier found with id: " + carrierId));
            new CarrierModelService(carrier).addTautliner(tautlinerEntity);
        }

        return tautlinerRepository.save(tautlinerEntity);
    }

    TautlinerEntity deleteTautlinerByPlates(String plates) {
        TautlinerEntity tautlinerByPlates = getTautlinerByPlates(plates);
        new TautlinerModelService(tautlinerByPlates).clearTautliner();
        tautlinerRepository.delete(tautlinerByPlates);
        return tautlinerByPlates;
    }

    TautlinerNewUpdateDTO updateTautlinerByPlates(String plates, TautlinerNewUpdateDTO tautlinerDto) {
        TautlinerEntity tautlinerByPlates = getTautlinerByPlates(plates);
        TautlinerEntity tautliner = transformer.newUpdateDTOtoEntity(tautlinerDto);

        if (tautliner.getXpo() != null && !tautliner.getXpo().equals(tautlinerByPlates.getXpo())) {
            tautlinerByPlates.setXpo(tautliner.getXpo());
        }

        if (tautliner.getTautlinerPlates() != null && !tautliner.getTautlinerPlates().equals(tautlinerByPlates.getTautlinerPlates())) {
            tautlinerByPlates.setTautlinerPlates(tautliner.getTautlinerPlates());
        }

        if (tautliner.getTechInspection() != null && !tautliner.getTechInspection().equals(tautlinerByPlates.getTechInspection())) {
            tautlinerByPlates.setTechInspection(tautliner.getTechInspection());
        }

        TautlinerEntity saved = tautlinerRepository.save(tautlinerByPlates);
        return transformer.entityToNewUpdateDTO(saved);
    }
}


