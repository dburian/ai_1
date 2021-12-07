import java.awt.Color;

import search.*;

import controllers.pacman.PacManControllerBase;
import game.core.G;
import game.core.Game;
import game.core.GameView;

public final class MyAgent extends PacManControllerBase {
  @Override
  public void tick(Game game, long timeDue) {
    // Code your agent here.
    // Dummy implementation: move in a random direction.  You won't live long this way,
    PProblem prob = new PProblem(game);
    Solution<PState, PAction> solut = Ucs.search(prob);

    //Possible if goals are surrounded by ghosts
    if (solut == null || solut.actions.size() == 0) {
      return;
    }

    // System.out.println(solut.pathCost);

    GameView.addPoints(game,Color.GREEN,game.getPath(game.getCurPacManLoc(),
          solut.goalState.loc));

    pacman.set(solut.actions.get(0).dir);
  }
}
