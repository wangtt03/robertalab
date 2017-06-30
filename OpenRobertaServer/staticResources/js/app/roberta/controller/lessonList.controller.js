define([ 'require', 'exports', 'log', 'util', 'comm', 'progList.model', 'lessonList.model', 'program.model', 'program.controller', 'blocks-msg', 'jquery', 'bootstrap-table' ], function(require, exports, LOG, UTIL, COMM, PROGLIST, LESSONLIST, PROGRAM, PROGRAM_C, Blockly, $) {

    /**
     * Initialize table of programs
     */
    function init() {

        initProgList();
        initProgListEvents();
        LOG.info('init lesson list view');
    }
    exports.init = init;

    function loadLessonAll() {
        LESSONLIST.loadLessonList(function(){
            alert('success');
            $("#show-lesson").modal("show");
        });
    }
    exports.loadLessonAll = loadLessonAll;

    function initProgList() {


    }

    function initProgListEvents() {



        function update(result) {

        }
    }

});
