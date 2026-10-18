package ru.nsu.tikhonov;

import java.util.Scanner;
import ru.nsu.tikhonov.expression.Expression;
import ru.nsu.tikhonov.expression.parser.ExpressionParser;

import ru.nsu.tikhonov.expression.operands.Number;
import ru.nsu.tikhonov.expression.operands.Variable;
import ru.nsu.tikhonov.expression.operations.Add;
import ru.nsu.tikhonov.expression.operations.Div;
import ru.nsu.tikhonov.expression.operations.Mul;
import ru.nsu.tikhonov.expression.operations.Sub;

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

        try {
            System.out.println("Введите математическое выражение:");
            String input = scanner.nextLine();
            Expression expression = parser.parse(input);

            System.out.println("Выражение:");
            expression.print();

            System.out.println();
            System.out.println("Введите переменную для производной:");
            String variable = scanner.nextLine();
            Expression derivative = expression.derivative(variable);

            System.out.println("Производная:");
            derivative.print();

            System.out.println();
            System.out.println("Введите значения переменных (например: x = 10; y = 13):");
            String assignments = scanner.nextLine();
            System.out.println("Значение выражения:");
            System.out.println(expression.eval(assignments));

            System.out.println("Значение производной:");
            System.out.println(derivative.eval(assignments));

        } catch (IllegalArgumentException e) {
            System.out.println("Некорректный ввод. " + e.getMessage());
        } catch (ArithmeticException e) {
            System.out.println("Арифметическая ошибка. " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("Не задано значение переменной.");
        } catch (RuntimeException e) {
            System.out.println("Ошибка. " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
