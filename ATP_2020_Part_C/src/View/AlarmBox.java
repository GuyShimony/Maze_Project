package View;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class AlarmBox {

    public static void display(String window_title,String message) {
        Stage window = new Stage();

        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(window_title);
        window.setMinWidth(250);

        Label label = new Label();
        label.setText(message);
        label.setStyle("-fx-font-weight: BOLD;-fx-font-family: \"Georgia\"; -fx-font-size: 20; -fx-text-fill: white");

        Button closeButton = new Button("Close this window");
        closeButton.setOnAction(e -> window.close());
        closeButton.setStyle("-fx-background-color: #a6b5c9");
        closeButton.setFont(Font.font("Verdana", FontWeight.BOLD, 14));

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-size:stretch; -fx-background-image: url('/Images/mario_map.jpg') ");
        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
}
