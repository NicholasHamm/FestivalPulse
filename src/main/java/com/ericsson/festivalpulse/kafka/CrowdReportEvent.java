package com.ericsson.festivalpulse.kafka;

import com.ericsson.festivalpulse.enums.CrowdLevel;

public record CrowdReportEvent(Long areaId, CrowdLevel crowdLevel) {}
