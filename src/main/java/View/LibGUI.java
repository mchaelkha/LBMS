package View;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * The graphical user interface to interact with the library book management
 * system. Input is read from a text field and output is sent to the text area.
 */
public class LibGUI extends Application {

    /**
     * Start the application by initializing the stage with the nodes.
     * Input is read to the input reader and a request is output. The
     * request is then executed by the associated account
     * @param primaryStage
     */
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
            String response = reader.read(input);
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

}
