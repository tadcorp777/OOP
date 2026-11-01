
package ru.nsu.tikhonov.expression.operations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import ru.nsu.tikhonov.expression.Expression;
import ru.nsu.tikhonov.expression.operands.Number;
import ru.nsu.tikhonov.expression.operands.Variable;

/**
 * Тесты для операции деления.
 */
class DivTest {

    /**
     * Строковое представление частного.
     */
    @Test
    void toStringTest() {
        Expression expression = new Div(
                new Number(10),
                new Variable("x")
        );

        assertEquals("(10/x)", expression.toString());
    }

    /**
     * Вычисление частного.
     */
    @Test
    void evalTest() {
        Expression expression = new Div(
                new Number(10),
                new Number(5)
        );

        assertEquals(2, expression.eval(""));
    }

    /**
     * Деление на ноль.
     */
    @Test
    void divisionByZeroTest() {
        Expression expression = new Div(
                new Number(10),
                new Number(0)
        );

        assertThrows(
                ArithmeticException.class,
                () -> expression.eval("")
        );
    }

    /**
     * Производная частного.
     */
    @Test
    void derivativeTest() {
        Expression expression = new Div(
                new Mul(
                        new Variable("x"),
                        new Variable("x")
                ),
                new Variable("x")
        );

        Expression derivative = expression.derivative("x");

        assertEquals(
                "(((((1*x)+(x*1))*x)-((x*x)*1))/(x*x))",
                derivative.toString()
        );
        assertEquals(1, derivative.eval("x=2"));
    }
}
