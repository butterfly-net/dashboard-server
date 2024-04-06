package it.unibz.butterfly_net.dashboard_server.core.repositories;

import it.unibz.butterfly_net.dashboard_server.core.model.SeleniumRecord;

import java.sql.SQLException;
import java.util.Set;

public interface SeleniumRecordRepository {
    SeleniumRecord create(Long projectId, Long timestamp, String pagePath, String issues) throws SQLException;
    Set<SeleniumRecord> findByProjectId(Long projectId);
}
