package pl.gov.cmp.cemetery.controller.protocol.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import pl.gov.cmp.cemetery.controller.protocol.request.UserCemeteryPageRequest;
import pl.gov.cmp.cemetery.controller.protocol.response.UserCemeteryElementResponse;
import pl.gov.cmp.cemetery.controller.protocol.response.UserCemeteryPageResponse;
import pl.gov.cmp.cemetery.model.dto.UserCemeteryCriteriaDto;
import pl.gov.cmp.cemetery.model.dto.UserCemeteryElementDto;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserCemeteryProtocolMapper {

    UserCemeteryCriteriaDto toUserCemeteryCriteriaDto(UserCemeteryPageRequest request);

    default UserCemeteryPageResponse toUserCemeteryPageResponse(Page<UserCemeteryElementDto> page, UserCemeteryCriteriaDto criteria) {
        UserCemeteryPageResponse response = new UserCemeteryPageResponse();
        response.setElements(toCemeteryElements(page.getContent()));
        response.setPageIndex(criteria.getPageIndex());
        response.setTotalPages(page.getTotalPages());
        response.setTotalElements(page.getTotalElements());
        response.setSortColumn(criteria.getSortColumn());
        response.setSortOrder(criteria.getSortOrder());
        return response;
    }
    List<UserCemeteryElementResponse> toCemeteryElements(List<UserCemeteryElementDto> elements);
}
