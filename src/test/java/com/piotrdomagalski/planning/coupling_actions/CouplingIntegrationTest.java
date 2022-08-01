package com.piotrdomagalski.planning.coupling_actions;

import com.piotrdomagalski.planning.PlanningApplication;
import com.piotrdomagalski.planning.carrier.CarrierEntity;
import com.piotrdomagalski.planning.carrier.CarrierRepository;
import com.piotrdomagalski.planning.tautliner.TautlinerEntity;
import com.piotrdomagalski.planning.tautliner.TautlinerRepository;
import com.piotrdomagalski.planning.truck.TruckEntity;
import com.piotrdomagalski.planning.truck.TruckRepository;
import com.piotrdomagalski.planning.truck_driver.TruckDriverEntity;
import com.piotrdomagalski.planning.truck_driver.TruckDriverRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = PlanningApplication.class)
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@WithMockUser(username = "Test", authorities = {"USER"})
class CouplingIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    CarrierRepository carrierRepository;

    @Autowired
    TruckRepository truckRepository;

    @Autowired
    TautlinerRepository tautlinerRepository;

    @Autowired
    TruckDriverRepository truckDriverRepository;

    @Test
    void coupleTruckWithDriver_should_return_code_200_and_couple() throws Exception {
        //given
        CarrierEntity carrier = CarrierEntity.newCarrier("123456", "Test Trans", "Testland", 1.2);
        carrierRepository.save(carrier);
        TruckEntity truck = TruckEntity.newTruck("TRUCK1234", true);
        truck.setCarrier(carrier);
        TruckDriverEntity driver = TruckDriverEntity.newTruckDriver("Test Driver", "555-555-555", "ID123446");
        driver.setCarrier(carrier);
        truckRepository.save(truck);
        truckDriverRepository.save(driver);

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.put("/couple/truck-driver").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "driver": 3,
                          "truck": "TRUCK1234"
                        }  
                        """));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isOk());
        TruckEntity truckEntity = truckRepository.findByTruckPlatesIgnoreCase("TRUCK1234").get();
        TruckDriverEntity truckDriverEntity = truckDriverRepository.findById(3L).get();
        assertEquals(truckEntity.getTruckPlates(), truckDriverEntity.getTruck().getTruckPlates());
        assertEquals(truckDriverEntity.getFullName(), truckEntity.getTruckDriver().getFullName());
    }

    @Test
    void coupleTruckWithDriver_should_return_not_found_when_driver_doenst_exist() throws Exception {
        //given
        CarrierEntity carrier = CarrierEntity.newCarrier("123456", "Test Trans", "Testland", 1.2);
        carrierRepository.save(carrier);
        TruckEntity truck = TruckEntity.newTruck("TRUCK1234", true);
        truck.setCarrier(carrier);
        truckRepository.save(truck);

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.put("/couple/truck-driver").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "driver": 3,
                          "truck": "TRUCK1234"
                        }  
                        """));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("No driver found with id: 3")));
    }

    @Test
    void coupleTruckWithDriver_should_return_not_found_when_truck_doenst_exist() throws Exception {
        //given
        CarrierEntity carrier = CarrierEntity.newCarrier("123456", "Test Trans", "Testland", 1.2);
        carrierRepository.save(carrier);
        TruckDriverEntity driver = TruckDriverEntity.newTruckDriver("Test Driver", "555-555-555", "ID123446");
        driver.setCarrier(carrier);
        truckDriverRepository.save(driver);

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.put("/couple/truck-driver").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "driver": 2,
                          "truck": "TRUCK1234"
                        }  
                        """));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("No truck found with plates: TRUCK1234")));
    }

    @Test
    void coupleTruckWithTautliner_should_return_code_200_and_couple() throws Exception {
        //given
        TruckEntity truck = TruckEntity.newTruck("TRUCK1234", true);
        TautlinerEntity tautliner = TautlinerEntity.newTautliner(true, "TAUT1234", LocalDateTime.of(2022, 10, 10, 0, 0, 0));
        truckRepository.save(truck);
        tautlinerRepository.save(tautliner);

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.put("/couple/truck-tautliner").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "tautliner": "TAUT1234",
                          "truck": "TRUCK1234"
                        }
                        """));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isOk());
        TruckEntity truckEntity = truckRepository.findByTruckPlatesIgnoreCase("TRUCK1234").get();
        TautlinerEntity tautlinerEntity = tautlinerRepository.findByTautlinerPlatesIgnoreCase("TAUT1234").get();
        assertEquals(tautlinerEntity.getTautlinerPlates(), truckEntity.getTautliner().getTautlinerPlates());
        assertEquals(truckEntity.getTruckPlates(), tautlinerEntity.getTruck().getTruckPlates());
    }

    @Test
    @Transactional
    void coupleTautlinerWithCarrier_should_return_code_200_and_couple() throws Exception {
        //given
        CarrierEntity carrier = CarrierEntity.newCarrier("123456", "Test Trans", "Testland", 1.2);
        TautlinerEntity tautliner = TautlinerEntity.newTautliner(true, "TAUT1234", LocalDateTime.of(2022, 10, 10, 0, 0, 0));
        carrierRepository.save(carrier);
        tautlinerRepository.save(tautliner);

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.put("/couple/tautliner-carrier").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "carrierSap": "123456",
                          "tautliner": "TAUT1234"
                        }
                        """));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isOk());
        CarrierEntity carrierEntity = carrierRepository.findBySap("123456").get();
        TautlinerEntity tautlinerEntity = tautlinerRepository.findByTautlinerPlatesIgnoreCase("TAUT1234").get();
        assertEquals(carrierEntity.getSap(), tautlinerEntity.getCarrier().getSap());
        assertEquals(tautlinerEntity.getTautlinerPlates(), carrierEntity.getTautliners().get(0).getTautlinerPlates());
    }

}
