package pl.gov.cmp.cemetery.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.gov.cmp.cemetery.model.dto.UserCemeteryElementDto;
import pl.gov.cmp.cemetery.model.entity.CemeteryEntity;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserCemeteryMapper {

    @Mapping(target = "type", source = "type.name")
    UserCemeteryElementDto toUserCemeteryElementDto(CemeteryEntity cemetery);

    List<UserCemeteryElementDto> toUserCemeteryElementDtoList(List<CemeteryEntity> cemeteries);
}
