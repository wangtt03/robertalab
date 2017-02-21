/**
 * Rest calls to the server related to user operations (create user, login ...)
 * 
 * @module rest/program
 */
define([ 'exports', 'comm' ], function(exports, COMM) {

  

    /**
     * Retrieve group from server.
     * 
     * @param groupName
     *            {String} - name of the group
     * 
     * 
     */
    function getGroupFromServer(groupName, successFn) {
        COMM.json("/usergroups", {
            "cmd" : "getGroup",
            "groupName" : groupName

        }, successFn, "get group '" + groupName + "' from server");
    }

    exports.getGroupFromServer = getGroupFromServer;

    /**
     * Create group to server.
     * 
     * @param groupName
     *            {String} - name of the group
     * @param passwd
     *            {String} - user password
     * 
     */
    function createGroupToServer(accountName, successFn) {
        COMM.json("/usergroups", {
            "cmd" : "createGroup",
            "groupName" : groupName,
            //"password" : passwd,
        }, successFn, "save group '" + groupName + "' to server");
    }

    exports.createGroupToServer = createGroupToServer;
    
    /**
     * Get all groups those belong to a user.
     * 
     * @param userName
     *            {String} - name of the user
     * 
     */
    function getOwnerGroups(userName, successFn) {
        COMM.json("/usergroups", {
            "cmd" : "getOwnerGroups",
            "userName" : userName,
        }, successFn, "get owner '" + userName + "' groups to server");
    }

    exports.getOwnerGroups = getOwnerGroups;
    
    /**
     * Get all users those belong to a group.
     * 
     * @param groupName
     *            {String} - name of the group
     * 
     */
    function getGroupMembers(groupName, successFn) {
        COMM.json("/usergroups", {
            "cmd" : "getGroupMembers",
            "groupName" : groupName,
        }, successFn, "get group '" + groupName + "' members to server");
    }

    exports.getGroupMembers = getGroupMembers;
    
    /**
     * Get all groups where the user is registered.
     * 
     * @param groupName
     *            {String} - group of the account
     * 
     */
    function getMemberGroups(userName, successFn) {
        COMM.json("/usergroups", {
            "cmd" : "getGroupMembers",
            "userName" : userName,
        }, successFn, "get group '" + userName + "' members to server");
    }

    exports.getMemberGroups = getMemberGroups;


 
    /**
     * Delete group from the server.
     * 
     * @param groupName
     *            {String} - group name
     * 
     */
    function deleteGroupOnServer(groupName, successFn) {
        COMM.json("/usergroups", {
            "cmd" : "deleteGroup",
            "groupName" : groupName,
            //"password" : passwd
        }, successFn, "delete group '" + groupName + "' on server");
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
        COMM.json("/usergroups", {
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
        COMM.json("/usergroups", {
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
        COMM.json("/usergroups", {
            "cmd" : "deleteUser",
            "userName" : userName,
            "groupName" : groupName,
        }, successFn, "get user '" + userName + "' -  group'" + groupName + "' relation");
    }

    exports.getUserGroup = getUserGroup;

});
