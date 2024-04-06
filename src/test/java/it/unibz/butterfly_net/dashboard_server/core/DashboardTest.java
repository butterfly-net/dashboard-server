package it.unibz.butterfly_net.dashboard_server.core;

import it.unibz.butterfly_net.dashboard_server.core.model.ProcessedData;
import it.unibz.butterfly_net.dashboard_server.core.model.ProjectType;
import it.unibz.butterfly_net.dashboard_server.core.model.SeleniumRecord;
import it.unibz.butterfly_net.dashboard_server.core.repositories.ProjectTypeRepository;
import it.unibz.butterfly_net.dashboard_server.core.repositories.SeleniumRecordRepository;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DashboardTest {
    private Dashboard underTest;

    @Test
    void givenSeleniumProjectId_thenReturnsSeleniumRecords() {
        // given
        ProjectTypeRepository projTypeRepo = projectId -> new ProjectType(projectId, "selenium");
        SeleniumRecordRepository selenRecRepo = new SeleniumRecordRepository() {
            @Override
            public SeleniumRecord create(Long projectId, Long timestamp, String pagePath, String issues) throws SQLException {
                return null;
            }

            @Override
            public Set<SeleniumRecord> findByProjectId(Long projectId) {
                return Set.of(new SeleniumRecord(
                        1L, projectId, 13421L,
                        "/home", "foo bar baz"
                ));
            }
        };
        underTest = new Dashboard(selenRecRepo, projTypeRepo);

        // when
        Set<ProcessedData> processedData = underTest.fetchProjectData(42L);

        // then
        boolean allSelenium = processedData.stream().allMatch(data -> data instanceof SeleniumRecord);
        assertTrue(allSelenium);
    }
}