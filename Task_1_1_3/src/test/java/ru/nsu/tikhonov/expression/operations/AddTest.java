package ru.nsu.tikhonov.expression.operations;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import ru.nsu.tikhonov.expression.Expression;
import ru.nsu.tikhonov.expression.operands.Number;
import ru.nsu.tikhonov.expression.operands.Variable;

/**
 * Тесты для операции сложения.
 */
class AddTest {

    /**
     * Строковое представление суммы.
     */
    @Test
    void toStringTest() {
        Expression expression = new Add(
                new Number(3),
                new Variable("x")
        );

        assertEquals("(3+x)", expression.toString());
    }

    /**
     * Вычисление суммы.
     */
    @Test
    void evalTest() {
        Expression expression = new Add(
                new Number(3),
                new Variable("x")
        );

        assertEquals(13, expression.eval("x = 10"));
    }

    /**
     * Производная суммы.
     */
    @Test
    void derivativeTest() {
        Expression expression = new Add(
                new Number(3),
                new Variable("x")
        );

        Expression derivative = expression.derivative("x");

        assertEquals("(0+1)", derivative.toString());
        assertEquals(1, derivative.eval(""));
    }

    /**
     * Сложение двух составных выражений.
     */
    @Test
    void complexExpressionTest() {
        Expression expression = new Add(
                new Mul(new Number(2), new Variable("x")),
                new Sub(new Variable("y"), new Number(3))
        );

        assertEquals("((2*x)+(y-3))", expression.toString());
        assertEquals(12, expression.eval("x = 5; y = 5"));
    }
}