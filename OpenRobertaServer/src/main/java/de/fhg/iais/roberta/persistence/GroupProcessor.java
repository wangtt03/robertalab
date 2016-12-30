package de.fhg.iais.roberta.persistence;

import java.util.List;

import org.codehaus.jettison.json.JSONArray;

import de.fhg.iais.roberta.persistence.bo.AccessRight;
import de.fhg.iais.roberta.persistence.bo.Group;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.dao.AccessRightDao;
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
    public Group getGroup(String groupName) {
        if ( !Util1.isValidJavaIdentifier(groupName) ) {
            setError(Key.GROUP_ERROR_ID_INVALID, groupName);
            return null;
        } else {
            final GroupDao groupDao = new GroupDao(this.dbSession);
            final Group group = groupDao.loadGroup(groupName);
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
    public JSONArray getProgramInfo(int ownerId) {
        UserDao userDao = new UserDao(this.dbSession);
        GroupDao groupDao = new GroupDao(this.dbSession);
        AccessRightDao accessRightDao = new AccessRightDao(this.dbSession);
        User owner = userDao.get(ownerId);
        List<Group> groups = groupDao.loadAll(owner);

        JSONArray programInfos = new JSONArray();
        for ( final Group group : groups ) {
            JSONArray programInfo = new JSONArray();
            programInfo.put(group.getName());
            programInfo.put(group.getOwnerId());
            List<AccessRight> accessRights = accessRightDao.loadAccessRightsByProgram(group);
        }
        setSuccess(Key.GROUP_GET_ALL_SUCCESS, "" + programInfos.length());
        return programInfos;
    }

    /**
     * Find out who are in a group
     *
     * @param groupName the name of the group
     * @param ownerId the owner of the group
     */
    public List<User> getProgramRelations(String groupName, int ownerId) {
        UserDao userDao = new UserDao(this.dbSession);
        GroupDao groupDao = new GroupDao(this.dbSession);
        User owner = userDao.get(ownerId);
        JSONArray relations = new JSONArray();
        List<User> user = groupDao.loadMembers(groupName);

        setSuccess(Key.GROUP_GET_ALL_SUCCESS, "" + user);
        return user;
    }

    //TODO: continue

    /**
     * insert or update a given group owned by a given user. Overwrites an existing group if mayExist == true.
     *
     * @param groupName the name of the program
     * @param userId the owner of the program
     */
    public Group persistProgramText(String groupName, int userId) {
        if ( !Util1.isValidJavaIdentifier(groupName) ) {
            setError(Key.GROUP_ERROR_ID_INVALID, groupName);
            return null;
        }
        if ( this.httpSessionState.isUserLoggedIn() ) {
            final UserDao userDao = new UserDao(this.dbSession);
            final GroupDao groupDao = new GroupDao(this.dbSession);
            final User user = userDao.get(userId);
            Pair<Key, Group> result;
            if ( isOwner ) {
                result = programDao.persistOwnProgram(programName, user, robot, programText, programTimestamp);
            } else {
                result = programDao.persistSharedProgramText(programName, user, robot, programText, programTimestamp);
            }
            // a bit strange, but necessary as Java has no N-tuple
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
    public void deleteByName(String groupName) {
        final GroupDao groupDao = new GroupDao(this.dbSession);
        final int rowCount = groupDao.deleteByName(groupName);
        if ( rowCount > 0 ) {
            setSuccess(Key.GROUP_DELETE_SUCCESS);
        } else {
            setError(Key.GROUP_DELETE_ERROR);
        }
    }
}
