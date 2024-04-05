package it.unibz.butterfly_net.dashboard_server.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unibz.butterfly_net.dashboard_server.core.dtos.SeleniumPayload;
import it.unibz.butterfly_net.dashboard_server.core.exceptions.UnknownProjectTypeError;
import it.unibz.butterfly_net.dashboard_server.core.model.ProjectType;
import it.unibz.butterfly_net.dashboard_server.core.model.RawData;
import it.unibz.butterfly_net.dashboard_server.core.repositories.RawDataRepository;
import it.unibz.butterfly_net.dashboard_server.core.repositories.SeleniumRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RawDataProcessor {
    private final Logger logger = LoggerFactory.getLogger(RawDataProcessor.class);
    private final RawDataRepository rawDataRepository;
    private final SeleniumRecordRepository seleniumRecordRepository;

    public RawDataProcessor(RawDataRepository rawDataRepository, SeleniumRecordRepository seleniumRecordRepository) {
        this.rawDataRepository = rawDataRepository;
        this.seleniumRecordRepository = seleniumRecordRepository;
    }

    public void run(String name, String parameter) throws JsonProcessingException {
        String message = String.format("I have been called with: %s, %s", name, parameter);
        logger.info(message);

        RawData lastRecord = rawDataRepository.getLast();
        switch (lastRecord.projectType().type()) {
            case ProjectType.SELENIUM -> selenium(lastRecord);

            default -> {
                String unknownType = lastRecord.projectType().type();
                String error = String.format("Unknown type %s", unknownType);
                logger.error(error);
                throw new UnknownProjectTypeError(unknownType);
            }
        }
    }

    private void selenium(RawData lastRecord) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SeleniumPayload payload = mapper.readValue(lastRecord.content(), SeleniumPayload.class);
        seleniumRecordRepository.create(lastRecord.projectType().projectId(), lastRecord.timestamp(), payload.pagePath(), payload.issues());
    }
}
