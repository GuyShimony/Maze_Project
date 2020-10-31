package View;

import ViewModel.MyViewModel;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public interface IView extends Observer  {

    public void setViewModel(MyViewModel viewModel);
    public void handleMenuNew() throws IOException;
    public void update(Observable o, Object arg);
    public void handleMenuSave() throws IOException;
    public void handleMenuLoad() throws IOException;
    public void handleMenuExit();
    public void handleMenuGameRules();
    public void handleMenuSymbols() throws IOException;
    public void handleMenuProperties();
    public void handleMenuAlgorithmUsed();
    public void handleMenuAboutTheProgrammers();
    public void keyPressed(KeyEvent keyEvent);
    public void keyReleased(KeyEvent keyEvent);
    public void mouseClicked(MouseEvent mouseEvent);
    public void showSolution(ActionEvent actionEvent);
    public void hideSolution(ActionEvent actionEvent);
    public void gameWon();
    public void zoom(ScrollEvent scroll_event);
    }
