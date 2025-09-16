package hero;

public class Fly implements MovementStrategy {
    public String name() { return "Полёт"; }
    public void move(Position from, Position to) {
        System.out.printf("Лечу из (%d,%d) в (%d,%d)%n", from.x, from.y, to.x, to.y);
    }
}
