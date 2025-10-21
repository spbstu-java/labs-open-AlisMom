package hero;
public class Walk implements MoveStrategy {
    @Override public String move(Position from, Position to) {
        return "Иду пешком из %s в %s".formatted(from, to);
    }
}
