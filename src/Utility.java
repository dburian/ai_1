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

  // TODO:
  // public static Comparator<Utility> getComparator(boolean isMaxPlayer) {
  //   if (isMaxPlayer) {
  //     return new Comparator<Utility>() {
  //       @Override
  //       public int compare(Utility util1, Utility util2) {
  //         if (util1.value != util2.value) {
  //           return util1.value < util2.value ? -1 : 1;
  //         }


  //       }
  //     };
  //   }
  // }

  public boolean isBetterThan(Utility other, boolean isMaxPlayer) {
    if (value != other.value)
      return isMaxPlayer ? value > other.value : value < other.value;

    //Treat draw as loosing
    boolean isLoosing = isMaxPlayer ?
      value < AbstractGame.DRAW : value > AbstractGame.DRAW;

    return isLoosing ? depth > other.depth : depth < other.depth;
  }

  @Override
  public String toString() {
    return String.format("Utility(value: %s, depth: %s)", value, depth);
  }

}
