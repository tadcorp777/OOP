package ru.nsu.tikhonov.expression.operations;

import ru.nsu.tikhonov.expression.Expression;

import java.util.Map;

/**
 * Выражение, представляющее разность двух выражений.
 */
public class Sub extends BinaryExpression {

    /**
     * Создаёт выражение разности.
     */
    public Sub(Expression left, Expression right) {
        super(left, right);
    }

    /**
     * Возвращает производную разности.
     */
    @Override
    public Expression derivative(String variable) {
        return new Sub(
                left.derivative(variable),
                right.derivative(variable)
        );
    }

    /**
     * Вычисляет значение разности.
     */
    @Override
    public int eval(Map<String, Integer> variables) {
        return left.eval(variables) - right.eval(variables);
    }

    /**
     * Возвращает строковое представление разности.
     */
    @Override
    public String toString() {
        return "(" + left + "-" + right + ")";
    }
}