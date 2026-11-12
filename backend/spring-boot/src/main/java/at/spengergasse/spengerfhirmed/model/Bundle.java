package at.spengergasse.spengerfhirmed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Bundle<T extends Resource> {

    public enum BundleType {searchset, document, collection}

    private String resourceType = "Bundle";
    private BundleType type = BundleType.searchset;
    private Integer total;
    private List<Link> link = new ArrayList<>();
    private List<Entry<T>> entry = new ArrayList<>();

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
    public static class Entry<E extends Resource> {
        private String fullUrl;
        private E resource;
    }
}

