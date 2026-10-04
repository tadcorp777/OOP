package ru.nsu.tikhonov.expression.operations;

import java.util.Map;

import ru.nsu.tikhonov.expression.Expression;

/**
 * Выражение, представляющее произведение двух выражений.
 */
public class Mul extends BinaryExpression {

    /**
     * Создаёт выражение произведения.
     */
    public Mul(Expression left, Expression right) {
        super(left, right);
    }

    /**
     * Возвращает производную произведения.
     */
    @Override
    public Expression derivative(String variable) {
        return new Add(
                new Mul(left.derivative(variable), right),
                new Mul(left, right.derivative(variable))
        );
    }

    /**
     * Вычисляет значение произведения.
     */
    @Override
    public int eval(Map<String, Integer> variables) {
        return left.eval(variables) * right.eval(variables);
    }

    /**
     * Возвращает строковое представление произведения.
     */
    @Override
    public String toString() {
        return "(" + left + "*" + right + ")";
    }
}