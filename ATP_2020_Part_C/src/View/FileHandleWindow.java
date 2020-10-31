package View;
import ViewModel.MyViewModel;
import com.sun.media.sound.InvalidDataException;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.nio.file.FileSystemNotFoundException;

public class FileHandleWindow  {

    private File user_maze_file;
    private MyViewModel vm;
    private FileChooser fileChooser;

    public FileHandleWindow(MyViewModel viewModel,boolean load_save) {
        this.vm = viewModel;
        Stage window = new Stage();
        fileChooser = new FileChooser();
        //Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        // We use a boolean value to know if the user wants to save or load a file.
        // This is used to prevent duplicated code.
        if(load_save)
            //Show save file dialog
            user_maze_file = fileChooser.showSaveDialog(window);
        else
            //show open file dialog
            user_maze_file = fileChooser.showOpenDialog(window);
    }

    public void loadTextFile()  throws IOException, ClassNotFoundException{
            vm.loadMaze(user_maze_file);
    }

    public void saveTextToFile() {
        try {
            vm.saveMaze(user_maze_file);
            showAlert("Maze data successfully saved!");
        }
        catch (InvalidDataException d){
            showAlert("Problem saving the file:\n" +
                    "Please generate a maze before trying to save.\n");
        }
        catch (FileSystemNotFoundException f){
            showAlert("Problem saving the file:\n " +
                    "file path is " + f.getMessage());
        } catch (IOException e) {
            showAlert("Can't write the data to file:" +
                    "\n" + e.getMessage());
        }
    }

    private void showAlert(String message)  {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);;
        alert.show();
    }

}
