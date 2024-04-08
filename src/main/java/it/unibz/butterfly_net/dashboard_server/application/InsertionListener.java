package it.unibz.butterfly_net.dashboard_server.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.unibz.butterfly_net.dashboard_server.core.RawDataProcessor;
import org.postgresql.PGConnection;
import org.postgresql.PGNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class InsertionListener implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(InsertionListener.class);
    private final RawDataProcessor processor;

    public InsertionListener(RawDataProcessor processor) {
        this.processor = processor;
    }

    @Override
    public void run() {
        logger.info("running...");
        try {
            Connection connection = DatabaseConnection.getConnection();
            PGConnection pgConnection = connection.unwrap(PGConnection.class);

            Statement statement = connection.createStatement();
            statement.execute("LISTEN record_inserted");
            statement.close();

            while (true) {
                PGNotification[] notifications = pgConnection.getNotifications();

                if (notifications != null) {
                    for (PGNotification notif : notifications) {
                        String message = String.format("Received %s", notif.getName());
                        logger.debug(message);
                        processor.run(notif.getName(), notif.getParameter());
                    }
                }

                Thread.sleep(500);
            }
        } catch (SQLException sqle) {
            logger.error(sqle.getMessage());
            sqle.printStackTrace();
        } catch (InterruptedException ie) {
            logger.error(ie.getMessage());
            ie.printStackTrace();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
