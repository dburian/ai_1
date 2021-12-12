import search.*;

import java.util.PriorityQueue;
import java.util.HashMap;
import java.util.HashSet;

// A* search

public class AStar<S, A> {
  public static <S, A> Solution<S, A> search(HeuristicProblem<S, A> prob) {
    var initialState = prob.initialState();
    HeuristicNode<S, A> startingNode = HeuristicNode.fromInitialState(
      initialState,
      prob.estimate(initialState)
      );
    var goalNode = AStar.nodeSearch(startingNode, prob);

    if (goalNode == null) return null;

    return goalNode.toSolution();
  }

  public static <S, A> HeuristicNode<S, A> nodeSearch(
      HeuristicNode<S, A> startingNode,
      HeuristicProblem<S, A> problem
    ) {
    var frontier = new PriorityQueue<HeuristicNode<S, A>>();
    frontier.add(startingNode);

    var exploredStates = new HashSet<S>();
    var stateToFrontier = new HashMap<S, HeuristicNode<S, A>>();

    while (frontier.size() > 0) {
      var n = frontier.poll();
      // System.out.format("Explored states: %s \n", exploredStates.size());

      if (exploredStates.contains(n.state)) continue;
      else exploredStates.add(n.state);

      if (problem.isGoal(n.state)) {
        System.out.format("Explored states: %s \n", exploredStates.size());
        return n;
      }

      for (var succ : n.expand(problem)) {
        var frontierRepre = stateToFrontier.get(succ.state);
        if (frontierRepre == null || frontierRepre.cost >= succ.cost) {
          stateToFrontier.put(succ.state, succ);
          frontier.add(succ);
        }
      }
    }

    return null;

  }
}
