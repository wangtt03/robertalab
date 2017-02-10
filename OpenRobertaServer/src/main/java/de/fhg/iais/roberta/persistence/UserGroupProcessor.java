package de.fhg.iais.roberta.persistence;

import de.fhg.iais.roberta.persistence.bo.UserGroup;
import de.fhg.iais.roberta.persistence.dao.GroupDao;
import de.fhg.iais.roberta.persistence.dao.UserGroupDao;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.util.Key;

public class UserGroupProcessor extends AbstractProcessor {
    public UserGroupProcessor(DbSession dbSession, HttpSessionState httpSessionState) {
        super(dbSession, httpSessionState);
    }

    /**
     * load a userGroup from the data base.
     *
     * @param userId - user id
     * @param groupId - group id
     * @return the userGroup; null, if no group was found
     */
    public UserGroup getUserGroup(int userId, int groupId) {
        UserGroupDao userGroupDao = new UserGroupDao(this.dbSession);
        UserGroup userGroup = userGroupDao.loadUserGroup(userId, groupId);
        if ( userGroup != null ) {
            setSuccess(Key.USER_GROUP_GET_ONE_SUCCESS);
            return userGroup;
        } else {
            setError(Key.USER_GROUP_GET_ONE_ERROR_NOT_FOUND);
            return null;
        }
    }

    /**
     * create a given group owned by a given user. Overwrites an existing group if mayExist == true.
     *
     * @param groupName the name of the program
     * @param userId the owner of the program
     * @throws Exception
     */
    public UserGroup persistGroup(int userId, int groupId) throws Exception {
        if ( this.httpSessionState.isUserLoggedIn() ) {
            UserGroupDao userGroupDao = new UserGroupDao(this.dbSession);
            UserGroup result;
            result = userGroupDao.persistUserGroup(userId, groupId);
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
    public void deleteByName(String groupName, int ownerId) {
        GroupDao groupDao = new GroupDao(this.dbSession);
        int rowCount = groupDao.deleteByName(groupName);
        if ( rowCount > 0 ) {
            setSuccess(Key.GROUP_DELETE_SUCCESS);
        } else {
            setError(Key.GROUP_DELETE_ERROR);
        }
    }
}
