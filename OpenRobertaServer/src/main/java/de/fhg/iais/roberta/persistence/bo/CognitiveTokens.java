package de.fhg.iais.roberta.persistence.bo;

/**
 * Created by t-zhhong on 2017/7/10.
 */

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "COGNITIVE_TOKENS")
public class CognitiveTokens implements WithSurrogateId{
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "DOMAIN")
    private String domain;

    @Column(name = "TOKEN")
    private String token;

    protected CognitiveTokens(){

    }

    public CognitiveTokens(String domain, String token){
        this.domain = domain;
        this.token = token;
    }

    @Override
    public int getId(){
        return id;
    }

    public String getDomain(){
        return domain;
    }

    public String getToken(){
        return token;
    }

    public void setDomain(String domain){
        this.domain = domain;
    }

    public void setToken(String token){
        this.token = token;
    }

    @Override
    public String toString(){
        return "CognitiveTokens[ Domain: " + this.domain + ", Token: " + this.token + " ]";
    }
}