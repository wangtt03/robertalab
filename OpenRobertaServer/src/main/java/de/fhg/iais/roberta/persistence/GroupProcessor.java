package de.fhg.iais.roberta.persistence;

import java.util.List;

import de.fhg.iais.roberta.persistence.bo.Group;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.dao.GroupDao;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Util1;

public class GroupProcessor extends AbstractProcessor {

    public GroupProcessor(DbSession dbSession, HttpSessionState httpSessionState) {
        super(dbSession, httpSessionState);
    }

    /**
     * load a group from the data base.
     *
     * @param groupName the group to load
     * @param groupId - group id
     * @return the group; null, if no group was found
     */
    public Group getGroup(String groupName) {
        if ( !Util1.isValidJavaIdentifier(groupName) ) {
            setError(Key.GROUP_ERROR_NAME_INVALID, groupName);
            return null;
        } else {
            GroupDao groupDao = new GroupDao(this.dbSession);
            Group group = groupDao.loadGroup(groupName);
            if ( group != null ) {
                setSuccess(Key.GROUP_GET_ONE_SUCCESS);
                return group;
            } else {
                setError(Key.GROUP_GET_ONE_ERROR_NOT_FOUND);
                return null;
            }
        }
    }

    public List<Group> loadOwnerGroups(int owner) {
        GroupDao groupDao = new GroupDao(this.dbSession);
        List<Group> groups = groupDao.loadOwnerGroups(owner);
        if ( groups != null ) {
            setSuccess(Key.GROUP_GET_ALL_SUCCESS);
            return groups;
        } else {
            setError(Key.GROUP_GET_ONE_ERROR_NOT_FOUND);
            return null;
        }
    }

    /**
     * Find out who are in a group
     *
     * @param groupName the name of the group
     */
    public List<User> getGroupMembers(String groupName) {
        GroupDao groupDao = new GroupDao(this.dbSession);
        List<User> user = groupDao.loadMembersByGroup(groupName);
        setSuccess(Key.GROUP_GET_ALL_SUCCESS, "" + user);
        return user;
    }

    /**
     * Find to which groups the user belongs
     *
     * @param userName the user
     */
    public List<Group> getMemberGroups(String userName) {
        GroupDao groupDao = new GroupDao(this.dbSession);
        List<Group> groups = groupDao.loadGroupsByMember(userName);
        setSuccess(Key.GROUP_GET_ALL_SUCCESS, "" + groups);
        return groups;
    }

    /**
     * create a given group owned by a given user. Overwrites an existing group if mayExist == true.
     *
     * @param groupName the name of the program
     * @param userId the owner of the program
     * @throws Exception
     */
    public Group persistGroup(String groupName, int userId) throws Exception {
        if ( !Util1.isValidJavaIdentifier(groupName) ) {
            setError(Key.GROUP_ERROR_NAME_INVALID, groupName);
            return null;
        }
        if ( this.httpSessionState.isUserLoggedIn() ) {
            GroupDao groupDao = new GroupDao(this.dbSession);
            Group result;
            result = groupDao.persistGroup(groupName, userId);
            return result;
        } else {
            setError(Key.USER_ERROR_NOT_LOGGED_IN);
            return null;
        }
    }

    /**
     * delete a given group
     *
     * @param groupName the name of the program
     */
    public void deleteByName(String groupName) {
        GroupDao groupDao = new GroupDao(this.dbSession);
        int rowCount = groupDao.deleteByName(groupName);
        if ( rowCount > 0 ) {
            setSuccess(Key.GROUP_DELETE_SUCCESS);
        } else {
            setError(Key.GROUP_DELETE_ERROR);
        }
    }
}
