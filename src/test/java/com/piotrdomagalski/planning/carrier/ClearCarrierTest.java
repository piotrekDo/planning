package com.piotrdomagalski.planning.carrier;

import com.piotrdomagalski.planning.tautliner.TautlinerEntity;
import com.piotrdomagalski.planning.truck.TruckEntity;
import com.piotrdomagalski.planning.truck_driver.TruckDriverEntity;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.*;


import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
class ClearCarrierTest {



    @ParameterizedTest
    @ArgumentsSource(CarrierClearCarrierArgumentsProvider.class)
    void clearCarrier_should_clear_carrier(){
        //given
        CarrierEntity carrier = getCarrier();
        TruckDriverEntity driver = carrier.getDrivers().get(0);
        TautlinerEntity tautlinerEntity = carrier.getTautliners().get(0);
        TruckEntity truck = carrier.getTrucks().get(0);

        //when
        boolean result = new ClearCarrier(carrier).execute();

        //then
        assertTrue(result);
        assertEquals(6, carrier.getDrivers().size());
        assertEquals(5, carrier.getTautliners().size());
        assertEquals(4, carrier.getTrucks().size());
        assertNull(driver.getCarrier());
        assertNull(driver.getTruck());
        assertNull(tautlinerEntity.getCarrier());
        assertNull(tautlinerEntity.getTruck());
        assertNull(truck.getCarrier());
        assertNull(truck.getTruckDriver());
        assertNull(truck.getTautliner());

    }








    private CarrierEntity getCarrier() {
        CarrierEntity test_carrier = CarrierEntity.newCarrier("123456", "Test Carrier", "Testland", 1.2);
        TruckEntity test123 = new TruckEntity("TEST123", false, test_carrier, null, null);
        TruckEntity abcd12345 = new TruckEntity("ABCD12345", false, test_carrier, null, null);
        TruckEntity po4378Y = new TruckEntity("PO4378Y", false, test_carrier, null, null);
        TruckEntity lol890KY = new TruckEntity("LOL890KY", false, test_carrier, null, null);
        test_carrier.getTrucks().add(test123);
        test_carrier.getTrucks().add(abcd12345);
        test_carrier.getTrucks().add(po4378Y);
        test_carrier.getTrucks().add(lol890KY);

        TautlinerEntity fzi65789 = new TautlinerEntity(true, "FZI65789",
                LocalDateTime.of(2022, 11, 10, 0, 0, 0), test_carrier, test123);
        TautlinerEntity fzi1245 = new TautlinerEntity(true, "FZI1245",
                LocalDateTime.of(2022, 10, 10, 0, 0, 0), test_carrier, null);
        TautlinerEntity po890KI = new TautlinerEntity(false, "PO890KI",
                LocalDateTime.of(2022, 12, 8, 0, 0, 0), test_carrier, abcd12345);
        TautlinerEntity sb89089Q = new TautlinerEntity(false, "SB89089Q",
                LocalDateTime.of(2022, 8, 30, 0, 0, 0), test_carrier, po4378Y);
        TautlinerEntity koh890IIU = new TautlinerEntity(true, "KOH890IIU",
                LocalDateTime.of(2022, 9, 1, 0, 0, 0), test_carrier, lol890KY);
        test_carrier.getTautliners().add(fzi65789);
        test_carrier.getTautliners().add(fzi1245);
        test_carrier.getTautliners().add(po890KI);
        test_carrier.getTautliners().add(sb89089Q);
        test_carrier.getTautliners().add(koh890IIU);
        test123.setTautliner(fzi65789);
        abcd12345.setTautliner(po890KI);
        po4378Y.setTautliner(sb89089Q);
        lol890KY.setTautliner(koh890IIU);

        TruckDriverEntity kryptonite = new TruckDriverEntity("Boilerdang Kryptonite", "555-545-909", "ID123456", test_carrier, test123);
        TruckDriverEntity coggleswort = new TruckDriverEntity("Boilerdang Coggleswort", "234-123-789", "AXV891789", test_carrier, abcd12345);
        TruckDriverEntity cuckatoo = new TruckDriverEntity("Baggageclaim Cuckatoo", "999-009-765", "IDO098123", test_carrier, po4378Y);
        TruckDriverEntity collywog = new TruckDriverEntity("Boobytrap Collywog", "765-435-698", "KIO876145", test_carrier, lol890KY);
        TruckDriverEntity chowderpants = new TruckDriverEntity("Wimbledon Chowderpants", "345-678-543", "IOP000987", test_carrier, null);
        TruckDriverEntity cameltoe = new TruckDriverEntity("Bandersnatch Cameltoe", "234-072-059", "WER789890", test_carrier, null);
        test_carrier.getDrivers().add(kryptonite);
        test_carrier.getDrivers().add(coggleswort);
        test_carrier.getDrivers().add(cuckatoo);
        test_carrier.getDrivers().add(collywog);
        test_carrier.getDrivers().add(chowderpants);
        test_carrier.getDrivers().add(cameltoe);
        test123.setTruckDriver(kryptonite);
        abcd12345.setTruckDriver(coggleswort);
        po4378Y.setTruckDriver(cuckatoo);
        lol890KY.setTruckDriver(collywog);

        return test_carrier;
    }

}