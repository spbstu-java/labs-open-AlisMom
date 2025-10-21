package hero;

public class Hero {
    private MoveStrategy strategy;
    public Hero(MoveStrategy s) { this.strategy = s; }
    public void setStrategy(MoveStrategy s) { this.strategy = s; }
    public String move(Position from, Position to) {
        return "[%s] %s".formatted(strategy.name(), strategy.move(from, to));
    }
}
