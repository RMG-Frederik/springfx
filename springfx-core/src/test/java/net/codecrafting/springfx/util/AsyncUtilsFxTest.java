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

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;
import org.testfx.api.FxToolkit;

public class AsyncUtilsFxTest
{

    @Rule
    public TestRule rule = RuleChain.outerRule(Timeout.millis(1000)).around(exception = ExpectedException.none());
    public ExpectedException exception;

    @BeforeClass
    public static void setUpClass()
    {
        try {
            FxToolkit.registerPrimaryStage();
        }
        catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests that nested calls of async methods triggers an exception.
     */
    @Test
    public void asyncFxNestedCallableWithException() throws Throwable
    {
        // given:
        AsyncUtils.printException = false;
        exception.expectCause(instanceOf(ExecutionException.class));
        Callable<Void> callable = () -> {
            Future<Void> future = AsyncUtils.asyncFx(() -> {
                throw new UnsupportedOperationException();
            });
            return future.get();
        };
        AsyncUtils.clearExceptions();

        // when:
        Future<Void> future = AsyncUtils.asyncFx(callable);
        waitForException(future);

        // then:
        try {
            AsyncUtils.checkException();
            fail("checkException didn't detect Exception");
        }
        catch (Throwable e) {
            if (!(e instanceof UnsupportedOperationException)) {
                throw e;
            }
        }
        AsyncUtils.printException = true;
        AsyncUtils.waitFor(50, MILLISECONDS, future);
        waitForThreads(future);
    }

    @Test
    public void asyncFxCallableWithException() throws Throwable
    {
        // given:
        AsyncUtils.printException = false;
        exception.expectCause(instanceOf(UnsupportedOperationException.class));
        Callable<Void> callable = () -> {
            throw new UnsupportedOperationException();
        };
        AsyncUtils.clearExceptions();

        // when:
        Future<Void> future = AsyncUtils.asyncFx(callable);
        waitForException(future);

        // then:
        try {
            AsyncUtils.checkException();
            fail("checkException didn't detect Exception");
        }
        catch (Throwable e) {
            if (!(e instanceof UnsupportedOperationException)) {
                throw e;
            }
        }
        AsyncUtils.printException = true;
        AsyncUtils.waitFor(50, MILLISECONDS, future);
        waitForThreads(future);
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
