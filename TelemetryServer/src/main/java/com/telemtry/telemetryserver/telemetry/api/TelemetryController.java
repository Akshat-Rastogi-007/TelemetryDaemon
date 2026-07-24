package com.telemtry.telemetryserver.telemetry.api;

import com.telemtry.telemetryserver.common.domain.ApiResponseDto;
import com.telemtry.telemetryserver.telemetry.api.respsonse.TelemetryBatchResponse;
import com.telemtry.telemetryserver.telemetry.application.TelemetryService;
import com.telemtry.telemetryserver.telemetry.api.request.TelemetryBatchRequest;
import com.telemtry.telemetryserver.telemetry.application.TelemetryServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/app/telemetry")
public class TelemetryController {

    private final TelemetryService telemetryService;

    public TelemetryController(TelemetryServiceImpl telemetryService) {
        this.telemetryService = telemetryService;
    }

    @GetMapping("/")
    public ResponseEntity<?> healthCheck(){
        return new ResponseEntity<>( "Hello",HttpStatus.OK);

    }

    @PostMapping("/submit")
    public ResponseEntity<?> submitTelemetryData(@RequestBody TelemetryBatchRequest batchRequest){

        telemetryService.submitTelemetry(batchRequest);

        return new ResponseEntity<ApiResponseDto<?>>(
                new ApiResponseDto<>(Collections.EMPTY_MAP, HttpStatus.OK,"Message Received"),
                HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getTelemetryData(@PathVariable String id){
        TelemetryBatchResponse batch = telemetryService.getBatch(id);

        return new ResponseEntity<>(

                new ApiResponseDto<>(
                        batch,
                        HttpStatus.OK,
                        "Latest Metric Retreived"
                ),
                HttpStatus.OK
        );

    }

}
