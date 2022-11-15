package com.piotrdomagalski.planning.app;

import com.piotrdomagalski.planning.app_user.*;
import com.piotrdomagalski.planning.carrier.CarrierEntity;
import com.piotrdomagalski.planning.carrier.CarrierRepository;
import com.piotrdomagalski.planning.logs.CoupleTruckTautlinerLogFactory;
import com.piotrdomagalski.planning.logs.LogEntity;
import com.piotrdomagalski.planning.logs.LogsRepository;
import com.piotrdomagalski.planning.logs.NewEntityLogFactory;
import com.piotrdomagalski.planning.tautliner.TautlinerEntity;
import com.piotrdomagalski.planning.tautliner.TautlinerRepository;
import com.piotrdomagalski.planning.truck.TruckEntity;
import com.piotrdomagalski.planning.truck.TruckRepository;
import com.piotrdomagalski.planning.truck_driver.TruckDriverEntity;
import com.piotrdomagalski.planning.truck_driver.TruckDriverRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

/**
 * Temporary initializing data for H2 database.
 */

@Component
public class Initializer {
    private final CarrierRepository carrierRepository;
    private final TruckRepository truckRepository;
    private final TautlinerRepository tautlinerRepository;
    private final TruckDriverRepository truckDriverRepository;
    private final LogsRepository logsRepository;
    private final AppUserService userService;
    private final AppUserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public Initializer(CarrierRepository carrierRepository, TruckRepository truckRepository,
                       TautlinerRepository tautlinerRepository, TruckDriverRepository truckDriverRepository,
                       LogsRepository logsRepository, AppUserService userService, AppUserRepository userRepository,
                       UserRoleRepository userRoleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.carrierRepository = carrierRepository;
        this.truckRepository = truckRepository;
        this.tautlinerRepository = tautlinerRepository;
        this.truckDriverRepository = truckDriverRepository;
        this.logsRepository = logsRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void run() {
        UserRole user = new UserRole("USER");
        UserRole moderator = new UserRole("MODERATOR");
        UserRole admin = new UserRole("ADMIN");
        userRoleRepository.save(user);
        userRoleRepository.save(moderator);
        userRoleRepository.save(admin);

        AppUser appAdmin = new AppUser("admin1", "fakeAdmin@fakemail.com", "admin");
        appAdmin.setUserPassword(passwordEncoder.encode(appAdmin.getUserPassword()));
        userRepository.save(appAdmin);

        AppUser appModerator = new AppUser("mod1", "fakeMod@fakemail.com", "mod");
        appModerator.setUserPassword(passwordEncoder.encode(appModerator.getUserPassword()));
        userRepository.save(appModerator);

        AppUser appUser = new AppUser("user1", "fakeUser@fakemail.com", "user");
        appUser.setUserPassword(passwordEncoder.encode(appUser.getUserPassword()));
        userRepository.save(appUser);

        userService.addRoleToUser(new RoleToUserForm("admin1", "USER"));
        userService.addRoleToUser(new RoleToUserForm("admin1", "MODERATOR"));
        userService.addRoleToUser(new RoleToUserForm("admin1", "ADMIN"));

        userService.addRoleToUser(new RoleToUserForm("mod1", "USER"));
        userService.addRoleToUser(new RoleToUserForm("mod1", "MODERATOR"));

        userService.addRoleToUser(new RoleToUserForm("user1", "USER"));

        CarrierEntity test_carrier = CarrierEntity.newCarrier("123456", "Test Carrier", "Testland", 1.2);
        carrierRepository.save(test_carrier);

        TruckEntity test123 = new TruckEntity("TEST123", false, test_carrier, null, null);
        TruckEntity abcd12345 = new TruckEntity("ABCD12345", false, test_carrier, null, null);
        TruckEntity po4378Y = new TruckEntity("PO4378Y", false, test_carrier, null, null);
        TruckEntity lol890KY = new TruckEntity("LOL890KY", false, test_carrier, null, null);

        LogEntity test123log = new NewEntityLogFactory().makeLog(test123.getTruckPlates(), "SYSTEM");
        logsRepository.save(test123log);
        truckRepository.save(test123);
        LogEntity abcd12345log = new NewEntityLogFactory().makeLog(abcd12345.getTruckPlates(), "SYSTEM");
        logsRepository.save(abcd12345log);
        truckRepository.save(abcd12345);
        LogEntity po4378Ylog = new NewEntityLogFactory().makeLog(po4378Y.getTruckPlates(), "SYSTEM");
        logsRepository.save(po4378Ylog);
        truckRepository.save(po4378Y);
        LogEntity lol890KYlog = new NewEntityLogFactory().makeLog(lol890KY.getTruckPlates(), "SYSTEM");
        logsRepository.save(lol890KYlog);
        truckRepository.save(lol890KY);

        TautlinerEntity fzi1245 = new TautlinerEntity(false, "TEA1245",
                LocalDateTime.of(2022, 10, 10, 0, 0, 0), test_carrier, null);
        TautlinerEntity fzi65789 = new TautlinerEntity(true, "FZI65789",
                LocalDateTime.of(2022, 11, 10, 0, 0, 0), test_carrier, test123);
        TautlinerEntity po890KI = new TautlinerEntity(false, "PO890KI",
                LocalDateTime.of(2022, 12, 8, 0, 0, 0), test_carrier, abcd12345);
        TautlinerEntity sb89089Q = new TautlinerEntity(false, "SB89089Q",
                LocalDateTime.of(2022, 8, 30, 0, 0, 0), test_carrier, po4378Y);
        TautlinerEntity koh890IIU = new TautlinerEntity(true, "KOH890IIU",
                LocalDateTime.of(2022, 9, 1, 0, 0, 0), test_carrier, lol890KY);
        LogEntity fzi1245log = new NewEntityLogFactory().makeLog(fzi1245.getTautlinerPlates(), "SYSTEM");
        logsRepository.save(fzi1245log);
        tautlinerRepository.save(fzi1245);
        LogEntity fzi65789log = new NewEntityLogFactory().makeLog(fzi65789.getTautlinerPlates(), "SYSTEM");
        logsRepository.save(fzi65789log);
        tautlinerRepository.save(fzi65789);
        LogEntity po890KIlog = new NewEntityLogFactory().makeLog(po890KI.getTautlinerPlates(), "SYSTEM");
        logsRepository.save(po890KIlog);
        tautlinerRepository.save(po890KI);
        LogEntity sb89089Qlog = new NewEntityLogFactory().makeLog(sb89089Q.getTautlinerPlates(), "SYSTEM");
        logsRepository.save(sb89089Qlog);
        tautlinerRepository.save(sb89089Q);
        LogEntity koh890IIUlog = new NewEntityLogFactory().makeLog(koh890IIU.getTautlinerPlates(), "SYSTEM");
        logsRepository.save(koh890IIUlog);
        tautlinerRepository.save(koh890IIU);
        test123.setTautliner(fzi65789);
        abcd12345.setTautliner(po890KI);
        po4378Y.setTautliner(sb89089Q);
        lol890KY.setTautliner(koh890IIU);
        logsRepository.save(new CoupleTruckTautlinerLogFactory().makeLog(fzi65789.getTautlinerPlates(), "SYSTEM", test123.getTruckPlates()));
        logsRepository.save(new CoupleTruckTautlinerLogFactory().makeLog(test123.getTruckPlates(), "SYSTEM", fzi65789.getTautlinerPlates()));
        logsRepository.save(new CoupleTruckTautlinerLogFactory().makeLog(po890KI.getTautlinerPlates(), "SYSTEM", abcd12345.getTruckPlates()));
        logsRepository.save(new CoupleTruckTautlinerLogFactory().makeLog(abcd12345.getTruckPlates(), "SYSTEM", po890KI.getTautlinerPlates()));
        logsRepository.save(new CoupleTruckTautlinerLogFactory().makeLog(sb89089Q.getTautlinerPlates(), "SYSTEM", po4378Y.getTruckPlates()));
        logsRepository.save(new CoupleTruckTautlinerLogFactory().makeLog(po4378Y.getTruckPlates(), "SYSTEM", sb89089Q.getTautlinerPlates()));
        logsRepository.save(new CoupleTruckTautlinerLogFactory().makeLog(koh890IIU.getTautlinerPlates(), "SYSTEM", lol890KY.getTruckPlates()));
        logsRepository.save(new CoupleTruckTautlinerLogFactory().makeLog(lol890KY.getTruckPlates(), "SYSTEM", koh890IIU.getTautlinerPlates()));


        TruckDriverEntity kryptonite = new TruckDriverEntity("Boilerdang Kryptonite", "555-545-909", "ID123456", test_carrier, test123);
        TruckDriverEntity coggleswort = new TruckDriverEntity("Boilerdang Coggleswort", "234-123-789", "AXV891789", test_carrier, abcd12345);
        TruckDriverEntity cuckatoo = new TruckDriverEntity("Baggageclaim Cuckatoo", "999-009-765", "IDO098123", test_carrier, po4378Y);
        TruckDriverEntity collywog = new TruckDriverEntity("Boobytrap Collywog", "765-435-698", "KIO876145", test_carrier, lol890KY);
        TruckDriverEntity chowderpants = new TruckDriverEntity("Wimbledon Chowderpants", "345-678-543", "IOP000987", test_carrier, null);
        TruckDriverEntity cameltoe = new TruckDriverEntity("Bandersnatch Cameltoe", "234-072-059", "WER789890", test_carrier, null);
        truckDriverRepository.save(kryptonite);
        truckDriverRepository.save(coggleswort);
        truckDriverRepository.save(cuckatoo);
        truckDriverRepository.save(collywog);
        truckDriverRepository.save(chowderpants);
        truckDriverRepository.save(cameltoe);
        test123.setTruckDriver(kryptonite);
        abcd12345.setTruckDriver(coggleswort);
        po4378Y.setTruckDriver(cuckatoo);
        lol890KY.setTruckDriver(collywog);
        truckRepository.save(test123);
        truckRepository.save(abcd12345);
        truckRepository.save(po4378Y);
        truckRepository.save(lol890KY);


        CarrierEntity transport_mania = CarrierEntity.newCarrier("654321", "Transport Mania", "Transport Land", 1.1);
        carrierRepository.save(transport_mania);

        TruckEntity fz5678 = new TruckEntity("FZ5678", true, transport_mania, null, null);
        TruckEntity sbe3456 = new TruckEntity("SBE3456", true, transport_mania, null, null);
        TruckEntity sbe0987 = new TruckEntity("SBE0987", true, transport_mania, null, null);
        TruckEntity sk89719 = new TruckEntity("SK89719", false, transport_mania, null, null);
        TruckEntity sk8769U = new TruckEntity("SK8769U", false, transport_mania, null, null);
        truckRepository.save(fz5678);
        truckRepository.save(sbe3456);
        truckRepository.save(sbe0987);
        truckRepository.save(sk89719);
        truckRepository.save(sk8769U);

        TautlinerEntity fz44456 = new TautlinerEntity(true, "FZ44456",
                LocalDateTime.of(2022, 10, 9, 0, 0, 0), transport_mania, fz5678);
        TautlinerEntity fz890KO = new TautlinerEntity(true, "FZ890KO",
                LocalDateTime.of(2022, 9, 1, 0, 0, 0), transport_mania, sbe3456);
        TautlinerEntity fzi9999 = new TautlinerEntity(true, "FZI9999",
                LocalDateTime.of(2022, 8, 12, 0, 0, 0), transport_mania, sbe0987);
        TautlinerEntity fz00009 = new TautlinerEntity(true, "FZ00009",
                LocalDateTime.of(2022, 7, 22, 0, 0, 0), transport_mania, sk89719);
        TautlinerEntity fzi1123 = new TautlinerEntity(true, "FZI1123",
                LocalDateTime.of(2022, 12, 25, 0, 0, 0), transport_mania, sk8769U);
        TautlinerEntity fzi9989 = new TautlinerEntity(true, "FZI9989",
                LocalDateTime.of(2022, 6, 10, 0, 0, 0), transport_mania, null);
        tautlinerRepository.save(fz44456);
        tautlinerRepository.save(fz890KO);
        tautlinerRepository.save(fzi9999);
        tautlinerRepository.save(fz00009);
        tautlinerRepository.save(fzi1123);
        tautlinerRepository.save(fzi9989);
        fz5678.setTautliner(fz44456);
        sbe3456.setTautliner(fz890KO);
        sbe0987.setTautliner(fzi9999);
        sk89719.setTautliner(fz00009);
        sk8769U.setTautliner(fzi1123);

        TruckDriverEntity chickenbroth = new TruckDriverEntity("Botany Chickenbroth", "555-545-009", "ABC124569", transport_mania, fz5678);
        TruckDriverEntity cankersore = new TruckDriverEntity("Bourgeoisie Cankersore", "101-779-909", "AKI009009", transport_mania, sbe3456);
        TruckDriverEntity custardwheel = new TruckDriverEntity("Blubberbutt Custardwheel", "909-545-567", "AVY018740", transport_mania, sbe0987);
        TruckDriverEntity crumplehorn = new TruckDriverEntity("Blubberbutt Crumplehorn", "658-775-909", "ABN837509", transport_mania, sk89719);
        truckDriverRepository.save(chickenbroth);
        truckDriverRepository.save(cankersore);
        truckDriverRepository.save(custardwheel);
        truckDriverRepository.save(crumplehorn);
        fz5678.setTruckDriver(chickenbroth);
        sbe3456.setTruckDriver(cankersore);
        sbe0987.setTruckDriver(custardwheel);
        sk89719.setTruckDriver(crumplehorn);
        truckRepository.save(fz5678);
        truckRepository.save(sbe3456);
        truckRepository.save(sbe0987);
        truckRepository.save(sk89719);
        truckRepository.save(sk8769U);


        CarrierEntity transport_solutions = CarrierEntity.newCarrier("345789", "Transport Solutions", "Some land", 1.3);
        carrierRepository.save(transport_solutions);

        TruckEntity po990U = new TruckEntity("PO990U", false, transport_solutions, null, null);
        TruckEntity po8909 = new TruckEntity("PO8909", false, transport_solutions, null, null);
        TruckEntity po99009 = new TruckEntity("PO99009", false, transport_solutions, null, null);
        TruckEntity po123UI = new TruckEntity("PO123UI", false, transport_solutions, null, null);
        TruckEntity po0009U = new TruckEntity("PO0009U", false, transport_solutions, null, null);
        truckRepository.save(po990U);
        truckRepository.save(po8909);
        truckRepository.save(po99009);
        truckRepository.save(po123UI);
        truckRepository.save(po0009U);

        TautlinerEntity po555O = new TautlinerEntity(false, "PO555O",
                LocalDateTime.of(2022, 7, 19, 0, 0, 0), transport_solutions, po990U);
        TautlinerEntity po5540 = new TautlinerEntity(false, "PO5540",
                LocalDateTime.of(2022, 12, 20, 0, 0, 0), transport_solutions, po8909);
        TautlinerEntity po5544P = new TautlinerEntity(false, "PO5544P",
                LocalDateTime.of(2022, 12, 1, 0, 0, 0), transport_solutions, po99009);
        TautlinerEntity po5454I = new TautlinerEntity(false, "PO5454I",
                LocalDateTime.of(2021, 8, 10, 0, 0, 0), transport_solutions, po123UI);
        tautlinerRepository.save(po555O);
        tautlinerRepository.save(po5540);
        tautlinerRepository.save(po5544P);
        tautlinerRepository.save(po5454I);
        po990U.setTautliner(po555O);
        po8909.setTautliner(po5540);
        po99009.setTautliner(po5544P);
        po123UI.setTautliner(po5454I);

        TruckDriverEntity frumblesnatch = new TruckDriverEntity("Dominique Frumblesnatch", "445-545-007", "BAV124569", transport_solutions, po990U);
        TruckDriverEntity candycrush = new TruckDriverEntity("Snozzlebert Candycrush", "887-678-766", "NNK165569", transport_solutions, po8909);
        TruckDriverEntity ampersand = new TruckDriverEntity("Dominique Ampersand", "090-545-341", "NAT990569", transport_solutions, po99009);
        TruckDriverEntity charizard = new TruckDriverEntity("Bulbasaur Charizard", "175-778-328", "ANI304569", transport_solutions, po123UI);
        TruckDriverEntity charmander = new TruckDriverEntity("Botany Charmander", "182-545-009", "PAR124569", transport_solutions, po0009U);
        TruckDriverEntity candlestick = new TruckDriverEntity("Liverswort Candlestick", "253-437-009", "ABC120069", transport_solutions, null);
        truckDriverRepository.save(frumblesnatch);
        truckDriverRepository.save(candycrush);
        truckDriverRepository.save(ampersand);
        truckDriverRepository.save(charizard);
        truckDriverRepository.save(charmander);
        truckDriverRepository.save(candlestick);
        po990U.setTruckDriver(frumblesnatch);
        po8909.setTruckDriver(candycrush);
        po99009.setTruckDriver(ampersand);
        po123UI.setTruckDriver(charizard);
        po0009U.setTruckDriver(charmander);
        truckRepository.save(po990U);
        truckRepository.save(po8909);
        truckRepository.save(po99009);
        truckRepository.save(po123UI);
        truckRepository.save(po0009U);


        CarrierEntity mega_trans = CarrierEntity.newCarrier("098318", "Mega Trans", "Nonexisting land", 1.15);
        carrierRepository.save(mega_trans);

        TruckEntity sk1201 = new TruckEntity("SK1201", true, mega_trans, null, null);
        TruckEntity sk3202 = new TruckEntity("SK3202", true, mega_trans, null, null);
        TruckEntity sk5503 = new TruckEntity("SK5503", true, mega_trans, null, null);
        TruckEntity sk7K04 = new TruckEntity("SK7K04", true, mega_trans, null, null);
        TruckEntity sk21205 = new TruckEntity("SK21205", true, mega_trans, null, null);
        TruckEntity sbi2106 = new TruckEntity("SBI2106", true, mega_trans, null, null);
        TruckEntity sbi2107 = new TruckEntity("SBI2107", true, mega_trans, null, null);
        TruckEntity sk9908 = new TruckEntity("SK9908", true, mega_trans, null, null);
        TruckEntity sk5609 = new TruckEntity("SK5609", true, mega_trans, null, null);
        TruckEntity sbi1210 = new TruckEntity("SBI1210", true, mega_trans, null, null);
        truckRepository.save(sk1201);
        truckRepository.save(sk3202);
        truckRepository.save(sk5503);
        truckRepository.save(sk7K04);
        truckRepository.save(sk21205);
        truckRepository.save(sbi2106);
        truckRepository.save(sbi2107);
        truckRepository.save(sk9908);
        truckRepository.save(sk5609);
        truckRepository.save(sbi1210);

        TautlinerEntity wa7080U = new TautlinerEntity(false, "WA7080U",
                LocalDateTime.of(2022, 7, 19, 0, 0, 0), mega_trans, sk1201);
        TautlinerEntity wa9090KI = new TautlinerEntity(false, "WA9090KI",
                LocalDateTime.of(2022, 7, 10, 0, 0, 0), mega_trans, sk3202);
        TautlinerEntity fzi9001A = new TautlinerEntity(true, "FZI9001A",
                LocalDateTime.of(2022, 8, 9, 0, 0, 0), mega_trans, sk5503);
        TautlinerEntity fz7802B = new TautlinerEntity(true, "FZ7802B",
                LocalDateTime.of(2021, 8, 10, 0, 0, 0), mega_trans, sk7K04);
        TautlinerEntity fzi3103C = new TautlinerEntity(true, "FZI3103C",
                LocalDateTime.of(2022, 10, 21, 0, 0, 0), mega_trans, sk21205);
        TautlinerEntity fzi5404D = new TautlinerEntity(true, "FZI5404D",
                LocalDateTime.of(2022, 5, 5, 0, 0, 0), mega_trans, sbi2106);
        TautlinerEntity fzi1205E = new TautlinerEntity(true, "FZI1205E",
                LocalDateTime.of(2022, 12, 7, 0, 0, 0), mega_trans, sbi2107);
        TautlinerEntity fz7906F = new TautlinerEntity(true, "FZ7906F",
                LocalDateTime.of(2022, 11, 14, 0, 0, 0), mega_trans, sk9908);
        TautlinerEntity fzi4707G = new TautlinerEntity(true, "FZI4707G",
                LocalDateTime.of(2021, 7, 28, 0, 0, 0), mega_trans, sk5609);
        TautlinerEntity fzi8308H = new TautlinerEntity(true, "FZI8308H",
                LocalDateTime.of(2022, 9, 17, 0, 0, 0), mega_trans, sbi1210);
        TautlinerEntity fzi1109I = new TautlinerEntity(true, "FZI1109I",
                LocalDateTime.of(2022, 11, 21, 0, 0, 0), mega_trans, null);
        TautlinerEntity fz3320J = new TautlinerEntity(true, "FZ3320J",
                LocalDateTime.of(2022, 10, 1, 0, 0, 0), mega_trans, null);
        tautlinerRepository.save(wa7080U);
        tautlinerRepository.save(wa9090KI);
        tautlinerRepository.save(fzi9001A);
        tautlinerRepository.save(fz7802B);
        tautlinerRepository.save(fzi3103C);
        tautlinerRepository.save(fzi5404D);
        tautlinerRepository.save(fzi1205E);
        tautlinerRepository.save(fz7906F);
        tautlinerRepository.save(fzi4707G);
        tautlinerRepository.save(fzi8308H);
        tautlinerRepository.save(fzi1109I);
        tautlinerRepository.save(fz3320J);
        sk1201.setTautliner(wa7080U);
        sk3202.setTautliner(wa9090KI);
        sk5503.setTautliner(fzi9001A);
        sk7K04.setTautliner(fz7802B);
        sk21205.setTautliner(fzi3103C);
        sbi2106.setTautliner(fzi5404D);
        sbi2107.setTautliner(fzi1205E);
        sk9908.setTautliner(fz7906F);
        sk5609.setTautliner(fzi4707G);
        sbi1210.setTautliner(fzi8308H);

        TruckDriverEntity colonist = new TruckDriverEntity("Wellington Colonist", "445-123-001", "BAV185569", mega_trans, sk1201);
        TruckDriverEntity curdlesnoot = new TruckDriverEntity("Bentobox Curdlesnoot", "222-321-002", "ANJ345569", mega_trans, sk3202);
        TruckDriverEntity crackerdong = new TruckDriverEntity("Brewery Crackerdong", "332-236-003", "ID124569", mega_trans, sk5503);
        TruckDriverEntity creamsicle = new TruckDriverEntity("Benadryl Creamsicle", "111-765-004", "IDV887569", mega_trans, sk7K04);
        TruckDriverEntity curdlesnoot2 = new TruckDriverEntity("Rinkydink Curdlesnoot", "455-535-005", "IDV090569", mega_trans, sk21205);
        TruckDriverEntity pumpkinpatch = new TruckDriverEntity("Butterfree Pumpkinpatch", "777-545-006", "ASV890569", mega_trans, sbi2106);
        TruckDriverEntity cankersore2 = new TruckDriverEntity("Snorkeldink Cankersore", "909-423-007", "ID124569", mega_trans, sbi2107);
        TruckDriverEntity nottinghill = new TruckDriverEntity("Barnacle Nottinghill", "356-990-008", "AJI333569", mega_trans, sk9908);
        TruckDriverEntity cummerbund = new TruckDriverEntity("Fiddlestick Cummerbund", "505-874-009", "KOP124569", mega_trans, null);
        TruckDriverEntity coochierash = new TruckDriverEntity("Benetton Coochierash", "454-274-010", "AKO124569", mega_trans, null);
        TruckDriverEntity cumbersniff = new TruckDriverEntity("Timothy Cumbersniff", "112-284-011", "NBO432456", mega_trans, null);
        TruckDriverEntity chowderpants2 = new TruckDriverEntity("Brendadirk Chowderpants", "112-109-012", "BAV431569", mega_trans, null);
        truckDriverRepository.save(colonist);
        truckDriverRepository.save(curdlesnoot);
        truckDriverRepository.save(crackerdong);
        truckDriverRepository.save(creamsicle);
        truckDriverRepository.save(curdlesnoot2);
        truckDriverRepository.save(pumpkinpatch);
        truckDriverRepository.save(cankersore2);
        truckDriverRepository.save(nottinghill);
        truckDriverRepository.save(cummerbund);
        truckDriverRepository.save(coochierash);
        truckDriverRepository.save(cumbersniff);
        truckDriverRepository.save(chowderpants2);
        sk1201.setTruckDriver(colonist);
        sk3202.setTruckDriver(curdlesnoot);
        sk5503.setTruckDriver(crackerdong);
        sk7K04.setTruckDriver(creamsicle);
        sk21205.setTruckDriver(curdlesnoot2);
        sbi2106.setTruckDriver(pumpkinpatch);
        sbi2107.setTruckDriver(cankersore2);
        sk9908.setTruckDriver(nottinghill);
        truckRepository.save(sk1201);
        truckRepository.save(sk3202);
        truckRepository.save(sk5503);
        truckRepository.save(sk7K04);
        truckRepository.save(sk21205);
        truckRepository.save(sbi2106);
        truckRepository.save(sbi2107);
        truckRepository.save(sk9908);
        truckRepository.save(sk5609);
        truckRepository.save(sbi1210);

    }
}
