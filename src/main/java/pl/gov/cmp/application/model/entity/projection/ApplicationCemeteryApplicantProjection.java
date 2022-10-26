package pl.gov.cmp.application.model.entity.projection;

import com.fasterxml.jackson.annotation.JsonInclude;
import pl.gov.cmp.application.model.entity.ApplicationCemeteryApplicantEntity;
import org.springframework.data.rest.core.config.Projection;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Projection(name = "investmentAggregation", types= ApplicationCemeteryApplicantEntity.class)
public interface ApplicationCemeteryApplicantProjection {
    String getFirstName();
    String getLastName();
}
