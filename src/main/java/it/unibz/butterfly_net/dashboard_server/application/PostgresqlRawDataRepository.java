package it.unibz.butterfly_net.dashboard_server.application;

import it.unibz.butterfly_net.dashboard_server.core.model.ProjectType;
import it.unibz.butterfly_net.dashboard_server.core.model.RawData;
import it.unibz.butterfly_net.dashboard_server.core.repositories.RawDataRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PostgresqlRawDataRepository extends PostgresqlRepository implements RawDataRepository {
    @Override
    public RawData getLast() {
        String query = "SELECT RECORDS.project_id AS pid, TYPES.type AS type, content, timestamp " +
                "FROM RECORDS " +
                "INNER JOIN TYPES " +
                "ON RECORDS.project_id = TYPES.project_id " +
                "ORDER BY RECORDS.id DESC " +
                "LIMIT 1";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (!resultSet.next())
                throw new RuntimeException("No raw data");

            RawData data = new RawData(
                    new ProjectType(
                            resultSet.getLong("pid"),
                            resultSet.getString("type")),
                    resultSet.getLong("timestamp"),
                    resultSet.getString("content")
            );
            logger.info(data.toString());
            return data;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}