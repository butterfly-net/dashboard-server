package it.unibz.butterfly_net.dashboard_server.core;

import it.unibz.butterfly_net.dashboard_server.core.model.SeleniumRecord;
import it.unibz.butterfly_net.dashboard_server.core.repositories.SeleniumRecordRepository;

public interface ObservableSeleniumRecordRepository extends SeleniumRecordRepository {
    SeleniumRecord getLast();
}
