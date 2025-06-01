package com.example.Bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recommend")
@Tag(name = "Health Controller", description = "Controller for health management")
public class HealthController {

    @GetMapping(value = "/healthcheck")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "200", description = "Health is OK")
    @Operation(summary = "Get Recommended Health")
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok("OK");
    }
}
