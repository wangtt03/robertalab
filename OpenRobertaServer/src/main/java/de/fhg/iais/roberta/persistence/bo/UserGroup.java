package de.fhg.iais.roberta.persistence.bo;

<<<<<<< 8392e1edb6414b520c125e1942246b8ba5392952
=======
import java.sql.Timestamp;

>>>>>>> added classes describing group and userGroup tables
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
<<<<<<< 8392e1edb6414b520c125e1942246b8ba5392952
=======
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
>>>>>>> added classes describing group and userGroup tables
import javax.persistence.Table;

@Entity
@Table(name = "USER_GROUP")
public class UserGroup implements WithSurrogateId {
    @Id
<<<<<<< 8392e1edb6414b520c125e1942246b8ba5392952
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "USER_ID")
    private int userID;

    @Column(name = "GROUP_ID")
    private int groupID;

=======
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
  
>>>>>>> added classes describing group and userGroup tables
    protected UserGroup() {
        // Hibernate
    }

    /**
     * assign a new user group
     *
     * @param user id - id of the user whom we assign the group
     * @param group id - id of the group that is being assignedsk
     */
<<<<<<< 8392e1edb6414b520c125e1942246b8ba5392952
=======
  
>>>>>>> added classes describing group and userGroup tables

    @Override
    public int getId() {
        return this.id;
    }

    public int getUserID() {
        return this.userID;
    }
<<<<<<< 8392e1edb6414b520c125e1942246b8ba5392952

=======
    
>>>>>>> added classes describing group and userGroup tables
    public int getGroupID() {
        return this.groupID;
    }

}
