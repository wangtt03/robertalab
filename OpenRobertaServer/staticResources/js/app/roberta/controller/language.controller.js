define([ 'exports', 'log', 'jquery', 'guiState.controller', 'program.controller', 'configuration.controller', 'user.controller', 'progHelp.controller' ], function(
        exports, LOG, $, GUISTATE_C, PROGRAM_C, CONFIGURATION_C, USER_C, HELP_C) {

    /**
     * Initialize language switching
     */
    function init() {
        var ready = new $.Deferred();
        var language;
        if (navigator.language.indexOf("de") > -1) {
            language = 'de';
        } else if (navigator.language.indexOf("fi") > -1) {
            language = 'fi';
        } else if (navigator.language.indexOf("da") > -1) {
            language = 'da';
        } else if (navigator.language.indexOf("es") > -1) {
            language = 'es';
        } else if (navigator.language.indexOf("fr") > -1) {
            language = 'fr';
        } else if (navigator.language.indexOf("it") > -1) {
            language = 'it';
        } else if (navigator.language.indexOf("ca") > -1) {
            language = 'ca';
        } else if (navigator.language.indexOf("pt") > -1) {
            language = 'pt';
        } else if (navigator.language.indexOf("pl") > -1) {
            language = 'pl';
        } else if (navigator.language.indexOf("ru") > -1) {
            language = 'ru';
        } else if (navigator.language.indexOf("cs") > -1) {
            language = 'cs';
//        } else if (navigator.language.indexOf("eu") > -1) {
//            language = 'eu';
//        } else if (navigator.language.indexOf("gl") > -1) {
//            language = 'gl';
        } else {
            language = 'en';
        }
        if (language === 'de') {
            $('.EN').css('display', 'none');
            $('.DE').css('display', 'inline');
        } else {
            $('.DE').css('display', 'none');
            $('.EN').css('display', 'inline');
        }
        $('#language li a[lang=' + language + ']').parent().addClass('disabled');
        var url = 'blockly/msg/js/' + language + '.js';
        getCachedScript(url).done(function(data) {
            translate();
            ready.resolve(language);
        });

        initEvents();
        return ready.promise(language);
    }

    exports.init = init;

    function initEvents() {

        $('#language').on('click', 'li a', function() {
            LOG.info('language clicked');
            var language = $(this).attr('lang');
            switchLanguage(language);
        }), 'switch language clicked';
    }

    function switchLanguage(language) {

        if (GUISTATE_C.getLanguage == language) {
            return;
        }
        GUISTATE_C.setLanguage(language);

        var url = 'blockly/msg/js/' + language.toLowerCase() + '.js';
        getCachedScript(url).done(function(data) {
            translate();
            PROGRAM_C.reloadView();
            CONFIGURATION_C.reloadView();
            USER_C.initValidationMessages();
            HELP_C.initView();
            var value = Blockly.Msg.MENU_START_BRICK;
            if (value.indexOf("$") >= 0) {
                value = value.replace("$", GUISTATE_C.getRobotRealName());
            }
            $('#menuRunProg').text(value);
            if (GUISTATE_C.getBlocklyWorkspace())
                GUISTATE_C.getBlocklyWorkspace().robControls.refreshTooltips(GUISTATE_C.getRobotRealName());
        });
        LOG.info('language switched to ' + language);
    }

    /**
     * Translate the web page
     */
    function translate() {
        $("[lkey]").each(function(index) {
            var lkey = $(this).attr('lkey');
            var key = lkey.replace("Blockly.Msg.", "");
            var value = Blockly.Msg[key];
            if (value == undefined) {
                console.log('UNDEFINED    key : value = ' + key + ' : ' + value);
            }
            if (lkey === 'Blockly.Msg.MENU_EDIT_TOOLTIP') {
                $('#head-navi-tooltip-program').attr('data-original-title', value);
                $('#head-navi-tooltip-configuration').attr('data-original-title', value);
            } else if (lkey == 'Blockly.Msg.MENU_SHOW_CODE') {
                $('#progCode').attr('data-original-title', value);
                $('#menuShowCode').html(value);
            } else if (lkey == 'Blockly.Msg.MENU_START_SIM') {
                $('#progSim').attr('data-original-title', value);
                $('#menuRunSim').html(value);
            } else if (lkey == 'Blockly.Msg.MENU_RIGHT_INFO_TOOLTIP') {
                $('#progInfo').attr('data-original-title', value);
            } else if (lkey == 'Blockly.Msg.MENU_RIGHT_HELP_TOOLTIP') {
                $('#progHelp').attr('data-original-title', value);
            } else if (lkey === 'Blockly.Msg.MENU_ROBOT_TOOLTIP') {
                $('#head-navi-tooltip-robot').attr('data-original-title', value);
            } else if (lkey === 'Blockly.Msg.MENU_HELP_TOOLTIP') {
                $('#head-navi-tooltip-help').attr('data-original-title', value);
            } else if (lkey === 'Blockly.Msg.MENU_USER_TOOLTIP') {
                $('#head-navi-tooltip-user').attr('data-original-title', value);
            } else if (lkey === 'Blockly.Msg.MENU_GALLERY_TOOLTIP') {
                $('#head-navi-tooltip-gallery').attr('data-original-title', value);
            } else if (lkey === 'Blockly.Msg.MENU_LANGUAGE_TOOLTIP') {
                $('#head-navi-tooltip-language').attr('data-original-title', value);
            } else if (lkey === 'Blockly.Msg.MENU_USER_STATE_TOOLTIP') {
                $('#iconDisplayLogin').attr('data-original-title', value);
            } else if (lkey === 'Blockly.Msg.MENU_ROBOT_STATE_TOOLTIP') {
                $('#iconDisplayRobotState').attr('data-original-title', value);
            } else if (lkey === "Blockly.Msg.MENU_SIM_START_TOOLTIP") {
                $('#simControl').attr('data-original-title', value);
            } else if (lkey === "Blockly.Msg.MENU_SIM_SCENE_TOOLTIP") {
                $('#simScene').attr('data-original-title', value);
            } else if (lkey === "Blockly.Msg.MENU_SIM_ROBOT_TOOLTIP") {
                $('#simRobot').attr('data-original-title', value);
            } else if (lkey == 'Blockly.Msg.MENU_SIM_VALUES_TOOLTIP') {
                $('#simValues').attr('data-original-title', value);
            } else if (lkey == 'Blockly.Msg.MENU_SIM_IMPORT_TOOLTIP') {
                $('#simImport').attr('data-original-title', value);
            } else if (lkey == 'Blockly.Msg.MENU_CODE_DOWNLOAD_TOOLTIP') {
                $('#codeDownload').attr('data-original-title', value);
            } else if (lkey == 'Blockly.Msg.MENU_CODE_REFRESH_TOOLTIP') {
                $('#codeRefresh').attr('data-original-title', value);
            } else if (lkey === "Blockly.Msg.BUTTON_EMPTY_LIST") {
                $('#logList>.bootstrap-table').find('button[name="refresh"]').attr('data-original-title', value);
            } else if (lkey === "Blockly.Msg.LIST_BACK_TOOLTIP") {
                $('.bootstrap-table').find('.backList').attr('data-original-title', value);
            } else if (lkey == 'Blockly.Msg.PROGLIST_DELETE_ALL_TOOLTIP') {
                $('#deleteSomeProg').attr('data-original-title', value);
            } else if (lkey == 'Blockly.Msg.PROGLIST_DELETE_TOOLTIP') {
                $('#programNameTable').find('.delete').attr('data-original-title', value);
            } else if (lkey == 'Blockly.Msg.PROGLIST_SHARE_TOOLTIP') {
                $('#programNameTable').find('.share').attr('data-original-title', value);
            } else if (lkey == 'Blockly.Msg.PROGLIST_LOAD_TOOLTIP') {
                $('#programNameTable').find('.load').attr('data-original-title', value);
            } else if (lkey == 'Blockly.Msg.CONFLIST_DELETE_ALL_TOOLTIP') {
                $('#deleteSomeConf').attr('data-original-title', value);
            } else if (lkey == 'Blockly.Msg.CONFLIST_DELETE_TOOLTIP') {
                $('#confNameTable').find('.delete').attr('data-original-title', value);
            } else if (lkey == 'Blockly.Msg.CONFLIST_LOAD_TOOLTIP') {
                $('#confNameTable').find('.load').attr('data-original-title', value);
            } else {
                $(this).html(value);
                $(this).attr('value', value);
            }
        });
    }

    /**
     * $.getScript() will append a timestamped query parameter to the url to
     * prevent caching. The cache control should be handled using http-headers.
     * see https://api.jquery.com/jquery.getscript/#caching-requests
     */
    function getCachedScript(url, options) {
        // Allow user to set any option except for dataType, cache, and url
        options = $.extend(options || {}, {
            dataType : "script",
            cache : true,
            url : url
        });

        // Use $.ajax() since it is more flexible than $.getScript
        // Return the jqXHR object so we can chain callbacks
        return jQuery.ajax(options);
    }
});
