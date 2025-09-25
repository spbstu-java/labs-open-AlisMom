package org.example;

import java.util.*;

public class App {
    public static void main(String[] args) {
        List<Integer> nums = Arrays.asList(1, 2, 2, 3, 4);
        List<String> strs = Arrays.asList("apple", "banana", "cherry");

        System.out.println("Среднее: " + StreamTasks.average(nums));
        System.out.println("В верхний регистр: " + StreamTasks.transformStrings(strs));
        System.out.println("Уникальные квадраты: " + StreamTasks.uniqueSquares(nums));
        System.out.println("Последний элемент: " + StreamTasks.getLast(strs));
        System.out.println("Сумма четных: " + StreamTasks.sumEven(new int[]{1, 2, 3, 4, 5, 6}));
        System.out.println("Преобразование в таблицу: " + StreamTasks.toMap(strs));
    }
}
