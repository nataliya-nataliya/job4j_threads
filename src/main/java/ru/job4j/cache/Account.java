package ru.job4j.cache;

public record Account(int id, int amount) {
    public Account {
        if (id < 1) {
            throw new IllegalArgumentException("Id can't be less than or equal to 0");
        }
    }
}
