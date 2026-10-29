package com.telemtry.telemetryserver.telemetry.api.controller;

import com.telemtry.telemetryserver.common.domain.ApiResponseDto;
import com.telemtry.telemetryserver.telemetry.api.respsonse.TelemetryBatchResponse;
import com.telemtry.telemetryserver.telemetry.application.TelemetryDashboardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/telemetry/dashboard/")
public class TelemetryDashboardController {

    private final TelemetryDashboardService telemetryDashboardService;

    public TelemetryDashboardController(TelemetryDashboardService telemetryDashboardService) {
        this.telemetryDashboardService = telemetryDashboardService;
    }



    @GetMapping("/{id}")
    public ResponseEntity<?> getTelemetryData(@PathVariable String id){
        TelemetryBatchResponse batch = telemetryDashboardService.getBatch(id);

        return new ResponseEntity<>(

                new ApiResponseDto<>(
                        batch,
                        HttpStatus.OK,
                        "Latest Metric Retrieved"
                ),
                HttpStatus.OK
        );

    }

}
