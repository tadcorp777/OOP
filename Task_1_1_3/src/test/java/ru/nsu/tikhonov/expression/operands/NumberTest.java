package ru.nsu.tikhonov.expression.operands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import ru.nsu.tikhonov.expression.Expression;

/**
 * Тесты для числового выражения.
 */
class NumberTest {

    /**
     * Строковое представление числа.
     */
    @Test
    void toStringTest() {
        Number number = new Number(42);

        assertEquals("42", number.toString());
    }

    /**
     * Вычисление значения числа.
     */
    @Test
    void evalTest() {
        Number number = new Number(42);

        assertEquals(42, number.eval(""));
    }

    /**
     * Производная числа равна нулю.
     */
    @Test
    void derivativeTest() {
        Number number = new Number(42);

        Expression derivative = number.derivative("x");

        assertEquals("0", derivative.toString());
        assertEquals(0, derivative.eval(""));
    }
}