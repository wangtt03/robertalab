define([ 'require', 'exports', 'log', 'util', 'comm', 'progList.model', 'lessonList.model', 'program.model', 'program.controller',  'progHelp.controller', 'robot.controller', 'blocks-msg', 'jquery', 'bootstrap-table' ], function(require, exports, LOG, UTIL, COMM, PROGLIST, LESSONLIST, PROGRAM, PROGRAM_C, PROGHELP_C, ROBOT_C, Blockly, $) {

    /**
     * Initialize table of programs
     */
    var lessonData;

    function init() {
        initLessonList();
        LOG.info('init lesson list view');
    }
    exports.init = init;

    function displayLessonMenu() {
        $(".lesson-menu")[0].style.display = "block";
        $("#lesson-menu-background")[0].style.display = "block";
    }
    exports.displayLessonMenu = displayLessonMenu;

    function hideLessonMenu(){
        $(".lesson-menu")[0].style.display = "none";
        $("#lesson-menu-background")[0].style.display = "none";
        $("#lesson-close")[0].style.display = "block";
    }
    exports.hideLessonMenu = hideLessonMenu;

    function initLessonList() {
        LESSONLIST.loadLessonList(function (result) {
            lessonData = result.data;
            var proto = $('#lesson-example');
            for (var i = 0; i < lessonData.length; i++) {
                var clone = proto.clone().prop('id', 'lesson-' + i.toString());
                clone.find('span:eq( 1 )').text(lessonData[i].name);
                clone.find('span:eq( 0 )').find('img').attr('src', lessonData[i].thumbnail);
                $(".lesson-select-container").append(clone);
            }
            proto.remove();
            initLessonListEvents();
        });
    }

    function initLessonListEvents() {
        $('.lesson-item-container').onWrap('click', function(event){
            var lessonId = parseInt(event.currentTarget.id.substring("lesson-".length));
            PROGHELP_C.initViewWithUrl(lessonData[lessonId].docurl);
            console.log(lessonData[lessonId].deviceType);
            ROBOT_C.switchRobot(lessonData[lessonId].deviceType, true);
            hideLessonMenu();
        }, 'lesson selected');
    }

});
