package View;

import com.sun.media.sound.InvalidDataException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.net.URL;
import java.util.InputMismatchException;
import java.util.ResourceBundle;

public class MazeSettingView extends AView implements Initializable {

    private String game_difficulty;
    @FXML
    public TextField textfield_maze_rows;
    @FXML
    public TextField textfield_maze_cols;
    @FXML
    public ComboBox<String> combo_box;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        combo_box.getItems().addAll("Easy Maze", "Hard Maze");
    }

    /*
        The binding method for the rows.
        When the user enter a row number for the new maze, the ViewModel rows attribute will change as well
         */
    public void setMazeRows() {
        viewModel.setMaze_rows(textfield_maze_rows.textProperty());
    }

    /*
    The binding method for the rows.
    When the user enter a column number for the new maze, the ViewModel column attribute will change as well
    */
    public void setMazeCols() {
        viewModel.setMaze_cols(textfield_maze_cols.textProperty());
    }

    /*
    When the user will click the generate maze button this function will be called.
    The View will use the ViewModel to create a new maze and update the main window.
    InputMismatchException - if the user send an invalid input an error will occur
     */
    public void generateMaze() throws IOException {
        // The user can't generate a maze without choosing the game difficulty
        if(game_difficulty == null) {
            showAlert("Please choose a maze difficulty");
            return;
        }
        try { viewModel.createGameForView();
            // Close the window after the maze was generated
            closeWindow();
            Main.MazeCharactersWindow();}
        catch (InputMismatchException i){ showAlert("Please Insert a positive number"); }
        catch (InvalidDataException i){
            if(i.getLocalizedMessage().equals("small"))
                showAlert("Can't create a 1X1 maze");
            else
                showAlert("Can't create a maze with more than 500 rows or columns");
        }
    }

    public void mazeDifficultyChoice(ActionEvent actionEvent) {
        game_difficulty = combo_box.getValue();
        viewModel.setMaze_difficulty(game_difficulty);
    }

    private void closeWindow(){
        textfield_maze_cols.getScene().getWindow().hide();
    }
}
