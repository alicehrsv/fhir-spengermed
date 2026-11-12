package at.spengergasse.spengerfhirmed.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Entity
@Table(name = "at_attachment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Atachment extends Element{

        public enum ContentType{
            @JsonProperty("application/jpg") image_jpg,
            @JsonProperty("image/apng") image_apng,
            @JsonProperty("image/bmp") image_bmp,
            @JsonProperty("image/avif") image_avif,
            @JsonProperty("image/gif") image_gif,
            @JsonProperty("image/jpeg") image_jpeg,
            @JsonProperty("image/png") image_png,
            @JsonProperty("image/svg+xml") image_svg_xml,
            @JsonProperty("image/webp") image_webp,
            @JsonProperty("image/x-icon") image_x_icon,
            @JsonProperty("image/vnd.microsoft.icon") image_vnd_microsoft_icon,
            @JsonProperty("image/tiff") image_tiff
        }


        @Enumerated(EnumType.STRING)
        @Column(name="at_content_type", columnDefinition = "LONGTEXT")
        private ContentType contentType = ContentType.image_jpeg;

        @Enumerated(EnumType.STRING)
        @Column(name="at_language")
        private Resource.LANGUAGE_CODE language = Resource.LANGUAGE_CODE.de_AT;

        @Lob
        @Column(name="at_data", columnDefinition = "LONGTEXT")
        private String data;

        @Column(name="at_size")
        private Integer size;

        @Column(name="at_hash")
        private String hash;

        @Column(name="at_creation")
        private LocalDateTime creation;

        @Min(1)
        @Column(name="at_height")
        private Integer height;

        @Min(1)
        @Column(name="at_width")
        private Integer width;

    }


