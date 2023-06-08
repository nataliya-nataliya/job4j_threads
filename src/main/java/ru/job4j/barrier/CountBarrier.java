package ru.job4j.barrier;

public class CountBarrier {
    private final Object monitor = this;

    private final int total;

    private int count = 0;

    public CountBarrier(final int total) {
        this.total = total;
    }

    public void count() {
        synchronized (monitor) {
            if (count < total) {
                count++;
            } else {
                monitor.notifyAll();
            }
        }
    }

    public void await() {
        synchronized (monitor) {
            while (count < total) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    public static void main(String[] args) {
        CountBarrier countBarrier = new CountBarrier(3);
        Thread thread1 = new Thread(
                () -> {
                    System.out.println(Thread.currentThread().getName() + " started");
                    for (int i = 0; i < countBarrier.total; i++) {
                        countBarrier.count();
                    }
                }, "Count thread 1");
        Thread thread2 = new Thread(
                () -> {
                    System.out.println(Thread.currentThread().getName() + " started");
                    for (int i = 0; i < countBarrier.total; i++) {
                        countBarrier.count();
                    }
                }, "Count thread 2");
        Thread thread3 = new Thread(
                () -> {
                    System.out.println(Thread.currentThread().getName() + " started");
                    countBarrier.await();
                }, "Await thread 3");
        Thread thread4 = new Thread(
                () -> {
                    System.out.println(Thread.currentThread().getName() + " started");
                    countBarrier.await();
                }, "Await thread 4");
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
    }
}
