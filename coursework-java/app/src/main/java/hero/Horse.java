package hero;
public class Horse implements MoveStrategy {
    @Override public String move(Position from, Position to) {
        return "Еду на лошади из %s в %s".formatted(from, to);
    }
}
