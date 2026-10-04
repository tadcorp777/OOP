package ru.nsu.tikhonov.expression.operations;

import java.util.Map;

import ru.nsu.tikhonov.expression.Expression;

/**
 * Выражение, представляющее сумму двух выражений.
 */
public class Add extends BinaryExpression {

    /**
     * Создаёт выражение суммы.
     */
    public Add(Expression left, Expression right) {
        super(left, right);
    }

    /**
     * Возвращает производную суммы.
     */
    @Override
    public Expression derivative(String variable) {
        return new Add(
                left.derivative(variable),
                right.derivative(variable)
        );
    }

    /**
     * Вычисляет значение суммы.
     */
    @Override
    public int eval(Map<String, Integer> variables) {
        return left.eval(variables) + right.eval(variables);
    }

    /**
     * Возвращает строковое представление суммы.
     */
    @Override
    public String toString() {
        return "(" + left + "+" + right + ")";
    }
}