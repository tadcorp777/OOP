package ru.nsu.tikhonov.expression;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import ru.nsu.tikhonov.expression.operands.Number;
import ru.nsu.tikhonov.expression.operands.Variable;
import ru.nsu.tikhonov.expression.operations.Add;
import ru.nsu.tikhonov.expression.operations.Div;
import ru.nsu.tikhonov.expression.operations.Mul;
import ru.nsu.tikhonov.expression.operations.Sub;
import ru.nsu.tikhonov.expression.parser.ExpressionParser;

/**
 * Дополнительные тесты для проверки граничных случаев.
 */
class ExpressionTest {

    private final ExpressionParser parser = new ExpressionParser();

    /**
     * Группировка выражений и вложенные скобки.
     */
    @Test
    void groupingTest() {
        Expression expression = parser.parse("(((x)))");

        assertInstanceOf(Variable.class, expression);
        assertEquals("x", expression.toString());

        expression = parser.parse("((x)+(y))");

        assertInstanceOf(Add.class, expression);
        assertEquals("(x+y)", expression.toString());

        expression = parser.parse("((a)+((b)*(c)))");

        assertEquals("(a+(b*c))", expression.toString());
    }

    /**
     * Многобуквенные имена переменных.
     */
    @Test
    void longVariableNamesTest() {
        assertEquals(
                "(alpha+beta)",
                parser.parse("(alpha+beta)").toString()
        );

        assertEquals(
                "(veryLongVariableName*short)",
                parser.parse("(veryLongVariableName * short)").toString()
        );
    }

    /**
     * Пробелы и форматирование выражений.
     */
    @Test
    void spacesTest() {
        assertEquals("(x+y)", parser.parse("( x + y )").toString());
        assertEquals("(x+y)", parser.parse("(x+ y)").toString());
        assertEquals("(x+y)", parser.parse("(x +y)").toString());
    }

    /**
     * Отрицательные числа и их использование в выражениях.
     */
    @Test
    void negativeNumbersTest() {
        Expression expression = parser.parse("(-5)");

        assertInstanceOf(Number.class, expression);
        assertEquals("-5", expression.toString());
        assertEquals(-5, expression.eval(""));

        expression = parser.parse("(x + (-3))");

        assertEquals("(x+-3)", expression.toString());
        assertEquals(2, expression.eval("x = 5"));
    }

    /**
     * Сравнение дерева, созданное парсером, с деревом, созданным вручную.
     */
    @Test
    void parsedAndConstructedExpressionsTest() {
        Expression parsed = parser.parse(
                "((a*b)+((c-d)/(1+2)))"
        );

        Expression constructed = new Add(
                new Mul(
                        new Variable("a"),
                        new Variable("b")
                ),
                new Div(
                        new Sub(
                                new Variable("c"),
                                new Variable("d")
                        ),
                        new Add(
                                new Number(1),
                                new Number(2)
                        )
                )
        );

        assertEquals(constructed.toString(), parsed.toString());

        String assignments = "a=2; b=3; c=10; d=4";

        assertEquals(8, parsed.eval(assignments));
        assertEquals(
                constructed.eval(assignments),
                parsed.eval(assignments)
        );
    }

    /**
     * Вычисление с разным порядком и лишними переменными.
     */
    @Test
    void evaluationAssignmentsTest() {
        Expression expression = new Add(
                new Mul(
                        new Variable("x"),
                        new Variable("y")
                ),
                new Div(
                        new Variable("z"),
                        new Number(2)
                )
        );

        assertEquals(17, expression.eval("x=3; y=4; z=10"));
        assertEquals(4, expression.eval("x=0; y=100; z=8"));
        assertEquals(17, expression.eval("z=10; x=3; y=4"));

        assertEquals(
                17,
                expression.eval("x=3; y=4; z=10; w=100")
        );
    }

    /**
     * Пробелы и пустые части в строке означиваний.
     */
    @Test
    void assignmentSpacesTest() {
        Expression expression = new Add(
                new Variable("x"),
                new Variable("y")
        );

        assertEquals(
                7,
                expression.eval(" x = 3 ; y = 4 ; ; ")
        );
    }

    /**
     * Ошибка при отсутствии значения переменной.
     */
    @Test
    void missingVariableTest() {
        Expression expression = new Add(
                new Variable("x"),
                new Variable("y")
        );

        assertThrows(
                RuntimeException.class,
                () -> expression.eval("x=3")
        );
    }

    /**
     * Ошибки при некорректных означиваниях.
     */
    @Test
    void invalidAssignmentsTest() {
        Expression expression = new Add(
                new Variable("x"),
                new Variable("y")
        );

        assertThrows(
                RuntimeException.class,
                () -> expression.eval("x=; y=5")
        );

        assertThrows(
                RuntimeException.class,
                () -> expression.eval("x=abc")
        );

        assertThrows(
                RuntimeException.class,
                () -> expression.eval("x=5 y=3")
        );

        assertThrows(
                RuntimeException.class,
                () -> expression.eval("x=3, y=4")
        );
    }

    /**
     * Дифференцирование чисел и переменных.
     */
    @Test
    void basicDerivativesTest() {
        assertEquals(
                "0",
                new Number(5).derivative("x").toString()
        );

        assertEquals(
                "1",
                new Variable("x").derivative("x").toString()
        );

        assertEquals(
                "0",
                new Variable("x").derivative("y").toString()
        );
    }

    /**
     * Повторное дифференцирование переменной.
     */
    @Test
    void repeatedDerivativeTest() {
        Expression expression = new Variable("x");

        Expression firstDerivative = expression.derivative("x");
        Expression secondDerivative = firstDerivative.derivative("x");

        assertEquals("1", firstDerivative.toString());
        assertEquals("0", secondDerivative.toString());
    }

    /**
     * Производная выражения, состоящего из констант.
     */
    @Test
    void constantExpressionDerivativeTest() {
        Expression expression = new Add(
                new Number(3),
                new Number(5)
        );

        Expression derivative = expression.derivative("x");

        assertEquals(0, derivative.eval(""));
    }

    /**
     * Производная куба выражения через правило произведения.
     */
    @Test
    void productRuleTest() {
        Expression inner = new Add(
                new Mul(new Variable("x"), new Variable("x")),
                new Mul(new Number(3), new Variable("x"))
        );

        Expression expression = new Mul(
                inner,
                new Mul(inner, inner)
        );

        Expression derivative = expression.derivative("x");

        assertEquals(2100, derivative.eval("x=2"));
    }

    /**
     * Отклонение некорректных выражений.
     */
    @Test
    void invalidExpressionsTest() {
        String[] invalidExpressions = {
                "(x+y",
                "x+y)",
                "(x # y)",
                "()",
                "(+)",
                "(x++)"
        };

        for (String input : invalidExpressions) {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> parser.parse(input),
                    input
            );
        }
    }
}