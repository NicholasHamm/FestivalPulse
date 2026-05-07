package com.ericsson.festivalpulse;

import com.ericsson.festivalpulse.enums.AlertStatus;
import com.ericsson.festivalpulse.enums.AreaType;
import com.ericsson.festivalpulse.enums.CrowdLevel;
import com.ericsson.festivalpulse.models.CrowdAlert;
import com.ericsson.festivalpulse.models.CrowdReport;
import com.ericsson.festivalpulse.models.FestivalArea;
import com.ericsson.festivalpulse.repository.CrowdAlertRepository;
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
    private final CrowdAlertRepository alertRepo;

    DataSeeder(FestivalAreaRepository areaRepo, CrowdReportRepository reportRepo, CrowdAlertRepository alertRepo) {
        this.areaRepo = areaRepo;
        this.reportRepo = reportRepo;
        this.alertRepo = alertRepo;
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

        List<FestivalArea> areas = areaRepo.saveAll(
            List.of(mainStage, secondStage, foodCourt, bar, nightMarket, welfare, medical, facilities, info, retail)
        );

        LocalDateTime now = LocalDateTime.now();
        record Seed(int idx, CrowdLevel level, String note) {}
        List<Seed> seeds = List.of(
            new Seed(0, CrowdLevel.FULL,   "Packed for headliner"),
            new Seed(1, CrowdLevel.MEDIUM, "Filling up nicely"),
            new Seed(2, CrowdLevel.FULL,   "Long queues at vendors"),
            new Seed(3, CrowdLevel.MEDIUM, "Busy but manageable"),
            new Seed(4, CrowdLevel.LOW,    "Quiet so far"),
            new Seed(5, CrowdLevel.LOW,    ""),
            new Seed(6, CrowdLevel.LOW,    ""),
            new Seed(7, CrowdLevel.MEDIUM, "Queues at toilets"),
            new Seed(8, CrowdLevel.LOW,    ""),
            new Seed(9, CrowdLevel.MEDIUM, "Steady footfall")
        );

        for (Seed s : seeds) {
            FestivalArea a = areas.get(s.idx());
            reportRepo.save(new CrowdReport(a, s.level(), now.minusMinutes(5 + s.idx() * 3), s.note()));
            if (s.level() == CrowdLevel.FULL) {
                CrowdAlert alert = new CrowdAlert();
                alert.setArea(a);
                alert.setMessage("Alert: " + a.getName() + " is at FULL capacity!");
                alert.setStatus(AlertStatus.ACTIVE);
                alert.setTimestamp(now.minusMinutes(5 + s.idx() * 3));
                alertRepo.save(alert);
            }
        }
    }

    private FestivalArea area(String name, String desc, AreaType type, double x, double y) {
        FestivalArea a = new FestivalArea(name, desc, type);
        a.setLocationX(x);
        a.setLocationY(y);
        return a;
    }
}
