package de.fhg.iais.roberta.persistence;

import de.fhg.iais.roberta.persistence.bo.UserGroup;
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
    public UserGroup getUserGroup(String userName, String groupName) {
        UserGroupDao userGroupDao = new UserGroupDao(this.dbSession);
        UserGroup userGroup = userGroupDao.loadUserGroup(userName, groupName);
        if ( userGroup != null ) {
            setSuccess(Key.USER_GROUP_GET_ONE_SUCCESS);
            return userGroup;
        } else {
            setError(Key.USER_GROUP_GET_ONE_ERROR_NOT_FOUND);
            return null;
        }
    }

    /**
     * create a given user-group connection. Overwrites an existing group if mayExist == true.
     *
     * @param userId the id of the user
     * @param groupId the id of the group
     * @throws Exception
     */
    public UserGroup persistUserGroup(String userName, String groupName) throws Exception {
        UserGroupDao userGroupDao = new UserGroupDao(this.dbSession);
        UserGroup result;
        result = userGroupDao.persistUserGroup(userName, groupName);
        return result;
    }

    /**
     * delete a given group
     *
     * @param groupName the name of the program
     */
    public void delete(String userName, String groupName) {
        UserGroupDao userGroupDao = new UserGroupDao(this.dbSession);
        int rowCount = userGroupDao.deleteByIds(userName, groupName);
        if ( rowCount > 0 ) {
            setSuccess(Key.GROUP_DELETE_SUCCESS);
        } else {
            setError(Key.GROUP_DELETE_ERROR);
        }
    }
}
