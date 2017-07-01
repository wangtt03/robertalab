define([ 'exports', 'log', 'util', 'message', 'comm', 'robot.controller', 'socket.controller', 'user.controller', 'guiState.controller', 'program.controller',
        'lessonList.controller', 'configuration.controller', 'enjoyHint', 'tour.controller', 'simulation.simulation', 'jquery', 'blocks' ], function(exports, LOG, UTIL, MSG, COMM,
        ROBOT_C, SOCKET_C, USER_C, GUISTATE_C, PROGRAM_C, LESSON_C, CONFIGURATION_C, EnjoyHint, TOUR_C, SIM, $, Blockly) {

    function init() {

        initMenu();
        initMenuEvents();
        initCourseMenu();
        /**
         * Regularly ping the server to keep status information up-to-date
         */
        function pingServer() {
            if (GUISTATE_C.doPing()) {
                COMM.ping(function(result) {
                    GUISTATE_C.setState(result);
                });
            }
        }
        var ping = setInterval(function() {
            pingServer();
        }, 3000);
        LOG.info('init menu view');

        var target = decodeURI(document.location.hash).split("&");
        if (target[0] === "#forgotPassword") {
            USER_C.showResetPassword(target[1]);
        } else if (target[0] === "#loadProgram" && target.length >= 4) {
            PROGRAM_C.openProgramFromXML(target);
        } else if (target[0] === "#activateAccount") {
          USER_C.activateAccount(target[1]);
        }
    }

    exports.init = init;

    function initMenu() {
        // fill dropdown menu robot
        $("#startupVersion").text(GUISTATE_C.getServerVersion());
        var robots = GUISTATE_C.getRobots();
        var proto = $('.robotType');
        var length = Object.keys(robots).length

        for (var i = 0; i < length; i++) {
            if (robots[i].name == 'sim') {
                i++;
            }
            var clone = proto.clone();
            var robotName = robots[i].name;
            var robotGroup = robots[i].group;
            clone.find('.typcn').addClass('typcn-' + robotGroup);
            clone.find('.typcn').text(robots[i].realName);
            clone.find('.typcn').attr('id', 'menu-' + robotName);
            clone.attr('data-type', robotName);
            clone.addClass(robotName);
            $("#navigation-robot>.anchor").before(clone);
        }
        proto.remove();
        // fill start popup
        proto = $('#popup-sim');

        var groupsDict = {};

        var addPair = function(key, value) {
            if (typeof groupsDict[key] != 'undefined') {
                groupsDict[key].push(value);
            } else {
                groupsDict[key] = [ value ];
            }
        }

        var giveValue = function(key) {
            return groupsDict[key];
        }

        var addInfoLink = function(clone, robotName) {
            if (GUISTATE_C.getRobotInfo(robotName) != 'Info not found') {
                clone.find('a').attr('onclick', 'window.open("' + GUISTATE_C.getRobotInfo(robotName) + '");return false;');
            } else {
                clone.find('a').remove();
            }
            return clone;
        }

        for (var i = 0; i < length; i++) {
            if (robots[i].name == 'sim') {
                i++;
                proto.attr('data-type', robots[i].name);
            }
            addPair(robots[i].group, robots[i].name);
        }

        for ( var key in groupsDict) {
            if (groupsDict.hasOwnProperty(key)) {
                if (groupsDict[key] == key) { //this means that a robot has no subgroup
                    var robotName = key; // robot name is the same as robot group
                    var clone = proto.clone().prop('id', 'menu-' + robotName);
                    clone.attr('data-type', robotName);
                    clone.find('span:eq( 0 )').removeClass('typcn-open');
                    clone.find('span:eq( 0 )').addClass('typcn-' + robotName);
                    clone.find('span:eq( 1 )').text(GUISTATE_C.getMenuRobotRealName(robotName));
                    addInfoLink(clone, robotName);
                    clone.find('img').css('visibility', 'hidden');
                    $("#popup-robot-container").append(clone);

                } else { // till the next for loop we create groups for robots
                    var robotGroup = key;
                    var clone = proto.clone().prop('id', 'menu-' + robotGroup);
                    clone.attr('data-type', robotGroup);
                    clone.find('span:eq( 1 )').text(robotGroup.charAt(0).toUpperCase() + robotGroup.slice(1)); // we have no real name for group 
                    clone.find('span:eq( 0 )').removeClass('typcn-open');
                    clone.find('span:eq( 0 )').addClass('typcn-' + robotGroup); // so we just capitalize the first letter + add typicon
                    clone.find('img.img-beta').css('visibility', 'hidden'); // groups do not have 'beta' labels
                    addInfoLink(clone, robotGroup); // this will just kill the link tag, because no description for group
                    clone.attr('data-group', true);
                    $("#popup-robot-container").append(clone);
                    for (var i = 0; i < groupsDict[key].length; i++) { // and here we put robots in subgroups
                        var robotName = groupsDict[key][i];
                        var clone = proto.clone().prop('id', 'menu-' + robotName);
                        clone.addClass('hidden');
                        clone.addClass('robotSubGroup');
                        clone.addClass(robotGroup);
                        if (!GUISTATE_C.getIsRobotBeta(robotName)) {
                            clone.find('img.img-beta').css('visibility', 'hidden');
                        }
                        addInfoLink(clone, robotName);
                        clone.attr('data-type', robotName);
                        clone.find('span:eq( 0 )').removeClass('typcn-open');
                        clone.find('span:eq( 0 )').addClass('img-' + robotName); // there are no typicons for robots
                        clone.find('span:eq( 1 )').text(GUISTATE_C.getMenuRobotRealName(robotName)); // instead we use images
                        clone.attr('data-type', robotName);
                        $("#popup-robot-container").append(clone);
                    }
                }
            }
        }

        for (var i = 0; i < length; i++) {

        }
        proto.find('.img-beta').css('visibility', 'hidden');
        proto.find('a[href]').css('visibility', 'hidden');
        $('#show-startup-message>.modal-body').append('<input type="button" class="btn backButton hidden" data-dismiss="modal" lkey="Blockly.Msg.POPUP_CANCEL"></input>');
        GUISTATE_C.setInitialState();
    }

    /**
     * Initialize the navigation bar in the head of the page
     */
    function initMenuEvents() {
        $('[rel="tooltip"]').not('.rightMenuButton').tooltip({
            placement : "right"
        });
        $('[rel="tooltip"].rightMenuButton').tooltip({
            placement : "auto"
        });
        // prevent Safari 10. from zooming
        document.addEventListener('gesturestart', function(e) {
            e.preventDefault();
            e.stopPropagation();
        });

        $('.modal').on('shown.bs.modal', function() {
            $(this).find('[autofocus]').focus();
        });

        $('#navbarCollapse').collapse({
            'toggle' : false
        });

        $('#navbarCollapse').onWrap('click', '.dropdown-menu a,.visible-xs', function(event) {
            $('#navbarCollapse').collapse('hide');
        });
        $('#simButtonsCollapse').onWrap('click', 'a', function(event) {
            $('#simButtonsCollapse').collapse('hide');
        });
        $('#navbarButtonsHead').onWrap('click', '', function(event) {
            $('#simButtonsCollapse').collapse('hide');
        });
        $('#simButtonsHead').onWrap('click', '', function(event) {
            $('#navbarCollapse').collapse('hide');
        });

        // EDIT Menu
        $('#head-navigation-program-edit').onWrap('click', '.dropdown-menu li:not(.disabled) a', function(event) {
            var domId = event.target.id;
            if (domId === 'menuRunProg') {
                PROGRAM_C.runOnBrick();
            } else if (domId === 'menuRunSim') {
                $('#progSim').trigger('click');
            } else if (domId === 'menuCheckProg') {
                PROGRAM_C.checkProgram();
            } else if (domId === 'menuNewProg') {
                PROGRAM_C.newProgram();
            } else if (domId === 'menuListProg') {
                $('#tabProgList').data('type', 'userProgram');
                $('#tabProgList').click();
            } else if (domId === 'menuListExamples') {
                $('#tabProgList').data('type', 'exampleProgram');
                $('#tabProgList').click();
            } else if (domId === 'menuSaveProg') {
                PROGRAM_C.saveToServer();
            } else if (domId === 'menuSaveAsProg') {
                PROGRAM_C.showSaveAsModal();
            } else if (domId === 'menuShowCode') {
                $('#progCode').trigger("click");
            } else if (domId === 'menuImportProg') {
                PROGRAM_C.importXml();
            } else if (domId === 'menuExportProg') {
                PROGRAM_C.exportXml();
            } else if (domId === 'menuLinkProg') {
                PROGRAM_C.linkProgram();
            } else if (domId === 'menuToolboxBeginner') {
                $('#beginner').trigger('click');
            } else if (domId === 'menuToolboxExpert') { // Submenu 'Program'
                $('#expert').trigger('click');
            }
        }, 'program edit clicked');

        // CONF Menu
        $('#head-navigation-configuration-edit').onWrap('click', '.dropdown-menu li:not(.disabled) a', function(event) {
            $('.modal').modal('hide'); // close all opened popups
            var domId = event.target.id;
            if (domId === 'menuCheckConfig') { //  Submenu 'Configuration'
                MSG.displayMessage("MESSAGE_NOT_AVAILABLE", "POPUP", "");
            } else if (domId === 'menuNewConfig') { //  Submenu 'Configuration'
                CONFIGURATION_C.newConfiguration();
            } else if (domId === 'menuListConfig') { //  Submenu 'Configuration'
                $('#tabConfList').click();
            } else if (domId === 'menuSaveConfig') { //  Submenu 'Configuration'
                CONFIGURATION_C.saveToServer();
            } else if (domId === 'menuSaveAsConfig') { //  Submenu 'Configuration'
                CONFIGURATION_C.showSaveAsModal();
            }
        }, 'configuration edit clicked');

        // ROBOT Menu
        $('#head-navigation-robot').onWrap('click', '.dropdown-menu li:not(.disabled) a', function(event) {
            $('.modal').modal('hide');
            var choosenRobotType = event.target.parentElement.dataset.type;
            //TODO: change from ardu to botnroll and mbot with friends
            //I guess it is changed now, check downstairs at menuConnect
            if (GUISTATE_C.getRobot() == "ardu") {
                console.log("bobot is ardu");
            }
            if (choosenRobotType) {
                ROBOT_C.switchRobot(choosenRobotType);
            } else {
                var domId = event.target.id;
                if (domId === 'menuConnect') {
                    console.log(GUISTATE_C.getIsAgent());
                    console.log(GUISTATE_C.getConnection());
                    if (GUISTATE_C.getConnection() == 'arduinoAgent'
                            || (GUISTATE_C.getConnection() == 'arduinoAgentOrToken' && GUISTATE_C.getIsAgent() == true)) {
                        var ports = SOCKET_C.getPortList();
                        var robots = SOCKET_C.getRobotList();
                        $('#singleModalListInput').empty();
                        i = 0;
                        ports.forEach(function(port) {
                            $('#singleModalListInput').append("<option value=\"" + port + "\" selected>" + robots[i] + " " + port + "</option>");
                            i++;
                        });
                        ROBOT_C.showListModal();
                    } else {
                        $('#buttonCancelFirmwareUpdate').css('display', 'inline');
                        $('#buttonCancelFirmwareUpdateAndRun').css('display', 'none');
                        ROBOT_C.showSetTokenModal();
                    }
                } else if (domId === 'menuRobotInfo') {
                    ROBOT_C.showRobotInfo();
                }
            }
        }, 'robot clicked');

        $('#head-navigation-help').onWrap('click', '.dropdown-menu li:not(.disabled) a', function(event) {
            $('.modal').modal('hide'); // close all opened popups
            var domId = event.target.id;
            if (domId === 'menuShowRelease') { // Submenu 'Help'
                if ($.cookie("OpenRoberta_" + GUISTATE_C.getServerVersion())) {
                    $('#checkbox_id').prop('checked', true);
                }
                $("#show-startup-message").modal("show");
            } else if (domId === 'menuAbout') { // Submenu 'Help'
                $("#version").text(GUISTATE_C.getServerVersion() + '-SNAPSHOT');
                $("#show-about").modal("show");
            } else if (domId === 'menuLogging') { // Submenu 'Help'
                $('#tabLogList').click();
            }
        }, 'help clicked');

        $('#head-navigation-user').onWrap('click', '.dropdown-menu li:not(.disabled) a', function(event) {
            $('.modal').modal('hide'); // close all opened popups
            var domId = event.target.id;
            if (domId === 'menuLogin') { // Submenu 'Login'
                USER_C.showLoginForm();
            } else if (domId === 'menuLogout') { // Submenu 'Login'
                USER_C.logout();
            } else if (domId === 'menuNewUser') { // Submenu 'Login'
                $("#register-user").modal('show');
            } else if (domId === 'menuChangeUser') { // Submenu 'Login'
                USER_C.showUserDataForm();
            } else if (domId === 'menuDeleteUser') { // Submenu 'Login'
                USER_C.showDeleteUserModal();
            } else if (domId === 'menuStateInfo') { // Submenu 'Help'
                USER_C.showUserInfo();
            }
            return false;
        }, 'user clicked');

        $('#head-navigation-gallery').onWrap('click', function(event) {
            $('#tabGalleryList').click();
            return false;
        }, 'gallery clicked');

        $('#head-navigation-lesson').onWrap('click', function(event) {
            LESSON_C.loadLessonAll();

        }, 'lesson clicked');

        $('.sim-nav').onWrap('click', 'li:not(.disabled) a', function(event) {
            $('.modal').modal('hide'); // head-navigation-sim-control
            var domId = event.target.id;
            if (domId === 'menuSimSimple') {
                $('.menuSim').parent().removeClass('disabled');
                $('.simSimple').parent().addClass('disabled');
                SIM.setBackground(2, SIM.setBackground);
                $("#simButtonsCollapse").collapse('hide');
            } else if (domId === 'menuSimDraw') {
                $('.menuSim').parent().removeClass('disabled');
                $('.simDraw').parent().addClass('disabled');
                SIM.setBackground(3, SIM.setBackground);
                $("#simButtonsCollapse").collapse('hide');
            } else if (domId === 'menuSimRoberta') {
                $('.menuSim').parent().removeClass('disabled');
                $('.simRoberta').parent().addClass('disabled');
                SIM.setBackground(4, SIM.setBackground);
                $("#simButtonsCollapse").collapse('hide');
            } else if (domId === 'menuSimRescue') {
                $('.menuSim').parent().removeClass('disabled');
                $('.simRescue').parent().addClass('disabled');
                SIM.setBackground(5, SIM.setBackground);
                $("#simButtonsCollapse").collapse('hide');
            } else if (domId === 'menuSimWRO') {
                $('.menuSim').parent().removeClass('disabled');
                $('.simWRO').parent().addClass('disabled');
                SIM.setBackground(6, SIM.setBackground);
                $("#simButtonsCollapse").collapse('hide');
            } else if (domId === 'menuSimMath') {
                $('.menuSim').parent().removeClass('disabled');
                $('.simMath').parent().addClass('disabled');
                SIM.setBackground(7, SIM.setBackground);
                $("#simButtonsCollapse").collapse('hide');
            }
        }, 'sim clicked');

        $('#menuTabProgram').onWrap('click', '', function(event) {
            if ($('#tabSimulation').hasClass('tabClicked')) {
                $('.scroller-left').click();
            }
            $('.scroller-left').click();
            $('#tabProgram').click();
        }, 'tabProgram clicked');

        $('#menuTabConfiguration').onWrap('click', '', function(event) {
            if ($('#tabProgram').hasClass('tabClicked')) {
                $('.scroller-right').click();
            } else if ($('#tabConfiguration').hasClass('tabClicked')) {
                $('.scroller-right').click();
            }
            $('#tabConfiguration').click();
        }, 'tabConfiguration clicked');

        // Close submenu on mouseleave
        $('.navbar-fixed-top').onWrap('mouseleave', function(event) {
            $('.navbar-fixed-top .dropdown').removeClass('open');
        });

        $('#imgLogo, #imgBeta').onWrap('click', function() {
            window.open('http://open-roberta.org');
        }, 'logo was clicked');

        $('#beta').onWrap('click', function() {
            window.open('http://open-roberta.org');
        }, 'beta logo was clicked');

        $('.menuGeneral').onWrap('click', function(event) {
            window.open("https://jira.iais.fraunhofer.de/wiki/display/ORInfo");
        }, 'head navigation menu item clicked');
        $('.menuFaq').onWrap('click', function(event) {
            window.open("https://jira.iais.fraunhofer.de/wiki/display/ORInfo/FAQ");
        }, 'head navigation menu item clicked');
        $('.menuPrivacy').onWrap('click', function(event) {
            window.open("TODO");
        }, 'head navigation menu item clicked');

        $('.simScene').onWrap('click', function(event) {
            SIM.setBackground(-1, SIM.setBackground);
            var scene = $("#simButtonsCollapse").collapse('hide');
            $('.menuSim').parent().removeClass('disabled');
            if (scene == 2) {
                $('.simSimple').parent().addClass('disabled');
            } else if (scene == 3) {
                $('.simDraw').parent().addClass('disabled');
            } else if (scene == 4) {
                $('.simRoberta').parent().addClass('disabled');
            } else if (scene == 5) {
                $('.simRescue').parent().addClass('disabled');
            } else if (scene == 6) {
                $('.simWRO').parent().addClass('disabled');
            } else if (scene == 7) {
                $('.simMath').parent().addClass('disabled');
            }
        }, 'simScene clicked');

        // preliminary (not used)
        $('#startNXT').onWrap('click', function(event) {
            switchRobot('nxt');
        }, 'start with nxt clicked');
        // preliminary (not used)
        $('#startEV3').onWrap('click', function(event) {
            switchRobot('ev3');
        }, 'start with ev3 clicked');

        $('#startPopupBack').on('click', function(event) {
            $('.popup-robot').removeClass('hidden');
            $('.popup-robot.robotSubGroup').addClass('hidden');
            $('.robotSpecial').removeClass('robotSpecial');
            $('#startPopupBack').addClass('hidden');
        });
        $('.popup-robot').onWrap('click', function(event) {
            event.preventDefault();
            $('#startPopupBack').trigger('click');
            var choosenRobotType = event.target.parentElement.parentElement.dataset.type || event.target.parentElement.dataset.type
                    || event.target.dataset.type;
            var choosenRobottGroup = event.target.parentElement.parentElement.dataset.group || event.target.parentElement.dataset.group
                    || event.target.dataset.group;
            if (event.target.className.indexOf("info") >= 0) {
                var win = window.open(GUISTATE_C.getRobots()[choosenRobotType].info, '_blank');
            } else {
                if (choosenRobotType) {
                    if (choosenRobottGroup) {
                        $('.popup-robot').addClass('hidden');
                        $('.popup-robot.' + choosenRobotType).removeClass('hidden');
                        $('.popup-robot.' + choosenRobotType).addClass('robotSpecial');
                        $('#startPopupBack').removeClass('hidden');
                        return;
                    } else {
                        ROBOT_C.switchRobot(choosenRobotType, true);
                    }
                }
                if ($('#checkbox_id').is(':checked')) {
                    $.cookie("OpenRoberta_" + GUISTATE_C.getServerVersion(), choosenRobotType, {
                        expires : 99,
                        secure : true,
                        domain : ''
                    });
                    // check if it is really stored: chrome issue
                    if (!$.cookie("OpenRoberta_" + GUISTATE_C.getServerVersion())) {
                        $.cookie("OpenRoberta_" + GUISTATE_C.getServerVersion(), choosenRobotType, {
                            expires : 99,
                            domain : ''
                        });
                    }
                } else {
                    $.removeCookie("OpenRoberta_" + GUISTATE_C.getServerVersion());
                }
                $('#show-startup-message').modal('hide');
            }
        }, 'robot choosen in start popup');

        $('#moreReleases').onWrap('click', function(event) {
            $('#oldReleases').show({
                start : function() {
                    $('#moreReleases').addClass('hidden');
                }
            });
        }, 'show more releases clicked');

        $('#confirmContinue').onWrap('click', function(event) {
            if ($('#confirmContinue').data('type') === 'program') {
                PROGRAM_C.newProgram(true);
            } else if ($('#confirmContinue').data('type') === 'configuration') {
                CONFIGURATION_C.newConfiguration(true);
            } else if ($('#confirmContinue').data('type') === 'switchRobot') {
                ROBOT_C.switchRobot($('#confirmContinue').data('robot'), true);
            } else {
                console.log('Confirmation with unknown data type clicked');
            }
        }, 'continue new program clicked');
        $('#takeATour').onWrap('click', function(event) {
            if (GUISTATE_C.getRobotGroup() !== 'ev3') {
                ROBOT_C.switchRobot('ev3lejos', true);
            }
            if (GUISTATE_C.getProgramToolboxLevel() !== 'beginner') {
                $('#beginner').trigger('click');
            }
            PROGRAM_C.newProgram(true);
            TOUR_C.start('welcome');
        }, 'take a tour clicked');

        $('#goToWiki').onWrap('click', function(event) {
            event.preventDefault();
            window.open('https://jira.iais.fraunhofer.de/wiki/display/ORInfo', '_blank');
            event.stopPropagation();
            $("#show-startup-message").modal("show");

        }, 'take a tour clicked');

        // init popup events

        $('.cancelPopup').onWrap('click', function() {
            $('.ui-dialog-titlebar-close').click();
        });

        $('#about-join').onWrap('click', function() {
            $('#show-about').modal('hide');
        });

        $(window).on('beforeunload', function(e) {
            if (!GUISTATE_C.isProgramSaved || !GUISTATE_C.isConfigurationSaved) {
                if (GUISTATE_C.isUserLoggedIn()) {
                    // Maybe a Firefox-Problem?                alert(Blockly.Msg['POPUP_BEFOREUNLOAD_LOGGEDIN']);
                    return Blockly.Msg.POPUP_BEFOREUNLOAD_LOGGEDIN;
                } else {
                    // Maybe a Firefox-Problem?                alert(Blockly.Msg['POPUP_BEFOREUNLOAD']);
                    return Blockly.Msg.POPUP_BEFOREUNLOAD;
                }
            }
        });

        $(window).on('resize', function(e) {
            Blockly.svgResize(GUISTATE_C.getBlocklyWorkspace());
            Blockly.svgResize(GUISTATE_C.getBricklyWorkspace());
        });
    }

    function initCourseMenu() {
        var proto = $('#popup-course-example');
        for (var i = 0; i < 5; i++) {
            var clone = proto.clone().prop('id', 'menu-course-' + i.toString());
            clone.attr('data-type', 'ev3');
            clone.find('span:eq( 0 )').removeClass('typcn-open');
            clone.find('span:eq( 0 )').addClass('typcn-ev3');
            clone.find('span:eq( 1 )').text('Course ' + i.toString());
            $("#popup-course-container").append(clone);
            proto.remove();
        }
    }
});
