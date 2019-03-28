package View;
import Controller.Request.Request;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class LibGUI extends Application implements EventHandler<ActionEvent> {

    @Override
    public void start(Stage primaryStage) {
        InputReader reader = InputReader.getInstance();
        primaryStage.setTitle("LBMS");

        BorderPane layout = new BorderPane();

        VBox terminal = new VBox();
        terminal.setMinSize(600,460);

        HBox field = new HBox();
        field.setMinWidth(600);

        TextField textField = new TextField();
        textField.setMinWidth(600);

        Text results = new Text("Library Book Management System");
        results.setFont(Font.font("arial", 20));
        TextArea textArea = new TextArea();
        textArea.setMinWidth(600);
        textArea.setMinHeight(450);
        textArea.setFont(Font.font(14));
        textArea.setEditable(false);
        textArea.setFocusTraversable(false);

        textField.setOnAction(e -> {
            String input = textField.getText();
            Request request = reader.read(input);
            if (request == null) {
                Platform.exit();
                System.exit(0);
            }
            String response = request.execute();
            textArea.setText(textArea.getText() + System.lineSeparator() + response);
            textField.setText("");
        });

        field.getChildren().add(textField);
        terminal.getChildren().addAll(results, textArea);

        layout.setCenter(terminal);
        layout.setBottom(field);

        Scene scene = new Scene(layout, 600, 500);

        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void handle(ActionEvent event) {

    }
}
