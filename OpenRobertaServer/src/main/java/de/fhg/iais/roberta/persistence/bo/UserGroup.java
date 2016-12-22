package de.fhg.iais.roberta.persistence.bo;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "USER_GROUP")
public class UserGroup implements WithSurrogateId {
    @Id
    @Column(name = "pk_USER_GROUP")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //TODO: check if it is really int. That might be an array.
    private int id;
    
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private int userID;

    @ManyToOne
    @JoinColumn(name = "GROUP_ID")
    private int groupID;
  
    protected UserGroup() {
        // Hibernate
    }

    /**
     * assign a new user group
     *
     * @param user id - id of the user whom we assign the group
     * @param group id - id of the group that is being assignedsk
     */
  

    @Override
    public int getId() {
        return this.id;
    }

    public int getUserID() {
        return this.userID;
    }
    
    public int getGroupID() {
        return this.groupID;
    }

}
