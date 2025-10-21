package hero;
public class Fly implements MoveStrategy {
    @Override public String move(Position from, Position to) {
        return "Лечу из %s в %s".formatted(from, to);
    }
}
