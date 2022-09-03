package com.piotrdomagalski.planning.tautliner;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tautliners")
 class TautlinerController {

    private final TautlinerService tautlinerService;

     TautlinerController(TautlinerService tautlinerService) {
        this.tautlinerService = tautlinerService;
    }

    @GetMapping
    List<TautlinerInfoDTO> getAllTautliners(@RequestParam(required = false) Boolean isXpo){
        return tautlinerService.getAllTautliners(isXpo);
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
