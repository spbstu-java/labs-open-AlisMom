package hero;

public class Walk implements MovementStrategy {
    public String name() { return "Пешком"; }
    public void move(Position from, Position to) {
        System.out.printf("Иду пешком из (%d,%d) в (%d,%d)%n", from.x, from.y, to.x, to.y);
    }
}
