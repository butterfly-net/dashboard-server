package it.unibz.butterfly_net.dashboard_server.core.model;

public record ProjectType(
        Long projectId,
        String type
) {
    public static final String SELENIUM = "selenium";
}
