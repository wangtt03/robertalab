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
            $("#select-lesson").modal("show");
        });
    }
    exports.loadLessonAll = loadLessonAll;

    function initProgList() {
        var proto = $('#popup-lesson-example');
        for (var i = 0; i < 5; i++) {
            var clone = proto.clone().prop('id', 'menu-lesson-' + i.toString());
            clone.attr('data-type', 'ev3');
            clone.find('span:eq( 1 )').text('Lesson ' + i.toString());
            $("#popup-lesson-container").append(clone);
            proto.remove();
        }
    }

    function initProgListEvents() {



        function update(result) {

        }
    }

});
