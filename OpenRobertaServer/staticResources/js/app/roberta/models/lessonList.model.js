/**
 * Rest calls to the server related to program operations (save, delete,
 * share...)
 * 
 * @module rest/lesson
 */
define([ 'exports', 'comm' ], function(exports, COMM) {

    /**
     * Refresh program list
     */
    function loadLessonList(successFn) {
        COMM.json("/lesson", {
            "cmd" : "loadLesson"
        }, successFn, "load lesson list");
    }
    exports.loadLessonList = loadLessonList;

});
