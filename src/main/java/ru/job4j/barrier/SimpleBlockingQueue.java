package ru.job4j.barrier;

import net.jcip.annotations.GuardedBy;

import java.util.LinkedList;
import java.util.Queue;

public class SimpleBlockingQueue<T> {
    @GuardedBy("this")
    private final Queue<T> queue = new LinkedList<>();
    int queueCapacity;
    int filledCapacity = 0;

    public SimpleBlockingQueue(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    public synchronized void offer(T value) {
        while (filledCapacity >= queueCapacity) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        filledCapacity++;
        notify();
        queue.offer(value);
    }

    public synchronized T poll() {
        while (filledCapacity < 1) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        filledCapacity--;
        notify();
        return queue.poll();
    }
}
