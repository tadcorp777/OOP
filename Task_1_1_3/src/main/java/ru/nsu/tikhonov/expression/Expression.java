package ru.nsu.tikhonov.expression;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Базовый класс для всех математических выражений.
 */
public abstract class Expression {

    /**
     * Возвращает производную выражения по заданной переменной.
     */
    public abstract Expression derivative(String variable);

    /**
     * Вычисляет значение выражения при заданных значениях переменных.
     */
    public abstract int eval(Map<String, Integer> variables);

    /**
     * Возвращает строковое представление выражения.
     */
    @Override
    public abstract String toString();

    /**
     * Выводит выражение в консоль.
     */
    public void print() {
        System.out.println(this);
    }

    /**
     * Выводит выражение в переданный поток.
     */
    public void print(PrintWriter out) {
        out.println(this);
    }

    /**
     * Вычисляет выражение по строке с означиваниями переменных.
     */
    public int eval(String assignments) {
        Map<String, Integer> variables = new HashMap<>();

        String[] parts = assignments.split(";");

        for (String part : parts) {
            part = part.trim();

            if (part.isEmpty()) {
                continue;
            }

            String[] assignment = part.split("=");

            String name = assignment[0].trim();
            int value = Integer.parseInt(assignment[1].trim());

            variables.put(name, value);
        }

        return eval(variables);
    }
}