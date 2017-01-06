package de.fhg.iais.roberta.persistence.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USER_GROUP")
public class UserGroup implements WithSurrogateId {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "USER_ID")
    private int userID;

    @Column(name = "GROUP_ID")
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
