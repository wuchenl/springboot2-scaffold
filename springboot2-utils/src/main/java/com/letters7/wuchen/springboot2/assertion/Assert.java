package com.letters7.wuchen.springboot2.assertion;

/**
 * 断言工具，用来实现<a
 * href="http://martinfowler.com/ieeeSoftware/failFast.pdf">Fail-Fast</a>。
 * <p>
 * <strong>注意事项：</strong>
 * </p>
 * <ul>
 * <li>
 * Assertion是用来明确设置程序中的条件和约定的，它是一种程序员之间交流的工具，而不是和最终用户交流的工具。
 * 一个经过完整单元测试的正确程序，不应该使任何一条assertion失败。
 * 应避免过于复杂的assertion条件，而占用过多的运行时间。除非Assertion失败。
 * Assertion的出错信息不是给最终用户看的，因此没必要写得过于详细，更没必要考虑国际化的问题，以至于浪费CPU的时间。
 * 例如下面的语句是要避免的：
 * assertTrue(type instanceof MyType, &quot;Unsupported type: &quot; + type);
 * 可以替换成：
 * assertTrue(type instanceof MyType, &quot;Unsupported type: %s&quot;, type);
 * 这样，当assertion顺利通过时，不会占用CPU时间。
 * </li>
 * </ul>
 * 此外，部分方法具有返回值，以方便编程，例如：
 * void foo(String param) {
 *     bar(assertNotNull(param));
 * }
 *
 * int bar(String param) {
 *     if (true) {
 *         ...
 *     }
 *
 *     return unreachableCode();
 * }
 *
 * @author Michael Zhou
 */
public final class Assert {
    /**
     * 确保对象不为空，否则抛出<code>IllegalArgumentException</code>。
     *
     * @param object 任意待判定的对象
     * @param <T>    任意
     * @return 是否通过
     */
    public static <T> T assertNotNull(T object) {
        return assertNotNull(object, null, null, (Object[]) null);
    }


    /**
     * 确保对象不为空，否则抛出<code>IllegalArgumentException</code>。
     *
     * @param object
     * @param message
     * @param args
     * @param <T>
     * @return
     */
    public static <T> T assertNotNull(T object, String message, Object... args) {
        return assertNotNull(object, null, message, args);
    }

    /**
     * 确保对象不为空，否则抛出指定异常，默认为<code>IllegalArgumentException</code>。
     *
     * @param object
     * @param exceptionType
     * @param message
     * @param args
     * @param <T>
     * @return
     */
    public static <T> T assertNotNull(T object, ExceptionType exceptionType, String message, Object... args) {
        if (object == null) {
            if (exceptionType == null) {
                exceptionType = ExceptionType.ILLEGAL_ARGUMENT;
            }

            throw exceptionType.newInstance(getMessage(message, args, "the argument is required:" + object.getClass()));
        }

        return object;
    }

    /**
     * 确保对象为空，否则抛出<code>IllegalArgumentException</code>。
     *
     * @param object
     * @param <T>
     * @return
     */
    public static <T> T assertNull(T object) {
        return assertNull(object, null, null, (Object[]) null);
    }

    /**
     * 确保对象为空，否则抛出<code>IllegalArgumentException</code>。
     *
     * @param object
     * @param message
     * @param args
     * @param <T>
     * @return
     */
    public static <T> T assertNull(T object, String message, Object... args) {
        return assertNull(object, null, message, args);
    }

    /**
     * 确保对象为空，否则抛出指定异常，默认为<code>IllegalArgumentException</code>。
     *
     * @param object
     * @param exceptionType
     * @param message
     * @param args
     * @param <T>
     * @return
     */
    public static <T> T assertNull(T object, ExceptionType exceptionType, String message, Object... args) {
        if (object != null) {
            if (exceptionType == null) {
                exceptionType = ExceptionType.ILLEGAL_ARGUMENT;
            }

            throw exceptionType.newInstance(getMessage(message, args,
                    "the object argument must be null"));
        }

        return object;
    }

    /**
     * 确保表达式为真，否则抛出<code>IllegalArgumentException</code>。
     *
     * @param expression
     */
    public static void assertTrue(boolean expression) {
        assertTrue(expression, null, null, (Object[]) null);
    }

