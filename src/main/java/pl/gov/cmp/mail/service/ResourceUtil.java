package pl.gov.cmp.mail.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ResourceUtil {
    public Map<String, ClassPathResource> getResourceFilesMap(String path) throws IOException {
        List<String> files = getResourceFiles(path);
        return files.stream().collect(Collectors.toMap(file -> file, file -> new ClassPathResource(path + "/" + file)));
    }

    public List<String> getResourceFiles(String path) throws IOException {
        List<String> filenames = new ArrayList<>();

        try (InputStream in = getResourceAsStream(path)) {
            if (in == null) {
                return filenames;
            }
            try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
                String resource;

                while ((resource = br.readLine()) != null) {
                    filenames.add(resource);
                }
            }
        }
        return filenames;
    }

    private InputStream getResourceAsStream(String resource) {
        final InputStream in
                = getContextClassLoader().getResourceAsStream(resource);

        return in == null ? getClass().getResourceAsStream(resource) : in;
    }

    private ClassLoader getContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
}
