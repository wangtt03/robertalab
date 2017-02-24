/**
 * Rest calls to the server related to user operations (create user, login ...)
 * 
 * @module rest/program
 */
define([ 'exports', 'comm' ], function(exports, COMM) {

  

    /**
     * Retrieve group from server.
     * 
     * @param userGroupName
     *            {String} - name of the group
     * 
     * 
     */
    function getUserGroupFromServer(groupName, successFn) {
        COMM.json("/groupusers", {
            "cmd" : "getUserGroup",
            "userGroup" : userGroup,

        }, successFn, "get userGroup '" + userName + " " + groupName + "' from server");
    }

    exports.getUserGroupFromServer = getUserGroupFromServer;

    /**
     * Create user group to server.
     * 
     * @param groupName
     *            {String} - name of the group
     * @param userName
     *            {String} - name of the user
     */
    function createGroupToServer(accountName, successFn) {
        COMM.json("/groupusers", {
            "cmd" : "createUserGroup",
            "userName" : userName,
            "groupName" : groupName,
        }, successFn, "save user group '" + userName + " " + groupName + "'s to server");
    }

    exports.createGroupToServer = createGroupToServer;
    
    
    /**
     * Delete user group from the server.
     * 
     * @param groupName
     *            {String} - name of the group
     * @param userName
     *            {String} - name of the user
     * 
     */
    function deleteGroupOnServer(groupName, successFn) {
        COMM.json("/groupusers", {
            "cmd" : "deleteUserGroup",
            "userName" : userName,
            "groupName" : groupName,
        }, successFn, "delete group '" + userName + " " + groupName + "' on server");
    }

    exports.deleteGroupOnServer = deleteGroupOnServer;
    
    
    /**
     * Add a user to a group, i.e. create a link in a user-group table.
     * 
     * @param groupName
     *            {String} - name of the group
     *            
     *            * @param userName
     *            {String} - name of the user
     * 
     */
    function addUser(userName, groupName, successFn) {
        COMM.json("/groupusers", {
            "cmd" : "addUser",
            "userName" : userName,
            "groupName" : groupName,
        }, successFn, "add user '" + userName + "' to a group '" + groupNames + "'");
    }

    exports.addUser = addUser;
    
    /**
     * Add a user to a group, i.e. create a link in a user-group table.
     * 
     * @param groupName
     *            {String} - name of the group
     *            
     *            * @param userName
     *            {String} - name of the user
     * 
     */
    function deleteUser(userName, groupName, successFn) {
        COMM.json("/groupusers", {
            "cmd" : "deleteUser",
            "userName" : userName,
            "groupName" : groupName,
        }, successFn, "delete user '" + userName + "' from a group'" + groupName + "'");
    }

    exports.deleteUser = deleteUser;
    
    /**
     * Add a user to a group, i.e. create a link in a user-group table.
     * 
     * @param groupName
     *            {String} - name of the group
     *            
     *            * @param userName
     *            {String} - name of the user
     * 
     */
    function getUserGroup(userName, groupName, successFn) {
        COMM.json("/groupusers", {
            "cmd" : "deleteUser",
            "userName" : userName,
            "groupName" : groupName,
        }, successFn, "get user '" + userName + "' -  group'" + groupName + "' relation");
    }

    exports.getUserGroup = getUserGroup;

});
