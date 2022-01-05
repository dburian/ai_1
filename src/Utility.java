import java.util.Comparator;

import minimax.AbstractGame;

public class Utility {
  double value;
  int depth;

  static Utility maxPlayerWorst = new Utility(Double.NEGATIVE_INFINITY, 0);
  static Utility minPlayerWorst = new Utility(Double.POSITIVE_INFINITY, 0);

  public Utility(double value, int depth) {
    this.value = value;
    this.depth = depth;
  }

  // Returns comparator, which sorts utilities so that the first one is the
  // better one according to `isMaxPlayer`
  public static Comparator<Utility> getComparator(boolean isMaxPlayer) {
    return new Comparator<Utility>() {
      @Override
      public int compare(Utility util1, Utility util2) {
        int maxPlayerInt = 0;

        if (util1.value != util2.value) {
          maxPlayerInt = util1.value < util2.value ? -1 : 1;
        } else if (util1.value == AbstractGame.DRAW) {
          maxPlayerInt = 0;
        } else {
          boolean isLoosing = util1.value < AbstractGame.DRAW;

          maxPlayerInt = isLoosing ? util1.depth - util2.depth : util2.depth - util1.depth;
        }

        if (isMaxPlayer)
          return maxPlayerInt;
        else
          return maxPlayerInt * -1;
      }
    };
  }

  public boolean isBetterThan(Utility other, boolean isMaxPlayer) {
    if (value != other.value)
      return isMaxPlayer ? value > other.value : value < other.value;

    // Treat draw as loosing
    boolean isLoosing = isMaxPlayer ? value < AbstractGame.DRAW : value > AbstractGame.DRAW;

    return isLoosing ? depth > other.depth : depth < other.depth;
  }

  @Override
  public String toString() {
    return String.format("Utility(value: %s, depth: %s)", value, depth);
  }
}
