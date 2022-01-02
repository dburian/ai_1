package minimax.trivial;

public class TrivialState {
    int p1move, p2move;

    TrivialState(int p1move, int p2move) {
        this.p1move = p1move;
        this.p2move = p2move;
    }

    @Override
    public String toString() {
      return String.format("TrivialState(p1move: %s, p2move: %s)", p1move, p2move);
    }
}
