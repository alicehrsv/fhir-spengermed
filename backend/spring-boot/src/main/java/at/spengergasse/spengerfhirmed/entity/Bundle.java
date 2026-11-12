package at.spengergasse.spengerfhirmed.entity;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bundle {

    private String resourceType = "Bundle";
    private BundleType type = BundleType.searchset;
    private Integer total;
    private List<Link> link = new ArrayList<>();
    private List<Entry> entry = new ArrayList<>();

    public enum BundleType {
        document, message, transaction, transaction_response,
        batch, batch_response, history, searchset, collection
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Link {
        private String relation;
        private String url;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Entry {
        private String fullUrl;
        private Object resource;
    }
}