/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2018 The TestFX Contributors
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the
 * European Commission - subsequent versions of the EUPL (the "Licence"); You may
 * not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the Licence for the
 * specific language governing permissions and limitations under the Licence.
 */
package net.codecrafting.springfx.util;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class AsyncUtilsTest {

    @Rule
    public TestRule rule = Timeout.millis(10000);

    @Test
    public void asyncCallable() throws Exception
    {
        // when:
        Future<String> future = AsyncUtils.async(() -> "foo");

        // then:
        Thread.sleep(10);
        assertThat(future.get(), CoreMatchers.is("foo"));
        waitForThreads(future);
    }

    @Test
    public void asyncCallableWithSleep() throws Exception
    {
        // when:
        Future<String> future = AsyncUtils.async(() -> {
            Thread.sleep(50);
            return "foo";
        });

        // then:
        assertThat(future.isDone(), CoreMatchers.is(false));
        AsyncUtils.sleep(250, MILLISECONDS);
        assertThat(future.get(), CoreMatchers.is("foo"));
        waitForThreads(future);
    }

    @Test
    public void asyncCallableWithException() throws Throwable
    {
        // given:
        AsyncUtils.printException = false;
        Callable<Void> callable = () -> {
            throw new UnsupportedOperationException();
        };
        AsyncUtils.clearExceptions();

        // when:
        Future<Void> future = AsyncUtils.async(callable);

        // then:
        waitForException(future);
        assertThatThrownBy(() -> {
            AsyncUtils.checkException();
            AsyncUtils.waitFor(50, MILLISECONDS, future);
            waitForThreads(future);
        }).isExactlyInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void clearExceptionsTest() throws Throwable
    {
        // given:
        AsyncUtils.printException = false;
        Callable<Void> callable = () -> {
            throw new UnsupportedOperationException();
        };
        AsyncUtils.clearExceptions();

        // when:
        Future<Void> future = AsyncUtils.async(callable);

        // then:
        waitForException(future);
        AsyncUtils.clearExceptions();
        AsyncUtils.checkException();
        waitForThreads(future);
    }

    @Test
    public void autoCheckExceptionTest()
    {
        // given:
        AsyncUtils.printException = false;
        AsyncUtils.autoCheckException = true;
        Callable<Void> callable = () -> {
            throw new UnsupportedOperationException();
        };
        AsyncUtils.clearExceptions();

        // when:
        Future<Void> future = AsyncUtils.async(callable);
        waitForThreads(future);

        // then:
        try {
            future = AsyncUtils.async(callable);
            fail("No exception thrown by autoCheck");
        }
        catch (Throwable e) {
            if (!(e instanceof UnsupportedOperationException)) {
                throw e;
            }
        }

        AsyncUtils.clearExceptions();
        waitForThreads(future);
    }

    @Test
    public void unhandledExceptionTest() throws Throwable
    {
        for (int i = 0; i < 20; i++) {
            // given:
            AsyncUtils.printException = false;
            Callable<Void> callable = () -> {
                throw new NullPointerException("unhandledExceptionTest");
            };
            AsyncUtils.clearExceptions();

            // when:
            Future<Void> future = AsyncUtils.async(callable);
            try {
                AsyncUtils.waitFor(50, MILLISECONDS, future);
                fail("No exception thrown");
            }
            catch (Throwable exception) {
                if (!exception.getMessage().contains("unhandledExceptionTest")) {
                    fail("Another exception was thrown: " + exception.getMessage());
                }
            }

            // then:
            AsyncUtils.printException = true;
            try {
                AsyncUtils.checkException();
            }
            catch (Throwable exception) {
                if (exception.getMessage().contains("unhandledExceptionTest")) {
                    fail("Handled exception not removed from stack");
                } else {
                    fail("Another exception was thrown: " + exception.getMessage());
                }
            }
            AsyncUtils.clearExceptions();
            waitForException(future);
        }
    }

    @Test
    public void waitForWithFuture() throws Exception
    {
        // when:
        Future<Void> future = AsyncUtils.async(() -> null);

        // then:
        AsyncUtils.waitFor(50, MILLISECONDS, future);
        waitForThreads(future);
    }

    @Test
    public void waitForWithFutureWithSleep()
    {
        // when:
        Future<Void> future = AsyncUtils.async(() -> {
            Thread.sleep(250);
            return null;
        });

        // then:
        assertThatThrownBy(() -> AsyncUtils.waitFor(50, MILLISECONDS, future))
                .isExactlyInstanceOf(TimeoutException.class);
        waitForThreads(future);
    }

    @Test
    public void waitForWithFutureCancelled()
    {
        // given:
        AsyncUtils.printException = false;

        // when:
        Future<Void> future = AsyncUtils.async(() -> {
            Thread.sleep(250);
            return null;
        });
        try {
            Thread.sleep(50);
        }
        catch (Exception ignore) {
        }
        future.cancel(true);
        waitForThreads(future);

        // then:
        try { // only thrown if really interrupted (need to be started first)
            AsyncUtils.checkException();
            fail("checkException didn't detect Exception");
        }
        catch (Throwable ignore) {
        }

        assertThatThrownBy(() -> AsyncUtils.waitFor(50, MILLISECONDS, future))
                .isExactlyInstanceOf(CancellationException.class);
    }

    @Test
    public void waitForWithBooleanCallable() throws Exception
    {
        AsyncUtils.waitFor(250, MILLISECONDS, () -> true);
    }

    @Test
    public void waitForWithBooleanCallableWithSleep() throws Exception
    {
        AsyncUtils.waitFor(250, MILLISECONDS, () -> {
            Thread.sleep(50);
            return true;
        });
    }

    @Test
    public void waitForWithBooleanCallableWithFalse()
    {
        assertThatThrownBy(() -> AsyncUtils.waitFor(250, MILLISECONDS, () -> false))
                .isExactlyInstanceOf(TimeoutException.class);
    }

    @Test
    public void waitForWithBooleanCallableWithException()
    {
        assertThatThrownBy(() -> AsyncUtils.waitFor(250, MILLISECONDS, () -> {
            throw new UnsupportedOperationException();
        })).hasCauseExactlyInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void waitForWithBooleanValue() throws Exception
    {
        // given:
        BooleanProperty property = new SimpleBooleanProperty(false);

        // when:
        AsyncUtils.async(() -> {
            Thread.sleep(50);
            property.set(true);
            return null;
        });

        // then:
        AsyncUtils.waitFor(250, MILLISECONDS, property);
    }

    @Test
    public void waitForWithBooleanValueWithFalse()
    {
        // given:
        BooleanProperty property = new SimpleBooleanProperty(false);

        // when:
        AsyncUtils.async(() -> {
            Thread.sleep(50);
            property.set(false);
            return null;
        });

        // then:
        assertThatThrownBy(() -> AsyncUtils.waitFor(250, MILLISECONDS, property))
                .isExactlyInstanceOf(TimeoutException.class);
    }

    @Test
    public void daemonThreads() throws Exception
    {
        final Future<Thread> future = AsyncUtils.async(Thread::currentThread);
        final Thread thread = future.get();
        assertThat(thread.isDaemon(), CoreMatchers.is(true));
    }

    public void waitForException(Future<?> f) throws InterruptedException
    {
        Thread.sleep(50);
        assertTrue(f.isDone());
    }

    public void waitForThreads(Future<?> f)
    {
        while (!f.isDone()) {
            try {
                Thread.sleep(1);
            }
            catch (Exception ignore) {
            }
        }
        try {
            Thread.sleep(50);
        }
        catch (Exception ignore) {
        }
    }

}
