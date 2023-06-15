package ru.job4j.barrier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


class SimpleBlockingQueueTest {
    @Test
    public void whenNumberOfElementsEqualsQueueCapacity() throws InterruptedException {
        List<Integer> numbersFromQueue = new ArrayList<>();
        int numberOfElements = 3;
        SimpleBlockingQueue<Integer> simpleBlockingQueue = new SimpleBlockingQueue<>(3);
        Thread producer = new Thread(
                () -> {
                    for (int i = 1; i <= numberOfElements; i++) {
                        try {
                            simpleBlockingQueue.offer(i);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, "producer thread");
        Thread consumer = new Thread(
                () -> {
                    for (int i = 1; i <= numberOfElements; i++) {
                        try {
                            numbersFromQueue.add(simpleBlockingQueue.poll());
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, "consumer thread");
        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
        Assertions.assertEquals(List.of(1, 2, 3), numbersFromQueue);
    }

    @Test
    public void whenNumberOfElementsMoreThanQueueCapacity() throws InterruptedException {
        List<Integer> numbersFromQueue = new ArrayList<>();
        int numberOfElements = 4;
        SimpleBlockingQueue<Integer> simpleBlockingQueue = new SimpleBlockingQueue<>(3);
        Thread producer = new Thread(
                () -> {
                    for (int i = 1; i <= numberOfElements; i++) {
                        try {
                            simpleBlockingQueue.offer(i);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, "producer thread");
        Thread consumer = new Thread(
                () -> {
                    for (int i = 1; i <= numberOfElements; i++) {
                        try {
                            numbersFromQueue.add(simpleBlockingQueue.poll());
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, "consumer thread");
        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
        Assertions.assertEquals(List.of(2, 3, 4), new ArrayList<>(
                numbersFromQueue.subList(numberOfElements - simpleBlockingQueue.getQueueCapacity(), numberOfElements)));
    }

    @Test
    public void whenNumberOfElementsIs0() throws InterruptedException {
        List<Integer> numbersFromQueue = new ArrayList<>();
        int numberOfElements = 0;
        SimpleBlockingQueue<Integer> simpleBlockingQueue = new SimpleBlockingQueue<>(3);
        Thread producer = new Thread(
                () -> {
                    for (int i = 1; i <= numberOfElements; i++) {
                        try {
                            simpleBlockingQueue.offer(i);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, "producer thread");
        Thread consumer = new Thread(
                () -> {
                    for (int i = 1; i <= numberOfElements; i++) {
                        try {
                            numbersFromQueue.add(simpleBlockingQueue.poll());
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, "consumer thread");
        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
        Assertions.assertTrue(numbersFromQueue.isEmpty());
    }

    @Test
    public void whenFetchAllIntegerDataThenGetIt() throws InterruptedException {
        final CopyOnWriteArrayList<Integer> buffer = new CopyOnWriteArrayList<>();
        final SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(5);
        Thread producer = new Thread(
                () -> {
                    for (int i = 0; i < 5; i++) {
                        try {
                            queue.offer(i);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        );
        producer.start();
        Thread consumer = new Thread(
                () -> {
                    while (!queue.isEmpty() || !Thread.currentThread().isInterrupted()) {
                        try {
                            buffer.add(queue.poll());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Thread.currentThread().interrupt();
                        }
                    }
                }
        );
        consumer.start();
        producer.join();
        consumer.interrupt();
        consumer.join();
        Assertions.assertEquals((Arrays.asList(0, 1, 2, 3, 4)), buffer);
    }

    @Test
    public void whenFetchAllStringDataThenGetIt1() throws InterruptedException {
        final CopyOnWriteArrayList<String> buffer = new CopyOnWriteArrayList<>();
        final SimpleBlockingQueue<String> queue = new SimpleBlockingQueue<>(5);
        Thread producer = new Thread(
                () -> {
                    try {
                        queue.offer("a");
                        queue.offer("b");
                        queue.offer("c");

                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
        producer.start();
        Thread consumer = new Thread(
                () -> {
                    while (!queue.isEmpty() || !Thread.currentThread().isInterrupted()) {
                        try {
                            buffer.add(queue.poll());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Thread.currentThread().interrupt();
                        }
                    }
                }
        );
        consumer.start();
        producer.join();
        consumer.interrupt();
        consumer.join();
        Assertions.assertEquals((Arrays.asList("a", "b", "c")), buffer);
    }
}
