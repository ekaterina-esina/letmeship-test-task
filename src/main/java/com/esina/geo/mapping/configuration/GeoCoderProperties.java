package com.esina.geo.mapping.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "geocoder")
public class GeoCoderProperties {
    private String urlSearchByAddress;
    private String urlSearchByCoordinates;
    private String key;
}
