define([ 'exports', 'log', 'util', 'comm', 'message', 'guiState.controller', 'blocks', 'configuration.model', 'jquery', 'jquery-validate' ], function(exports,
        LOG, UTIL, COMM, MSG, guiStateController, Blockly, CONFIGURATION, $) {

    var $formSingleModal;

    var bricklyWorkspace;
    var listenToBricklyEvents = true;
    var seen = false;

    function init() {
        initView();
        initEvents();
        initConfigurationForms();
        initConfigurationEnvironment();
        LOG.info('init configuration view');
    }

    exports.init = init;

    /**
     * Inject Brickly with initial toolbox
     * 
     * @param {response}
     *            toolbox
     */
    function initView() {
        var toolbox = guiStateController.getConfigurationToolbox();
        bricklyWorkspace = Blockly.inject(document.getElementById('bricklyDiv'), {
            path : '/blockly/',
            toolbox : toolbox,
            trashcan : true,
            scrollbars : true,
            media : '../blockly/media/',
            zoom : {
                controls : true,
                wheel : true,
                startScale : 1.0,
                maxScale : 4,
                minScale : .25,
                scaleSpeed : 1.1
            },
            checkInTask : [ '-Brick' ],
            variableDeclaration : true,
            robControls : true
        });
        bricklyWorkspace.setDevice(guiStateController.getRobotGroup());
        bricklyWorkspace.setVersion('2.0');
        // Configurations can't be executed
        //bricklyWorkspace.robControls.runOnBrick.setAttribute("style", "display : none");
        $('#progSim').hide();
        guiStateController.setBricklyWorkspace(bricklyWorkspace);
        bricklyWorkspace.robControls.disable('saveProgram');
        $('#save-button').addClass('disabled');
    }

    function initEvents() {

        $('#tabConfiguration').on('show.bs.tab', function(e) {
            guiStateController.setView('tabConfiguration');
            bricklyWorkspace.markFocused();
        });

        $('#tabConfiguration').onWrap('shown.bs.tab', function(e) {
            if (guiStateController.isConfigurationUsed()) {
                bricklyWorkspace.setVisible(true);
            } else {
                bricklyWorkspace.setVisible(false);
            }
            reloadConf();
        }, 'tabConfiguration clicked');

        $('#tabConfiguration').on('hide.bs.tab', function(e) {
            var dom = Blockly.Xml.workspaceToDom(bricklyWorkspace);
            var xml = Blockly.Xml.domToText(dom);
            guiStateController.setConfigurationXML(xml);
        });
        Blockly.bindEvent_(bricklyWorkspace.robControls.saveProgram, 'mousedown', null, function(e) {
            LOG.info('saveConfiguration from brickly button');
            saveToServer();
        });
        bricklyWorkspace.addChangeListener(function(event) {
            if (listenToBricklyEvents && event.type != Blockly.Events.UI && guiStateController.isConfigurationSaved()) {
                guiStateController.setConfigurationSaved(false);
            }
            if (event.type === Blockly.Events.DELETE) {
                if (bricklyWorkspace.getAllBlocks().length === 0) {
                    newConfiguration(true);
                }
            }
        });
    }

    function initConfigurationForms() {
        $formSingleModal = $('#single-modal-form');
    }
    exports.initConfigurationForms = initConfigurationForms;

    /**
     * Save configuration to server
     */
    function saveToServer() {
        $('.modal').modal('hide'); // close all opened popups
        var dom = Blockly.Xml.workspaceToDom(bricklyWorkspace);
        var xmlText = Blockly.Xml.domToText(dom);
        CONFIGURATION.saveConfigurationToServer(guiStateController.getConfigurationName(), xmlText, function(result) {
            if (result.rc === 'ok') {
                guiStateController.setConfigurationSaved(true);
                LOG.info('save brick configuration ' + guiStateController.getConfigurationName());
            }
            MSG.displayInformation(result, "MESSAGE_EDIT_SAVE_CONFIGURATION", result.message, guiStateController.getConfigurationName());
        });
    }
    exports.saveToServer = saveToServer;

    /**
     * Save configuration with new name to server
     */
    function saveAsToServer() {
        $formSingleModal.validate();
        if ($formSingleModal.valid()) {
            $('.modal').modal('hide'); // close all opened popups
            var confName = $('#singleModalInput').val().trim();
            var dom = Blockly.Xml.workspaceToDom(bricklyWorkspace);
            var xmlText = Blockly.Xml.domToText(dom);
            CONFIGURATION.saveAsConfigurationToServer(confName, xmlText, function(result) {
                if (result.rc === 'ok') {
                    result.name = confName;
                    guiStateController.setConfiguration(result);
                    LOG.info('save brick configuration ' + guiStateController.getConfigurationName());
                }
                MSG.displayInformation(result, "MESSAGE_EDIT_SAVE_CONFIGURATION_AS", result.message, guiStateController.getConfigurationName());
            });
        }
    }
    exports.saveAsToServer = saveAsToServer;

    /**
     * Load the configuration that was selected in configurations list
     */
    function loadFromListing(conf) {
        LOG.info('loadFromList ' + conf[0]);
        CONFIGURATION.loadConfigurationFromListing(conf[0], conf[1], function(result) {
            if (result.rc === 'ok') {
                result.name = conf[0];
                $('#tabConfiguration').one('shown.bs.tab', function() {
                    showConfiguration(result);
                });
                $('#tabConfiguration').trigger('click');
            }
            MSG.displayInformation(result, "", result.message);
        });
    }
    exports.loadFromListing = loadFromListing;

    function initConfigurationEnvironment() {
        var conf = guiStateController.getConfigurationConf();
        configurationToBricklyWorkspace(conf);
        if (isVisible()) {
            if ($(window).width() < 768) {
                x = $(window).width() / 50;
                y = 25;
            } else {
                x = $(window).width() / 5;
                y = 50;
            }
            var blocks = bricklyWorkspace.getTopBlocks(true);
            if (blocks[0]) {
                var coord = blocks[0].getRelativeToSurfaceXY();
                blocks[0].moveBy(x - coord.x, y - coord.y);
            }
            seen = true;
        } else {
            seen = false
        }
        var dom = Blockly.Xml.workspaceToDom(bricklyWorkspace);
        var xml = Blockly.Xml.domToText(dom);
        guiStateController.setConfigurationXML(xml);
    }
    exports.initConfigurationEnvironment = initConfigurationEnvironment;

    function showSaveAsModal() {
        $.validator.addMethod("regex", function(value, element, regexp) {
            value = value.trim();
            return value.match(regexp);
        }, "No special Characters allowed here. Use only upper and lowercase letters (A through Z; a through z) and numbers.");

        UTIL.showSingleModal(function() {
            $('#singleModalInput').attr('type', 'text');
            $('#single-modal h3').text(Blockly.Msg["MENU_SAVE_AS"]);
            $('#single-modal label').text(Blockly.Msg["POPUP_NAME"]);
        }, saveAsToServer, function() {

        }, {
            rules : {
                singleModalInput : {
                    required : true,
                    regex : /^[a-zA-Z_öäüÖÄÜß$€][a-zA-Z0-9_öäüÖÄÜß$€]*$/
                }
            },
            errorClass : "form-invalid",
            errorPlacement : function(label, element) {
                label.insertAfter(element);
            },
            messages : {
                singleModalInput : {
                    required : jQuery.validator.format(Blockly.Msg["VALIDATION_FIELD_REQUIRED"]),
                    regex : jQuery.validator.format(Blockly.Msg["MESSAGE_INVALID_NAME"])
                }
            }
        });
    }
    exports.showSaveAsModal = showSaveAsModal;

    /**
     * New configuration
     */
    function newConfiguration(opt_further) {
        var further = opt_further || false;
        if (further || guiStateController.isConfigurationSaved()) {
            var result = {};
            result.name = guiStateController.getRobot().toUpperCase() + "basis";
            result.lastChanged = '';
            guiStateController.setConfiguration(result);
            initConfigurationEnvironment();
        } else {
            $('#confirmContinue').data('type', 'configuration');
            if (guiStateController.isUserLoggedIn()) {
                MSG.displayMessage("POPUP_BEFOREUNLOAD_LOGGEDIN", "POPUP", "", true);
            } else {
                MSG.displayMessage("POPUP_BEFOREUNLOAD", "POPUP", "", true);
            }
        }
    }
    exports.newConfiguration = newConfiguration;

    /**
     * Show configuration
     * 
     * @param {load}
     *            load configuration
     * @param {data}
     *            data of server call
     */
    function showConfiguration(result) {
        if (result.rc == 'ok') {
            configurationToBricklyWorkspace(result.data);
            guiStateController.setConfiguration(result);
            LOG.info('show configuration ' + guiStateController.getConfigurationName());
        }
    }
    exports.showConfiguration = showConfiguration;

    function getBricklyWorkspace() {
        return bricklyWorkspace;
    }
    exports.getBricklyWorkspace = getBricklyWorkspace;

    function reloadConf() {
        var conf = guiStateController.getConfigurationXML();
        configurationToBricklyWorkspace(conf);
        if (!seen) {
            if ($(window).width() < 768) {
                x = $(window).width() / 50;
                y = 25;
            } else {
                x = $(window).width() / 5;
                y = 50;
            }
            var blocks = bricklyWorkspace.getTopBlocks(true);
            if (blocks[0]) {
                var coord = blocks[0].getRelativeToSurfaceXY();
                blocks[0].moveBy(x - coord.x, y - coord.y);
            }
            seen = true;
        }
    }

    function reloadView() {
        if (isVisible()) {
            var dom = Blockly.Xml.workspaceToDom(bricklyWorkspace);
            var xml = Blockly.Xml.domToText(dom);
            configurationToBricklyWorkspace(xml);
        }
        var toolbox = guiStateController.getConfigurationToolbox();
        bricklyWorkspace.updateToolbox(toolbox);
    }
    exports.reloadView = reloadView;

    function resetView() {
        bricklyWorkspace.setDevice(guiStateController.getRobotGroup());
        bricklyWorkspace.setVersion('2.0');
        initConfigurationEnvironment();
        var toolbox = guiStateController.getConfigurationToolbox();
        bricklyWorkspace.updateToolbox(toolbox);
    }
    exports.resetView = resetView;

    function isVisible() {
        return guiStateController.getView() == 'tabConfiguration';
    }

    function configurationToBricklyWorkspace(xml) {
        // removing changelistener in blockly doesn't work, so no other way
        listenToBricklyEvents = false;
        Blockly.hideChaff();
        bricklyWorkspace.clear();
        Blockly.svgResize(bricklyWorkspace);
        var dom = Blockly.Xml.textToDom(xml, bricklyWorkspace);
        Blockly.Xml.domToWorkspace(dom, bricklyWorkspace);
        setTimeout(function() {
            listenToBricklyEvents = true;
        }, 500);
        if (guiStateController.isConfigurationUsed()) {
            bricklyWorkspace.setVisible(true);
        } else {
            bricklyWorkspace.setVisible(false);
        }
    }
});
