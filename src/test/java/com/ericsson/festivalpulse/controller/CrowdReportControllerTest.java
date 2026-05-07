package com.ericsson.festivalpulse.controller;

import com.ericsson.festivalpulse.enums.CrowdLevel;
import com.ericsson.festivalpulse.models.CrowdReport;
import com.ericsson.festivalpulse.models.FestivalArea;
import com.ericsson.festivalpulse.service.CrowdAlertService;
import com.ericsson.festivalpulse.service.CrowdReportService;
import com.ericsson.festivalpulse.service.FestivalAreaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CrowdReportController.class)
class CrowdReportControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockitoBean
	CrowdReportService reportService;
	@MockitoBean
	FestivalAreaService areaService;
	@MockitoBean
	CrowdAlertService alertService;

	private CrowdReport makeReport(Long id, CrowdLevel level) {
		FestivalArea area = new FestivalArea("Main Stage", "Primary stage", null);
		area.setId(1L);
		CrowdReport report = new CrowdReport(area, level, LocalDateTime.now(), "test note");
		report.setId(id);
		return report;
	}

	@Test
	void getRecentReports_returnsReportList() throws Exception {
		when(reportService.getRecentCrowdReports()).thenReturn(List.of(makeReport(1L, CrowdLevel.MEDIUM)));

		mockMvc.perform(get("/api/reports")).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[0].crowdLevel").value("MEDIUM"));
	}

	@Test
	void getRecentReports_returnsEmptyList_whenNoReports() throws Exception {
		when(reportService.getRecentCrowdReports()).thenReturn(List.of());

		mockMvc.perform(get("/api/reports")).andExpect(status().isOk()).andExpect(jsonPath("$").isEmpty());
	}

	@Test
	void submitReport_returnsOk_whenValid() throws Exception {
		CrowdReport saved = makeReport(1L, CrowdLevel.LOW);
		when(reportService.submitReport(any(), any(), any())).thenReturn(saved);

		mockMvc.perform(post("/api/reports").contentType(MediaType.APPLICATION_JSON)
				.content("{\"area\":{\"id\":1},\"crowdLevel\":\"LOW\",\"shortNote\":\"test note\"}"))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.crowdLevel").value("LOW"));
	}

	@Test
	void submitReport_returns400_whenAreaMissing() throws Exception {
		when(reportService.submitReport(any(), any(), any()))
				.thenThrow(new IllegalArgumentException("Area ID is required"));

		mockMvc.perform(
				post("/api/reports").contentType(MediaType.APPLICATION_JSON).content("{\"crowdLevel\":\"LOW\"}"))
				.andExpect(status().isBadRequest());
	}

	@Test
	void submitReport_returns400_whenAreaNotFound() throws Exception {
		when(reportService.submitReport(any(), any(), any())).thenThrow(new IllegalArgumentException("Area not found"));

		mockMvc.perform(post("/api/reports").contentType(MediaType.APPLICATION_JSON)
				.content("{\"area\":{\"id\":99},\"crowdLevel\":\"FULL\"}")).andExpect(status().isBadRequest());
	}
}
