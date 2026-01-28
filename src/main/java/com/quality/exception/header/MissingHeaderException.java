package com.quality.exception.header;

import com.quality.validation.ErrorCodeConstants;
import com.quality.validation.HeaderConstants;

/**
 * Exception thrown when a required header is missing.
 */
public class MissingHeaderException extends HeaderException {

    public MissingHeaderException(String headerName, String invalidValue) {
        super(
                "Missing " + headerName + " header",
                headerName,
                invalidValue != null ? invalidValue : "Header value is missing or null",
                ErrorCodeConstants.TYPE_CODE_HEADER,
                ErrorCodeConstants.TYPE_HEADER,
                ErrorCodeConstants.SUBTYPE_CODE_MISSING,
                ErrorCodeConstants.SUBTYPE_MISSING
        );
    }

    @Override
    public String getCorrectFormat() {
        return HeaderConstants.CORRECT_FORMAT_MESSAGE;
    }

    @Override
    public String getDocumentationUrl() {
        return ErrorCodeConstants.DOCUMENTATION_BASE_URL + ErrorCodeConstants.SUBTYPE_CODE_MISSING;
    }
}
