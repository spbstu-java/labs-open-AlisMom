package hero;

public class Horse implements MovementStrategy {
    public String name() { return "Лошадь"; }
    public void move(Position from, Position to) {
        System.out.printf("Еду на лошади из (%d,%d) в (%d,%d)%n", from.x, from.y, to.x, to.y);
    }
}
