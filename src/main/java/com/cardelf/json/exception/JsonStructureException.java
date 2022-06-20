package com.cardelf.json.exception;

/**
 * Json解析异常
 *
 * @author bluecrush
 */
public class JsonStructureException extends Exception {

    private static final long serialVersionUID = -7044122328089687043L;

    /**
     * Instantiates a new Json parse exception.
     *
     * @param message the message
     */
    public JsonStructureException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Json parse exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public JsonStructureException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
