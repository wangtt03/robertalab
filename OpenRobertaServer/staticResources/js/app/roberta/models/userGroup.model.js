/**
 * Rest calls to the server related to user operations (create user, login ...)
 * 
 * @module rest/program
 */
define([ 'exports', 'comm' ], function(exports, COMM) {


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
        }, successFn, "add user '" + userName + "' to a group '" + groupName + "'");
    }

    exports.addUser = addUser;
    
    /**
     * Delete user-group connection, i.e. delete a link in a user-group table.
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

});
