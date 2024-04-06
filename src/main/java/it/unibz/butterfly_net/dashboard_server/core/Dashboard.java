package it.unibz.butterfly_net.dashboard_server.core;

import it.unibz.butterfly_net.dashboard_server.core.model.ProcessedData;
import it.unibz.butterfly_net.dashboard_server.core.model.ProjectType;
import it.unibz.butterfly_net.dashboard_server.core.repositories.ProjectTypeRepository;
import it.unibz.butterfly_net.dashboard_server.core.repositories.SeleniumRecordRepository;

import java.util.Collections;
import java.util.Set;

public class Dashboard {
    private final SeleniumRecordRepository seleniumRecordRepository;
    private final ProjectTypeRepository projectTypeRepository;

    public Dashboard(SeleniumRecordRepository seleniumRecordRepository, ProjectTypeRepository projectTypeRepository) {
        this.seleniumRecordRepository = seleniumRecordRepository;
        this.projectTypeRepository = projectTypeRepository;
    }

    public Set<ProcessedData> fetchProjectData(Long projectId) {
        ProjectType projType = projectTypeRepository.findByProjectId(projectId);

        Set<ProcessedData> processedData;

        switch (projType.type()) {
            case ProjectType.SELENIUM -> processedData = fetchSeleniumData(projectId);
            default -> processedData = Set.of();
        }

        return processedData;
    }

    private Set<ProcessedData> fetchSeleniumData(Long projectId) {
        return Collections.unmodifiableSet(seleniumRecordRepository.findByProjectId(projectId));
    }
}
