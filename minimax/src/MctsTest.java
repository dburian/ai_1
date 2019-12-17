public class MctsTest {
    public static boolean testTicTacToe() {
        TicTacToe game = new TicTacToe();

        int Games = 400;

        Mcts<TTState, Integer> mcts =
            new Mcts<>(game, new BasicTicTacToeStrategy(),
                       new TTActionGenerator(), new TTResultGenerator(),
                       1000);

        int[][] wins = Runner.play2(game, mcts, new BasicTicTacToeStrategy(), Games);
        mcts.reportStats();
        return Runner.report(Games, wins, true, 45, 3);
    }

    public static boolean testPig() {
        Pig game = new Pig();

        int Games = 100;

        Mcts<PigState, Boolean> mcts =
            new Mcts<>(game, new RandomPigStrategy(),
                       new PigActionGenerator(), new PigResultGenerator(),
                       5000); 

        int[][] wins = Runner.play2(game, mcts, new RandomPigStrategy(), Games);
        mcts.reportStats();
        return Runner.report(Games, wins, false, 55, 50);
    }

    public static void test(String[] args) {
        testTicTacToe();
        testPig();
    }
}
