package pl.gov.cmp.cemetery.controller.protocol.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import pl.gov.cmp.cemetery.controller.protocol.request.CemeteriesGeometriesRequest;
import pl.gov.cmp.cemetery.controller.protocol.request.CemeteryPageRequest;
import pl.gov.cmp.cemetery.controller.protocol.response.CemeteryElementResponse;
import pl.gov.cmp.cemetery.controller.protocol.response.CemeteryPageResponse;
import pl.gov.cmp.cemetery.controller.protocol.response.CemeteryResponse;
import pl.gov.cmp.cemetery.controller.protocol.response.SimplifiedCemeteryElementResponse;
import pl.gov.cmp.cemetery.controller.protocol.response.CemeterySimpleResponse;
import pl.gov.cmp.cemetery.model.dto.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CemeteryProtocolMapper {

    CemeteryCriteriaDto toCemeteryCriteriaDto(CemeteryPageRequest request);

    CemeteryElementResponse toCemeteryElementResponse(CemeteryElementDto element);

    CemeteryResponse toCemeteryResponse(CemeteryDto cemeteryDto);

    CemeterySimpleResponse toCemeterySimpleResponse(CemeteryResponseDto cemeteryDto);

    default CemeteryPageResponse toCemeteryPageResponse(Page<CemeteryElementDto> page, CemeteryCriteriaDto criteria) {
        CemeteryPageResponse response = new CemeteryPageResponse();
        response.setElements(toCemeteryElements(page.getContent()));
        response.setPageIndex(criteria.getPageIndex());
        response.setTotalPages(page.getTotalPages());
        response.setTotalElements(page.getTotalElements());
        response.setSortColumn(criteria.getSortColumn());
        response.setSortOrder(criteria.getSortOrder());
        return response;
    }

    List<CemeteryElementResponse> toCemeteryElements(List<CemeteryElementDto> elements);

    SimplifiedCemeteryDto toSimplifiedCemeteryDto(CemeteriesGeometriesRequest cemeteriesGeometriesRequest);

    List<SimplifiedCemeteryElementResponse> toSimplifiedCemeteryResponse(List<SimplifiedCemeteryElementDto> list);
}
