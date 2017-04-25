package de.fhg.iais.roberta.syntax.stmt;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.methods.Method;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.AstLanguageVisitor;
import de.fhg.iais.roberta.visitor.AstVisitor;

/**
 * Wraps subclasses of the class {@link Method} so they can be used as {@link Stmt} in statements.
 */
public class MethodStmt<V> extends Stmt<V> {
    private final Method<V> method;

    private MethodStmt(Method<V> method) {
        super(BlockTypeContainer.getByName("METHOD_STMT"), method.getProperty(), method.getComment());
        Assert.isTrue(method != null && method.isReadOnly());
        this.method = method;
        setReadOnly();
    }

    /**
     * Create object of the class {@link MethodStmt}.
     *
     * @param method that we want to wrap
     * @return statement with wrapped method inside
     */
    public static <V> MethodStmt<V> make(Method<V> method) {
        return new MethodStmt<V>(method);
    }

    /**
     * @return the method
     */
    public Method<V> getMethod() {
        return this.method;
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((AstLanguageVisitor<V>) visitor).visitMethodStmt(this);

    }

    @Override
    public String toString() {
        return "MethodStmt [" + this.method + "]";
    }

    @Override
    public Block astToBlock() {
        return getMethod().astToBlock();
    }
}
