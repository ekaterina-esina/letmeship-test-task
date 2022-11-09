package com.esina.geo.mapping.service;

import com.esina.geo.mapping.dao.Coordinates;
import org.springframework.stereotype.Service;

@Service
public class GeoCoderService {
    public Coordinates getByAddress(String address) {
        return new Coordinates();
    }

    public String getByCoordinates(double xCoordinate, double yCoordinate) {
        return new String();
    }
}
