package ru.nsu.tikhonov.expression.operations;

import java.util.Map;
import ru.nsu.tikhonov.expression.Expression;

/**
 * Выражение, представляющее частное двух выражений.
 */
public class Div extends BinaryExpression {

    /**
     * Создаёт выражение частного.
     */
    public Div(Expression left, Expression right) {
        super(left, right);
    }

    /**
     * Возвращает производную частного.
     */
    @Override
    public Expression derivative(String variable) {
        Expression numerator = new Sub(
                new Mul(left.derivative(variable), right),
                new Mul(left, right.derivative(variable))
        );

        Expression denominator = new Mul(right, right);

        return new Div(numerator, denominator);
    }

    /**
     * Вычисляет значение частного.
     */
    @Override
    public int eval(Map<String, Integer> variables) {
        return left.eval(variables) / right.eval(variables);
    }

    /**
     * Возвращает строковое представление частного.
     */
    @Override
    public String toString() {
        return "(" + left + "/" + right + ")";
    }
}