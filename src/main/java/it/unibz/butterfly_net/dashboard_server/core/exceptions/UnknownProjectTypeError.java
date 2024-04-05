package it.unibz.butterfly_net.dashboard_server.core.exceptions;

public class UnknownProjectTypeError extends RuntimeException {
    public UnknownProjectTypeError(String type) {
        super(String.format("Unknown project type %s", type));
    }
}
