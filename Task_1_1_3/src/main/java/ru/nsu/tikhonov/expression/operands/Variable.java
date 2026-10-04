package ru.nsu.tikhonov.expression.operands;

import java.util.Map;

import ru.nsu.tikhonov.expression.Expression;

/**
 * Выражение, представляющее переменную.
 */
public class Variable extends Expression {

    private final String name;

    /**
     * Создаёт переменное выражение.
     */
    public Variable(String name) {
        this.name = name;
    }

    /**
     * Возвращает производную переменной.
     */
    @Override
    public Expression derivative(String variable) {
        if (name.equals(variable)) {
            return new Number(1);
        }

        return new Number(0);
    }

    /**
     * Вычисляет значение переменной.
     */
    @Override
    public int eval(Map<String, Integer> variables) {
        return variables.get(name);
    }

    /**
     * Возвращает имя переменной.
     */
    @Override
    public String toString() {
        return name;
    }
}