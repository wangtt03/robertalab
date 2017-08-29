/**
 * Created by t-zhhong on 2017/7/4.
 */
define([ 'exports', 'log', 'util', 'message', 'comm', 'robot.controller', 'socket.controller', 'user.controller', 'guiState.controller', 'program.controller',
    'lessonList.controller', 'configuration.controller', 'enjoyHint', 'tour.controller', 'progHelp.controller', 'progInfo.controller', 'progCode.controller', 'progSim.controller', 'simulation.simulation', 'jquery', 'blocks' ], function(exports, LOG, UTIL, MSG, COMM,
                                                                                                                                                 ROBOT_C, SOCKET_C, USER_C, GUISTATE_C, PROGRAM_C, LESSON_C, CONFIGURATION_C, EnjoyHint, TOUR_C, PROGHELP_C, PROGINFO_C, PROGCODE_C, PROGSIM_C, SIM, $, Blockly) {

    function init(){
        initMenuEvent();
    }

    exports.init = init;

    function initMenuEvent() {
        $('#lesson-button').onWrap('click', function(event) {
            console.log("Lesson clicked");
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
                window.webkit.messageHandlers.requireScanToConnect.postMessage("Require to scan QR Code to connect devices.");
                // SwiftWebViewBridge.callSwiftHandler("requireScanToConnect", {}, function (responseData) {
                //     log('JS got responds from Swift: ', responseData);
                // })
            }
        }, 'connect clicked');
        $('#run-on-brick-button').onWrap('click', function (event) {
            LOG.info('Run On Brick from bar menu');
            PROGRAM_C.runOnBrick();
            return false;
        }, 'run on brick clicked');
        $('#new-file-button').onWrap('click', function (event) {
            LOG.info('Create new file from bar menu');
            PROGRAM_C.newProgram();
            return false;
        }, 'new file clicked');
        $('#prog-list-button').onWrap('click', function (event) {
            LOG.info('Check program list from bar menu');
            $('#tabProgList').data('type', 'userProgram');
            $('#tabProgList').click();
        }, 'prog list clicked');
        $('#save-button').onWrap('click', function (event) {
            LOG.info('Save file from bar menu');
            PROGRAM_C.saveToServer();
            return false;
        }, 'save file clicked');
        $('#user-button').onWrap('click', function (event) {
            LOG.info('Check user profile from bar menu');
            USER_C.showUserInfo();
            return false;
        }, 'user button clicked');
        $('#gallery-button').onWrap('click', function (event) {
            console.log("Gallery Clicked");
            LOG.info('Gallery from bar menu');
            $('#tabGalleryList').click();
            return false;
        }, 'gallery button clicked');
        $('#help-document-button').onWrap('click', function (event) {
            LOG.info('Open help document from bar menu');
            PROGHELP_C.toggleHelp();
            return false;
        }, 'help doc button clicked');
        $('#info-button').onWrap('click', function (event) {
            LOG.info('Open info document from bar menu');
            PROGINFO_C.toggleInfo();
            return false;
        }, 'info doc button clicked');
        $('#code-button').onWrap('click', function (event) {
            LOG.info('Open code view from bar menu');
            PROGCODE_C.toggleCode();
            return false;
        }, 'code view button clicked');
        $('#sim-button').onWrap('click', function (event) {
            LOG.info('Open sim view from bar menu');
            PROGSIM_C.toggleSim();
            return false;
        }, 'sim view button clicked');
    }
});