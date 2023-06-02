package ru.job4j.cache;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.HashMap;
import java.util.Optional;

@ThreadSafe
public class AccountStorage {
    @GuardedBy("this")
    private final HashMap<Integer, Account> accounts = new HashMap<>();

    public synchronized boolean add(Account account) {
        return accounts.putIfAbsent(account.id(), account) == null;
    }

    public synchronized boolean update(Account account) {
        return accounts.put(account.id(), account) == account;
    }

    public synchronized boolean delete(int id) {
        return accounts.remove(id) == accounts.get(id);
    }

    public synchronized Optional<Account> getById(int id) {
        return Optional.ofNullable(accounts.get(id));
    }

    public synchronized boolean transfer(int fromId, int toId, int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount can't be less than or equal to 0");
        }
        boolean isUpdateSourceAndTarget = false;
        if (accounts.get(fromId).amount() >= amount && fromId != toId) {
            Account sourceAcc = new Account(fromId, accounts.get(fromId).amount() - amount);
            Account targetAcc = new Account(toId, accounts.get(toId).amount() + amount);
            isUpdateSourceAndTarget = update(sourceAcc) & update(targetAcc);
        }
        return isUpdateSourceAndTarget;
    }
}
