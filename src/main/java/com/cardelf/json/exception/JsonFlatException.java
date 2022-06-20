package com.cardelf.json.exception;

/**
 * Json解析异常
 *
 * @author bluecrush
 */
public class JsonFlatException extends Exception {

    private static final long serialVersionUID = -7044122328089687043L;

    /**
     * Instantiates a new Json parse exception.
     *
     * @param message the message
     */
    public JsonFlatException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Json parse exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public JsonFlatException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
