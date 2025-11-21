package puppy.code;

public class HighScoreManager {

    private int highScore;

    private HighScoreManager() {
        this.highScore = 0;
    }

    private static class SingletonHolder {
        private static final HighScoreManager INSTANCE = new HighScoreManager();
    }

    public static HighScoreManager getInstance() {
        return SingletonHolder.INSTANCE;
    }
    public int getHighScore() { return highScore; }
    public void setHighScore(int score) {
        if (score > this.highScore) this.highScore = score;
    }
}
