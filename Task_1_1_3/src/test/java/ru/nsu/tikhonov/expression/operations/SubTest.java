package ru.nsu.tikhonov.expression.operations;

import org.junit.jupiter.api.Test;
import ru.nsu.tikhonov.expression.Expression;
import ru.nsu.tikhonov.expression.operands.Number;
import ru.nsu.tikhonov.expression.operands.Variable;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Тесты для операции вычитания.
 */
class SubTest {

    /**
     * Строковое представление разности.
     */
    @Test
    void toStringTest() {
        Expression expression = new Sub(
                new Number(10),
                new Variable("x")
        );

        assertEquals("(10-x)", expression.toString());
    }

    /**
     * Вычисление разности.
     */
    @Test
    void evalTest() {
        Expression expression = new Sub(
                new Number(10),
                new Variable("x")
        );

        assertEquals(6, expression.eval("x = 4"));
    }

    /**
     * Производная разности.
     */
    @Test
    void derivativeTest() {
        Expression expression = new Sub(
                new Variable("x"),
                new Number(5)
        );

        Expression derivative = expression.derivative("x");

        assertEquals("(1-0)", derivative.toString());
        assertEquals(1, derivative.eval(""));
    }

    /**
     * Разность двух составных выражений.
     */
    @Test
    void complexExpressionTest() {
        Expression expression = new Sub(
                new Mul(new Number(2), new Variable("x")),
                new Add(new Variable("y"), new Number(3))
        );

        assertEquals("((2*x)-(y+3))", expression.toString());
        assertEquals(2, expression.eval("x = 5; y = 5"));
    }
}