/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


//11/19/18
//********* Import Statements ************
import java.io.File;
import java.util.*;
import javafx.application.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.stage.*;
import java.io.IOException;
import javafx.scene.input.MouseEvent;
//******************************************


public class HangmanGUI extends Application {

        // ******************************** Variables ***************************************************
	private String currentWord; // the randomly selected word
	private TextField guessField; // the user enters their guess here
	private Text currentWordText; // show the current word (with - for unguessed letters)
	private Text outcomeText; // show the outcome of each guess and the game
	private Text wrongGuessesText; // show a list of incorrect guesses
	private Text wrongGuessNumberText; // show how many incorrect guesses (or how many guesses remain)
	private Button playAgain; // play again button
	private int gameWatch;
	private int correct;
	private final static int MAX_WRONG_GUESSES = 7;
	private static final Color TITLE_AND_OUTCOME_COLOR = Color.rgb(221, 160, 221);
	private static final Color INFO_COLOR = Color.rgb(224, 255, 255);
	private static final Color WORD_COLOR = Color.rgb(224, 255, 255);
	private List<String> guessed = new ArrayList<>();
	private List<String> wrong = new ArrayList<>();
        
        // *********************************************************************************************
       
        
	public void start(Stage primaryStage) {

		VBox mainVBox = new VBox();
		mainVBox.setStyle("-fx-background-color: royalblue");
		mainVBox.setAlignment(Pos.CENTER);
		mainVBox.setSpacing(10);

		Text welcomeText = new Text("Welcome to Hangman!");
		welcomeText.setFont(Font.font("Helvetica", FontWeight.BOLD, 36));
		welcomeText.setFill(TITLE_AND_OUTCOME_COLOR);
		Text introText1 = new Text("Guess a letter.");
		Text introText2 = new Text("You can make " + MAX_WRONG_GUESSES + " wrong guesses!");
		introText1.setFont(Font.font("Helvetica", 24));
		introText1.setFill(INFO_COLOR);
		introText2.setFont(Font.font("Helvetica", 24));
		introText2.setFill(INFO_COLOR);

		VBox introBox = new VBox(welcomeText, introText1, introText2);
		introBox.setAlignment(Pos.CENTER);
		introBox.setSpacing(10);
		mainVBox.getChildren().add(introBox);


		// create before game is started
		outcomeText = new Text("");
		guessField = new TextField();
		wrongGuessNumberText = new Text("Number of Guesses Remaining = " + (MAX_WRONG_GUESSES - gameWatch));
		currentWord = chooseWord();
		currentWordText = new Text(hideWord(currentWord));
		wrongGuessesText = new Text("Wrong Guesses: "+wrong);
		gameWatch = 0;
		correct = 0;


		currentWordText.setFont(Font.font("Helvetica", FontWeight.BOLD, 48));
		currentWordText.setFill(WORD_COLOR);
		HBox currentBox = new HBox(currentWordText);
		currentBox.setAlignment(Pos.CENTER);
		currentBox.setSpacing(10);
		mainVBox.getChildren().add(currentBox);

		Text guessIntroText = new Text("Enter your guess: ");
		guessIntroText.setFont(Font.font("Helvetica", 26));
		guessIntroText.setFill(INFO_COLOR);
		guessField.setOnAction(this::handleGuessField);
		HBox guessBox = new HBox(guessIntroText, guessField);
		guessBox.setAlignment(Pos.CENTER);
		guessBox.setSpacing(10);
		mainVBox.getChildren().add(guessBox);

		outcomeText.setFont(Font.font("Helvetica", 28));
		outcomeText.setFill(TITLE_AND_OUTCOME_COLOR);
		HBox outcomeBox = new HBox(outcomeText);
		outcomeBox.setAlignment(Pos.CENTER);
		outcomeBox.setSpacing(10);
		mainVBox.getChildren().add(outcomeBox);

		wrongGuessesText.setFont(Font.font("Helvetica", 24));
		wrongGuessesText.setFill(INFO_COLOR);
		HBox wrongGuessesBox = new HBox(wrongGuessesText);
		wrongGuessesBox.setAlignment(Pos.CENTER);
		wrongGuessesBox.setSpacing(10);
		mainVBox.getChildren().add(wrongGuessesBox);

		wrongGuessNumberText.setFont(Font.font("Helvetica", 24));
		wrongGuessNumberText.setFill(INFO_COLOR);
		HBox wrongGuessNumberBox = new HBox(wrongGuessNumberText);
		wrongGuessNumberBox.setAlignment(Pos.CENTER);
		mainVBox.getChildren().add(wrongGuessNumberBox);

		playAgain = new Button("Play Again");
		playAgain.setAlignment(Pos.CENTER);
                playAgain.setOnMouseClicked(this::resetGame);
		mainVBox.getChildren().add(playAgain);
		playAgain.setVisible(false);
		
          
               
		Scene scene = new Scene(mainVBox, 630, 500);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();


	}
        // *********************** Helper Methods *************************
               private void repeatGuess (String c) throws IllegalArgumentException
        {
            for (int i = 0; i < guessed.size(); i++)
            {
                if(c.equals(guessed.get(i))){
                    throw new IllegalArgumentException();
                    
                }
            }
        }
        
