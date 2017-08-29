package de.fhg.iais.roberta.persistence.dao;

import de.fhg.iais.roberta.persistence.bo.CognitiveTokens;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.util.dbc.Assert;
import org.hibernate.Query;

import java.util.List;

/**
 * Created by t-zhhong on 2017/7/10.
 */
public class CognitiveTokensDao extends AbstractDao<CognitiveTokens> {
    public CognitiveTokensDao(DbSession session){
        super(CognitiveTokens.class, session);
    }

    public CognitiveTokens persistCognitiveTokens(String domain, String token){
        Query hql = this.session.createQuery("from CognitiveTokens where domain=:domain");
        hql.setString("domain", domain);
        @SuppressWarnings("unchecked")
        CognitiveTokens cognitive = new CognitiveTokens(domain, token);
        List<CognitiveTokens> il = hql.list();
        for (int i = 0; i < il.size(); i++){
            this.session.delete(il.get(i));
        }
        this.session.save(cognitive);
        this.session.commit();
        return cognitive;
    }

    public boolean deleteCognitiveTokensByDomain(String domain){
        Query hql = this.session.createQuery("from CognitiveTokens where domain=:domain");
        hql.setString("domain", domain);
        @SuppressWarnings("unchecked")
        List<CognitiveTokens> il = hql.list();
        for (int i = 0; i < il.size(); i++){
            this.session.delete(il.get(i));
        }
        this.session.commit();
        return true;
    }

    public String getTokenByDomain(String domain){
        Query hql = this.session.createQuery("from CognitiveTokens where domain=:domain");
        hql.setString("domain", domain);
        @SuppressWarnings("unchecked")
        List<CognitiveTokens> il = hql.list();
        if(il.size() <= 0){
            return null;
        }
        else{
            return il.get(0).getToken();
        }
    }

    public List<CognitiveTokens> getAllTokens(){
        Query hql = this.session.createQuery("from CognitiveTokens");
        @SuppressWarnings("unchecked")
        List<CognitiveTokens> il = hql.list();
        return il;
    }
}
