package ru.nsu.tikhonov.expression.parser;

import ru.nsu.tikhonov.expression.Expression;
import ru.nsu.tikhonov.expression.operands.Number;
import ru.nsu.tikhonov.expression.operands.Variable;
import ru.nsu.tikhonov.expression.operations.Add;
import ru.nsu.tikhonov.expression.operations.Div;
import ru.nsu.tikhonov.expression.operations.Mul;
import ru.nsu.tikhonov.expression.operations.Sub;

/**
 * Класс для создания выражения из строки.
 */
public class ExpressionParser {

    private String input;
    private int position;

    /**
     * Создаёт парсер выражений.
     */
    public ExpressionParser() {
    }

    /**
     * Создаёт выражение по заданной строке.
     */
    public Expression parse(String input) {
        this.input = input;
        this.position = 0;

        Expression result = parseExpression();

        skipSpaces();

        if (position != input.length()) {
            throw new IllegalArgumentException(
                    "Лишние символы в конце выражения: " + input.substring(position)
            );
        }

        return result;
    }

    /**
     * Разбирает одно выражение.
     */
    private Expression parseExpression() {
        skipSpaces();

        if (position >= input.length()) {
            throw new IllegalArgumentException("Неожиданный конец выражения");
        }

        char current = input.charAt(position);

        if (current == '(') {
            return parseOperation();
        }

        if (Character.isDigit(current)
                || (current == '-' && hasNextDigit())) {
            return parseNumber();
        }

        if (Character.isLetter(current)) {
            return parseVariable();
        }

        throw new IllegalArgumentException(
                "Неожиданный символ: " + current
        );
    }

    /**
     * Разбирает выражение, состоящее из двух операндов.
     */
    private Expression parseOperation() {
        position++;

        Expression left = parseExpression();

        skipSpaces();

        if (position >= input.length()) {
            throw new IllegalArgumentException(
                    "Ожидался оператор"
            );
        }

        char operator = input.charAt(position);
        position++;

        Expression right = parseExpression();

        skipSpaces();

        if (position >= input.length() || input.charAt(position) != ')') {
            throw new IllegalArgumentException(
                    "Ожидалась закрывающая скобка"
            );
        }

        position++;

        return createOperation(operator, left, right);
    }

    /**
     * Создаёт объект операции по её символу.
     */
    private Expression createOperation(
            char operator,
            Expression left,
            Expression right
    ) {
        switch (operator) {
            case '+':
                return new Add(left, right);

            case '-':
                return new Sub(left, right);

            case '*':
                return new Mul(left, right);

            case '/':
                return new Div(left, right);

            default:
                throw new IllegalArgumentException(
                        "Неизвестный оператор: " + operator
                );
        }
    }

    /**
     * Разбирает числовую константу.
     */
    private Expression parseNumber() {
        int start = position;

        if (input.charAt(position) == '-') {
            position++;
        }

        while (position < input.length()
                && Character.isDigit(input.charAt(position))) {
            position++;
        }

        int value = Integer.parseInt(
                input.substring(start, position)
        );

        return new Number(value);
    }

    /**
     * Разбирает имя переменной.
     */
    private Expression parseVariable() {
        int start = position;

        while (position < input.length()
                && Character.isLetterOrDigit(input.charAt(position))) {
            position++;
        }

        String name = input.substring(start, position);

        return new Variable(name);
    }

    /**
     * Проверяет, является ли следующий символ цифрой.
     */
    private boolean hasNextDigit() {
        return position + 1 < input.length()
                && Character.isDigit(input.charAt(position + 1));
    }

    /**
     * Пропускает пробелы в строке.
     */
    private void skipSpaces() {
        while (position < input.length()
                && Character.isWhitespace(input.charAt(position))) {
            position++;
        }
    }
}