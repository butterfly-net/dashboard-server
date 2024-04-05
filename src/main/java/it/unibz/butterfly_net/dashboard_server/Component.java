package it.unibz.butterfly_net.dashboard_server;

import it.unibz.butterfly_net.dashboard_server.application.DatabaseConnection;
import it.unibz.butterfly_net.dashboard_server.application.InsertionListener;
import it.unibz.butterfly_net.dashboard_server.application.PostgresqlRawDataRepository;
import it.unibz.butterfly_net.dashboard_server.application.PostgresqlSeleniumRecordRepository;
import it.unibz.butterfly_net.dashboard_server.core.RawDataProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class Component {
    private static final Logger logger = LoggerFactory.getLogger(Component.class);

    public static void main(String[] args) throws SQLException {
        logger.info("Running...");

        RawDataProcessor processor = new RawDataProcessor(
                new PostgresqlRawDataRepository(),
                new PostgresqlSeleniumRecordRepository()
        );

        Connection connection = DatabaseConnection.getConnection();
        InsertionListener listener = new InsertionListener(connection, processor);
        listener.run();
    }
}
