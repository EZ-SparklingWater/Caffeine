import java.util.Scanner;
import java.io.File;

public class Board {
  private String lettersGuessed = "";
  private String phrase = "";
  private boolean revealed = false;

  public Board() {
    this.setPhrase();
  }

  private String loadPhrase() {
    String tempPhrase = "";

    int numOfLines = 0;
    tempPhrase = "how are you";

    try {
      Scanner sc = new Scanner(new File(/* Replace with the path */"/workspace/Caffeine/PhraseSolver/phrases.txt"));
      while (sc.hasNextLine()) {
        tempPhrase = sc.nextLine().trim();
        numOfLines++;
      }
    } catch (Exception e) {
      System.out.println("Error reading or parsing phrases.txt");
    }

    int randomInt = (int) ((Math.random() * numOfLines) + 1);

    try {
      int count = 0;
      Scanner sc = new Scanner(new File(/* Replace with the path */"/workspace/Caffeine/PhraseSolver/phrases.txt"));
      while (sc.hasNextLine()) {
        count++;
        String temp = sc.nextLine().trim();
        if (count == randomInt) {
          tempPhrase = temp;
        }
      }
    } catch (Exception e) {
      System.out.println("Error reading or parsing phrases.txt");
    }
    return tempPhrase;
  }

//list of letters guessed that round
  public String getLettersGuessed() {
    return this.lettersGuessed;
  }

  //correctly guessed letters are shown in their proper places, unguessed letters are represented by _
  public boolean getRevealed() {
    return this.revealed;
  }

  //loads a new phrase after the previous is comshpletely guessed
  public void setPhrase() {
    this.phrase = loadPhrase();
  }

  // checks if the entire phrase has been guessed correctly
  // if all letters in the phrase lie in the lettersGuessed letter bank, the phrase is deemed as fully guessed
  public void setRevealed() {
    for (int i = 0; i < phrase.length(); i++) {
      String currentChar = phrase.substring(i, i + 1);
      if (!currentChar.equals(" ")) {
        if (!lettersGuessed.contains(currentChar)) {
          this.revealed = false;
          return;
        }
      }
    }
    this.revealed = true;
  }

  public void setRevealed(boolean revealed) {
    this.revealed = revealed;
  }

//checks if a player's letter input has already been guessed
// DO NOT CALL WITH LETTERS ALREADY GUESSED
  public boolean guessLetter(String letter) {
    this.lettersGuessed += letter;
    this.setRevealed();
    return this.phrase.contains(letter);
  }

  //checks if the player's guessed phrase matches the mystery phrase
  public boolean guessPhrase(String guessPhrase) {
    return guessPhrase.toLowerCase().equals(phrase);
  } 

  //takes original phrase, keeps any correctly guessed letters, and replaces all other letters with _
  @Override
  public String toString() {
    String ret = "";
    for (int i = 0; i < phrase.length(); i++) {
      String currentChar = phrase.substring(i, i + 1);
      if (lettersGuessed.contains(currentChar) || currentChar.equals(" "))  ret += currentChar;
      else ret += "_";
    }
    return ret;
  }
}