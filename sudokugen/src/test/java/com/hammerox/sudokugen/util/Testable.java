package com.hammerox.sudokugen.util;

import org.junit.Assert;

/**
 * Created by Mauricio on 04-Feb-17.
 */

public abstract class Testable {

    public abstract void run() throws Exception;

    public static <T extends Exception> T assertThrows(
            final Class<T> expected,
            final Testable codeUnderTest) throws Exception {
        T result = null;
        try {
            codeUnderTest.run();
            Assert.fail("Expecting exception but none was thrown.");
        } catch(final Exception actual) {
            if (expected.isInstance(actual)) {
                result = expected.cast(actual);
            }
            else {
                throw actual;
            }
        }
        return result;
    }
}
