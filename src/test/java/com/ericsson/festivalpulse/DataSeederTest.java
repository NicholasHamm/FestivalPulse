package com.ericsson.festivalpulse;

import com.ericsson.festivalpulse.models.CrowdReport;
import com.ericsson.festivalpulse.models.FestivalArea;
import com.ericsson.festivalpulse.repository.CrowdAlertRepository;
import com.ericsson.festivalpulse.repository.CrowdReportRepository;
import com.ericsson.festivalpulse.repository.FestivalAreaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataSeederTest {

	@Mock
	FestivalAreaRepository areaRepo;
	@Mock
	CrowdReportRepository reportRepo;
	@Mock
	CrowdAlertRepository alertRepo;

	DataSeeder seeder;

	@BeforeEach
	void setUp() {
		seeder = new DataSeeder(areaRepo, reportRepo,alertRepo);
	}

	@Test
	void skipsSeeding_whenDataAlreadyExists() throws Exception {
		when(areaRepo.count()).thenReturn(1L);

		seeder.run();

		verify(areaRepo, never()).saveAll(anyList());
		verify(reportRepo, never()).saveAll(anyList());
	}

	@Test
	void seeds10Areas_whenRepositoryIsEmpty() throws Exception {
		when(areaRepo.count()).thenReturn(0L);

		ArgumentCaptor<List<FestivalArea>> areaCaptor = ArgumentCaptor.forClass(List.class);
		when(areaRepo.saveAll(areaCaptor.capture())).thenAnswer(inv -> areaCaptor.getValue());

		seeder.run();

		assertThat(areaCaptor.getValue()).hasSize(10);
	}

	@Test
	void eachAreaHasLocationCoordinates() throws Exception {
		when(areaRepo.count()).thenReturn(0L);

		ArgumentCaptor<List<FestivalArea>> areaCaptor = ArgumentCaptor.forClass(List.class);
		when(areaRepo.saveAll(areaCaptor.capture())).thenAnswer(inv -> areaCaptor.getValue());

		seeder.run();

		areaCaptor.getValue().forEach(area -> {
			assertThat(area.getLocationX()).isNotNull();
			assertThat(area.getLocationY()).isNotNull();
		});
	}
}
