package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.sensor.brickpi.DetectFace;
import de.fhg.iais.roberta.syntax.sensor.brickpi.SpeechRecognition;
import de.fhg.iais.roberta.syntax.sensor.brickpi.OCR;
import de.fhg.iais.roberta.syntax.action.brickpi.SayText;

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
}