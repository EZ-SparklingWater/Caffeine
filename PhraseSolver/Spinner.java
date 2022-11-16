public class Spinner {
    private int range;
    private int min;

    public static final char[] visual = new char[] { '/', '-', '|', '\\' };

    public Spinner() {
        range = 400;
        min = 100;
    }

    public Spinner(int range, int min) {
        this.range = range;
        this.min = min;
    }

    public int getValue() {
        return ((int) (Math.random() * range) + min);
    }
}
