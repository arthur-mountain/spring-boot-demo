package com.arthurmountain.eventtracker.apis.v1.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系統健康檢查
 */
@RestController
public class HealthController {

  @GetMapping("/api/v1/health")
  public ResponseEntity<String> healthCheck() {
    return ResponseEntity.ok("OK");
  }
}
