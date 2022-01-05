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

    debugPrint(String.format("Depth: %s, Maximizing player: %s", depth, isMaxPlayer), 1);

    var chosenUtility = isMaxPlayer ? Utility.maxPlayerWorst : Utility.minPlayerWorst;
    A chosenAction = null;

    for (var action : game.actions(state)) {
      var newState = game.clone(state);
      game.apply(newState, action);
      var newUtility = exploreState(newState, depth + 1, alpha, beta);

      if (comp.compare(newUtility, chosenUtility) >= 1) {
        debugPrint(String.format(
            "New best move for max %s. Action: %s, Utility: %s, Best utility: %s",
            isMaxPlayer,
            action,
            newUtility,
            chosenUtility), 1);

        chosenUtility = newUtility;
        chosenAction = action;

        if (isMaxPlayer) {
          alpha = comp.compare(newUtility, alpha) >= 1 ? newUtility : alpha;
        } else {
          beta = comp.compare(newUtility, beta) >= 1 ? newUtility : beta;
        }
      }

      if (isMaxPlayer && comp.compare(chosenUtility, beta) >= 0) {
        debugPrint(
            String.format("Beta cut-off. Beta: %s, util: %s", beta, chosenUtility),
            1);
        return new Move(chosenAction, chosenUtility);
      } else if (!isMaxPlayer && comp.compare(chosenUtility, alpha) >= 0) {
        debugPrint(
            String.format("Alpha cut-off. Alpha: %s, util: %s", alpha, chosenUtility),
            1);
        return new Move(chosenAction, chosenUtility);
      }

      debugPrint(
        String.format("Un cut-off state. Max player: %s, [%s, %s], util: %s, state:\n%s",
          isMaxPlayer,
          alpha,
          beta,
          newUtility,
          newState
          ), 1);
    }

    debugPrint(
        String.format(
            "Choosing action %s, with utility: %s",
            chosenAction,
            chosenUtility),
        1);

    return new Move(chosenAction, chosenUtility);
  }

  private Utility exploreState(S state, int depth, Utility alpha, Utility beta) {
    if (game.isDone(state)) {
      debugPrint(
          String.format("Game done. State: %s, outcome: %s", state, game.outcome(state)),
          1);

      return new Utility(game.outcome(state), depth);
    }

    if (limit != 0 && depth == limit) {
      debugPrint(
          String.format(
              "Limit %s reached. State: %s, evaluation: %s",
              depth,
              state,
              game.evaluate(state)),
          1);

      return new Utility(game.evaluate(state), Integer.MAX_VALUE);
    }

    return chooseBestMove(state, depth, alpha, beta).utility;
  }

  private void debugPrint(String msg, int level) {
    if (level <= debugLevel) {
      out.println(msg);
    }
  }

}
