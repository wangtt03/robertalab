define(['exports', 'comm', 'message', 'log', 'util', 'simulation.simulation', 'guiState.controller', 'program.model', 'tour.controller', 'blocks', 'jquery',
    'jquery-validate', 'blocks-msg'
], function(exports, COMM, MSG, LOG, UTIL, SIM, GUISTATE_C, PROGRAM, TOUR_C, Blockly, $) {

    var blocklyWorkspace;
    /**
     *
     */
    function init(workspace) {
        blocklyWorkspace = workspace;
        //initView();
        initEvents();
        $('#progSim').hide();
        // LOG.sim('init sim view');
    }
    exports.init = init;

    function initEvents() {
        $('#progSim').off('click touchend');
        $('#progSim').on('click touchend', function(event) {
            toggleSim();
            return false;
        });

        $('#simRobotModal').removeClass("modal-backdrop");
        $('#simControl').onWrap('click', function(event) {
            if ($('#simControl').hasClass('typcn-media-play-outline')) {
                Blockly.hideChaff();
                var xmlProgram = Blockly.Xml.workspaceToDom(blocklyWorkspace);
                var xmlTextProgram = Blockly.Xml.domToText(xmlProgram);
                var xmlTextConfiguration = GUISTATE_C.getConfigurationXML();
                GUISTATE_C.setConfigurationXML(xmlTextConfiguration);

                PROGRAM.runInSim(GUISTATE_C.getProgramName(), GUISTATE_C.getConfigurationName(), xmlTextProgram, xmlTextConfiguration, function(result) {
                    if (result.rc == "ok") {
                        MSG.displayMessage("MESSAGE_EDIT_START", "TOAST", GUISTATE_C.getProgramName());
                        $('#simControl').addClass('typcn-media-stop').removeClass('typcn-media-play-outline');
                        $('#simControl').attr('data-original-title', Blockly.Msg.MENU_SIM_STOP_TOOLTIP);
                        SIM.init(result.javaScriptProgram, false, GUISTATE_C.getRobotGroup());
                        setTimeout(function() {
                            SIM.setPause(false);
                        }, 500);
                    } else {
                        MSG.displayInformation(result, "", result.message, "");
                    }
                    reloadProgram(result);
                });
            } else {
                $('#simControl').addClass('typcn-media-play-outline').removeClass('typcn-media-stop');
                $('#simControl').attr('data-original-title', Blockly.Msg.MENU_SIM_START_TOOLTIP);
                SIM.stopProgram();
            }
        });
        $('#simImport').onWrap('click', function(event) {
            SIM.importImage();
        }, 'simImport clicked');

        $('#simButtonsCollapse').collapse({
            'toggle': false
        });
        $('#simRobotModal').removeClass("modal-backdrop");

        $('.simInfo').onWrap('click', function(event) {
            SIM.setInfo();
            $("#simButtonsCollapse").collapse('hide');
        }, 'simInfo clicked');

        $('#simRobot').on('click', function(event) {
            $("#simRobotModal").modal("toggle");
            var robot = GUISTATE_C.getRobot();
            var position = $("#simDiv").position();
            position.top += 12;
            if (robot == 'calliope' || robot == 'microbit') {
                position.left = $("#blocklyDiv").width() + 12;
                $("#simRobotModal").css({
                    top: position.top,
                    left: position.left
                });
            } else {
                position.left += 48;
                $("#simRobotModal").css({
                    top: position.top,
                    left: position.left
                });
            }
            $('#simRobotModal').draggable();
            $("#simButtonsCollapse").collapse('hide');
        });

        $('#simValues').onWrap('click', function(event) {
            $("#simValuesModal").modal("toggle");
            var position = $("#simDiv").position();
            position.top += 12;
            $("#simValuesModal").css({
                top: position.top,
                right: 12,
                left: 'initial',
                bottom: 'inherit'
            });
            $('#simValuesModal').draggable();

            $("#simButtonsCollapse").collapse('hide');
        }, 'simValues clicked');
    }

    function reloadProgram(opt_result) {
        xml = GUISTATE_C.getProgramXML();
        if (opt_result) {
            xml = opt_result.data;
        }
        listenToBlocklyEvents = false;
        Blockly.hideChaff();
        blocklyWorkspace.clear();
        var dom = Blockly.Xml.textToDom(xml, blocklyWorkspace);
        Blockly.Xml.domToWorkspace(dom, blocklyWorkspace);
        // update right panel if it is already open
        if ($('.fromRight').hasClass('rightActive')) {
            $('#infoContent').html(blocklyWorkspace.description);
            var xmlConfiguration = GUISTATE_C.getConfigurationXML();
            var dom = Blockly.Xml.workspaceToDom(blocklyWorkspace);
            var xmlProgram = Blockly.Xml.domToText(dom);
            var xmlConfiguration = GUISTATE_C.getConfigurationXML();
        }
        setTimeout(function() {
            listenToBlocklyEvents = true;
        }, 500);
    }

    function toggleSim() {
        Blockly.hideChaff();
        if ($('#simDiv').hasClass('rightActive')) {
            SIM.cancel();
            $(".sim").addClass('hide');
            $("#simButtonsCollapse").collapse('hide');
            $('.blocklyToolboxDiv').css('display', 'inherit');
            $('#simControl').addClass('typcn-media-play-outline').removeClass('typcn-media-stop');
            Blockly.svgResize(blocklyWorkspace);
            $('#progSim').hide();
            $('#code-button').removeClass('disabled');
            $('#info-button').removeClass('disabled');
            $('#help-document-button').removeClass('disabled');
            $('#progSim').animate({
                right: '0px',
            }, {
                duration: 750
            });
            $('#blocklyDiv').animate({
                width: '100%'
            }, {
                duration: 750,
                start: function() {
                    $(".modal").modal("hide");
                },
                step: function() {
                    $(window).resize();
                    Blockly.svgResize(blocklyWorkspace);
                },
                done: function() {
                    $('#blocklyDiv').removeClass('rightActive');
                    $('#simDiv').removeClass('rightActive');
                    $('#menuSim').parent().addClass('disabled');
                    $('.nav > li > ul > .robotType').removeClass('disabled');
                    $('.' + GUISTATE_C.getRobot()).addClass('disabled');
                    $(window).resize();
                    Blockly.svgResize(blocklyWorkspace);
                    $('#sliderDiv').hide();
                    $('#progSim').removeClass('shifted');
                }
            });
        } else {
            //LOG.info('run ' + GUISTATE_C.getProgramName() + 'in simulation');
            var xmlProgram = Blockly.Xml.workspaceToDom(blocklyWorkspace);
            var xmlTextProgram = Blockly.Xml.domToText(xmlProgram);
            var xmlTextConfiguration = GUISTATE_C.getConfigurationXML();
            GUISTATE_C.setConfigurationXML(xmlTextConfiguration);
            PROGRAM.runInSim(GUISTATE_C.getProgramName(), GUISTATE_C.getConfigurationName(), xmlTextProgram, xmlTextConfiguration, function(result) {
                if (result.rc == "ok") {
                    //                    MSG.displayMessage("MESSAGE_EDIT_START", "TOAST", GUISTATE_C.getProgramName());
                    SIM.init(result.javaScriptProgram, true, GUISTATE_C.getRobotGroup());
                    $(".sim").removeClass('hide');
                    $('#blocklyDiv').addClass('rightActive');
                    $('#simDiv').addClass('rightActive');
                    $('#simButtonsCollapse').collapse({
                        'toggle': false
                    });
                    var width;
                    var smallScreen = $('#device-size').find('div:visible').first().attr('id') == 'xs';
                    if (smallScreen) {
                        width = 52;
                    } else {
                        width = $('#blocklyDiv').width() * 0.3;
                    }
                    $('#progSim').show();
                    $('#code-button').addClass('disabled');
                    $('#info-button').addClass('disabled');
                    $('#help-document-button').addClass('disabled');
                    $('#progSim').animate({
                        right: $('#blocklyDiv').width() - width + 4,
                    }, {
                        duration: 750
                    });
                    $('#blocklyDiv').animate({
                        width: width
                    }, {
                        duration: 750,
                        step: function() {
                            $(window).resize();
                            Blockly.svgResize(blocklyWorkspace);
                        },
                        done: function() {
                            if (smallScreen) {
                                $('.blocklyToolboxDiv').css('display', 'none');
                            }
                            $('#sliderDiv').css({
                                'left': width - 10
                            });
                            $('#sliderDiv').show();
                            $('#progSim').addClass('shifted');
                            $(window).resize();
                            Blockly.svgResize(blocklyWorkspace);
                            if (TOUR_C.getInstance()) {
                                TOUR_C.getInstance().trigger('SimLoaded');
                            }
                            //                            setTimeout(function() {
                            //                                SIM.setPause(false);
                            //                            }, 500);
                        }
                    });
                } else {
                    MSG.displayInformation(result, "", result.message, "");
                }
                reloadProgram(result);
            });
        }
    }
    exports.toggleSim = toggleSim;
});
