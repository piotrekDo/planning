package com.piotrdomagalski.planning.tautliner;

import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for tautliner entity.
 */

@RestController
@RequestMapping("/tautliners")
 class TautlinerController {

    private final TautlinerService tautlinerService;

     TautlinerController(TautlinerService tautlinerService) {
        this.tautlinerService = tautlinerService;
    }

    /**
     *
     * @param isXpo boolean determines if only organization's tautliner are wanted.
     * @param page if not passed will return zeroth page.
     * @param size if not passed will return default value.
     * @return
     */

    @GetMapping
    Page<TautlinerInfoDTO> getAllTautliners(@RequestParam(required = false) Boolean isXpo,
                                            @RequestParam(required = false) Integer page,
                                            @RequestParam(required = false) Integer size){
        return tautlinerService.getAllTautliners(isXpo, page, size);
    }

    @GetMapping("/{plates}")
    TautlinerInfoDTO getTautlinerByPlates(@PathVariable String plates) {
       return tautlinerService.getTautlinerByPlates(plates);
    }

    @PostMapping("/{carrierSap}")
    TautlinerInfoDTO addNewTautliner(@PathVariable(required = false) String carrierSap, @RequestBody @Validated(value = AddTautliner.class) TautlinerNewUpdateDTO dto){
        return tautlinerService.addNewTautliner(carrierSap, dto);
    }

    @DeleteMapping("/{plates}")
    TautlinerEntity deleteTautlinerByPlates(@PathVariable String plates){
        return tautlinerService.deleteTautlinerByPlates(plates);
    }

    @PutMapping("/{plates}")
    TautlinerNewUpdateDTO updateTautlinerByPlates(@PathVariable String plates, @RequestBody @Validated(value = UpdateTautliner.class) TautlinerNewUpdateDTO dto){
        return tautlinerService.updateTautlinerByPlates(plates, dto);
    }

}
