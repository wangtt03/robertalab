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
    private User user;

    @Column(name = "GROUP_ID")
    private Group group;

    protected UserGroup() {
        // Hibernate
    }

    protected UserGroup(User user, Group group) {
        this.user = user;
        this.group = group;
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

    public User getUser() {
        return this.user;
    }

    public Group getGroup() {
        return this.group;
    }

    @Override
    public String toString() {
        return "UserGroup [id=" + this.id + ", user=" + this.user + ", group=" + this.group;
    }

}
