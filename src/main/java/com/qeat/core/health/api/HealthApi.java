package com.qeat.core.health.api;

import static com.qeat.core.health.api.HealthApi.PREFIX;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(PREFIX)
public class HealthApi {

    public static final String PREFIX = "/api/health";

    @GetMapping
    public ResponseEntity<String> check() {
        return ResponseEntity.ok(HttpStatus.OK.toString());
    }
}
