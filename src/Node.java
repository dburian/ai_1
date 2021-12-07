import search.*;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collections;

public class Node<S, A> implements Comparable<Node<S, A>> {
  public Node<S, A> prev_node;
  public A action;
  public S state;
  public double cost;

  private Node(Node<S, A> prev_node, A action, S state, double cost) {
    this.prev_node = prev_node;
    this.action = action;
    this.state = state;
    this.cost = cost;
  }

  public static <S, A> Node<S, A> fromInitialState(S state) {
    return new Node<S, A>(null, null, state, 0);
  }

  public List<Node<S, A>> expand(Problem<S, A> problem) {
    List<Node<S, A>> successors = new ArrayList<Node<S, A>>();
    for (A action : problem.actions(state)) {
      var succ_state = problem.result(state, action);
      var succ_cost = cost + problem.cost(state, action);

      successors.add(new Node<S, A>(
        this,
        action,
        succ_state,
        succ_cost
      ));
    }

    return successors;
  }

  public Solution<S, A> toSolution() {
    List<A> actions = new ArrayList<A>();
    var node = this;
    while (node != null && node.action != null) {
      actions.add(node.action);
      node = node.prev_node;
    }

    Collections.reverse(actions);
    return new Solution<S, A>(actions, state, cost);
  }

  public int compareTo(Node<S, A> other) {
    if ((cost - other.cost) > 0) return 1;
    else return -1;
  }
}
