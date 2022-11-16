import java.util.Scanner;

public class App {
    enum CurrentPlayer {
        PLAYER_ONE,
        PLAYER_TWO
    }

    // the letter bank, with all letters of the alphabet that can be guessed
    private static final String alphabet = "abcdefghijklmnopqrstuvwxyz";

    public static void main(String[] args) {
        boolean finished = false;
        CurrentPlayer currentPlayer = CurrentPlayer.PLAYER_ONE;
        Spinner spinner = new Spinner();

        // player names
        Scanner sc = new Scanner(System.in);
        // todo: catch exceptions
        System.out.print("Name for player 1: ");
        Player player1 = new Player(sc.nextLine());

        System.out.print("Name for player 2: ");
        Player player2 = new Player(sc.nextLine());

        System.out.println();

        while (!finished) {
            Board board = new Board();

            System.out.printf("Player 1 (%s), please press ENTER to spin the spinner!", player1.getName());
            sc.nextLine(); // eat next line
            try {
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        System.out.print(Spinner.visual[j]);
                        Thread.sleep(50);
                        System.out.print('\b');
                    }
                }
            } catch (Exception e) {
                System.out.println();
            }
            player1.setSpinnerVal(spinner.getValue());
            System.out.println("You spun " + player1.getSpinnerVal() + "!");

            System.out.printf("Player 2 (%s), please press ENTER to spin the spinner!", player2.getName());
            sc.nextLine(); // eat next line
            try {
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 4; j++) {
                        System.out.print(Spinner.visual[j]);
                        Thread.sleep(50);
                        System.out.print('\b');
                    }
                }
            } catch (Exception e) {
                System.out.println();
            }
            player2.setSpinnerVal(spinner.getValue());
            System.out.println("You spun " + player2.getSpinnerVal() + "!");

            try {
                Thread.sleep(2500);
            } catch (Exception e) {
                System.out.println(e);
            }

            while (!board.getRevealed()) {
                System.out.print("\u001b[2J" + "\u001b[H");
                System.out.println();
                System.out.println("Player " + (currentPlayer == CurrentPlayer.PLAYER_ONE
                        ? "1 (" + player1.getName() + ")'s turn (score: " + player1.getScore() + ")"
                        : "2 (" + player2.getName() + ")'s turn (score: " + player2.getScore() + ")"));

                boolean validGuess = false;
                boolean goodGuess = false;
                while (!validGuess || goodGuess) {
                    if (board.getRevealed())
                        break;

                    // display phrase + pletters
                    System.out.println();
                    System.out.println("Phrase: " + "\u001B[1m" + board + "\u001B[0m"); // todo: handle letter not in word
                    System.out.println("Letters guessed: [" + "\u001B[1m" + "\u001B[36m" + board.getLettersGuessed()
                            + "\u001B[0m" + "]");
                    System.out.println();

                    System.out.print("Please input a letter ('/' to guess the phrase!): ");
                    String letter = sc.nextLine();

                    // '/' allows to enter the full phrase
                    if (letter.equals("/")) {
                        validGuess = true;
                        System.out.print("Please enter your guess: ");
                        System.out.print("\u001B[7m");
                        String guessPhrase = sc.nextLine();
                        System.out.print("\u001B[0m");
                        if (board.guessPhrase(guessPhrase)) {
                            board.setRevealed(true);
                            System.out.println("Correct!");
                        } else {
                            System.out.println("Sorry! Your guess was incorrect.");
                        }
                        // run checks to see if input is valid
                    } else {
                        if (letter.length() == 1) {
                            if (alphabet.contains(letter)) {
                                if (!board.getLettersGuessed().contains(letter)) {
                                    validGuess = true;
                                    // guess the letter and put it in the bank
                                    goodGuess = board.guessLetter(letter);
                                } else {
                                    validGuess = false;
                                    System.out.println("Letter already guessed!");
                                    continue;
                                }
                            } else {
                                validGuess = false;
                                System.out.println("Please input a lowercase letter!");
                                continue;
                            }
                        } else {
                            validGuess = false;
                            System.out.println("Please input a singular letter!");
                            continue;
                        }
                    }
                }

                // switch the player only if the board isn't revealed
                if (!board.getRevealed()) {
                    // switch the current player
                    if (currentPlayer == CurrentPlayer.PLAYER_ONE)
                        currentPlayer = CurrentPlayer.PLAYER_TWO;
                    else
                        currentPlayer = CurrentPlayer.PLAYER_ONE;
                }
            }

            // display winner + updated score
            System.out.println(
                    (currentPlayer == CurrentPlayer.PLAYER_ONE ? "Player 1" : "Player 2") + " wins the round!");
            if (currentPlayer == CurrentPlayer.PLAYER_ONE)
                player1.addScore(player1.getSpinnerVal());
            else
                player2.addScore(player2.getSpinnerVal());
            System.out.println("You have gained "
                    + (currentPlayer == CurrentPlayer.PLAYER_ONE ? player1.getSpinnerVal() : player2.getSpinnerVal())
                    + " points for a new total of "
                    + (currentPlayer == CurrentPlayer.PLAYER_ONE ? player1.getScore() : player2.getScore()) + "!");

            // ask for continuing game/looping
            System.out.println("Would you like to keep going? (y/n)");
            String response = "";
            while (!response.equals("y") && !response.equals("n")) {
                response = sc.nextLine();
                if (!response.equals("y") && !response.equals("n")) {
                    System.out.println("Invalid input!");
                }
            }

            if (response.equals("n"))
                break;

            System.out.println();
        }
        sc.close();
    }
}
