package ru.nsu.tikhonov;

import java.util.Scanner;
import ru.nsu.tikhonov.expression.Expression;
import ru.nsu.tikhonov.expression.parser.ExpressionParser;

/**
 * Главный класс программы для работы с математическими выражениями.
 */
public class Main {

    /**
     * Запускает программу.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ExpressionParser parser = new ExpressionParser();

        System.out.println("Введите математическое выражение:");
        String input = scanner.nextLine();

        Expression expression = parser.parse(input);

        System.out.println("Выражение:");
        expression.print();

        System.out.println();
        System.out.println("Введите переменную, по которой нужно взять производную:");
        String variable = scanner.nextLine();

        Expression derivative = expression.derivative(variable);

        System.out.println("Производная:");
        derivative.print();

        System.out.println();
        System.out.println("Введите значения переменных для вычисления выражения:");
        System.out.println("Например: x = 10; y = 13");
        String assignments = scanner.nextLine();

        int result = expression.eval(assignments);

        System.out.println("Значение выражения:");
        System.out.println(result);

        scanner.close();
    }
}