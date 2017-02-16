package de.fhg.iais.roberta.javaServer.restServices.all;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.google.inject.Inject;

import de.fhg.iais.roberta.javaServer.provider.OraData;
import de.fhg.iais.roberta.persistence.GroupProcessor;
import de.fhg.iais.roberta.persistence.UserGroupProcessor;
import de.fhg.iais.roberta.persistence.bo.Group;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.util.AliveData;
import de.fhg.iais.roberta.util.ClientLogger;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.Util1;

@Path("/usergroups")
public class ClientGroup {
    private static final Logger LOG = LoggerFactory.getLogger(ClientGroup.class);

    private final RobotCommunicator brickCommunicator;

    @Inject
    public ClientGroup(RobotCommunicator brickCommunicator) {
        this.brickCommunicator = brickCommunicator;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response command(@OraData HttpSessionState httpSessionState, @OraData DbSession dbSession, JSONObject fullRequest) throws Exception {
        AliveData.rememberClientCall();
        MDC.put("sessionId", String.valueOf(httpSessionState.getSessionNumber()));
        MDC.put("userId", String.valueOf(httpSessionState.getUserId()));
        MDC.put("groupId", String.valueOf(httpSessionState.getGroupId()));
        new ClientLogger().log(ClientGroup.LOG, fullRequest);
        final int userId = httpSessionState.getUserId();
        final int groupId = httpSessionState.getGroupId();
        JSONObject response = new JSONObject();
        try {
            final JSONObject request = fullRequest.getJSONObject("data");
            final String cmd = request.getString("cmd");
            ClientGroup.LOG.info("command is: " + cmd + ", userId is " + userId);
            response.put("cmd", cmd);
            final GroupProcessor gp = new GroupProcessor(dbSession, httpSessionState);
            final UserGroupProcessor ugp = new UserGroupProcessor(dbSession, httpSessionState);

            final String groupName = request.getString("name");
            final int userToManageId = request.getInt("userToManage");

            Group group;
            switch ( cmd ) {
                case "addUser":
                    // add a user to an already existing group
                    ugp.persistUserGroup(userToManageId, groupId);
                    break;
                case "deleteUser":
                    ugp.deleteByIds(userToManageId, groupId);
                    break;
                case "createGroup":
                    group = gp.persistGroup(groupName, userId);
                    break;
                case "deleteGroup":
                    gp.deleteByName(groupName);
                    break;
                case "getGroup":
                    group = gp.getGroup(groupName);
                    break;
                default:
                    break;
            }

            if ( gp.isOk() && (cmd.equals("addUser") || cmd.equals("deleteUser")) ) {
                group = gp.getGroup(groupName);
                if ( group == null ) {
                    ClientGroup.LOG.error("TODO: check potential error: the saved group should never be null");
                }
            }

            Util.addResultInfo(response, gp);
            Util.addResultInfo(response, ugp);
        } catch ( final Exception e ) {
            dbSession.rollback();
            final String errorTicketId = Util1.getErrorTicketId();
            ClientGroup.LOG.error("Exception. Error ticket: " + errorTicketId, e);
            Util.addErrorInfo(response, Key.SERVER_ERROR).append("parameters", errorTicketId);
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
        Util.addFrontendInfo(response, httpSessionState, this.brickCommunicator);
        MDC.clear();
        return Response.ok(response).build();
    }
}