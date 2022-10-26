package pl.gov.cmp.cemetery.controller.protocol.response;

import lombok.Getter;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;
import org.wololo.geojson.Geometry;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public class GeoJsonObject {

    private static final String CSR_PARAMETER_NAME = "crs";
    private static final String CSR_PARAMETER_VALUE_PREFIX = "EPSG:";
    private Geometry geometry;
    @Singular
    private Map<String, String> properties = new LinkedHashMap<>();

    public void setCrs(String value) {
        properties.put(CSR_PARAMETER_NAME, CSR_PARAMETER_VALUE_PREFIX + value);
    }

}
