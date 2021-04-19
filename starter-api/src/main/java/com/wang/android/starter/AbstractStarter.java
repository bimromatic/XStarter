package com.wang.android.starter;

public abstract class AbstractStarter implements IStarterExecutor{

    public AbstractStarter next;

    public AbstractStarter pre;

    public abstract int priority();

    public abstract boolean isSync();

    public abstract boolean isDelay();

}
