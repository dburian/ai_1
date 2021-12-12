import search.*;

import java.util.PriorityQueue;
import java.util.HashMap;
import java.util.HashSet;

// uniform-cost search

public class Ucs<S, A> {
  public static <S, A> Solution<S, A> search(Problem<S, A> prob) {
    Node<S, A> startingNode = Node.fromInitialState(prob.initialState());
    Node<S, A> goalNode = Ucs.nodeSearch(startingNode, prob);

    if (goalNode == null) return null;

    return goalNode.toSolution();
  }

  private static <S, A> Node<S, A> nodeSearch(Node<S, A> startingNode, Problem<S, A> problem) {
    PriorityQueue<Node<S, A>> frontier = new PriorityQueue<Node<S, A>>();
    frontier.add(startingNode);

    HashSet<S> exploredStates = new HashSet<S>();
    HashMap<S, Node<S, A>> stateToFrontier = new HashMap<S, Node<S, A>>();

    while (frontier.size() > 0) {
      var n = frontier.poll();

      if (exploredStates.contains(n.state)) continue;
      else exploredStates.add(n.state);

      if (problem.isGoal(n.state)) return n;

      for (var succ : n.expand(problem)) {
        var frontierRepre = stateToFrontier.get(succ.state);
        if (frontierRepre == null || frontierRepre.cost > succ.cost) {
          stateToFrontier.put(succ.state, succ);
          frontier.add(succ);
        }
      }
    }

    return null;
  }
}
