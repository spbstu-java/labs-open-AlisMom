package hero;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Position from = new Position(0, 0);
        Position to   = new Position(10, 5);
        Hero hero = new Hero(new Walk());

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("""
                === Перемещение героя ===
                1) Пешком
                2) Лошадь
                3) Полёт
                4) Переместиться из (0,0) в (10,5)
                0) Выход
                """);
            System.out.print("Выбор: ");
            String c = sc.nextLine().trim();
            switch (c) {
                case "1" -> hero.setStrategy(new Walk());
                case "2" -> hero.setStrategy(new Horse());
                case "3" -> hero.setStrategy(new Fly());
                case "4" -> hero.move(from, to);
                case "0" -> { return; }
                default -> System.out.println("?");
            }
        }
    }
}
