package ru.nsu.tikhonov.expression.operations;

import ru.nsu.tikhonov.expression.Expression;

/**
 * Базовый класс для бинарных математических операций.
 */
public abstract class BinaryExpression extends Expression {

    protected final Expression left;
    protected final Expression right;

    /**
     * Создаёт бинарное выражение.
     */
    protected BinaryExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }
}