package com.esina.geo.mapping.controller;

import com.esina.geo.mapping.dao.Coordinates;
import com.esina.geo.mapping.service.GeoCoderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = GeoCoderController.ENDPOINT_NAME)
public class GeoCoderController {
    public static final String ENDPOINT_NAME = "/api/v1/geo";
    private final GeoCoderService service;

    public GeoCoderController(GeoCoderService service) {
        this.service = service;
    }

    @GetMapping(params = {"address"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Coordinates getByAddress(@RequestParam String address) {
        return service.getByAddress(address);
    }

    @GetMapping(params = {"x", "y"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String getByCoordinates(@RequestParam double xCoordinate, @RequestParam double yCoordinate) {
        return service.getByCoordinates(xCoordinate, yCoordinate);
    }
}
