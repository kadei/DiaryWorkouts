package ru.kadei.diaryworkouts.threads;

import junit.framework.Assert;

import ru.kadei.diaryworkouts.ApplicationTest;

/**
 * Created by kadei on 05.09.15.
 */
public class ReflectionTaskTests extends ApplicationTest implements TaskInterface{

    public void testExecuteTask() throws Exception {
        Task task = new Task();
        task
                .setClient(this)
                .setExecutedMethod("one", String.class)
                .setParameters("test string")
                .setCompleteMethod("resultOne");

        task.execute();
        task.noticeCompletion();
    }

    public int one(String str) {
        return str.length();
    }

    public void resultOne(int len) {
        Assert.assertEquals(len, 11);
    }

    public void testSetClientError() throws Exception {
        Task t = new Task();
        try {
            t.setExecutedMethod("one", String.class);
            fail("Should be throw Exception");
        } catch (RuntimeException e) {
            Assert.assertEquals(e.getMessage(), "You first need call [setClient(Object)] method");
        }
    }

    public void testSetExecutedMethodError() throws Exception {
        Task t = new Task();
        try {
            t.setCompleteMethod("one");
            fail("Should be throw Exception");
        } catch (RuntimeException e) {
            Assert.assertEquals(e.getMessage(), "You first need call [setExecutedMethod(String, Class...)] method");
        }
    }

    public void testSetFailMethodError() throws Exception {
        try {
            Task t = new Task()
                    .setClient(this)
                    .setFailMethod("failWithoutParameter");
            fail("Should throw Exception");
        } catch (RuntimeException e) {
            Assert.assertEquals(e.getMessage(), "\"Fail\" method should take ONE parameter Throwable.class");
        }
    }

    public void failWithoutParameter() {
    }

    public void testExecuteWithException() throws Exception {
        Task t = new Task()
                .setClient(this)
                .setExecutedMethod("executeWithException")
                .setCompleteMethod("complete")
                .setFailMethod("failExecuteWithException");

        try {
            t.execute();
            fail("Should throw Exception");
        } catch (TaskException e) {
            t.exception = e.getOriginalException();
            t.noticeFail();
        }
    }

    private void executeWithException() {
        throw new RuntimeException("test exception");
    }

    private void complete() {
    }

    private void failExecuteWithException(Throwable e) {
        Assert.assertEquals(e.getMessage(), "test exception");
    }

    public void testExecuteInterfaceMethods() throws Exception {
        Task t = new Task()
                .setClient(this)
                .setExecutedMethod("exe")
                .setCompleteMethod("end")
                .setFailMethod("oops");

        t.execute();
        t.noticeCompletion();
    }

    @Override
    public String[] exe() {
        return new String[]{"one", "two", "three"};
    }

    @Override
    public void end(String[] strings) {
        String[] s = new String[]{"one", "two", "three"};
        assertEquals(s, strings);
    }

    @Override
    public void oops(Throwable t) {
    }
}
