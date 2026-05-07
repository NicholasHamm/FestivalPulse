package com.ericsson.festivalpulse.controller;

import com.ericsson.festivalpulse.enums.AlertStatus;
import com.ericsson.festivalpulse.enums.CrowdLevel;
import com.ericsson.festivalpulse.models.CrowdAlert;
import com.ericsson.festivalpulse.models.FestivalArea;
import com.ericsson.festivalpulse.service.CrowdAlertService;
import com.ericsson.festivalpulse.service.CrowdReportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CrowdAlertController.class)
class CrowdAlertControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockitoBean
	CrowdAlertService alertService;
	@MockitoBean
	CrowdReportService reportService;

	private CrowdAlert makeAlert(Long id, AlertStatus status) {
		FestivalArea area = new FestivalArea("Main Stage", "Primary stage", null);
		area.setId(1L);
		CrowdAlert alert = new CrowdAlert(area, "Area is FULL", status, LocalDateTime.now());
		alert.setId(id);
		return alert;
	}

	@Test
	void getActiveAlerts_returnsActiveAlerts() throws Exception {
		when(alertService.getAlertsByStatus(AlertStatus.ACTIVE)).thenReturn(List.of(makeAlert(1L, AlertStatus.ACTIVE)));

		mockMvc.perform(get("/api/alerts")).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[0].status").value("ACTIVE"));
	}

	@Test
	void getActiveAlerts_returnsEmptyList_whenNoActiveAlerts() throws Exception {
		when(alertService.getAlertsByStatus(AlertStatus.ACTIVE)).thenReturn(List.of());

		mockMvc.perform(get("/api/alerts")).andExpect(status().isOk()).andExpect(jsonPath("$").isEmpty());
	}

	@Test
	void resolveAlert_returnsOk_whenAlertExists() throws Exception {
		CrowdAlert resolved = makeAlert(1L, AlertStatus.RESOLVED);
		when(alertService.resolveAlert(1L, CrowdLevel.LOW, reportService)).thenReturn(resolved);

		mockMvc.perform(post("/api/alerts/1/resolve").param("newLevel", "LOW")).andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("RESOLVED"));
	}

	@Test
	void resolveAlert_returns404_whenAlertNotFound() throws Exception {
		when(alertService.resolveAlert(99L, CrowdLevel.LOW, reportService)).thenReturn(null);

		mockMvc.perform(post("/api/alerts/99/resolve").param("newLevel", "LOW")).andExpect(status().isNotFound());
	}
}
