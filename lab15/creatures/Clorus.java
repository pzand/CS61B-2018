package creatures;

import huglife.*;

import java.awt.Color;
import java.util.Map;
import java.util.List;

public class Clorus extends Creature {
    private int r;
    private int g;
    private int b;

    public Clorus() {
        this(1.0);
    }

    // All Cloruses must have a name equal to exactly “clorus”
    public Clorus(double energy) {
        super("clorus");
        r = 34;
        g = 0;
        b = 231;
        this.energy = energy;
    }

    // Cloruses should always return the color red = 34, green = 0, blue = 231
    @Override
    public Color color() {
        return color(r, g, b);
    }

    // lose 0.03 units of energy on a MOVE action
    @Override
    public void move() {
        energy -= 0.03;
    }

    // If a Clorus attacks another creature, it should gain that creature’s energy
    @Override
    public void attack(Creature c) {
        energy += c.energy();
    }

    // When a Clorus replicates, it keeps 50% of its energy. The other 50% goes to its offspring
    @Override
    public Creature replicate() {
        energy = energy() * 0.5;
        return new Clorus(energy());
    }

    // lose 0.01 units of energy on a STAY action
    @Override
    public void stay() {
        energy -= 0.01;
    }

    /**
     * If there are no empty squares, the Clorus will STAY (even if there are Plips nearby they could attack).
     * Otherwise, if any Plips are seen, the Clorus will ATTACK one of them randomly.
     * Otherwise, if the Clorus has energy greater than or equal to one, it will REPLICATE to a random empty square.
     * Otherwise, the Clorus will MOVE to a random empty square
     */
    @Override
    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        List<Direction> empty = getNeighborsOfType(neighbors, "empty");
        if (empty.size() == 0) {
            return new Action(Action.ActionType.STAY);
        }

        List<Direction> plips = getNeighborsOfType(neighbors, "plip");
        if (plips.size() >= 1) {
            return new Action(Action.ActionType.ATTACK, HugLifeUtils.randomEntry(plips));
        }

        if (energy() >= 1) {
            return new Action(Action.ActionType.REPLICATE, HugLifeUtils.randomEntry(empty));
        }

        return new Action(Action.ActionType.MOVE, HugLifeUtils.randomEntry(empty));
    }
}
