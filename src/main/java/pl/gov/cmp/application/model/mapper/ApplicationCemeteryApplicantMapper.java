package pl.gov.cmp.application.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.gov.cmp.application.controller.protocol.response.ApplicationCemeteryApplicantResponse;
import pl.gov.cmp.application.model.dto.ApplicationCemeteryApplicantDto;
import pl.gov.cmp.application.model.entity.projection.ApplicationCemeteryApplicantProjection;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ApplicationCemeteryApplicantMapper {

    List<ApplicationCemeteryApplicantDto> toApplicantDtoList(List<ApplicationCemeteryApplicantProjection> applicants);

    List<ApplicationCemeteryApplicantResponse> toApplicantResponseList(List<ApplicationCemeteryApplicantDto> applicants);
}
