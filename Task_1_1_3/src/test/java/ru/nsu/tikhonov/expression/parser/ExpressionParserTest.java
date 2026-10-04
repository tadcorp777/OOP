package ru.nsu.tikhonov.expression.parser;

import org.junit.jupiter.api.Test;
import ru.nsu.tikhonov.expression.Expression;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тесты для парсера математических выражений.
 */
class ExpressionParserTest {

    private final ExpressionParser parser = new ExpressionParser();

    /**
     * Чтение числовой константы.
     */
    @Test
    void numberTest() {
        Expression expression = parser.parse("42");

        assertEquals("42", expression.toString());
        assertEquals(42, expression.eval(""));
    }

    /**
     *Чтение отрицательной числовой константы.
     */
    @Test
    void negativeNumberTest() {
        Expression expression = parser.parse("-15");

        assertEquals("-15", expression.toString());
        assertEquals(-15, expression.eval(""));
    }

    /**
     * Чтение переменной.
     */
    @Test
    void variableTest() {
        Expression expression = parser.parse("x");

        assertEquals("x", expression.toString());
        assertEquals(10, expression.eval("x = 10"));
    }

    /**
     * Чтение переменной с длинным именем.
     */
    @Test
    void longVariableNameTest() {
        Expression expression = parser.parse("longVariable");

        assertEquals("longVariable", expression.toString());
        assertEquals(25, expression.eval("longVariable = 25"));
    }

    /**
     * Чтение операции сложения.
     */
    @Test
    void addTest() {
        Expression expression = parser.parse("(3+x)");

        assertEquals("(3+x)", expression.toString());
        assertEquals(13, expression.eval("x = 10"));
    }

    /**
     * Чтение операции вычитания.
     */
    @Test
    void subTest() {
        Expression expression = parser.parse("(10-x)");

        assertEquals("(10-x)", expression.toString());
        assertEquals(6, expression.eval("x = 4"));
    }

    /**
     * Чтение операции умножения.
     */
    @Test
    void mulTest() {
        Expression expression = parser.parse("(2*x)");

        assertEquals("(2*x)", expression.toString());
        assertEquals(20, expression.eval("x = 10"));
    }

    /**
     * Чтение операции деления.
     */
    @Test
    void divTest() {
        Expression expression = parser.parse("(10/x)");

        assertEquals("(10/x)", expression.toString());
        assertEquals(2, expression.eval("x = 5"));
    }

    /**
     * Чтение вложенного выражения.
     */
    @Test
    void nestedExpressionTest() {
        Expression expression = parser.parse("(3+(2*x))");

        assertEquals("(3+(2*x))", expression.toString());
        assertEquals(23, expression.eval("x = 10"));
    }

    /**
     * Чтение сложного вложенного выражения.
     */
    @Test
    void complexExpressionTest() {
        Expression expression = parser.parse(
                "((3*x)+(y-(10/2)))"
        );

        assertEquals("((3*x)+(y-(10/2)))", expression.toString());
        assertEquals(33, expression.eval("x = 10; y = 8"));
    }

    /**
     * Работа парсера с пробелами.
     */
    @Test
    void spacesTest() {
        Expression expression = parser.parse(
                " ( 3 + ( 2 * x ) ) "
        );

        assertEquals("(3+(2*x))", expression.toString());
        assertEquals(23, expression.eval("x = 10"));
    }

    /**
     * Использование нескольких переменных.
     */
    @Test
    void multipleVariablesTest() {
        Expression expression = parser.parse(
                "((x*y)+(a-b))"
        );

        assertEquals("((x*y)+(a-b))", expression.toString());
        assertEquals(
                17,
                expression.eval("x = 2; y = 5; a = 20; b = 13")
        );
    }

    /**
     * Ошибка при отсутствии закрывающей скобки.
     */
    @Test
    void missingClosingBracketTest() {
        assertThrows(
                IllegalArgumentException.class,
                () -> parser.parse("(3+x")
        );
    }

    /**
     * Ошибка при неизвестном операторе.
     */
    @Test
    void unknownOperatorTest() {
        assertThrows(
                IllegalArgumentException.class,
                () -> parser.parse("(3%x)")
        );
    }

    /**
     * Ошибка при лишних символах после выражения.
     */
    @Test
    void extraCharactersTest() {
        assertThrows(
                IllegalArgumentException.class,
                () -> parser.parse("(3+x)abc")
        );
    }

    /**
     * Ошибка при пустом выражении.
     */
    @Test
    void emptyExpressionTest() {
        assertThrows(
                IllegalArgumentException.class,
                () -> parser.parse("")
        );
    }
}