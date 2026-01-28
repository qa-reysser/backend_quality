package com.quality.exception.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Complete error detail structure for resource not found errors (404).
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class ErrorDetailResource {
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS")
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String typeCode;
    private String type;
    private String subtypeCode;
    private String subtype;
    private ErrorDetailsResourceInfo details;
    private String path;
    private String documentationUrl;

    @JsonProperty("_links")
    private Map<String, ErrorLink> links;
}
