package it.unibz.butterfly_net.dashboard_server.core.repositories;

import it.unibz.butterfly_net.dashboard_server.core.model.ProjectType;

public interface ProjectTypeRepository {
    ProjectType findByProjectId(Long projectId);
}
