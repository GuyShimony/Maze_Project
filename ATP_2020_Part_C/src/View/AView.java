package View;

import Server.Configurations;
import ViewModel.MyViewModel;
import algorithms.mazeGenerators.MyMazeGenerator;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Observable;

public abstract class AView implements IView {

    protected MyViewModel viewModel;

    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void handleMenuProperties() {
        String generator ="";
        if(Configurations.getGenerateAlgorithm() instanceof MyMazeGenerator)
            generator = "MyMazeGenerator";
        else
            generator = "SimpleMazeGenerator";

        String message = String.format("Program properties: \n" +
                        "1. Maze generation algorithm: %s\n" +
                        "2. Maze solving algorithm: %s\n" +
                        "3. Max Clients the servers can handle: %s",
                generator,Configurations.getSearchingAlgorithm().getName(),Configurations.getMaxThreads());
        AlarmBox.display("Properties",message);
    }

    @Override
    public void handleMenuAlgorithmUsed() {
        String bfs = "Algorithms used in the game:\n\n" +
                "BFS: Breadth-first search is an algorithm for searching tree or graph data structures.\n" +
                " It starts at the tree root" +
                " and explores all of the neighbor nodes at the present depth prior to moving \n on to the nodes at the next depth level.\n\n"+
                "\tInformation Link Wikipedia: https://en.wikipedia.org/wiki/Breadth-first_search\n\n\n";

        String dfs = "DFS: Depth-first search is an algorithm for traversing or searching tree or graph data structures.\n" +
                " The algorithm starts " +
                "at the root and explores as far as possible along each branch before backtracking.\n\n"+
                "\tInformation Link Wikipedia: https://en.wikipedia.org/wiki/Depth-first_search\n\n\n";

        String best = "Best: Best-first search is an algorithm which explores a graph by expanding" +
                " the most promising node\n chosen according to a specified rule, in our case the rule is according to weights.\n\n"+
                "\tInformation Link Wikipedia: https://en.wikipedia.org/wiki/Best-first_search";

        AlarmBox.display("Algorithms",bfs+dfs+best);
    }

    @Override
    public void handleMenuAboutTheProgrammers() {
        String message_guy ="1. Guy Shimony\n\n" +
                "\tFavorite Food: Sushi\n" +
                "\tFavorite Animal: Tiger\n" +
                "\tFavorite Programming language: Python\n" +
                "\tFavorite Hobby: Soccer\n" +
                "\tDream Vacation Spot: Hawaii - sitting on a yacht sipping a strawberry shake!\n" +
                "\tFavorite University course: Advanced Topics in Programming\n\n\n";
        String message_eden = "2. Eden Yavin\n\n" +
                "\tFavorite Food: Shakshuka\n" +
                "\tFavorite Animal: Aligator\n" +
                "\tFavorite Programming language: C++\n" +
                "\tFavorite Hobby: Dancing in the Sunset\n" +
                "\tDream Vacation Spot: New Zealand - climbing a tall snowy mountain!\n" +
                "\tFavorite University course: Advanced Topics in Programming\n";
        AlarmBox.display("Meet The Programmers",message_guy+message_eden);
    }

    @Override
    public void handleMenuGameRules() {
        String message = "Game Rules:\n\n"+"Use your Mario Character to pass through the maze to reach the character's school item.\n\n" +
                "Controls:\n\n"+"Use Your computer Numpad to travel through the maze:\n" +
                "\t8: Move your player UP\n"+
                "\t2: Move your player DOWN\n"+
                "\t4: Move your player LEFT\n"+
                "\t6: Move your player RIGHT\n"+
                "\t1: Move your player diagonally DOWN and LEFT\n"+
                "\t3: Move your player diagonally DOWN and RIGHT\n"+
                "\t7: Move your player diagonally UP and LEFT\n"+
                "\t9: Move your player diagonally UP and RIGHT\n\n\n"+
                "H: Use the H key to show and hide the path to the character's school item\n"+
                "M: Use the M key to mute and unmute the game's background music.\n" +
                "S: Use the S key to pick a new character. Your goal will be updated accordingly :)\n\n" +
                "To see the rules again go to Help -> Game Rules\n";
        AlarmBox.display("Game Rules",message);
    }

    @Override
    public void handleMenuSymbols() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/View/SymbolsView.fxml"));
        Parent root = (Parent) loader.load();
        Stage window = new Stage();
        window.setTitle("Game Symbols");
        window.setScene(new Scene(root, 600, 600));
        window.show();
    }

    @Override
    public void handleMenuExit() {
        // When exiting the program we need to shut down the servers
        Main.exitProgram();
    }

    public void showAlert(String message)  {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);;
        alert.show();
    }

    protected void play_audio(String path){
        AudioClip note = new AudioClip(Paths.get(path).toUri().toString());
        note.setVolume(0.1);
        note.play();
    }

    public void gameWon(){
        play_audio("./resources/Music/FinishMusic.mp3");
        showAlert("YOU WON!");
    }

    public void handleMenuNew() throws IOException {
        //
    }

    public void handleMenuLoad() throws IOException {
        //
    }

    public void handleMenuSave() {
        //
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        // Not needed
    }

    @Override
    public void keyReleased(KeyEvent keyEvent){
        // Not needed
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        // Not needed
    }

    @Override
    public void showSolution(ActionEvent actionEvent) {
        // Not needed
    }

    @Override
    public void hideSolution(ActionEvent actionEvent) {
        // Not needed
    }

    public void zoom(ScrollEvent scroll_event){
        // Not needed
    }

    public void update(Observable o, Object arg){
        //
    }








}
