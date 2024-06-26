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

import java.sql.SQLException;
import java.util.HashMap;

public class RawDataProcessor {
    private final Logger logger = LoggerFactory.getLogger(RawDataProcessor.class);
    private final RawDataRepository rawDataRepository;
    private final SeleniumRecordRepository seleniumRecordRepository;

    private record RequestContent(
            SeleniumPayload body,
            HashMap<String, Object> headers,
            HashMap<String, Object> queryParams
    ) {}

    public RawDataProcessor(RawDataRepository rawDataRepository, SeleniumRecordRepository seleniumRecordRepository) {
        this.rawDataRepository = rawDataRepository;
        this.seleniumRecordRepository = seleniumRecordRepository;
    }

    public void run(String name, String parameter) throws JsonProcessingException, SQLException {
        String message = String.format("I have been called with: %s, %s", name, parameter);
        logger.info(message);

        RawData lastRecord = rawDataRepository.getLast();
        switch (lastRecord.projectType().type()) {
            case ProjectType.SELENIUM -> selenium(lastRecord);

            default -> {
                String unknownType = lastRecord.projectType().type();
                String error = String.format("Unknown type \"%s\"", unknownType);
                logger.error(error);
                throw new UnknownProjectTypeError(unknownType);
            }
        }
    }

    private void selenium(RawData lastRecord) throws JsonProcessingException, SQLException {
        ObjectMapper mapper = new ObjectMapper();
        RequestContent request = mapper.readValue(lastRecord.content(), RequestContent.class);
        SeleniumPayload payload = request.body();
        seleniumRecordRepository.create(lastRecord.projectType().projectId(), lastRecord.timestamp(), payload.pagePath(), payload.issues());
    }
}