        private void emptyGuess(String c) throws InputMismatchException
        {
            if (c.isEmpty() == true)
            {
                throw new InputMismatchException();
            }
        }
        
        private void tooLong(String c) throws NoSuchElementException
        {
            if (guessField.getText().length() > 1)
                throw new NoSuchElementException();
        }
        
        private void isNum(String c) throws InputMismatchException
        {
            char s = c.charAt(0);
            
            if (Character.isLetter(s) == false)
            {
                throw new InputMismatchException();
            }
        }

        // *******************************************************************
        
	private void handleGuessField(ActionEvent event) {
            
		//reset outcomeText
		outcomeText.setText("");
               
            // reads in input from guessField and checks validity of input
                
            try{ 
                /* 
                    repeatGuess method checks if user already guessed this letter.
                    Throws exception that is caught in catch block if user has, then 
                    prints out an error message. Does same for empty input, too
                    long input, or numerical input.
                */
                tooLong(guessField.getText());
                 emptyGuess(guessField.getText());
                 isNum(guessField.getText());
                repeatGuess(guessField.getText());
                guessed.add(guessField.getText());
                
                // Message 
                boolean message = false;
		for(int x = 0; x < currentWord.length(); x++){
			if(guessField.getText().equals(String.valueOf(currentWord.charAt(x)))){
			message = true;
                    }
		}
		if (message == true){
			outcomeText.setText("Good guess!");
			correct++;
                       // gameWatch++;
		} else {
                    outcomeText.setText("Nope! Try again.");
                    wrong.add(guessField.getText().toUpperCase());
                    wrongGuessesText.setText("Wrong Guesses: "+ wrong);
                    gameWatch++;
                    
                    
                                
                }
		//updates display
                for(int y = 0; y < guessed.size(); y++){
                    for(int x = 0; x < currentWord.length(); x++){
                        if(guessed.get(y).equals(String.valueOf(currentWord.charAt(x)))){
				// deobfuscate
				revealWord(y);
			}
                    }
                }
       
            } catch (InputMismatchException | IllegalFormatException notOrEmpty){
                    outcomeText.setText("Error! Please input a letter");
                    guessField.clear();
            } catch(NoSuchElementException tooLong){
                    outcomeText.setText("Error! You must enter 1 letter");
                    guessField.clear();
            } catch(IllegalArgumentException alreadyGuessed){
                    outcomeText.setText("You already guessed this letter");
            } finally {
                    guessField.clear();
            }

            
            wrongGuessNumberText.setText("Number of Guesses Remaining = " + (MAX_WRONG_GUESSES - gameWatch));
		guessField.clear();

		// Then also check if the game is over
		// check string array is equal to String from chooseWord
		// or max wrong guesses
		if(gameWatch >= MAX_WRONG_GUESSES){
			outcomeText.setText("Better luck next time! " + "("+currentWord.toUpperCase()+")");
			guessField.setText("");
                        
			guessField.setDisable(true);
                        playAgain.setVisible(true);
                        
                      
		}

		if(correct == currentWord.length()){
			guessField.setText("");
			guessField.setDisable(true);
			outcomeText.setText("You guessed it! Great job!");
                        playAgain.setVisible(true);
                        
			
		}
        }
        
        public void resetGame(MouseEvent e){
            
            // resets the game's variables and clears guessed and wrong arrays
            gameWatch = 0;
            correct = 0;
            wrong.clear();
            guessed.clear();
            
            // Sets text to default values and chooses new word
            wrongGuessNumberText.setText(("Number of Guesses Remaining = " + (MAX_WRONG_GUESSES - gameWatch)));
            currentWord = chooseWord();
            currentWordText.setText((hideWord(currentWord)));
            wrongGuessesText.setText(("Wrong Guesses: "+wrong));
             outcomeText.setText("");
             
             // enables and clears guess field and hides button 
            guessField.setDisable(false);
            guessField.clear();
            playAgain.setVisible(false);
            
           
        }
        

	private String chooseWord() {

		ArrayList<String> wordList = new ArrayList<String>();
		String chooseWord = "";
		String chooseWordFormat="";
		try {
			Scanner scan = new Scanner(new File("words.txt"));
			while (scan.hasNext()) {
				String word = scan.nextLine();
				wordList.add(word);
		}
		Random generator = new Random();
		chooseWord = wordList.get(generator.nextInt(wordList.size()));

		} catch (IOException ex) {
			chooseWord = "Error: No dictionary";
			guessField.setText(chooseWord);
		}
	return chooseWord;

	}

	private String hideWord(String word){
		// this return the ---
		return String.join("", Collections.nCopies(word.length(),"- "));
	}

	//reveals the word to the display
	private void revealWord(int position){
		String[] revealedWord = new String[currentWord.length()];
		for(int j = 0; j < guessed.size();j++){
			for(int i = 0; i < currentWord.length(); i++) {
					if( guessed.get(j).equals(String.valueOf(currentWord.charAt(i)))){
						revealedWord[i] = guessed.get(j).toUpperCase();
					}
			}
		}
		for(int x = 0; x < revealedWord.length; x++){
			if(revealedWord[x] == null){
				revealedWord[x] = "-";
			}
		}
		currentWordText.setText(Arrays.toString(revealedWord).replace("[","").replace("]","").replace(",",""));
	}

	public static void main(String[] args) {
		launch(args);

	}

}

