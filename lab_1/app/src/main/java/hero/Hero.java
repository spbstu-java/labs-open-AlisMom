package hero;

public class Hero {
    private MovementStrategy strategy;
    public Hero(MovementStrategy strategy) { this.strategy = strategy; }
    public void setStrategy(MovementStrategy s) {
        this.strategy = s;
        System.out.println("Стратегия: " + s.name());
    }
    public void move(Position from, Position to) { strategy.move(from, to); }
}
