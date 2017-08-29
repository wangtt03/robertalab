package de.fhg.iais.roberta.javaServer.restServices.all;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.fhg.iais.roberta.javaServer.provider.OraData;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.util.AliveData;
import de.fhg.iais.roberta.util.ClientLogger;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.VersionChecker;

@Path("/{version:([^/]+/)?}ping")
public class ClientPing {
    private static final Logger LOG = LoggerFactory.getLogger(ClientPing.class);

    private static final int EVERY_REQUEST = 100; // after arrival of EVERY_PING many ping requests , a log entry is written
    private static final AtomicInteger pingCounterForLogging = new AtomicInteger(0);

    private final String openRobertaServerVersion;
    private final RobotCommunicator brickCommunicator;

    @Inject
    public ClientPing(@Named("openRobertaServer.version") String openRobertaServerVersion, RobotCommunicator brickCommunicator) {
        this.openRobertaServerVersion = openRobertaServerVersion;
        this.brickCommunicator = brickCommunicator;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response handle(@OraData HttpSessionState httpSessionState, JSONObject fullRequest, @PathParam("version") String version) throws Exception {
        VersionChecker.checkRestVersion(version);
        AliveData.rememberClientCall();
        MDC.put("sessionId", String.valueOf(httpSessionState.getSessionNumber()));
        MDC.put("userId", String.valueOf(httpSessionState.getUserId()));
        MDC.put("robotName", String.valueOf(httpSessionState.getRobotName()));
        int logLen = new ClientLogger().log(LOG, fullRequest);
        int counter = pingCounterForLogging.incrementAndGet();

        if ( counter % EVERY_REQUEST == 0 ) {
            LOG.info("/ping [count:" + counter + "]");
        }
        Date date = new Date();
        JSONObject response =
            new JSONObject().put("version", this.openRobertaServerVersion).put("date", date.getTime()).put("dateAsString", date.toString()).put(
                "logged",
                logLen);
        Util.addFrontendInfo(response, httpSessionState, this.brickCommunicator);
        MDC.clear();
        return Response.ok(response).build();
    }
}