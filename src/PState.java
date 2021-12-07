public class PState {
  public int loc;
  public int dir;
  public int ticks;

  public PState(int loc, int dir, int ticks) {
    this.loc = loc;
    this.dir = dir;
    this.ticks = ticks;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof PState)) return false;

    PState state = (PState)o;
    return state.loc == loc;
  }

  @Override
  public int hashCode() {
    return loc;
  }
}
