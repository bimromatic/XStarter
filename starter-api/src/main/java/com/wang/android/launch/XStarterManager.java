package com.wang.android.launch;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;


import com.wang.android.starter.AbstractStarter;
import com.wang.android.starter.IManager;
import com.wang.android.thread.DefaultPoolExecutor;
import com.wang.android.utils.ThreadUtil;

import java.util.List;

class XStarterManager {

    //同步不延迟
    private StarterWarp syncNotDelay;

    //同步延迟
    private StarterWarp syncDelay;

    //异步不延迟
    private StarterWarp asyncNotDelay;

    //异步延迟
    private StarterWarp asyncDelay;


    public void assembleStarter(IManager manager) {
        List<AbstractStarter> abstractStarters = manager.initExecutor();
        for (AbstractStarter starter : abstractStarters) {
            parseStarter(starter);
        }
    }

    //开始执行初始化流程
    public void executeStarter() {

        if (asyncNotDelay != null) {
            //开始异步立即初始化操作
            asyncNotDelay.executeAsyncNotDelay();
        }

        if (syncNotDelay != null) {
            //开始同步立即初始化操作
            syncNotDelay.executeSyncNotDelay();
        }

        if (syncDelay != null) {
            syncDelay.executeSyncDelay();
        }

        if (asyncDelay != null) {
            asyncDelay.executeAsyncDelay();
        }

    }


    private void parseStarter(AbstractStarter starter) {
        if (starter.isSync()) {
            if (starter.isDelay()) {
                if (syncDelay == null) {
                    syncDelay = new StarterWarp();
                }
                syncDelay.put(starter);
            } else {
                if (syncNotDelay == null) {
                    syncNotDelay = new StarterWarp();
                }
                syncNotDelay.put(starter);
            }
        } else {
            if (starter.isDelay()) {
                if (asyncDelay == null) {
                    asyncDelay = new StarterWarp();
                }
                asyncDelay.put(starter);
            } else {
                if (asyncNotDelay == null) {
                    asyncNotDelay = new StarterWarp();
                }
                asyncNotDelay.put(starter);
            }
        }
    }


    class StarterWarp {

        private AbstractStarter starterHead;

        private AbstractStarter starterTail;

        private AbstractStarter tempStarterHead;

        private Handler mHandler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };

        public void put(AbstractStarter starter) {
            if (starterHead == null || starterTail == null) {
                starterHead = starter;
                starterTail = starter;
                return;
            }

            if (starter.priority() >= starterHead.priority()) {
                starter.next = starterHead;
                starterHead.pre = starter;
                starterHead = starter;
                return;
            }

            if (starter.priority() <= starterTail.priority()) {
                starter.pre = starterTail;
                starterTail.next = starter;
                starterTail = starter;
                return;
            }

            AbstractStarter tempNode = starterHead.next;
            while (tempNode != null) {
                if (starter.priority() >= tempNode.priority()) {
                    tempNode.pre.next = starter;
                    starter.pre = tempNode.pre;

                    starter.next = tempNode;
                    tempNode.pre = starter;
                    return;
                }
                tempNode = tempNode.next;
            }
        }

        /**
         * 初始化同步不延迟方法
         */
        public void executeSyncNotDelay() {

            tempStarterHead = starterHead;
            while (tempStarterHead != null) {
                try {
                    startExecute(tempStarterHead);
                    tempStarterHead.onFinish(null);
                } catch (Exception e) {
                    tempStarterHead.onFinish(e);
                } finally {
                    tempStarterHead = tempStarterHead.next;
                }
            }
        }

        public void executeAsyncNotDelay() {
            AbstractStarter tempNode = starterHead;

            while (tempNode != null) {
                threadExecute(tempNode);
                tempNode = tempNode.next;
            }
        }

        public void executeSyncDelay() {
            tempStarterHead = starterHead;
            Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
                @Override
                public boolean queueIdle() {
                    try {
                        startExecute(tempStarterHead);
                        tempStarterHead.onFinish(null);
                    } catch (Exception e) {
                        tempStarterHead.onFinish(e);
                    } finally {
                        tempStarterHead = tempStarterHead.next;
                    }
                    return tempStarterHead != null;
                }
            });
        }

        public void executeAsyncDelay() {
            tempStarterHead = starterHead;
            Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
                @Override
                public boolean queueIdle() {
                    if (XStarter.isDebug) {
                        System.out.println("IdleHandler queueIdle is running");
                    }
                    final AbstractStarter tempNode = tempStarterHead;
                    threadExecute(tempNode);
                    tempStarterHead = tempStarterHead.next;
                    return tempStarterHead != null;
                }
            });
        }

        private void threadExecute(final AbstractStarter starter) {
            DefaultPoolExecutor.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    Exception exception = null;
                    try {
                        startExecute(starter);
                    } catch (Exception e) {
                        exception = e;
                    } finally {
                        final Exception finalException = exception;

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                starter.onFinish(finalException);
                            }
                        });
                    }
                }
            });
        }

        private void startExecute(final AbstractStarter starter) {
            long startTime = 0;
            if (XStarter.isDebug) {
                startTime = System.currentTimeMillis();
                ThreadUtil.printThreadInfo();
            }
            starter.execute(XStarter.mApplication);

            if (XStarter.isDebug) {
                long costTime = System.currentTimeMillis() - startTime;
                System.out.println("初始化耗时 ： " + costTime + " ms");
            }
        }
    }

}
