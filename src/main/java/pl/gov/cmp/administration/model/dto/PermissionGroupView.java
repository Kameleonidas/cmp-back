package pl.gov.cmp.administration.model.dto;

import lombok.Value;

@Value
public class PermissionGroupView {
    long id;
    String name;
    String description;
}
