package pl.gov.cmp.file.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@Validated
@ConfigurationProperties(prefix = "cmp.files")
public class FileConfiguration {

    @NotBlank
    private String rootPath;

    @NotBlank
    private String attachmentsStoragePath;

    @NotBlank
    private String imagesStoragePath;

}
