package View;

import Model.FinishException;
import algorithms.mazeGenerators.Maze;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.*;


public class MyViewController extends AView implements Initializable {

    private boolean hide_solution = false;
    private boolean mute_music = true;
    //set of keys for the control and scroll option--> only if the control is pressed it is
    //put in the set and the zoom can occur, when it is released it is deleted from the set
    private Set<KeyCode> pressedKeys = new HashSet<>();
    private Stage window = Main.stage;

    @FXML
    public MazeDisplayer mazeDisplayer;
    @FXML
    public String PlayerMoveSound;
    @FXML
    public Label instruction;
    @FXML
    public BorderPane borderpane_main_scene;
    @FXML
    public Pane game_pane;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Bind the main scene to the stage window size
        borderpane_main_scene.prefHeightProperty().bind(window.heightProperty());
        borderpane_main_scene.prefWidthProperty().bind(window.widthProperty());
        // Bind the pane that holds the canvas to the main scene
        game_pane.prefWidthProperty().bind(borderpane_main_scene.prefHeightProperty());
        game_pane.prefHeightProperty().bind(borderpane_main_scene.prefHeightProperty());
        // Bind the label with the main_pane.
        instruction.prefWidthProperty().bind(borderpane_main_scene.widthProperty());
        instruction.prefHeightProperty().bind(borderpane_main_scene.heightProperty());
    }

    /**
     * Override of the observer update method.
     * The observable of the view is the viewmodel
     * @param o - instance of a class that extends observable -> ViewModel
     * @param arg - extra data that was given in update time by the observable (optional)
     */
    @Override
    public void update(Observable o, Object arg) {
        // If the update was a new maze - need to draw a new maze on the canvas
        if (arg instanceof Maze)
            mazeDisplayer.drawMaze((Maze)arg);
        // If the update was int[] - a new player position update has occurred. Need to show the player
        // updated position on the canvas
        else if(arg instanceof int[]){
            int[] player_position = (int[])arg;
            mazeDisplayer.set_player_position(player_position[0],player_position[1]);
        }
        // If the update was String - the player direction has changed.
        // Need to draw the picture of the new player direction.
        else if (arg instanceof String){
            mazeDisplayer.setPlayerImage((String)arg);
        }
        // If the update was String - a new player image was chosen by the user.
        // The update is the path to the image. Need to redraw the player's image.
        else if (arg instanceof String[]){
            mazeDisplayer.setPlayerImage(((String[])arg)[0]);
            mazeDisplayer.setFinishImage(((String[])arg)[1]);
        }
        else if (arg instanceof ArrayList){
            mazeDisplayer.showSolution((ArrayList<String>)arg);        }
    }

    public void handleMenuNew() throws IOException {
        mazeDisplayer.widthProperty().bind(mazeDisplayer.getScene().widthProperty());
        mazeDisplayer.heightProperty().bind(mazeDisplayer.getScene().heightProperty().subtract(30));
        game_pane.getChildren().remove(instruction);
        Main.MazeSettingWindow();
    }

    public void handleMenuLoad() throws IOException {
        mazeDisplayer.widthProperty().bind(mazeDisplayer.getScene().widthProperty());
        mazeDisplayer.heightProperty().bind(mazeDisplayer.getScene().heightProperty().subtract(30));
        FileHandleWindow fw = new FileHandleWindow(viewModel,false);
        try {
            fw.loadTextFile();
            game_pane.getChildren().remove(instruction);
            Main.MazeCharactersWindow();
        }
        catch (IOException | ClassNotFoundException e) {
            showAlert("Could not find file: " + e.getMessage());
        }
    }

    public void handleMenuSave() {
        FileHandleWindow fw = new FileHandleWindow(viewModel,true);
        if(fw == null)
            showAlert("Please save the maze to a file or exit");
        fw.saveTextToFile();
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        try{
            switch (keyEvent.getCode()){
                case H:
                    if(!hide_solution) {
                        showSolution(new ActionEvent());
                        hide_solution = true;
                    }
                    else {
                        hideSolution(new ActionEvent());
                        hide_solution = false;
                    }
                    break;

                case M:
                    if(mute_music) {
                        mazeDisplayer.muteBackgroundMusic();
                        mute_music = false;
                    }
                    else{
                        mazeDisplayer.playBackgroundMusic();
                        mute_music = true;
                    }
                    break;
                case S:
                    Main.MazeCharactersWindow();
                case CONTROL:
                    pressedKeys.add(keyEvent.getCode());
                    break;
                default: // Don't move player
                    play_audio(PlayerMoveSound);
                    viewModel.updatePlayerPosition(keyEvent);
            }
        }
        catch (FinishException e){
            gameWon();
        }
        catch (IOException f){
            showAlert("Could not load CharactersView.fxml file");
        }
        keyEvent.consume();
    }

    /**
     * This function is called whenever the user push ctrl+mouse scroll to zoom in on the maze.
     * A new event is catched and a anonymous class is created to handle the event and zoom in/out on the maze.
     * @param scroll_event
     */
    public void zoom(ScrollEvent scroll_event) {
        mazeDisplayer.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (pressedKeys.contains(KeyCode.CONTROL)) {
                    double zoomFactor = 1.05;
                    double deltaY = event.getDeltaY();

                    if (deltaY < 0) {
                        zoomFactor = 0.95;
                    }
                    mazeDisplayer.setScaleX(mazeDisplayer.getScaleX() * zoomFactor);
                    mazeDisplayer.setScaleY(mazeDisplayer.getScaleY() * zoomFactor);
                    event.consume();
                }
            }
        });
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }

    public void keyReleased(KeyEvent keyEvent) {
        pressedKeys.remove(keyEvent.getCode());
    }

    @Override
    public void showSolution(ActionEvent actionEvent) {
        try {
            viewModel.showSolutionViewModel();
        }
        catch (NullPointerException n){
            showAlert("Please create or load a game before pushing the button");
        }
    }

    @Override
    public void hideSolution(ActionEvent actionEvent) {
        mazeDisplayer.hideMazeSolution();
    }

    /**
     * This function is called when the user gets to the goal point, meaning
     * He won the maze.
     * The function will then play the victory song.
     * The function will ask the user if he/she want to play again and if not will exit.
     */
    public void gameWon(){
        mazeDisplayer.muteBackgroundMusic();
        play_audio("./resources/Music/FinishMusic.mp3");
        showAlert("YOU WON!");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Ask the user if he wants to play again and if not exit the program
        if(ConfirmBox.display("YOU WON!","Play Again?")) {
            try {
                handleMenuNew();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
            handleMenuExit();
    }


}
