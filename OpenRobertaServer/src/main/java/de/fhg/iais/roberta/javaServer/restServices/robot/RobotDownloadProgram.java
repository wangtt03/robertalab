package de.fhg.iais.roberta.javaServer.restServices.robot;

import java.io.File;
import java.io.FileInputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import de.fhg.iais.roberta.robotCommunication.RobotCommunicationData;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.util.AliveData;
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * REST service for downloading user program
 */
@Path("/download")
public class RobotDownloadProgram {
    private static final Logger LOG = LoggerFactory.getLogger(RobotDownloadProgram.class);

    private final RobotCommunicator brickCommunicator;
    private final String pathToCrosscompilerBaseDir;

    @Inject
    public RobotDownloadProgram(RobotCommunicator brickCommunicator) {
        this.brickCommunicator = brickCommunicator;
        this.pathToCrosscompilerBaseDir = RobertaProperties.getTempDirForUserProjects();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response handle(JSONObject requestEntity) throws JSONException {
        AliveData.rememberRobotCall();
        try {
            String token = requestEntity.getString("token");
            LOG.info("/download - request for token " + token);
            RobotCommunicationData state = this.brickCommunicator.getState(token);
            String programName = state.getProgramName();

            String fileName = null;
            String filePath = null;

            // FIXME as the number of supported robot system grows, we should think about a better solution here :-D
            switch ( state.getFirmwareName() ) {
                case "lejos":
                    fileName = programName + ".jar";
                    filePath = this.pathToCrosscompilerBaseDir + token + "/target";
                    break;
                case "Nao":
                case "ev3dev":
                    fileName = programName + ".py";
                    filePath = this.pathToCrosscompilerBaseDir + token + "/src";
                    break;
                case "NXT":
                    fileName = programName + ".rxe";
                    filePath = this.pathToCrosscompilerBaseDir + token;
                    break;
                case "Arduino":
                    fileName = programName + ".ino.hex";
                    filePath = this.pathToCrosscompilerBaseDir + token + "/" + programName + "/target";
                    break;
                case "brickpi":
                    fileName = programName + ".py";
                    filePath = this.pathToCrosscompilerBaseDir + token + "/src";
                    break;
                default:
                    LOG.error("unsupported firmware name " + state.getFirmwareName());
                    return Response.serverError().build();
            }

            File resultDir = new File(filePath);
            if ( resultDir.isDirectory() ) {
                File resultFile = new File(resultDir, fileName);
                if ( resultFile.isFile() ) {
                    ResponseBuilder response = Response.ok(new FileInputStream(resultFile), MediaType.APPLICATION_OCTET_STREAM);
                    response.header("Content-Disposition", "attachment; filename=" + fileName);
                    response.header("Filename", fileName);
                    return response.build();
                } else {
                    LOG.error("upload error: file '" + resultFile.getName() + "' to upload to robot not found.");
                }
            } else {
                LOG.error("upload error: directory '" + resultDir.getName() + "' containing file to upload to robot not found.");
            }
            return Response.serverError().build();
        } catch ( Exception e ) {
            LOG.error("exception caught and rethrown", e);
            throw new DbcException("exception caught and rethrown", e);
        }
    }
}