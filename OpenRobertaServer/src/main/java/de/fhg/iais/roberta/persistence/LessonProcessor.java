package de.fhg.iais.roberta.persistence;

import de.fhg.iais.roberta.javaServer.restServices.all.ClientLesson;
import de.fhg.iais.roberta.persistence.bo.*;
import de.fhg.iais.roberta.persistence.dao.*;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Pair;
import de.fhg.iais.roberta.util.Util1;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.List;

public class LessonProcessor extends AbstractProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(LessonProcessor.class);

    public LessonProcessor(DbSession dbSession, HttpSessionState httpSessionState) {
        super(dbSession, httpSessionState);
    }

    public JSONArray getLessons(){
        LessonDao lessonDao = new LessonDao(this.dbSession);
        List<Lesson> lessons = lessonDao.loadAll();
        JSONArray programInfos = new JSONArray();
        for (Lesson lesson : lessons) {
            LOG.info(lesson.toString());
            JSONObject lessonJson = new JSONObject();
            try {
                lessonJson.put("name", lesson.getName());
                lessonJson.put("docurl", lesson.getDocUrl());
                lessonJson.put("thumbnail", lesson.getThumbnail());
                lessonJson.put("prgurl", lesson.getProgramUrl());
                lessonJson.put("deviceType", lesson.getDeviceType());
            } catch (JSONException e) {

            }
            programInfos.put(lessonJson);
        }

        setSuccess(Key.LESSON_GET_ALL_SUCCESS);
        return programInfos;
    }
}
