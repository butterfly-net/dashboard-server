package it.unibz.butterfly_net.dashboard_server;

import it.unibz.butterfly_net.dashboard_server.application.*;
import it.unibz.butterfly_net.dashboard_server.core.Dashboard;
import it.unibz.butterfly_net.dashboard_server.core.RawDataProcessor;
import it.unibz.butterfly_net.dashboard_server.core.repositories.ProjectTypeRepository;
import it.unibz.butterfly_net.dashboard_server.core.repositories.RawDataRepository;
import it.unibz.butterfly_net.dashboard_server.core.repositories.SeleniumRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Component {
    private static final Logger logger = LoggerFactory.getLogger(Component.class);
    private static final String TYPE_LISTENER = "listener";


    public static void main(String[] args) {
        String executionType = args[0];

        SeleniumRecordRepository seleniumRecordRepository = new PostgresqlSeleniumRecordRepository();
        ProjectTypeRepository projectTypeRepository = new PostgresqlProjectTypeRepository();
        RawDataRepository rawDataRepository = new PostgresqlRawDataRepository();

        boolean isRunningListener = Objects.equals(executionType, TYPE_LISTENER);
        if (isRunningListener) runListener(seleniumRecordRepository, rawDataRepository);
        else runServer(seleniumRecordRepository, projectTypeRepository);
    }

    public static void runServer(SeleniumRecordRepository seleniumRecordRepository, ProjectTypeRepository projectTypeRepository) {
        logger.info("Running listener");
        Dashboard dashboard = new Dashboard(seleniumRecordRepository, projectTypeRepository);
        new HttpServer(dashboard).run();
    }

    public static void runListener(SeleniumRecordRepository seleniumRecordRepository, RawDataRepository rawDataRepository) {
        logger.info("Running server");
        RawDataProcessor processor = new RawDataProcessor(rawDataRepository, seleniumRecordRepository);
        new InsertionListener(processor).run();
    }
}
