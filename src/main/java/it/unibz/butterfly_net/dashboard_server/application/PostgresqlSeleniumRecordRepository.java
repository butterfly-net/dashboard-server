package it.unibz.butterfly_net.dashboard_server.application;

import it.unibz.butterfly_net.dashboard_server.core.model.SeleniumRecord;
import it.unibz.butterfly_net.dashboard_server.core.repositories.SeleniumRecordRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class PostgresqlSeleniumRecordRepository extends PostgresqlRepository implements SeleniumRecordRepository {
    @Override
    public SeleniumRecord create(Long projectId, Long timestamp, String pagePath, String issues) {
        String insertQuery = "INSERT INTO " +
                "SELENIUM_RECORDS(project_id, timestamp, page_path, issues) " +
                "VALUES (?, ?, ?, ?)";

        String readQuery = "SELECT * " +
                "FROM SELENIUM_RECORDS " +
                "ORDER BY id DESC " +
                "LIMIT 1";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            logger.info(preparedStatement.toString());
            preparedStatement.setLong(1, projectId);
            preparedStatement.setLong(2, timestamp);
            preparedStatement.setString(3, pagePath);
            preparedStatement.setString(4, issues);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected <= 0) {
                String message = String.format("Failed to insert selenium record: %d @ %d", projectId, timestamp);
                logger.error(message);
                throw new RuntimeException(message);
            }

            String message = String.format("Inserted selenium record: %d @ %d", projectId, timestamp);
            logger.info(message);

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(readQuery);

            if (!resultSet.next()) {
                String error = "Couldn't read inserted data";
                logger.error(error);
                throw new RuntimeException(error);
            }

            return new SeleniumRecord(
                    resultSet.getLong("id"),
                    resultSet.getLong("project_id"),
                    resultSet.getLong("timestamp"),
                    resultSet.getString("page_path"),
                    resultSet.getString("issues")
            );
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<SeleniumRecord> findByProjectId(Long projectId) {
        String query = "SELECT * FROM SELENIUM_RECORDS " +
                "WHERE project_id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, projectId);
            ResultSet resultSet = preparedStatement.executeQuery();
            Set<SeleniumRecord> seleniumRecords = new HashSet<>();

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                long timestamp = resultSet.getLong("timestamp");
                String pagePath = resultSet.getString("page_path");
                String issues = resultSet.getString("issues");
                seleniumRecords.add(new SeleniumRecord(
                        id, projectId, timestamp,
                        pagePath, issues
                ));
            }
            return seleniumRecords;
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
