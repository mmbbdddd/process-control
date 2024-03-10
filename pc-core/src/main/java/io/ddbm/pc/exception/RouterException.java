package io.ddbm.pc.exception;

import lombok.Getter;


@Getter
public class RouterException extends FlowException {
    private final String router;

    private final String message;

    public RouterException(String router, String message) {
        this.router = router;
        this.message = message;
    }
}
