package it.unibz.butterfly_net.dashboard_server.core.repositories;

import it.unibz.butterfly_net.dashboard_server.core.model.RawData;

public interface RawDataRepository {
    RawData getLast();
}
