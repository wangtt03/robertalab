/**
 * Rest calls to the server related to the robot.
 * 
 * @module rest/program
 */
define([ 'exports', 'comm' ], function(exports, COMM) {

    /**
     * Update firmware of the robot.
     * 
     */
    function updateFirmware(successFn) {
        COMM.json("/admin", {
            "cmd" : "updateFirmware"
        }, successFn, "update firmware");
    }

    exports.updateFirmware = updateFirmware;

    /**
     * Set token for paring with the robot.
     * 
     * @param token
     *            {String} - token for paring
     */
    function setToken(token, successFn) {
        COMM.json("/admin", {
            "cmd" : "setToken",
            "token" : token
        }, successFn, "set token '" + token + "'");
    }

    exports.setToken = setToken;

    /**
     * Set token for paring with the robot.
     *
     * @param deviceName
     *            {String} - name of the device
     * @param successFn
     *            {Function} - function to deal with return value
     */
    function getToken(deviceName, successFn) {
        COMM.json("/admin", {
            "cmd": "getToken",
            "deviceName": deviceName
        }, successFn, "get token of device '" + deviceName + "'");
    }

    exports.getToken = getToken;

    /**
     * Set robot type
     * 
     * @param robot
     *            {String} - robot type
     */
    function setRobot(robot, successFn) {
        return COMM.json("/admin", {
            "cmd" : "setRobot",
            "robot" : robot
        }, successFn, "set robot '" + robot + "'");
    }

    exports.setRobot = setRobot;
});
