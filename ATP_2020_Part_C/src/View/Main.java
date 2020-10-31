package View;

import Server.*;
import Model.IModel;
import Model.MyModel;
import Server.Server;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {

    private static IModel model= new MyModel();
    private static MyViewModel vm = new MyViewModel(model);
    private static Server mazeGeneratingServer,solveSearchProblemServer;
    public static Stage stage,characters_stage;
    public static int gen_port = 5400;
    public static int solve_port = 5401;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        model.addObserver(vm);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/MyView.fxml"));
        Parent root = (Parent) loader.load();
        IView myView = loader.getController();
        myView.setViewModel(vm);
        vm.addObserver(myView);

        primaryStage.setMinWidth(500);
        primaryStage.setMinHeight(500);
        primaryStage.setTitle("Board Game");
        Screen screen = Screen.getPrimary();
        double height = screen.getVisualBounds().getHeight()-40;
        double width = screen.getVisualBounds().getWidth();
        primaryStage.setScene(new Scene(root, width, height));
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
           // exitProgram();
            closeServers();
            System.exit(0);
        });
    }

    public static void MazeSettingWindow() throws IOException {

        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/View/MazeSettingWindow.fxml"));
        Parent root = (Parent) loader.load();
        IView mazeView = loader.getController();
        mazeView.setViewModel(vm);
        vm.addObserver(mazeView);

        Stage window = new Stage();
        window.setTitle("Maze Settings");
        //root = FXMLLoader.load(Main.class.getResource("/View/MazeSettingWindow.fxml"));
        window.setScene(new Scene(root, 500, 400));
        window.show();
    }

    public static void MazeCharactersWindow() throws IOException {

        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/View/CharactersView.fxml"));
        Parent root = (Parent) loader.load();
        IView mazeView = loader.getController();
        mazeView.setViewModel(vm);
        vm.addObserver(mazeView);

        characters_stage = new Stage();

        characters_stage.setTitle("Mario Characters");
        //root = FXMLLoader.load(Main.class.getResource("/View/MazeSettingWindow.fxml"));
        characters_stage.setScene(new Scene(root, 600, 600));
        characters_stage.show();
        characters_stage.setOnCloseRequest(event -> {
            mazeView.handleMenuGameRules();

        });

    }


    public static void main(String[] args) {
        mazeGeneratingServer = new Server(gen_port, 1000, new ServerStrategyGenerateMaze());
        gen_port+=2;
        solveSearchProblemServer = new Server(solve_port, 1000, new ServerStrategySolveSearchProblem());
        solve_port+=2;

        //Starting  servers
        solveSearchProblemServer.start();
        mazeGeneratingServer.start();

        launch(args);
    }

    public static void closeServers(){
        solveSearchProblemServer.stop();
        mazeGeneratingServer.stop();/*
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        solveSearchProblemServer = null;
        mazeGeneratingServer = null;
    }

    public static void exitProgram(){
        if(!ConfirmBox.display("Exit","Are you sure you want to exit?"))
            return;
        closeServers();
        System.exit(0);
    }

    public static void restartServers(){
        closeServers();
        //handle bug --> servers using the same port(shuts down after the new is created ..)
        //after reaching a certain max port number we can reuse the old ones
        if(gen_port==5500)
            gen_port = 5400;
        if(solve_port==5499)
            solve_port = 5401;
        mazeGeneratingServer = new Server(gen_port, 1000, new ServerStrategyGenerateMaze());
        gen_port+=2;
        solveSearchProblemServer = new Server(solve_port, 1000, new ServerStrategySolveSearchProblem());
        solve_port+=2;
        solveSearchProblemServer.start();
        mazeGeneratingServer.start();
    }
}
