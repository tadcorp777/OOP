package ru.nsu.tikhonov.expression.operands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import ru.nsu.tikhonov.expression.Expression;

/**
 * Тесты для переменного выражения.
 */
class VariableTest {

    /**
     * Строковое представление переменной.
     */
    @Test
    void toStringTest() {
        Variable variable = new Variable("x");

        assertEquals("x", variable.toString());
    }

    /**
     * Вычисление значения переменной.
     */
    @Test
    void evalTest() {
        Variable variable = new Variable("x");

        assertEquals(10, variable.eval("x = 10"));
    }

    /**
     * Производная переменной по самой себе.
     */
    @Test
    void derivativeByItselfTest() {
        Variable variable = new Variable("x");

        Expression derivative = variable.derivative("x");

        assertEquals("1", derivative.toString());
        assertEquals(1, derivative.eval(""));
    }

    /**
     * Производная переменной по другой переменной.
     */
    @Test
    void derivativeByAnotherVariableTest() {
        Variable variable = new Variable("x");

        Expression derivative = variable.derivative("y");

        assertEquals("0", derivative.toString());
        assertEquals(0, derivative.eval(""));
    }

    /**
     * Работа с многобуквенным именем переменной.
     */
    @Test
    void longVariableNameTest() {
        Variable variable = new Variable("longVariable");

        assertEquals("longVariable", variable.toString());
        assertEquals(25, variable.eval("longVariable = 25"));
    }
}
