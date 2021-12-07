import java.util.List;
import java.util.ArrayList;

import search.*;

import game.core.Game;
import game.core.G;
import game.core.Game.DM;

public class PProblem implements Problem<PState, PAction> {
  private Game game;

  private static final int SAFE_GHOST_DIST = 10;
  private static final int GHOST_DIST_PEN = 500;
  private static final double EDIBLE_GHOST_SPEED_PEN = 0.3;

  public PProblem(Game game) {
    this.game = game;
  }
  public PState initialState() {
    return new PState(game.getCurPacManLoc(), game.getCurPacManDir(), 0);
  }

  public List<PAction> actions(PState state) {
    List<PAction> actions = new ArrayList<>();
    for (int dir : game.getPossibleDirs(state.loc, state.dir, true)) {
      actions.add(new PAction(dir));
    }

    return actions;
  }

  public PState result(PState state, PAction action) {
    int newLoc = game.getNeighbour(state.loc, action.dir);
    return new PState(newLoc, action.dir, state.ticks + 1);
  }

  public boolean isGoal(PState state) {
    int pillIndex = game.getPillIndex(state.loc);
    boolean isActivePill = pillIndex != -1 && game.checkPill(pillIndex);
    if (isActivePill) return true;

    int powerPillIndex = game.getPowerPillIndex(state.loc);
    boolean isActivePowerPill = powerPillIndex != -1 &&
      game.checkPowerPill(powerPillIndex);

    if (isActivePowerPill) return true;


    int ghostInd = getGhostAt(state.loc);
    boolean isEdibleGhost = ghostInd != -1 &&
      willBeGhostEdible(
        game.getPathDistance(state.loc, game.getCurGhostLoc(ghostInd)),
        state.ticks,
        ghostInd
      );
    if (isEdibleGhost) return true;

    return false;
  }

  public double cost(PState state, PAction action) {
    double cost = 1;
    cost += ghostAproximityCost(state, action);

    return cost;
  }

  private double ghostAproximityCost(PState state, PAction action) {
    int newLoc = game.getNeighbour(state.loc, action.dir);

    List<Integer> dangerGhostsLocs = new ArrayList<Integer>(4);
    for (int i = 0; i < 4; i++) {
      if (headingIntoGhost(state.loc, action.dir, i)) {
        int distToGhost = game.getPathDistance(newLoc, game.getCurGhostLoc(i));

        if (!willBeGhostEdible(distToGhost, state.ticks + 1, i))
          dangerGhostsLocs.add(game.getCurGhostLoc(i));
      }
    }

    if (dangerGhostsLocs.size() == 0) return 0;

    int nearestDangerGhostLoc = game.getTarget(
      newLoc,
      dangerGhostsLocs.stream().mapToInt(i -> i).toArray(),
      true,
      DM.PATH
    );
    int watchedDist = game.getPathDistance(newLoc, nearestDangerGhostLoc);

    double cost = Math.max(0, (SAFE_GHOST_DIST - watchedDist)) * GHOST_DIST_PEN;

    return cost;
  }

  private int[] getGhostLocs() {
    int[] ghostLocs = new int[4];
    for (int i = 0; i < ghostLocs.length; i++) ghostLocs[i] = game.getCurGhostLoc(i);

    return ghostLocs;
  }

  private boolean willBeGhostEdible(int dist, int passedTicks, int whichGhost) {
    if (!game.isEdible(whichGhost)) return false;

    //Assume ghost is moving from us - wcs
    return dist + EDIBLE_GHOST_SPEED_PEN * passedTicks <= game.getEdibleTime(whichGhost);
  }

  private int getGhostAt(int loc) {
    int[] ghostLocs = getGhostLocs();
    for (int i = 0; i < 4; i++) {
      if (ghostLocs[i] == loc) return i;
    }

    return -1;
  }

  private boolean headingIntoGhost(int loc, int dir, int whichGhost) {
    int newLoc = game.getNeighbour(loc, dir);
    int ghostLoc = game.getCurGhostLoc(whichGhost);

    int oldDist = game.getPathDistance(loc, ghostLoc);
    int newDist = game.getPathDistance(newLoc, ghostLoc);
    return newDist < oldDist;
  }

}
