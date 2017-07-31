package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.sensor.brickpi.DetectFace;
import de.fhg.iais.roberta.syntax.sensor.brickpi.SpeechRecognition;
import de.fhg.iais.roberta.syntax.sensor.brickpi.EmotionRecognition;
import de.fhg.iais.roberta.syntax.sensor.brickpi.DescribeImage;
import de.fhg.iais.roberta.syntax.sensor.brickpi.OCR;
import de.fhg.iais.roberta.syntax.action.brickpi.SayText;
import de.fhg.iais.roberta.syntax.action.brickpi.SetWakeupWord;

/**
 * Interface to be used with the visitor pattern to traverse an AST (and generate code, e.g.).
 */
public interface BrickpiAstVisitor<V> extends AstVisitor<V> {

    /**
     * visit a {@link DetectFace}.
     *
     * @param DetectFace on phrase to be visited
     */
    V visitDetectFace(DetectFace<V> detectFace);

    /**
     * visit a {@link SpeechRecognition}.
     *
     * @param SpeechRecognition on phrase to be visited
     */
    V visitSpeechRecognition(SpeechRecognition<V> speechRecognition);

    /**
     * visit a {@link Speak}.
     *
     * @param Speak on phrase to be visited
     */
    V visitSayText(SayText<V> sayText);

    /**
     * visit a {@link OCR}.
     *
     * @param OCR on phrase to be visited
     */
    V visitOCR(OCR<V> ocr);

    /**
     * visit a {@link EmotionRecognition}.
     *
     * @param EmotionRecognition on phrase to be visited
     */
    V visitEmotionRecognition(EmotionRecognition<V> emotionRecognition);

    /**
     * visit a {@link DescribeImage}.
     *
     * @param DescribeImage on phrase to be visited
     */
    V visitDescribeImage(DescribeImage<V> describeImage);

    /**
     * visit a {@link SetWakeupWord}.
     *
     * @param SetWakeupWord on phrase to be visited
     */
    V visitSetWakeupWord(SetWakeupWord<V> setWakeupWord);
}