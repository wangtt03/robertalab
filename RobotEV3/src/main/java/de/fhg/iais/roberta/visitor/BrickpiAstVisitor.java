package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.sensor.brickpi.DetectFace;

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
}