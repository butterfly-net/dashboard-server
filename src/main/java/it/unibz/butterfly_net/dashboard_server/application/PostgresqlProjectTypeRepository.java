package it.unibz.butterfly_net.dashboard_server.application;

import it.unibz.butterfly_net.dashboard_server.core.model.ProjectType;
import it.unibz.butterfly_net.dashboard_server.core.repositories.ProjectTypeRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PostgresqlProjectTypeRepository extends PostgresqlRepository implements ProjectTypeRepository {
    @Override
    public ProjectType findByProjectId(Long projectId) {
        String query = "SELECT * FROM TYPES " +
                "WHERE project_id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, projectId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                String error = String.format("Project #%d has no type", projectId);
                logger.error(error);
                throw new RuntimeException(error);
            }

            String type = resultSet.getString("type");
            return new ProjectType(projectId, type);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
