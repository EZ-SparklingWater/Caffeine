import java.util.Scanner;

public class App {
    // enum for players
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

        // loop until players want to exit
        // player starts as last winner
        while (!finished) {
            // create a new board each game to reset props like phrase
            Board board = new Board();

            System.out.printf("Player 1 (%s), please press ENTER to spin the spinner!", player1.getName());
            sc.nextLine(); // eat next line
            // display spinner (maybe make into a function)
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
            // fairly self explanatory, player spin print out
            player1.setSpinnerVal(spinner.getValue());
            System.out.println("You spun " + player1.getSpinnerVal() + "!");

            System.out.printf("Player 2 (%s), please press ENTER to spin the spinner!", player2.getName());
            sc.nextLine(); // eat next line
            // display spinner
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
            // just a delay, so after the second player's spin the code will pause to allow
            // the players to read the spun number
            try {
                Thread.sleep(2500);
            } catch (Exception e) {
                System.out.println(e);
            }

            while (!board.getRevealed()) {
                // clear screen every time player switches (when one person's turn is over)
                System.out.print("\u001b[2J" + "\u001b[H");
                // System.out.println();
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
                    System.out.println("Phrase: " + "\u001B[1m" + board + "\u001B[0m"); // display board in bold
                    System.out.println("Letters guessed: [" + "\u001B[1m" + "\u001B[36m" + board.getLettersGuessed() // displayed
                                                                                                                     // bold
                                                                                                                     // and
                                                                                                                     // orange
                            + "\u001B[0m" + "]");
                    System.out.println();

                    System.out.print("Please input a letter ('/' to guess the phrase!): ");
                    String letter = sc.nextLine();

                    // '/' allows to enter the full phrase
                    if (letter.equals("/")) {
                        validGuess = true;
                        System.out.print("Please enter your guess: ");
                        // make user input inverted in color
                        System.out.print("\u001B[7m");
                        String guessPhrase = sc.nextLine();
                        System.out.print("\u001B[0m");
                        if (board.guessPhrase(guessPhrase)) {
                            board.setRevealed(true);
                            System.out.println("Correct!");
                        } else {
                            System.out.println("\u001B[31m" + "Sorry! Your guess was incorrect." + "\u001B[0m");
                        }
                    } else { // run checks to see if input is valid
                        if (letter.length() == 1) {
                            if (alphabet.contains(letter)) {
                                if (!board.getLettersGuessed().contains(letter)) {
                                    validGuess = true;
                                    // guess the letter and put it in the bank
                                    goodGuess = board.guessLetter(letter);
                                } else {
                                    validGuess = false;
                                    System.out.println("\u001B[31m" + "Letter already guessed!" + "\u001B[0m");
                                    continue;
                                }
                            } else {
                                validGuess = false;
                                System.out.println("\u001B[31m" + "Please input a lowercase letter!" + "\u001B[0m");
                                continue;
                            }
                        } else {
                            validGuess = false;
                            System.out.println("\u001B[31m" + "Please input a singular letter!" + "\u001B[0m");
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

            // display winner of round + update scores
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
            System.out.println();
            System.out.println("\u001B[3m" + "Would you like to keep going? (y/n)" + "\u001B[0m");
            String response = "";
            while (!response.equals("y") && !response.equals("n")) {
                response = sc.nextLine();
                if (!response.equals("y") && !response.equals("n")) {
                    System.out.println("Invalid input!");
                }
            }

            // display overall winner and exit game
            if (response.equals("n")) {
                String winner = "";
                if (player1.getScore() > player2.getScore()) {
                    winner = "Player 1 (" + player1.getName() + ")!";
                } else if (player1.getScore() < player2.getScore()) {
                    winner = "Player 2 (" + player2.getName() + ")!";
                } else {
                    winner = "nobody...";
                }

                System.out.println("The winner is: " + winner);
                System.out.println("Thanks for playing!");

                break;
            }

            System.out.println();
        }

        // close scanner to prevent leakage
        sc.close();
    }
}
