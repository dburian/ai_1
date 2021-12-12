import search.*;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class HeuristicNode<S, A> implements Comparable<HeuristicNode<S, A>> {
  public HeuristicNode<S, A> prevNode;
  public A action;
  public S state;
  public double cost;
  public double heureisticCost;

  private HeuristicNode(
      HeuristicNode<S, A> prevNode,
      A action,
      S state,
      double cost,
      double heureisticCost
    ) {
    this.prevNode = prevNode;
    this.action = action;
    this.state = state;
    this.cost = cost;
    this.heureisticCost = heureisticCost;
  }

  public static <S, A> HeuristicNode<S, A> fromInitialState(
      S initialState,
      double heureisticCost
    ) {
    return new HeuristicNode<S, A>(
        null,
        null,
        initialState,
        0,
        heureisticCost
      );
  }

  public int compareTo(HeuristicNode<S, A> other) {
    double thisTotalCost = cost + heureisticCost;
    double otherTotalCost = other.cost + other.heureisticCost;

    if (thisTotalCost > otherTotalCost) return 1;
    else return -1;
  }

  public List<HeuristicNode<S, A>> expand(HeuristicProblem<S, A> problem) {
    var successors = new ArrayList<HeuristicNode<S, A>>();
    for (A action : problem.actions(state)) {
      var succState = problem.result(state, action);
      var succCost = cost + problem.cost(state, action);
      var succHeureisticCost = problem.estimate(succState);

      successors.add(new HeuristicNode<S, A>(
          this,
          action,
          succState,
          succCost,
          succHeureisticCost
      ));
    }

    return successors;
  }

  public Solution<S, A> toSolution() {
    List<A> actions = new ArrayList<A>();
    var node = this;
    while (node != null && node.action != null) {
      actions.add(node.action);
      node = node.prevNode;
    }

    Collections.reverse(actions);
    return new Solution<S, A>(actions, state, cost);
  }
}
