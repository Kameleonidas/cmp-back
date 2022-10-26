package pl.gov.cmp.gugik.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.gov.cmp.gugik.model.dto.AddressDto;
import pl.gov.cmp.gugik.model.entity.GugikAddressEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GugikAddressDtoMapper {

    GugikAddressEntity toGugikAddressEntity(AddressDto addressDto);

    AddressDto toAddressDto(GugikAddressEntity gugikAddressEntity);
}
