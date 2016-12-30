package de.fhg.iais.roberta.persistence.bo;

import java.sql.Timestamp;

import javax.persistence.Column;

//TODO: add messages about groups to html

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "GROUP")
public class Group implements WithSurrogateId {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "OWNER_ID")
    private int ownerId;

    @Column(name = "NAME")
    private String name;

    protected Group() {
        // Hibernate
    }

    /**
     * create a new group
     *
     * @param name the name of the group, not null
     * @param owner the user who created and thus owns the program
     */

    @Override
    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Timestamp getCreated() {
        // TODO Auto-generated method stub
        return null;
    }

    public int getOwnerId() {
        return this.ownerId;
    }

}
