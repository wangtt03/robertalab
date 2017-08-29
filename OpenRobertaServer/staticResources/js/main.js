require.config({
    baseUrl : 'js/libs',
    paths : {
        'blockly' : '../../blockly/blockly_compressed',
        'blocks' : '../../blockly/blocks_compressed',
        'blocks-msg' : '../../blockly/msg/js/en',
        'bootstrap' : 'bootstrap/bootstrap-3.3.1-dist/dist/js/bootstrap.min',
        'bootstrap-table' : 'bootstrap/bootstrap-3.3.1-dist/dist/js/bootstrap-table.min',
        'datatables' : 'jquery/jquery.dataTables.min',
        'enjoyHint' : 'enjoyHint/enjoyhint.min',
        'jquery' : 'jquery/jquery-2.1.4.min',
        'jquery-cookie' : 'jquery/jquery.cookie',
        'jquery-scrollto' : 'jquery/jquery.scrollTo.min',
        'jquery-validate' : 'jquery/jquery.validate.min',
        'jquery-hotkeys' : 'jquery/jquery.hotkeys',
        'prettify' : 'code-prettify/prettify',
        'volume-meter' : 'sound/volume-meter',
        'bootstrap.wysiwyg' : 'bootstrap/bootstrap-3.3.1-dist/dist/js/bootstrap-wysiwyg.min',
        'socket.io' : 'socket.io/socket.io',

        'confDelete.controller' : '../app/roberta/controller/confDelete.controller',
        'configuration.controller' : '../app/roberta/controller/configuration.controller',
        'configuration.model' : '../app/roberta/models/configuration.model',
        'confList.controller' : '../app/roberta/controller/confList.controller',
        'confList.model' : '../app/roberta/models/confList.model',
        'galleryList.controller' : '../app/roberta/controller/galleryList.controller',
        'guiState.controller' : '../app/roberta/controller/guiState.controller',
        'guiState.model' : '../app/roberta/models/guiState.model',
        'language.controller' : '../app/roberta/controller/language.controller',
        'logList.controller' : '../app/roberta/controller/logList.controller',
        'logList.model' : '../app/roberta/models/logList.model',
        'menu.controller' : '../app/roberta/controller/menu.controller',
        'progCode.controller' : '../app/roberta/controller/progCode.controller',
        'progDelete.controller' : '../app/roberta/controller/progDelete.controller',
        'progHelp.controller' : '../app/roberta/controller/progHelp.controller',
        'progInfo.controller' : '../app/roberta/controller/progInfo.controller',
        'progList.controller' : '../app/roberta/controller/progList.controller',
        'progList.model' : '../app/roberta/models/progList.model',
        'program.controller' : '../app/roberta/controller/program.controller',
        'program.model' : '../app/roberta/models/program.model',
        'progShare.controller' : '../app/roberta/controller/progShare.controller',
        'progSim.controller' : '../app/roberta/controller/progSim.controller',
        'robot.controller' : '../app/roberta/controller/robot.controller',
        'robot.model' : '../app/roberta/models/robot.model',
        'tour.controller' : '../app/roberta/controller/tour.controller',
        'user.controller' : '../app/roberta/controller/user.controller',
        'user.model' : '../app/roberta/models/user.model',
        'rest.robot' : '../app/roberta/rest/robot',
        'socket.controller' : '../app/roberta/controller/socket.controller',

        'simulation.constants' : '../app/simulation/simulationLogic/constants',
        'simulation.math' : '../app/simulation/simulationLogic/math',
        'simulation.program.builder' : '../app/simulation/robertaLogic/program.builder',
        'simulation.program.eval' : '../app/simulation/robertaLogic/program.eval',
        'simulation.robot' : '../app/simulation/simulationLogic/robot',
        'simulation.robot.draw' : '../app/simulation/simulationLogic/robot.draw',
        'simulation.robot.mbed' : '../app/simulation/simulationLogic/robot.mbed',
        'simulation.robot.calliope' : '../app/simulation/simulationLogic/robot.calliope',
        'simulation.robot.calliope2016' : '../app/simulation/simulationLogic/robot.calliope2016',
        'simulation.robot.calliope2017' : '../app/simulation/simulationLogic/robot.calliope2017',
        'simulation.robot.microbit' : '../app/simulation/simulationLogic/robot.microbit',
        'simulation.robot.math' : '../app/simulation/simulationLogic/robot.math',
        'simulation.robot.rescue' : '../app/simulation/simulationLogic/robot.rescue',
        'simulation.robot.roberta' : '../app/simulation/simulationLogic/robot.roberta',
        'simulation.robot.simple' : '../app/simulation/simulationLogic/robot.simple',
        'simulation.robot.ev3' : '../app/simulation/simulationLogic/robot.ev3',
        'simulation.robot.nxt' : '../app/simulation/simulationLogic/robot.nxt',
        'simulation.scene' : '../app/simulation/simulationLogic/scene',
        'simulation.simulation' : '../app/simulation/simulationLogic/simulation',

        'comm' : '../helper/comm',
        'log' : '../helper/log',
        'message' : '../helper/msg',
        'util' : '../helper/util',
        'wrap' : '../helper/wrap',

        'robertaLogic.actors' : '../app/simulation/robertaLogic/actors',
        'robertaLogic.constants' : '../app/simulation/robertaLogic/constants',
        'robertaLogic.memory' : '../app/simulation/robertaLogic/memory',
        'robertaLogic.motor' : '../app/simulation/robertaLogic/motor',
        'robertaLogic.program' : '../app/simulation/robertaLogic/program',
        'robertaLogic.timer' : '../app/simulation/robertaLogic/timer',
        'robertaLogic.gyro' : '../app/simulation/robertaLogic/gyro',

    },
    shim : {
        'bootstrap' : {
            deps : [ 'jquery' ]
        },
        'blocks-msg' : {
            deps : [ 'blocks' ],
            exports : 'Blockly'
        },
        'blocks' : {
            deps : [ 'blockly' ],
            exports : 'Blockly'
        },
        'volume-meter' : {
            exports : "Volume",
            init : function() {
                return {
                    createAudioMeter : createAudioMeter
                };
            }
        },
        'jquery-validate' : {
            deps : [ 'jquery' ]
        },
        'jquery-cookie' : {
            deps : [ 'jquery' ]
        },
    }
});

