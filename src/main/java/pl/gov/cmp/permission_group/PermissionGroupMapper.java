package pl.gov.cmp.permission_group;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

@Mapper
interface PermissionGroupMapper {

    PermissionGroupMapper PERMISSION_GROUP_MAPPER = Mappers.getMapper(PermissionGroupMapper.class);

    List<PermissionGroupResponse> toPermissionGroupResponses(Set<PermissionGroupEntity> permissionGroups);
}
