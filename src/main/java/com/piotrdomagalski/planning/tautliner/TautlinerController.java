package com.piotrdomagalski.planning.tautliner;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tautliners")
 class TautlinerController {

    private final TautlinerRestService tautlinerRestService;

     TautlinerController(TautlinerRestService tautlinerRestService) {
        this.tautlinerRestService = tautlinerRestService;
    }

    @GetMapping
    List<TautlinerInfoDTO> getAllTautliners(@RequestParam(required = false) Boolean isXpo){
        return tautlinerRestService.getAllTautliners(isXpo);
    }

    @GetMapping("/{plates}")
    TautlinerInfoDTO getTautlinerByPlates(@PathVariable String plates) {
       return tautlinerRestService.getTautlinerByPlates(plates);
    }

    @PostMapping("/{carrierSap}")
    TautlinerInfoDTO addNewTautliner(@PathVariable(required = false) String carrierSap, @RequestBody @Validated(value = AddTautliner.class) TautlinerNewUpdateDTO dto){
        return tautlinerRestService.addNewTautliner(carrierSap, dto);
    }

    @DeleteMapping("/{plates}")
    TautlinerEntity deleteTautlinerByPlates(@PathVariable String plates){
        return tautlinerRestService.deleteTautlinerByPlates(plates);
    }

    @PutMapping("/{plates}")
    TautlinerNewUpdateDTO updateTautlinerByPlates(@PathVariable String plates, @RequestBody @Validated(value = UpdateTautliner.class) TautlinerNewUpdateDTO dto){
        return tautlinerRestService.updateTautlinerByPlates(plates, dto);
    }

}
