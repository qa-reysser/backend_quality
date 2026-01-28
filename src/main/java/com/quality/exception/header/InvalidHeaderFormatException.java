package com.quality.exception.header;

import com.quality.validation.ErrorCodeConstants;
import com.quality.validation.HeaderConstants;

/**
 * Exception thrown when a header does not comply with UUID format.
 */
public class InvalidHeaderFormatException extends HeaderException {

    public InvalidHeaderFormatException(String headerName, String invalidValue) {
        super(
                "Invalid " + headerName + " header; does not comply with the UUID format",
                headerName,
                invalidValue,
                ErrorCodeConstants.TYPE_CODE_HEADER,
                ErrorCodeConstants.TYPE_HEADER,
                ErrorCodeConstants.SUBTYPE_CODE_INVALID_FORMAT,
                ErrorCodeConstants.SUBTYPE_INVALID_FORMAT
        );
    }

    @Override
    public String getCorrectFormat() {
        return HeaderConstants.CORRECT_FORMAT_MESSAGE;
    }

    @Override
    public String getDocumentationUrl() {
        return ErrorCodeConstants.DOCUMENTATION_BASE_URL + ErrorCodeConstants.SUBTYPE_CODE_INVALID_FORMAT;
    }
}
