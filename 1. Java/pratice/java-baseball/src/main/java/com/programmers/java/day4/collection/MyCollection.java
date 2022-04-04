package com.programmers.java.day4.collection;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import practice.day4.iterator.MyIterator;

public class MyCollection<T> {
    private final List<T> list;

    public MyCollection(List<T> list) {
        this.list = list;
    }

    public int size() {
        return list.size();
    }

    public MyCollection<T> filter(Predicate<T> predicate) {
        List<T> newList = new ArrayList<>();
        foreach(d -> {
            if (predicate.test(d))
                newList.add(d);
        });
        return new MyCollection<>(newList);
    }

    public <U> MyCollection<U> map(Function<T, U> function) {
        List<U> newList = new ArrayList<>();
        foreach(d -> newList.add(function.apply(d)));
        return new MyCollection<>(newList);
    }

    public void foreach(Consumer<T> consumer) {
        for (int i = 0; i < list.size(); i++) {
            // 여기서 무엇인가 해야함 ~
            T data = list.get(i);
            consumer.accept(data);
            // data 관련 로직
            // 리턴 없음 -> Consumer
        }
    }

    public MyIterator<T> iterator() {
        return new MyIterator<T>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < list.size();
            }

            @Override
            public T next() {
                return list.get(index++);
            }
        };
    }
}
