package com.quality.exception.header;

import com.quality.validation.ErrorCodeConstants;
import com.quality.validation.HeaderConstants;

/**
 * Exception thrown when a header value is too long.
 */
public class HeaderTooLongException extends HeaderException {

    public HeaderTooLongException(String headerName, String invalidValue) {
        super(
                headerName + " header is too long",
                headerName,
                invalidValue,
                ErrorCodeConstants.TYPE_CODE_HEADER,
                ErrorCodeConstants.TYPE_HEADER,
                ErrorCodeConstants.SUBTYPE_CODE_TOO_LONG,
                ErrorCodeConstants.SUBTYPE_TOO_LONG
        );
    }

    @Override
    public String getCorrectFormat() {
        return HeaderConstants.CORRECT_FORMAT_MESSAGE;
    }

    @Override
    public String getDocumentationUrl() {
        return ErrorCodeConstants.DOCUMENTATION_BASE_URL + ErrorCodeConstants.SUBTYPE_CODE_TOO_LONG;
    }
}