    /**
     * 确保表达式为真，否则抛出<code>IllegalArgumentException</code>。
     *
     * @param expression
     * @param message
     * @param args
     */
    public static void assertTrue(boolean expression, String message, Object... args) {
        assertTrue(expression, null, message, args);
    }

    /**
     * 确保表达式为真，否则抛出指定异常，默认为<code>IllegalArgumentException</code>。
     *
     * @param expression
     * @param exceptionType
     * @param message
     * @param args
     */
    public static void assertTrue(boolean expression, ExceptionType exceptionType, String message, Object... args) {
        if (!expression) {
            if (exceptionType == null) {
                exceptionType = ExceptionType.ILLEGAL_ARGUMENT;
            }

            throw exceptionType.newInstance(getMessage(message, args,
                    "the template must be true"));
        }
    }

    /**
     * 不可能到达的代码。
     *
     * @param <T>
     * @return
     */
    public static <T> T unreachableCode() {
        unreachableCode(null, (Object[]) null);
        return null;
    }

    /**
     * 不可能到达的代码。
     *
     * @param message
     * @param args
     * @param <T>
     * @return
     */
    public static <T> T unreachableCode(String message, Object... args) {
        throw ExceptionType.UNREACHABLE_CODE.newInstance(getMessage(message, args,
                "the code is expected as unreachable"));
    }

    /**
     * 不可能发生的异常。
     *
     * @param e
     * @param <T>
     * @return
     */
    public static <T> T unexpectedException(Throwable e) {
        unexpectedException(e, null, (Object[]) null);
        return null;
    }

    /**
     * 不可能发生的异常。
     *
     * @param e
     * @param message
     * @param args
     * @param <T>
     * @return
     */
    public static <T> T unexpectedException(Throwable e, String message, Object... args) {
        RuntimeException exception = ExceptionType.UNEXPECTED_FAILURE.newInstance(getMessage(message, args,
                "unexpected exception is thrown"));

        exception.initCause(e);

        throw exception;
    }

    /**
     * 未预料的失败。
     *
     * @param <T>
     * @return
     */
    public static <T> T fail() {
        fail(null, (Object[]) null);
        return null;
    }

    /**
     * 未预料的失败。
     *
     * @param message
     * @param args
     * @param <T>
     * @return
     */
    public static <T> T fail(String message, Object... args) {
        throw ExceptionType.UNEXPECTED_FAILURE.newInstance(getMessage(message, args, "unexpected failure"));
    }


    /**
     * 不支持的操作。
     *
     * @param <T>
     * @return
     */
    public static <T> T unsupportedOperation() {
        unsupportedOperation(null, (Object[]) null);
        return null;
    }

    /**
     * 不支持的操作。
     *
     * @param message
     * @param args
     * @param <T>
     * @return
     */
    public static <T> T unsupportedOperation(String message, Object... args) {
        throw ExceptionType.UNSUPPORTED_OPERATION.newInstance(getMessage(message, args,
                "unsupported operation or unimplemented function"));
    }

    /**
     * 取得带参数的消息。
     *
     * @param message
     * @param args
     * @param defaultMessage
     * @return
     */
    private static String getMessage(String message, Object[] args, String defaultMessage) {
        if (message == null) {
            message = defaultMessage;
        }

        if (args == null || args.length == 0) {
            return message;
        }

        return String.format(message, args);
    }


    /**
     * Assertion错误类型。
     */
    public static enum ExceptionType {
        ILLEGAL_ARGUMENT {
            @Override
            RuntimeException newInstance(String message) {
                return new IllegalArgumentException(message);
            }
        },

        ILLEGAL_STATE {
            @Override
            RuntimeException newInstance(String message) {
                return new IllegalStateException(message);
            }
        },

        NULL_POINT {
            @Override
            RuntimeException newInstance(String message) {
                return new NullPointerException(message);
            }
        },

        UNREACHABLE_CODE {
            @Override
            RuntimeException newInstance(String message) {
                return new UnreachableCodeException(message);
            }
        },

        UNEXPECTED_FAILURE {
            @Override
            RuntimeException newInstance(String message) {
                return new UnexpectedFailureException(message);
            }
        },

        UNSUPPORTED_OPERATION {
            @Override
            RuntimeException newInstance(String message) {
                return new UnsupportedOperationException(message);
            }
        };

        abstract RuntimeException newInstance(String message);
    }
}
