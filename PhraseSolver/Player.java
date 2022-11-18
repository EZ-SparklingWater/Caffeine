public class Player {
    private String name;
    private int score;
    private int spinnerVal;

    // player, score, spinner val
    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public int getScore() {
        return this.score;
    }

    public int getSpinnerVal() {
        return this.spinnerVal;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setSpinnerVal(int spinnerVal) {
        this.spinnerVal = spinnerVal;
    }

    public void addScore(int score) {
        this.score += score;
        // this.score = this.score + score;
    }
}
