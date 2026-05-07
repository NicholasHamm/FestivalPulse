package com.ericsson.festivalpulse;

import com.ericsson.festivalpulse.enums.AreaType;
import com.ericsson.festivalpulse.enums.CrowdLevel;
import com.ericsson.festivalpulse.models.CrowdReport;
import com.ericsson.festivalpulse.models.FestivalArea;
import com.ericsson.festivalpulse.repository.CrowdReportRepository;
import com.ericsson.festivalpulse.repository.FestivalAreaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final FestivalAreaRepository areaRepo;
    private final CrowdReportRepository reportRepo;

    DataSeeder(FestivalAreaRepository areaRepo, CrowdReportRepository reportRepo) {
        this.areaRepo = areaRepo;
        this.reportRepo = reportRepo;
    }

    @Override
    public void run(String... args) {
        if (areaRepo.count() > 0) return;

        FestivalArea mainStage   = area("Main Stage",      "Primary performance stage",          AreaType.ENTERTAINMENT, 19.0, 17.0);
        FestivalArea secondStage = area("Second Stage",    "Acoustic & emerging artists",        AreaType.ENTERTAINMENT, 13.0, 32.0);
        FestivalArea foodCourt   = area("Food Court",      "Street food vendors",                AreaType.FOOD_AND_DRINK, 34.0, 14.0);
        FestivalArea bar         = area("Bar Area",        "Cocktails, beer & soft drinks",      AreaType.FOOD_AND_DRINK, 57.0, 40.0);
        FestivalArea nightMarket = area("Night Market",    "Late-night food & snacks",           AreaType.FOOD_AND_DRINK, 24.6, 28.0);
        FestivalArea welfare     = area("Welfare Hub",     "Support & wellbeing services",       AreaType.WELFARE,        56.0, 70.0);
        FestivalArea medical     = area("Medical Point",   "First aid & medical assistance",     AreaType.WELFARE,        42.0, 29.0);
        FestivalArea facilities  = area("Facilities",      "Toilets & washing stations",         AreaType.FACILITIES,     34.0, 60.0);
        FestivalArea info        = area("Info Point",      "Maps, lost & found, schedules",      AreaType.FACILITIES,     34.0, 88.0);
        FestivalArea retail      = area("Merch & Retail",  "Official merchandise & vendors",     AreaType.RETAIL,         83.0, 52.0);

        List<FestivalArea> saved = areaRepo.saveAll(
            List.of(mainStage, secondStage, foodCourt, bar, nightMarket, welfare, medical, facilities, info, retail)
        );

        LocalDateTime now = LocalDateTime.now();
        reportRepo.saveAll(List.of(
            new CrowdReport(saved.get(0), CrowdLevel.FULL,   now.minusMinutes(5),  "Packed for headliner"),
            new CrowdReport(saved.get(1), CrowdLevel.MEDIUM, now.minusMinutes(12), "Filling up nicely"),
            new CrowdReport(saved.get(2), CrowdLevel.FULL,   now.minusMinutes(8),  "Long queues at vendors"),
            new CrowdReport(saved.get(3), CrowdLevel.MEDIUM, now.minusMinutes(20), "Busy but manageable"),
            new CrowdReport(saved.get(4), CrowdLevel.LOW,    now.minusMinutes(30), "Quiet so far"),
            new CrowdReport(saved.get(5), CrowdLevel.LOW,    now.minusMinutes(15), ""),
            new CrowdReport(saved.get(6), CrowdLevel.LOW,    now.minusMinutes(40), ""),
            new CrowdReport(saved.get(7), CrowdLevel.MEDIUM, now.minusMinutes(10), "Queues at toilets"),
            new CrowdReport(saved.get(8), CrowdLevel.LOW,    now.minusMinutes(25), ""),
            new CrowdReport(saved.get(9), CrowdLevel.MEDIUM, now.minusMinutes(18), "Steady footfall")
        ));
    }

    private FestivalArea area(String name, String desc, AreaType type, double x, double y) {
        FestivalArea a = new FestivalArea(name, desc, type);
        a.setLocationX(x);
        a.setLocationY(y);
        return a;
    }
}
