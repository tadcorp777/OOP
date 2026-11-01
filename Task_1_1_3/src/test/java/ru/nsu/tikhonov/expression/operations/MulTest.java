package ru.nsu.tikhonov.expression.operations;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import ru.nsu.tikhonov.expression.Expression;
import ru.nsu.tikhonov.expression.operands.Number;
import ru.nsu.tikhonov.expression.operands.Variable;

/**
 * Тесты для операции умножения.
 */
class MulTest {

    /**
     * Строковое представление произведения.
     */
    @Test
    void toStringTest() {
        Expression expression = new Mul(
                new Number(2),
                new Variable("x")
        );

        assertEquals("(2*x)", expression.toString());
    }

    /**
     * Вычисление произведения.
     */
    @Test
    void evalTest() {
        Expression expression = new Mul(
                new Number(2),
                new Variable("x")
        );

        assertEquals(20, expression.eval("x = 10"));
    }

    /**
     * Производная произведения.
     */
    @Test
    void derivativeTest() {
        Expression expression = new Mul(
                new Number(2),
                new Variable("x")
        );

        Expression derivative = expression.derivative("x");

        assertEquals("((0*x)+(2*1))", derivative.toString());
        assertEquals(2, derivative.eval("x = 10"));
    }

    /**
     * Производная произведения двух переменных.
     */
    @Test
    void derivativeOfTwoVariablesTest() {
        Expression expression = new Mul(
                new Variable("x"),
                new Variable("y")
        );

        Expression derivative = expression.derivative("x");

        assertEquals("((1*y)+(x*0))", derivative.toString());
        assertEquals(5, derivative.eval("x = 10; y = 5"));
    }

    /**
     * Произведение двух составных выражений.
     */
    @Test
    void complexExpressionTest() {
        Expression expression = new Mul(
                new Add(new Number(2), new Variable("x")),
                new Sub(new Variable("y"), new Number(1))
        );

        assertEquals("((2+x)*(y-1))", expression.toString());
        assertEquals(36, expression.eval("x = 4; y = 7"));
    }
}
