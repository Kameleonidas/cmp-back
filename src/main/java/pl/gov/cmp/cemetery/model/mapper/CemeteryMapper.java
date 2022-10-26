package pl.gov.cmp.cemetery.model.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Geometry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.wololo.jts2geojson.GeoJSONWriter;
import pl.gov.cmp.administration.model.dto.InstitutionInformationDto;
import pl.gov.cmp.cemetery.controller.protocol.response.CemeteryManagerSimpleResponse;
import pl.gov.cmp.cemetery.controller.protocol.response.CemeteryOwnerSimpleResponse;
import pl.gov.cmp.cemetery.controller.protocol.response.GeoJsonObject;
import pl.gov.cmp.cemetery.model.dto.CemeteryDto;
import pl.gov.cmp.cemetery.model.dto.CemeteryElementDto;
import pl.gov.cmp.cemetery.model.dto.CemeteryResponseDto;
import pl.gov.cmp.cemetery.model.dto.SimplifiedCemeteryElementDto;
import pl.gov.cmp.cemetery.model.entity.CemeteryEntity;
import pl.gov.cmp.cemetery.model.entity.CemeteryGeometryEntity;
import pl.gov.cmp.cemetery.model.entity.CemeteryManagerEntity;
import pl.gov.cmp.cemetery.model.entity.CemeteryOwnerEntity;

import java.util.List;

import static pl.gov.cmp.application.model.enums.LegalForm.NATURAL_PERSON;
import static pl.gov.cmp.application.model.enums.LegalForm.PRIVATE_PERSON;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
@Slf4j
public abstract class CemeteryMapper {

    @Autowired
    private ObjectMapper objectMapper;

    @Mapping(target = "type", source = "type.name")
    @Mapping(target = "facilityType", source = "facilityType.name")
    @Mapping(target = "status", source = "status.name")
    @Mapping(target = "source", source = "source.name")
    @Mapping(target = "sourceCode", source = "source.code")
    @Mapping(target = "religion", source = "religion.name")
    @Mapping(target = "contactAddress", ignore = true)
    public abstract CemeteryDto toCemeteryDto(CemeteryEntity cemetery);

    @Mapping(target = "type", source = "type.name")
    @Mapping(target = "facilityType", source = "facilityType.name")
    @Mapping(target = "status", source = "status.name")
    @Mapping(target = "source", source = "source.name")
    @Mapping(target = "religion", source = "religion.name")
    @Mapping(target = "ownerCategory", source = "ownerCategory.name")
    @Mapping(target = "owner", source = "owner", qualifiedByName = "getOwnerSimpleResponse")
    @Mapping(target = "manager", source = "manager", qualifiedByName = "getManagerSimpleResponse")
    public abstract CemeteryResponseDto toCemeteryResponseDto(CemeteryEntity cemetery);

    public String geometryToString(Geometry geometry) {
        if (geometry == null) {
            return null;
        }
        GeoJsonObject geoJsonObjectResult = new GeoJsonObject();
        geoJsonObjectResult.setCrs(Integer.toString(geometry.getSRID()));
        geoJsonObjectResult.setGeometry(new GeoJSONWriter().write(geometry));
        try {
            return objectMapper.writeValueAsString(geoJsonObjectResult);
        } catch (JsonProcessingException jsonProcessingException) {
            return null;
        }
    }

    @Mapping(target = "type", source = "type.name")
    @Mapping(target = "facilityType", source = "facilityType.name")
    @Mapping(target = "status", source = "status.name")
    @Mapping(target = "source", source = "source.name")
    @Mapping(target = "religion", source = "religion.name")
    @Mapping(target = "ownerCategory", source = "ownerCategory.name")
    @Mapping(target = "voivodeship", source = "locationAddress.voivodeship")
    @Mapping(target = "district", source = "locationAddress.district")
    @Mapping(target = "commune", source = "locationAddress.commune")
    @Mapping(target = "place", source = "locationAddress.place")
    public abstract CemeteryElementDto toCemeteryElementDto(CemeteryEntity cemetery);

    public abstract List<CemeteryElementDto> toCemeteryElementDtoList(List<CemeteryEntity> cemeteries);


    public abstract InstitutionInformationDto toEntityDto(CemeteryEntity cemeteryEntity);

    public Long mapToId(CemeteryEntity value) {
        return value.getId();
    }

    public abstract List<SimplifiedCemeteryElementDto> toSimplifiedCemeteryElementDtoList(List<CemeteryEntity> cemeteryEntityList);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cemetery", ignore = true)
    public abstract CemeteryGeometryEntity toCemeteryGeometry(CemeteryGeometryEntity cemeteryGeometry);

    @Named("getManagerSimpleResponse")
    public static CemeteryManagerSimpleResponse getManagerSimpleResponse(CemeteryManagerEntity manager) {
        if (manager != null) {
            var builder = CemeteryManagerSimpleResponse.builder();

            if (!NATURAL_PERSON.equals(manager.getType())) {
                builder.name(manager.getName());
                builder.nip(manager.getNip());
                builder.regon(manager.getRegon());
            }

            builder.type(manager.getType());
            builder.representative(manager.getRepresentative() != null ? PRIVATE_PERSON.getName() : null);

            return builder.build();
        }
        return null;
    }

    @Named("getOwnerSimpleResponse")
    public static CemeteryOwnerSimpleResponse getOwnerSimpleResponse(CemeteryOwnerEntity owner) {
        if (owner != null) {
            return CemeteryOwnerSimpleResponse.builder()
                    .name(owner.getName())
                    .nip(owner.getNip())
                    .regon(owner.getRegon())
                    .representative(owner.getRepresentative() != null ? PRIVATE_PERSON.getName() : null)
                    .build();
        }
        return null;
    }

}