require([ 'require', 'wrap', 'jquery', 'jquery-cookie', 'guiState.controller', 'progList.controller', 'logList.controller', 'confList.controller',
        'progDelete.controller', 'confDelete.controller', 'progShare.controller', 'menu.controller', 'user.controller', 'robot.controller',
        'program.controller', 'configuration.controller', 'language.controller', 'socket.controller', 'volume-meter' ], function(require) {

    $ = require('jquery', 'jquery-cookie');
    WRAP = require('wrap');
    COMM = require('comm');
    confDeleteController = require('confDelete.controller');
    configurationController = require('configuration.controller');
    confListController = require('confList.controller');
    guiStateController = require('guiState.controller');
    languageController = require('language.controller');
    logListController = require('logList.controller');
    menuController = require('menu.controller');
    progDeleteController = require('progDelete.controller');
    progListController = require('progList.controller');
    galleryListController = require('galleryList.controller');
    programController = require('program.controller');
    progShareController = require('progShare.controller');
    robotController = require('robot.controller');
    userController = require('user.controller');
    socketController = require('socket.controller');

    $(document).ready(WRAP.fn3(init, 'page init'));
});

/**
 * Initializations
 */
function init() {
    COMM.setErrorFn(handleServerErrors);
    $.when(languageController.init()).then(function(language) {
        return guiStateController.init(language);
    }).then(function() {
        return robotController.init();
    }).then(function() {
        return userController.init();
    }).then(function() {
        galleryListController.init();
        progListController.init();
        progDeleteController.init();
        confListController.init();
        confDeleteController.init();
        progShareController.init();
        logListController.init();
        configurationController.init();
        programController.init();
        menuController.init();
        //socketController.init();

        //console.log(robotList);
        $(".cover").fadeOut(100, function() {
            if (guiStateController.noCookie()) {
                $("#show-startup-message").modal("show");
            }
        });
        $(".pace").fadeOut(500);
    });
}

/**
 * Handle server errors
 */
function handleServerErrors() {
    // TODO more?        
    guiStateController.setPing(false);
    $('#message').attr('lkey', Blockly.Msg.SERVER_NOT_AVAILABLE);
    $('#message').html(Blockly.Msg.SERVER_NOT_AVAILABLE);
    $('#show-message').modal({
        backdrop : 'static',
        keyboard : false
    })
    $('#show-message :button').hide();
    $('#show-message').on('hidden.bs.modal', function(e) {
        // $("#show-message").modal("show");
        guiStateController.setPing(true);
    });
    $("#show-message").modal("show");
}
