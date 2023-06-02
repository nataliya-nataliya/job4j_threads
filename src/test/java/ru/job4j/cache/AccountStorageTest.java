package ru.job4j.cache;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class AccountStorageTest {
    @Test
    void whenAdd() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        var firstAccount = storage.getById(1)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
        Assertions.assertEquals(100, firstAccount.amount());
    }

    @Test
    void whenUpdate() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        storage.update(new Account(1, 200));
        var firstAccount = storage.getById(1)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
        Assertions.assertEquals(200, firstAccount.amount());
    }

    @Test
    void whenDelete() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        storage.delete(1);
        Assertions.assertTrue(storage.getById(0).isEmpty());
    }

    @Test
    void whenTransfer() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        storage.add(new Account(2, 100));
        storage.transfer(1, 2, 100);
        var firstAccount = storage.getById(1)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
        var secondAccount = storage.getById(2)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
        Assertions.assertEquals(0, firstAccount.amount());
        Assertions.assertEquals(200, secondAccount.amount());
    }

    @Test
    void whenTransferButMoneyIsNotEnough() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 99));
        storage.add(new Account(2, 100));
        storage.transfer(1, 2, 100);
        var firstAccount = storage.getById(1)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
        var secondAccount = storage.getById(2)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
        Assertions.assertEquals(99, firstAccount.amount());
        Assertions.assertEquals(100, secondAccount.amount());
    }

    @Test
    void whenFromIdAndToIdAreSameThenAmountDoesNotChange() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        storage.transfer(1, 1, 100);
        var firstAccount = storage.getById(1)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
        Assertions.assertEquals(100, firstAccount.amount());
    }

    @Test
    void whenTransferAmountIs0ThenThrowsIllegalStateException() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        storage.add(new Account(2, 100));
        Assertions.assertThrows(IllegalArgumentException.class, () -> storage.transfer(1, 2, 0));
    }

    @Test
    void whenTransferAmountIsNegativeNumberThenThrowsIllegalStateException() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        storage.add(new Account(2, 100));
        Assertions.assertThrows(IllegalArgumentException.class, () -> storage.transfer(1, 2, -1));
    }
}
