package it.unibz.butterfly_net.dashboard_server.core.repositories;

import it.unibz.butterfly_net.dashboard_server.core.model.SeleniumRecord;

public interface SeleniumRecordRepository {
    SeleniumRecord create(Long projectId, Long timestamp, String pagePath, String issues);
    SeleniumRecord findByProjectId(Long projectId);
}
