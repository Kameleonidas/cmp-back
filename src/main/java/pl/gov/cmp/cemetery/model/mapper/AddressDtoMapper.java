package pl.gov.cmp.cemetery.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.gov.cmp.cemetery.model.entity.CemeteryAddressEntity;
import pl.gov.cmp.gugik.model.dto.AddressDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AddressDtoMapper {

    CemeteryAddressEntity toCemeteryAddressEntity(AddressDto addressDto);
}
