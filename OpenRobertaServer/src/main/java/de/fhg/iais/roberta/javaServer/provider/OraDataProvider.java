package de.fhg.iais.roberta.javaServer.provider;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.sun.jersey.api.model.Parameter;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.persistence.UserProcessor;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.dao.UserDao;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.util.RobertaProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@Provider
public class OraDataProvider implements InjectableProvider<OraData, Parameter> {
    private static final Logger LOG = LoggerFactory.getLogger(OraDataProvider.class);
    private static final String OPEN_ROBERTA_STATE = "openRobertaState";
    private static final AtomicLong SESSION_COUNTER = new AtomicLong();

    public OraDataProvider() {
    }

    @Override
    public ComponentScope getScope() {
        return ComponentScope.PerRequest;
    }

    @Context
    private HttpServletRequest servletRequest;

    @Inject
    private RobotCommunicator robotCommunicator;

    @Inject
    @Named("robotPluginMap")
    private Map<String, IRobotFactory> robotPluginMap;

    @Inject
    private SessionFactoryWrapper sessionFactoryWrapper;

    @Override
    public Injectable<?> getInjectable(ComponentContext ic, OraData a, Parameter p) {
        if ( HttpSessionState.class.isAssignableFrom(p.getParameterClass()) ) {
            return getInjectableHttpSessionState();
        } else if ( DbSession.class.isAssignableFrom(p.getParameterClass()) ) {
            return getInjectableDbSessionState();
        } else {
            return null;
        }
    }

    private Injectable<DbSession> getInjectableDbSessionState() {
        return new Injectable<DbSession>() {
            @Override
            public DbSession getValue() {
                return OraDataProvider.this.sessionFactoryWrapper.getSession();
            }
        };
    }

    private Injectable<HttpSessionState> getInjectableHttpSessionState() {
        return new Injectable<HttpSessionState>() {

            @Override
            public HttpSessionState getValue() {
                HttpSession httpSession = OraDataProvider.this.servletRequest.getSession(true);
                HttpSessionState httpSessionState = (HttpSessionState) httpSession.getAttribute(OPEN_ROBERTA_STATE);

                if ( httpSessionState == null ) {
                    long sessionNumber = SESSION_COUNTER.incrementAndGet();
                    LOG.info("session #" + sessionNumber + " created");
                    httpSessionState = HttpSessionState.init(OraDataProvider.this.robotCommunicator, OraDataProvider.this.robotPluginMap, sessionNumber);
                    httpSession.setAttribute(OPEN_ROBERTA_STATE, httpSessionState);
                }

                User user = getAuthenticatedUser(OraDataProvider.this .sessionFactoryWrapper.getSession(), httpSessionState);
                if (user != null) {
                    httpSessionState.setUserClearDataKeepTokenAndRobotId(user.getId());
                }
                return httpSessionState;
            }
        };
    }

    private User getAuthenticatedUser(DbSession dbSession, HttpSessionState httpSessionState){
        LOG.debug("in getAuthenticatedUser");
        Cookie[] cookies = OraDataProvider.this.servletRequest.getCookies();
        boolean authenticated = false;
        for (Cookie c: cookies) {
            if (c.getName().equals("Auth")) {
                LOG.debug("auth cookie found.");
                String token = c.getValue();
                String userId = verifyJWTToken(token);
                if (userId != null && !userId.equals("")){
                    LOG.debug("userId: " + userId);
                    //get user from stemweb service.
                    UserProcessor up = new UserProcessor(dbSession, httpSessionState);
                    User user = up.getUser(userId);
                    if (user == null) {
                        try {
                            up.createUser(userId, UUID.randomUUID().toString(), "name", "STUDENT", "email", "tag", true);
                            dbSession.commit();
                            OraDataProvider.this.sessionFactoryWrapper.getSession().commit();
                            user = up.getUser(userId);
                            return user;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        return user;
                    }
                }
                break;
            }
        }

        return null;
    }

    private static String verifyJWTToken(String token) {
        String userId = "";
        try {
            String key = RobertaProperties.getStringProperty("jwt.key");
            String secret = RobertaProperties.getStringProperty("jwt.secret");

            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
            userId = jwt.getClaim("userId").asString();
        } catch (UnsupportedEncodingException exception){
            //UTF-8 encoding not supported
        } catch (JWTVerificationException exception){
            //Invalid signature/claims
        }

        return userId;
    }
}