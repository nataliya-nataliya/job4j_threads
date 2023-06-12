package ru.job4j.barrier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;


class SimpleBlockingQueueTest {
    @Test
    public void whenNumberOfElementsEqualsQueueCapacity() throws InterruptedException {
        List<Integer> numbersFromQueue = new ArrayList<>();
        int numberOfElements = 3;
        SimpleBlockingQueue<Integer> simpleBlockingQueue = new SimpleBlockingQueue<>(3);
        Thread producer = new Thread(
                () -> {
                    for (int i = 1; i <= numberOfElements; i++) {
                        simpleBlockingQueue.offer(i);
                    }
                }, "producer thread");
        Thread consumer = new Thread(
                () -> {
                    for (int i = 1; i <= numberOfElements; i++) {
                        numbersFromQueue.add(simpleBlockingQueue.poll());
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
                        simpleBlockingQueue.offer(i);
                    }
                }, "producer thread");
        Thread consumer = new Thread(
                () -> {
                    for (int i = 1; i <= numberOfElements; i++) {
                        numbersFromQueue.add(simpleBlockingQueue.poll());
                    }
                }, "consumer thread");
        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
        Assertions.assertEquals(List.of(2, 3, 4), new ArrayList<>(
                numbersFromQueue.subList(numberOfElements - simpleBlockingQueue.queueCapacity, numberOfElements)));
    }

    @Test
    public void whenNumberOfElementsIs0() throws InterruptedException {
        List<Integer> numbersFromQueue = new ArrayList<>();
        int numberOfElements = 0;
        SimpleBlockingQueue<Integer> simpleBlockingQueue = new SimpleBlockingQueue<>(3);
        Thread producer = new Thread(
                () -> {
                    for (int i = 1; i <= numberOfElements; i++) {
                        simpleBlockingQueue.offer(i);
                    }
                }, "producer thread");
        Thread consumer = new Thread(
                () -> {
                    for (int i = 1; i <= numberOfElements; i++) {
                        numbersFromQueue.add(simpleBlockingQueue.poll());
                    }
                }, "consumer thread");
        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
        Assertions.assertTrue(numbersFromQueue.isEmpty());
    }
}
