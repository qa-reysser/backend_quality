package com.quality.exception.header;

import com.quality.validation.ErrorCodeConstants;
import com.quality.validation.HeaderConstants;

/**
 * Exception thrown when a header value is too short.
 */
public class HeaderTooShortException extends HeaderException {

    public HeaderTooShortException(String headerName, String invalidValue) {
        super(
                headerName + " header is too short",
                headerName,
                invalidValue,
                ErrorCodeConstants.TYPE_CODE_HEADER,
                ErrorCodeConstants.TYPE_HEADER,
                ErrorCodeConstants.SUBTYPE_CODE_TOO_SHORT,
                ErrorCodeConstants.SUBTYPE_TOO_SHORT
        );
    }

    @Override
    public String getCorrectFormat() {
        return HeaderConstants.CORRECT_FORMAT_MESSAGE;
    }

    @Override
    public String getDocumentationUrl() {
        return ErrorCodeConstants.DOCUMENTATION_BASE_URL + ErrorCodeConstants.SUBTYPE_CODE_TOO_SHORT;
    }
}
