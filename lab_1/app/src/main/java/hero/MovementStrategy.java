package hero;

public interface MovementStrategy {
    String name();
    void move(Position from, Position to);
}
