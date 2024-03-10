package io.ddbm.pc.exception;

public class NoSuchRouterException extends RuntimeException {
    private final String router;

    public NoSuchRouterException(String router) {
        this.router = router;
    }
}
