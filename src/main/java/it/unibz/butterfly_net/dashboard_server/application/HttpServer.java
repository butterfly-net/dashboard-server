package it.unibz.butterfly_net.dashboard_server.application;

import io.javalin.Javalin;
import it.unibz.butterfly_net.dashboard_server.core.Dashboard;
import it.unibz.butterfly_net.dashboard_server.core.model.ProcessedData;
import it.unibz.butterfly_net.dashboard_server.core.utils.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Set;

public class HttpServer implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(HttpServer.class);
    private final int PORT;
    private final Dashboard dashboard;

    public HttpServer(Dashboard dashboard) {
        this.dashboard = dashboard;
        try {
            String configPort = Config.getInstance().property("SERVER_PORT");
            PORT = Integer.parseInt(configPort);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        logger.info("running");
        Javalin app = Javalin.create();

        app.before(ctx -> {
            String message = String.format("%s %s", ctx.method(), ctx.path());
            logger.info(message);
        });

        app.get("/projects/{projectId}", ctx -> {
            String projectIdParam = ctx.pathParam("projectId");
            long projectId = Long.parseLong(projectIdParam);
            Set<ProcessedData> projectData = dashboard.fetchProjectData(projectId);
            ctx.json(projectData);
        });

        app.start(PORT);
    }
}
