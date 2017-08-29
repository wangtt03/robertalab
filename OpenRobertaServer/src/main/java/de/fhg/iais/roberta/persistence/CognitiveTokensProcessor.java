package de.fhg.iais.roberta.persistence.util;

import de.fhg.iais.roberta.persistence.AbstractProcessor;
import de.fhg.iais.roberta.persistence.bo.CognitiveTokens;
import de.fhg.iais.roberta.persistence.dao.CognitiveTokensDao;
import de.fhg.iais.roberta.util.Key;

/**
 * Created by t-zhhong on 2017/7/10.
 */
public class CognitiveTokensProcessor extends AbstractProcessor {
    public CognitiveTokensProcessor(DbSession dbSession, HttpSessionState httpSessionState){
        super(dbSession, httpSessionState);
    }

    public String getTokenByDomain(String domain){
        CognitiveTokensDao cognitiveTokensDao = new CognitiveTokensDao(this.dbSession);
        String token = cognitiveTokensDao.getTokenByDomain(domain);
        if(domain == null){
            System.out.println("Error GET_TOKEN_BY_DOMAIN, domain: " + domain);
            return null;
        }
        else{
            System.out.println("Success GET_TOKEN_BY_DOMAIN, domain: " + domain);
            return token;
        }
    }
}