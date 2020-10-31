package View;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class CharactersView extends AView{

    @FXML
    GridPane main_scene;
    @FXML
    ImageView princess_right;

    private static boolean start=true;

    public void chooseCharacter(MouseEvent mouseEvent) {
        viewModel.updateCharacterType(mouseEvent);
        main_scene.getScene().getWindow().hide();
        if(start) {
            this.handleMenuGameRules();
            start = false;
        }
    }


}
