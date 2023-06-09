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
        return accounts.put(account.id(), account) != null;
    }

    public synchronized boolean delete(int id) {
        return accounts.remove(id) != null;
    }

    public synchronized Optional<Account> getById(int id) {
        return Optional.ofNullable(accounts.get(id));
    }

    public synchronized boolean transfer(int fromId, int toId, int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount can't be less than or equal to 0");
        }
        boolean isTransfer = false;
        Optional<Account> accFromId = getById(fromId);
        Optional<Account> accToId = getById(toId);
        if (accFromId.isPresent() && accToId.isPresent()) {
            if (accFromId.get().amount() >= amount && fromId != toId) {
                Account sourceAcc = new Account(fromId, accFromId.get().amount() - amount);
                Account targetAcc = new Account(toId, accToId.get().amount() + amount);
                update(sourceAcc);
                update(targetAcc);
                isTransfer = true;
            }
        }
        return isTransfer;
    }
}
