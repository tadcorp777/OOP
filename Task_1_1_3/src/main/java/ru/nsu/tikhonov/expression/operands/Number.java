package ru.nsu.tikhonov.expression.operands;

import ru.nsu.tikhonov.expression.Expression;

import java.util.Map;

/**
 * Выражение, представляющее целое число.
 */
public class Number extends Expression {

    private final int value;

    /**
     * Создаёт числовое выражение.
     */
    public Number(int value) {
        this.value = value;
    }

    /**
     * Возвращает производную числа.
     */
    @Override
    public Expression derivative(String variable) {
        return new Number(0);
    }

    /**
     * Вычисляет значение числа.
     */
    @Override
    public int eval(Map<String, Integer> variables) {
        return value;
    }

    /**
     * Возвращает строковое представление числа.
     */
    @Override
    public String toString() {
        return String.valueOf(value);
    }
}