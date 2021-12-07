import search.*;

import java.util.PriorityQueue;
import java.util.HashMap;
import java.util.HashSet;

// uniform-cost search

public class Ucs<S, A> {
  public static <S, A> Solution<S, A> search(Problem<S, A> prob) {
    Node<S, A> starting_node = Node.fromInitialState(prob.initialState());
    Node<S, A> goal_node = Ucs.nodeSearch(starting_node, prob);

    if (goal_node == null) return null;

    return goal_node.toSolution();
  }

  private static <S, A> Node<S, A> nodeSearch(Node<S, A> starting_node, Problem<S, A> problem) {
    PriorityQueue<Node<S, A>> frontier = new PriorityQueue<Node<S, A>>();
    frontier.add(starting_node);

    HashSet<S> explored_states = new HashSet<S>();
    HashMap<S, Node<S, A>> state_to_frontier = new HashMap<S, Node<S, A>>();

    while (frontier.size() > 0) {
      var n = frontier.poll();

      if (explored_states.contains(n.state)) continue;
      else explored_states.add(n.state);

      if (problem.isGoal(n.state)) return n;

      for (var succ : n.expand(problem)) {
        var frontier_repre = state_to_frontier.get(succ.state);
        if (frontier_repre == null || frontier_repre.cost > succ.cost) {
          state_to_frontier.put(succ.state, succ);
          frontier.add(succ);
        }
      }
    }

    return null;
  }
}
