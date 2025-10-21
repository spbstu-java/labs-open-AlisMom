package hero;

public interface MoveStrategy {
    String move(Position from, Position to);
    default String name() { return getClass().getSimpleName(); }
}