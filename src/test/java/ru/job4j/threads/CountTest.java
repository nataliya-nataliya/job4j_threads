package ru.job4j.threads;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class CountTest {
    @Test
    public void whenExecute2ThreadThen2() throws InterruptedException {
        var count = new Count();
        var first = new Thread(count::increment);
        var second = new Thread(count::increment);
        first.start();
        second.start();
        first.join();
        second.join();
        Assertions.assertEquals(2, count.get());
    }

}