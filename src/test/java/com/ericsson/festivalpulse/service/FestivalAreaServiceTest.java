package com.ericsson.festivalpulse.service;

import com.ericsson.festivalpulse.dto.FestivalAreaStatus;
import com.ericsson.festivalpulse.enums.AreaType;
import com.ericsson.festivalpulse.enums.CrowdLevel;
import com.ericsson.festivalpulse.models.CrowdReport;
import com.ericsson.festivalpulse.models.FestivalArea;
import com.ericsson.festivalpulse.repository.FestivalAreaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FestivalAreaServiceTest {

	@Mock
	FestivalAreaRepository areaRepo;
	@Mock
	CrowdReportService crowdReportService;
	@Mock
	CrowdAlertService alertService;

	FestivalAreaService service;

	@BeforeEach
	void setUp() {
		service = new FestivalAreaService(areaRepo, crowdReportService);
	}

	private FestivalArea makeArea(Long id, String name) {
		FestivalArea area = new FestivalArea(name, "desc", AreaType.ENTERTAINMENT);
		area.setId(id);
		return area;
	}

	@Test
	void createArea_savesAndReturnsArea() {
		FestivalArea area = makeArea(1L, "Main Stage");
		when(areaRepo.existsFestivalAreasByName("Main Stage")).thenReturn(false);
		when(areaRepo.save(area)).thenReturn(area);

		assertThat(service.createArea(area)).isEqualTo(area);
		verify(areaRepo).save(area);
	}

	@Test
	void createArea_throws_whenNameAlreadyExists() {
		FestivalArea area = makeArea(1L, "Main Stage");
		when(areaRepo.existsFestivalAreasByName("Main Stage")).thenReturn(true);

		assertThatThrownBy(() -> service.createArea(area)).isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Area already exists");
	}

	@Test
	void getAreaById_returnsArea_whenFound() {
		FestivalArea area = makeArea(1L, "Main Stage");
		when(areaRepo.findById(1L)).thenReturn(Optional.of(area));

		assertThat(service.getAreaById(1L)).isEqualTo(area);
	}

	@Test
	void getAreaById_returnsNull_whenNotFound() {
		when(areaRepo.findById(99L)).thenReturn(Optional.empty());

		assertThat(service.getAreaById(99L)).isNull();
	}

	@Test
	void getAllAreas_returnsAllAreas() {
		List<FestivalArea> areas = List.of(makeArea(1L, "Main Stage"), makeArea(2L, "Food Court"));
		when(areaRepo.findAll()).thenReturn(areas);

		assertThat(service.getAllAreas()).hasSize(2);
	}

	@Test
	void getDashboardSummary_returnsCorrectCounts() {
		when(areaRepo.count()).thenReturn(5L);
		when(crowdReportService.countReports()).thenReturn(20L);
		when(alertService.countActiveAlerts()).thenReturn(3L);

		Map<String, Object> summary = service.getDashboardSummary(crowdReportService, alertService);

		assertThat(summary).containsEntry("totalAreas", 5L).containsEntry("totalReports", 20L)
				.containsEntry("activeAlerts", 3L);
	}

	@Test
	void getAreasStatus_returnsUnknownLevel_whenNoReport() {
		FestivalArea area = makeArea(1L, "Main Stage");
		when(areaRepo.findAll()).thenReturn(List.of(area));
		when(crowdReportService.getLatestReportForArea(area)).thenReturn(Optional.empty());

		List<FestivalAreaStatus> statuses = service.getAreasStatus();

		assertThat(statuses).hasSize(1);
		assertThat(statuses.get(0).getLatestLevel()).isEqualTo(CrowdLevel.UNKNOWN);
		assertThat(statuses.get(0).getLatestTime()).isNull();
	}

	@Test
	void getAreasStatus_returnsLatestLevel_whenReportExists() {
		FestivalArea area = makeArea(1L, "Main Stage");
		CrowdReport report = new CrowdReport(area, CrowdLevel.FULL, LocalDateTime.now());
		when(areaRepo.findAll()).thenReturn(List.of(area));
		when(crowdReportService.getLatestReportForArea(area)).thenReturn(Optional.of(report));

		List<FestivalAreaStatus> statuses = service.getAreasStatus();

		assertThat(statuses.get(0).getLatestLevel()).isEqualTo(CrowdLevel.FULL);
		assertThat(statuses.get(0).getLatestTime()).isNotNull();
	}
}
