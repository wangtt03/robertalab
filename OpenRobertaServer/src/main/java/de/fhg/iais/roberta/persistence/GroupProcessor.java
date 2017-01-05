package de.fhg.iais.roberta.persistence;

import java.util.List;

import org.codehaus.jettison.json.JSONArray;

import de.fhg.iais.roberta.persistence.bo.Group;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.dao.GroupDao;
import de.fhg.iais.roberta.persistence.dao.UserDao;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Pair;
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
    public Group getGroup(String groupName, int ownerId) {
        if ( !Util1.isValidJavaIdentifier(groupName) ) {
            setError(Key.GROUP_ERROR_ID_INVALID, groupName);
            return null;
        } else {
            GroupDao groupDao = new GroupDao(this.dbSession);
            Group group = groupDao.loadGroup(groupName, ownerId);
            if ( group != null ) {
                setSuccess(Key.GROUP_GET_ONE_SUCCESS);
                return group;
            } else {
                setError(Key.GROUP_GET_ONE_ERROR_NOT_FOUND);
                return null;
            }
        }
    }

    /**
     * Get information about all the groups which a user owns
     *
     * @param ownerId the owner of the program
     */
    public JSONArray getGroupInfo(int ownerId) {
        UserDao userDao = new UserDao(this.dbSession);
        GroupDao groupDao = new GroupDao(this.dbSession);
        User owner = userDao.get(ownerId);
        List<Group> groups = groupDao.loadAll(owner);
        JSONArray groupsInfos = new JSONArray();
        for ( final Group group : groups ) {
            JSONArray groupInfos = new JSONArray();
            groupInfos.put(group.getName());
            groupInfos.put(group.getOwnerId());
        }
        setSuccess(Key.GROUP_GET_ALL_SUCCESS, "" + groupsInfos.length());
        return groupsInfos;
    }

    /**
     * Find out who are in a group
     *
     * @param groupName the name of the group
     * @param ownerId the owner of the group
     */
    public List<User> getGroupRelations(String groupName, int ownerId) {
        UserDao userDao = new UserDao(this.dbSession);
        GroupDao groupDao = new GroupDao(this.dbSession);
        User owner = userDao.get(ownerId);
        JSONArray relations = new JSONArray();
        List<User> user = groupDao.loadMembers(groupName);

        setSuccess(Key.GROUP_GET_ALL_SUCCESS, "" + user);
        return user;
    }

    /**
     * create a given group owned by a given user. Overwrites an existing group if mayExist == true.
     *
     * @param groupName the name of the program
     * @param userId the owner of the program
     */
    public Group persistGroup(String groupName, int userId, boolean isOwner) {
        if ( !Util1.isValidJavaIdentifier(groupName) ) {
            setError(Key.GROUP_ERROR_ID_INVALID, groupName);
            return null;
        }
        if ( this.httpSessionState.isUserLoggedIn() ) {
            GroupDao groupDao = new GroupDao(this.dbSession);
            Pair<Key, Group> result;
            if ( isOwner ) {
                result = groupDao.persistOwnGroup(groupName, userId);
            } else {
                result = null;
            }
            if ( result.getFirst() == Key.PROGRAM_SAVE_SUCCESS ) {
                setSuccess(Key.PROGRAM_SAVE_SUCCESS);
            } else {
                setError(result.getFirst());
            }
            return result.getSecond();
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
    public void deleteByName(String groupName, int ownerId) {
        GroupDao groupDao = new GroupDao(this.dbSession);
        int rowCount = groupDao.deleteByName(groupName, ownerId);
        if ( rowCount > 0 ) {
            setSuccess(Key.PROGRAM_DELETE_SUCCESS);
        } else {
            setError(Key.PROGRAM_DELETE_ERROR);
        }
    }
}
