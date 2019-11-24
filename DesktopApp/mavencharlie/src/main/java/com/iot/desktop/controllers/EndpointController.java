package com.iot.desktop.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class EndpointController {

    @GetMapping()
    public ResponseEntity sampleGet() {
        return new ResponseEntity(HttpStatus.OK);
    }

}
