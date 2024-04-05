package it.unibz.butterfly_net.dashboard_server.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class PostgresqlRepository {
    protected Logger logger = LoggerFactory.getLogger(PostgresqlRepository.class);
    protected final Connection connection;

    public PostgresqlRepository() {
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
