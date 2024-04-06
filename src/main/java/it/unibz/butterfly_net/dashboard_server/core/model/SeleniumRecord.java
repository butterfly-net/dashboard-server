package it.unibz.butterfly_net.dashboard_server.core.model;

public record SeleniumRecord(
        Long id,
        Long projectId,
        Long timestamp,
        String pagePath,
        String issues
) implements ProcessedData {
}
