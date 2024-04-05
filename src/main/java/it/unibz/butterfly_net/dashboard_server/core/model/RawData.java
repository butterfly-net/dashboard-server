package it.unibz.butterfly_net.dashboard_server.core.model;

public record RawData(
        ProjectType projectType,
        Long timestamp,
        String content
) {
}
