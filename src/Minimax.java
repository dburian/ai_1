import minimax.*;

import static java.lang.System.out;

import java.util.Comparator;

public class Minimax<S, A> implements Strategy<S, A> {
  int debugLevel = 0;

  private class Move {
    A action;
    Utility utility;

    public Move(A action, Utility utility) {
      this.action = action;
      this.utility = utility;
    }
  }

  int limit;
  HeuristicGame<S, A> game;

  public Minimax(HeuristicGame<S, A> game, int limit) {
    this.limit = limit;
    this.game = game;
  }

  public A action(S state) {
    var bestMove = chooseBestMove(
        state,
        0,
        Utility.maxPlayerWorst,
        Utility.minPlayerWorst);

    return bestMove.action;
  }

  private Move chooseBestMove(S state, int depth, Utility alpha, Utility beta) {
    boolean isMaxPlayer = game.player(state) == 1;
    Comparator<Utility> comp = Utility.getComparator(isMaxPlayer);

    var chosenUtility = isMaxPlayer ? Utility.maxPlayerWorst : Utility.minPlayerWorst;
    A chosenAction = null;

    for (var action : game.actions(state)) {
      var newState = game.clone(state);
      game.apply(newState, action);
      var newUtility = exploreState(newState, depth + 1, alpha, beta);

      if (comp.compare(newUtility, chosenUtility) >= 1) {
        chosenUtility = newUtility;
        chosenAction = action;

        if (isMaxPlayer) {
          alpha = comp.compare(newUtility, alpha) >= 1 ? newUtility : alpha;
        } else {
          beta = comp.compare(newUtility, beta) >= 1 ? newUtility : beta;
        }
      }

      if (isMaxPlayer && comp.compare(chosenUtility, beta) >= 0) {
        return new Move(chosenAction, chosenUtility);
      } else if (!isMaxPlayer && comp.compare(chosenUtility, alpha) >= 0) {
        return new Move(chosenAction, chosenUtility);
      }

    }

    return new Move(chosenAction, chosenUtility);
  }

  private Utility exploreState(S state, int depth, Utility alpha, Utility beta) {
    if (game.isDone(state)) {

      return new Utility(game.outcome(state), depth);
    }

    if (limit != 0 && depth == limit) {

      return new Utility(game.evaluate(state), Integer.MAX_VALUE);
    }

    return chooseBestMove(state, depth, alpha, beta).utility;
  }

}
