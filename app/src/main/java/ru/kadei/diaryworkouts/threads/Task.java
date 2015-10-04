package ru.kadei.diaryworkouts.threads;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by kadei on 05.09.15.
 */
public class Task {

    private Object client;

    private Method executedMethod;
    private Class returnType;
    private Object returnValue;

    private Method completeMethod;
    private Method failMethod;
    Throwable exception;

    public Task setClient(Object object) {
        client = object;
        return this;
    }

    public Task setExecutedMethod(String name) {
        validateClient();
        try {
            executedMethod = client.getClass().getDeclaredMethod(name);
            executedMethod.setAccessible(true);
            returnType = executedMethod.getReturnType();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public Task setExecutedMethod(String name, Class... parametersTypes) {
        validateClient();
        try {
            executedMethod = client.getClass().getDeclaredMethod(name, parametersTypes);
            executedMethod.setAccessible(true);
            returnType = executedMethod.getReturnType();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    private void validateClient() {
        if (client == null)
            throw new RuntimeException("You first need call [setClient(Object)] method");
    }

    public Task setCompleteMethod(String name) {
        if (returnType == null)
            throw new RuntimeException("You first need call [setExecutedMethod(String, Class...)] method");

        try {
            if (returnType == Void.TYPE)
                completeMethod = client.getClass().getDeclaredMethod(name);
            else
                completeMethod = client.getClass().getDeclaredMethod(name, returnType);

            completeMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Type parameter for ["+name+"] " +
                    "should be same as return type in ["+executedMethod.getName()+"]");
        }
        return this;
    }

    public Task setFailMethod(String name) {
        validateClient();
        try {
            failMethod = client.getClass().getDeclaredMethod(name, Throwable.class);
            failMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("\"Fail\" method should take ONE parameter Throwable.class");
        }
        return this;
    }

    void execute(Object... parameters) throws TaskException {
        try {
            returnValue = executedMethod.invoke(client, parameters);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            throw new TaskException(e.getTargetException());
        }
    }

    void noticeCompletion() {
        try {
            completeMethod.invoke(client, returnValue);
            returnValue = null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    void noticeFail() {
        try {
            failMethod.invoke(client, exception);
            exception = null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    boolean isSuccessful() {
        return exception == null;
    }
}
