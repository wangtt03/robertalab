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

    function displayLessonMenu() {
        $(".lesson-menu")[0].style.display = "block";
    }
    exports.displayLessonMenu = displayLessonMenu;

    function hideLessonMenu(){
        $(".lesson-menu")[0].style.display = "none";
    }
    exports.hideLessonMenu = hideLessonMenu;

    function initProgList() {
        LESSONLIST.loadLessonList(function (result) {
            var lessonData = result.data;
            var proto = $('#lesson-example');
            console.log(lessonData.length);
            for (var i = 0; i < lessonData.length; i++) {
                var clone = proto.clone().prop('id', 'lesson-' + i.toString());
                clone.find('span:eq( 1 )').text(lessonData[i].name);
                clone.find('span:eq( 0 )').find('img').attr('src', lessonData[i].thumbnail);
                $(".lesson-select-container").append(clone);
            }
            proto.remove();
        });
    }

    function initProgListEvents() {
        function update(result) {

        }
    }

});
