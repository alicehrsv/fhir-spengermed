package at.spengergasse.spengerfhirmed.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.net.URI;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceDTO {

   private URI u;

    // Pagination
    private Integer page;
    private Integer count;

    public static boolean isValid(String param) {
        return param != null && !param.trim().isEmpty();
    }
}

