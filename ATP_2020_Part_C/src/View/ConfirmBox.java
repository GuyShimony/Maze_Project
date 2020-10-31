package View;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class ConfirmBox {

    //Create variable
    static boolean answer;

    public static boolean display(String title, String message) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        Label label = new Label();
        label.setText(message);
        label.setStyle("-fx-font-family: \"Comic Sans MS\"; -fx-font-size: 17; -fx-text-fill: #8b0000");

        //Create two buttons
        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");
        yesButton.setStyle("-fx-background-color: #a6b5c9");
        yesButton.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        noButton.setStyle("-fx-background-color: #a6b5c9");
        noButton.setFont(Font.font("Verdana", FontWeight.BOLD, 14));

        //Clicking will set answer and close window
        yesButton.setOnAction(e -> {
            answer = true;
            window.close();
        });
        noButton.setOnAction(e -> {
            answer = false;
            window.close();
        });

        VBox layout = new VBox(10);
        layout.setStyle("-fx-background-color: white");

        //Add buttons
        layout.getChildren().addAll(label, yesButton, noButton);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        //Make sure to return answer
        return answer;
    }
}

