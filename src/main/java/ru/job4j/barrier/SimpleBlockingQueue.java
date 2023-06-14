package ru.job4j.barrier;

import net.jcip.annotations.GuardedBy;

import java.util.LinkedList;
import java.util.Queue;

public class SimpleBlockingQueue<T> {
    @GuardedBy("this")
    private final Queue<T> queue = new LinkedList<>();
    private final int queueCapacity;

    public SimpleBlockingQueue(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public synchronized void offer(T value) throws InterruptedException {
        while (queue.size() >= queueCapacity) {
            wait();
        }
        queue.offer(value);
        notify();
    }

    public synchronized T poll() throws InterruptedException {
        while (queue.size() == 0) {
            wait();
        }
        T result = queue.poll();
        notify();
        return result;
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
}
