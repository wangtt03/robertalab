/**
 * Created by t-zhhong on 2017/7/4.
 */
define([ 'exports', 'log', 'util', 'message', 'comm', 'robot.controller', 'socket.controller', 'user.controller', 'guiState.controller', 'program.controller',
    'lessonList.controller', 'configuration.controller', 'enjoyHint', 'tour.controller', 'simulation.simulation', 'jquery', 'blocks' ], function(exports, LOG, UTIL, MSG, COMM,
                                                                                                                                                 ROBOT_C, SOCKET_C, USER_C, GUISTATE_C, PROGRAM_C, LESSON_C, CONFIGURATION_C, EnjoyHint, TOUR_C, SIM, $, Blockly) {

    function init(){
        initMenuEvent();
    }

    exports.init = init;

    function initMenuEvent() {
        $('#lesson-button').onWrap('click', function(event) {
            LESSON_C.displayLessonMenu();
            return false;
        }, 'lesson clicked');

        // $('.lesson-close-button')[0].onclick = function() {
        //     LESSON_C.hideLessonMenu();
        //     return false;
        // };

        $('#connect-button').onWrap('click', function (event) {
            console.log(GUISTATE_C.getIsAgent());
            console.log(GUISTATE_C.getConnection());
            if (GUISTATE_C.getConnection() == 'arduinoAgent'
                || (GUISTATE_C.getConnection() == 'arduinoAgentOrToken' && GUISTATE_C.getIsAgent() == true)) {
                var ports = SOCKET_C.getPortList();
                var robots = SOCKET_C.getRobotList();
                $('#singleModalListInput').empty();
                i = 0;
                ports.forEach(function (port) {
                    $('#singleModalListInput').append("<option value=\"" + port + "\" selected>" + robots[i] + " " + port + "</option>");
                    i++;
                });
                ROBOT_C.showListModal();
            } else {
                $('#buttonCancelFirmwareUpdate').css('display', 'inline');
                $('#buttonCancelFirmwareUpdateAndRun').css('display', 'none');
                SwiftWebViewBridge.callSwiftHandler("requireScanToConnect", {}, function (responseData) {
                    log('JS got responds from Swift: ', responseData);
                })
            }
        }, 'connect clicked');
    }
});