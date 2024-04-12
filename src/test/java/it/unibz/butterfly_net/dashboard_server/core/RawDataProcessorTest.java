package it.unibz.butterfly_net.dashboard_server.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.unibz.butterfly_net.dashboard_server.core.exceptions.UnknownProjectTypeError;
import it.unibz.butterfly_net.dashboard_server.core.model.ProjectType;
import it.unibz.butterfly_net.dashboard_server.core.model.RawData;
import it.unibz.butterfly_net.dashboard_server.core.model.SeleniumRecord;
import it.unibz.butterfly_net.dashboard_server.core.repositories.RawDataRepository;
import it.unibz.butterfly_net.dashboard_server.core.repositories.SeleniumRecordRepository;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RawDataProcessorTest {
    private RawDataProcessor underTest;

    @Test
    void givenSeleniumProject_itStoresSeleniumRecord() throws JsonProcessingException {
        // given
        Long pid = 1L;
        Long ts = 12341L;
        String path = "/home";
        String issues = "foo bar baz";
        String mockContent = mockContent(path, issues);
        RawData mockData = new RawData(new ProjectType(pid, ProjectType.SELENIUM), ts, mockContent);
        ObservableSeleniumRecordRepository seleniumRepo = observableSeleniumRepo();
        underTest = new RawDataProcessor(
                mockRawDataRepo(mockData),
                seleniumRepo
        );

        // when
        assertDoesNotThrow(() -> underTest.run("test", "test"));

        // then
        SeleniumRecord lastSeleniumRecord = seleniumRepo.getLast();
        assertNotNull(lastSeleniumRecord);
        assertEquals(pid, lastSeleniumRecord.projectId());
        assertEquals(ts, lastSeleniumRecord.timestamp());
        assertEquals(path, lastSeleniumRecord.pagePath());
        assertEquals(issues, lastSeleniumRecord.issues());
    }

    @Test
    void givenUnknownProject_itThrows() {
        // given
        underTest = new RawDataProcessor(
                mockRawDataRepo(new RawData(new ProjectType(1L, "unknown"), 412L, "anything")),
                mockSeleniumRepo()
        );

        // when ... then
        assertThrows(
                UnknownProjectTypeError.class,
                () -> underTest.run("test", "test")
        );
    }

    private String mockContent(String page, String issues) {
        String template = "{" +
                "\"queryParams\":{}," +
                "\"body\":{\"pagePath\":\"%s\",\"issues\":\"%s\"}," +
                "\"headers\":{\"Authentication-Key\":\"abcd-1234\",\"Connection\":\"Upgrade, HTTP2-Settings\",\"User-Agent\":\"Java-http-client/17.0.9\",\"Host\":\"localhost:8884\",\"Project-Id\":\"1\",\"HTTP2-Settings\":\"AAEAAEAAAAIAAAABAAMAAABkAAQBAAAAAAUAAEAA\",\"Content-Length\":\"42\",\"Upgrade\":\"h2c\"}" +
                "}";
        return String.format(template, page, issues);
    }

    private SeleniumRecordRepository mockSeleniumRepo() {
        return null;
    }

    private ObservableSeleniumRecordRepository observableSeleniumRepo() {
        return new ObservableSeleniumRecordRepository() {
            private SeleniumRecord last;
            private Long nextId = 1L;

            @Override
            public SeleniumRecord getLast() {
                return last;
            }

            @Override
            public SeleniumRecord create(Long projectId, Long timestamp, String pagePath, String issues) {
                SeleniumRecord record = new SeleniumRecord(nextId++, projectId, timestamp, pagePath, issues);
                last = record;
                return record;
            }

            @Override
            public Set<SeleniumRecord> findByProjectId(Long projectId) {
                return null;
            }
        };
    }

    private RawDataRepository mockRawDataRepo(RawData mockData) {
        return () -> mockData;
    }
}