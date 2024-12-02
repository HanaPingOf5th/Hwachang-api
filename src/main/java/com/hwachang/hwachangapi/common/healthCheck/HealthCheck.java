package com.hwachang.hwachangapi.common.healthCheck;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class HealthCheck {
    @GetMapping("")
    public ResponseEntity<String> healthCheck(){
        boolean isHealthy = checkHealthCondition();
        if (isHealthy) {
            return ResponseEntity.ok("ok");
        } else {
            return new ResponseEntity<>("Service Unavailable", HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    private boolean checkHealthCondition() {
        return true;
    }
}
